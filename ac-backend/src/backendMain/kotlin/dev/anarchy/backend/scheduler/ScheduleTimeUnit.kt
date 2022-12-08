package dev.anarchy.backend.scheduler

import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

data class ScheduleTimeUnit(val value: Long, val unit: TemporalUnit) {
    companion object {
        val NOW = ScheduleTimeUnit(0, ChronoUnit.NANOS)
        val NEVER = ScheduleTimeUnit(1, ChronoUnit.FOREVER)
    }
}
