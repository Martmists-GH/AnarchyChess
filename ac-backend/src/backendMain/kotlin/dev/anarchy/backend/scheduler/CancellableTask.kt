package dev.anarchy.backend.scheduler

interface CancellableTask {
    fun cancel()
    fun get()
}
