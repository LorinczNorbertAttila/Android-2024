package main

import java.time.LocalDate

data class Date(val year: Int = LocalDate.now().year,
                val month: Int = LocalDate.now().monthValue,
                val day: Int = LocalDate.now().dayOfMonth) : Comparable<Date> {
    override fun compareTo(other: Date): Int {
        return when {
            this.year != other.year -> this.year.compareTo(other.year)
            this.month != other.month -> this.month.compareTo(other.month)
            else -> this.day.compareTo(other.day)
        }
    }
}



