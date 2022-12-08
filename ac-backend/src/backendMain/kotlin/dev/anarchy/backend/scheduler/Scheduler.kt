package dev.anarchy.backend.scheduler

import com.martmists.commons.logging.logger
import java.time.LocalDateTime
import java.time.temporal.UnsupportedTemporalTypeException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

open class Scheduler(private val poolSize: Int) {
    @Volatile
    private var running = false
    private val schedule = PriorityBlockingQueue<Entry>(100) { a, b ->
        a.runAt.compareTo(b.runAt)
    }
    private val log by logger()
    private lateinit var pool: List<Thread>

    data class Entry(val task: Task, val runAt: LocalDateTime)
    inner class Task(val interval: ScheduleTimeUnit, val check: () -> Boolean, private val onCompleteBlock: () -> Unit, val block: () -> Unit) :
        CancellableTask {
        private val fut = CompletableFuture<Unit>()

        override fun cancel() {
            schedule.removeIf {
                it.task === this
            }
        }

        override fun get() {
            fut.get()
        }

        fun onComplete() {
            fut.complete(Unit)
            onCompleteBlock()
        }
    }

    private fun createPool() = List(poolSize) {
        thread(start = false, isDaemon = true, name = "Gunpowder Scheduler Thread #$it") {
            while (running) {
                try {
                    // Poll for an available task
                    val entry = schedule.poll(1, TimeUnit.SECONDS)

                    if (entry != null) {
                        val task = entry.task
                        if (entry.runAt < LocalDateTime.now()) {
                            val shouldRun = try {
                                // Check if the task is still valid
                                task.check()
                            } catch (e: Exception) {
                                log.error("An error occurred checking if a scheduled task should run", e)
                                continue
                            }

                            if (shouldRun) {
                                val success = try {
                                    task.block()
                                    true
                                } catch (e: Exception) {
                                    log.error("An error occurred running a scheduled task", e)
                                    false
                                }

                                if (success) {
                                    // Schedule the task again because it didn't error
                                    try {
                                        schedule.add(
                                            Entry(
                                                task,
                                                entry.runAt.plus(task.interval.value, task.interval.unit)
                                            )
                                        )
                                    } catch (e: UnsupportedTemporalTypeException) {
                                        // ChronoUnit.FOREVER or similar, ignore because it shouldn't run again
                                        task.onComplete()
                                    }
                                }
                            } else {
                                task.onComplete()
                            }
                        } else {
                            // This task shouldn't run yet, add it back to the queue
                            schedule.add(entry)
                        }
                    }
                } catch (e: InterruptedException) {
                    break
                }
            }
        }
    }

    fun start() {
        running = true
        pool = createPool()
        for (thread in pool) {
            thread.start()
        }
    }

    fun stop() {
        running = false
        for (thread in pool) {
            thread.interrupt()
        }
        schedule.clear()
    }

    fun schedule(
        runAfter: ScheduleTimeUnit = ScheduleTimeUnit.NOW,
        interval: ScheduleTimeUnit = ScheduleTimeUnit.NEVER,
        check: () -> Boolean = { true },
        onComplete: () -> Unit = { },
        block: () -> Unit
    ): CancellableTask {
        val task = Task(interval, check, onComplete, block)
        schedule.add(Entry(task, LocalDateTime.now().plus(runAfter.value, runAfter.unit)))
        return task
    }
}
