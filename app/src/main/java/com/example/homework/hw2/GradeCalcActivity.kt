package com.example.homework.hw2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly

private const val GrayishPink = 0xFFCCC2DC

class GradeCalcActivity : ComponentActivity() {

    private val gradingService = GradingService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gradingService.initGrades(resources, filesDir.path + "/grades.json")
        setContent {
            Surface(
                color = Color(GrayishPink)
            ) {
                GradeCalculatorScreen()
            }
        }
    }


    // I tried to make this scrollable but couldn't,
    // Can you, please, tell me in the comments
    // how I should have done this?
    @Composable
    fun GradeCalculatorScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Grade Calculator",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ), textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            HomeworkList()
            AddResetButtons()
            OtherAssignmentList()

            val textToShow = remember { mutableStateOf("Answer") }

            Column(
                Modifier
                    .padding(vertical = 5.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = textToShow.value,
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Row() {

                Button(
                    onClick = {
                        gradingService.saveGrades(filesDir.path + "/grades.json")
                    },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = "Save")
                }

                Button(
                    onClick = {
                        textToShow.value = gradingService.calculate()
                    },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = "Calculate Final Grade")
                }
            }
        }

    }

    @Composable
    private fun AddResetButtons() {
        var showDialog by remember { mutableStateOf(false) }
        var showNoHwMessage by remember {
            mutableStateOf(gradingService.getHomeworks().size == 0)
        }
        if (showNoHwMessage) {
            Row() {
                Text("As no homeworks were added, the grade for homeworks will be counted 100")
            }
        }

        Row() {
            Button(
                onClick = {
                    showDialog = true
                }, enabled = gradingService.getHomeworks().size < 5,
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = "Add Homework")
            }
            Button(
                onClick = {
                    gradingService.resetHomeworks()
                    showNoHwMessage = true
                },
                enabled = gradingService.getHomeworks().size != 0,
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = "Reset")
            }
        }
        if (showDialog) {
            AddHomeworkDialogue(
                onDismiss = { showDialog = false },
                onSave = { grade ->
                    val assignment = Assignment(
                        getNextHomeworkName(),
                        grade.toInt(),
                        20
                    )
                    gradingService.addHomework(assignment)
                    showNoHwMessage = false
                }
            )
        }
    }

    @Composable
    private fun OtherAssignmentList() {
        LazyColumn(
            Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth()
        ) {
            itemsIndexed(gradingService.getOtherAssignments()) { index, assignment ->
                AssignmentItem(assignment = assignment)
            }
        }
    }

    @Composable
    private fun HomeworkList() {
        LazyColumn(
            Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth()
        ) {
            itemsIndexed(gradingService.getHomeworks()) { index, assignment ->
                AssignmentItem(assignment = assignment)
            }
        }
    }


    @Composable
    fun AssignmentItem(assignment: Assignment) {
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = 25.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = assignment.name, modifier = Modifier.width(100.dp))
                var text by remember { mutableStateOf(TextFieldValue(text = assignment.grade.toString())) }
                var isTextValid by remember { mutableStateOf(true) }
                TextField(
                    value = text,
                    onValueChange = { newValue ->
                        text = newValue.copy(
                            text = newValue.text.filter { it.isDigit() }
                        )
                        isTextValid = isValidGrade(text.text)
                        if (isTextValid) assignment.grade = text.text.toInt()
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    label = { Text("Grade (0-100)") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = if (isTextValid) Color.Transparent else Color(0xFFED8282),
                        textColor = Color.Black
                    )
                )
            }
        }
    }


    @Composable
    fun AddHomeworkDialogue(
        onDismiss: () -> Unit,
        onSave: (grade: String) -> Unit
    ) {
        var grade by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Add Homework Grade") },
            text = {
                Column {
                    Text(
                        text = getNextHomeworkName(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = grade,
                        onValueChange = { newValue ->
                            val isTextValid = isValidGrade(newValue)

                            grade = if (isTextValid) newValue else "0"
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        label = { Text(text = "Grade (0-100)") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    onSave(grade)
                    onDismiss()
                }) {
                    Text(text = "Save")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    private fun isValidGrade(newValue: String) = (newValue.isNotBlank()
            && newValue.isDigitsOnly()
            && newValue.toInt() <= 100
            && newValue.toInt() >= 0)


    private fun getNextHomeworkName() = "Homework ${gradingService.getHomeworks().size + 1}"

}