package com.example.homework.hw2

import android.content.res.Resources
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.homework.R
import kotlinx.serialization.*

import kotlinx.serialization.json.Json
import java.io.File

class GradingService {

    val jsonReader: Json = Json { ignoreUnknownKeys = true }

    @Serializable
    class Grades {
        lateinit var homeworks: List<Assignment>

        @Transient
        var homeworks2: SnapshotStateList<Assignment> = mutableStateListOf()
        lateinit var otherAssignments: List<Assignment>
    }

    private var grades = Grades()

    fun addHomework(assignment: Assignment) {
        grades.homeworks2.add(assignment)
    }

    fun getHomeworks(): SnapshotStateList<Assignment> {
        return grades.homeworks2
    }

    fun getOtherAssignments(): List<Assignment> {
        return grades.otherAssignments
    }


    fun initGrades(resources: Resources, gradesFilePath: String) {
        val gradesFile = File(gradesFilePath)
        val json: String
        if (gradesFile.exists()) {
            json = gradesFile.readText()
        } else {
            gradesFile.createNewFile()
            val inputStream =
                resources.openRawResource(R.raw.grades) // replace "filename" with your actual JSON file name
            json = inputStream.bufferedReader().use { it.readText() }
            inputStream.close()
        }
        grades = jsonReader.decodeFromString<Grades>(json)
        grades.homeworks2.addAll(grades.homeworks)
        saveGrades(gradesFilePath)
    }

    fun saveGrades(gradesFilesPath: String) {
        grades.homeworks = grades.homeworks2
        val jsonOutput = Json.encodeToString(grades)
        val outputFile = File(gradesFilesPath)
        outputFile.writeText(jsonOutput)
    }

    fun resetHomeworks() {
        grades.homeworks2.removeAll(grades.homeworks2)
    }

    fun calculate(): String {
        val hws: Float = if (grades.homeworks2.size == 0)
            100.0F
        else {
            grades
                .homeworks2
                .sumOf { assignment -> assignment.grade * (assignment.weight / 100.0) / grades.homeworks2.size }
                .toFloat()
        }
        val others = grades
            .otherAssignments
            .sumOf { assignment -> assignment.grade * (assignment.weight / 100.0) }
            .toFloat()
        return (hws + others).toString()
    }
}