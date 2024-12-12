package dev.prince.monthviewapp.util

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun testGetDaysInMonth() {

        val januaryDays = getDaysInMonth(2024, 1)
        assertEquals(1, januaryDays.takeWhile { it.isEmpty() }.count())
        assertEquals("1", januaryDays[1])
        assertEquals("31", januaryDays.last())

        val aprilDays = getDaysInMonth(2024, 4)
        assertEquals(1, aprilDays.takeWhile { it.isEmpty() }.count())
        assertEquals("1", aprilDays[1])
        assertEquals("30", aprilDays.last())

        val februaryDays = getDaysInMonth(2024, 2)
        assertEquals(4, februaryDays.takeWhile { it.isEmpty() }.count())
        assertEquals("1", februaryDays[4])
        assertEquals("29", februaryDays.last())
    }

    @Test
    fun testGetMonthName() {
        assertEquals("January", getMonthName(1))
        assertEquals("February", getMonthName(2))
        assertEquals("December", getMonthName(12))
        assertEquals("July", getMonthName(7))
        assertEquals("April", getMonthName(4))
    }
}