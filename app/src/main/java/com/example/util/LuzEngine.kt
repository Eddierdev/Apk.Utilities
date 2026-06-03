package com.example.util

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object LuzEngine {
    fun parseDate(dateStr: String): LocalDate? {
        return try {
            LocalDate.parse(dateStr)
        } catch (e: Exception) {
            null
        }
    }

    fun getDaySchedule(startCycleDateStr: String, targetDate: LocalDate): DaySchedule {
        val startDate = parseDate(startCycleDateStr) ?: LocalDate.of(2024, 3, 12)

        val daysBetween = ChronoUnit.DAYS.between(startDate, targetDate)

        val hoursList = mutableListOf<HourStatus>()
        var totalLuz = 0
        var totalSinLuz = 0

        for (h in 0..23) {
            val totalHours = daysBetween * 24 + h
            var p = (totalHours % 9).toInt()
            if (p < 0) {
                p += 9
            }
            val hasLuz = p >= 6
            if (hasLuz) {
                totalLuz++
            } else {
                totalSinLuz++
            }
            hoursList.add(HourStatus(hour = h, hasLuz = hasLuz))
        }

        return DaySchedule(
            date = targetDate,
            hours = hoursList,
            totalHoursLuz = totalLuz,
            totalHoursSinLuz = totalSinLuz
        )
    }
}

data class HourStatus(
    val hour: Int,
    val hasLuz: Boolean
)

data class DaySchedule(
    val date: LocalDate,
    val hours: List<HourStatus>,
    val totalHoursLuz: Int,
    val totalHoursSinLuz: Int
)
