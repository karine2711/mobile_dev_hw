package com.example.homework.hw2

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.File

@RunWith(AndroidJUnit4::class)
class GradeCalculatorTest {
    private val gradingService = GradingService()

    @Test
    fun calculateGrade() {
        // Context of the app
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        gradingService.initGrades(
            appContext.resources,
            appContext.filesDir.path + "/testGrades.json"
        )

        val result = gradingService.calculate()
        // The expected value will need to be changed if you change
        // the grade.json file in resources
        assertEquals(98.5F, result.toFloat())

        File(appContext.filesDir.path + "/testGrades.json").delete()
    }
}