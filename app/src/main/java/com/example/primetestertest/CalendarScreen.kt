package com.example.primetestertest

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Button
import androidx.datastore.preferences.preferencesDataStore
import androidx.compose.foundation.border
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun CalendarScreen(
    context: Context,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.background) {

            var selectedAgeInDays by remember { mutableStateOf<Int?>(null) }
            val birthdayFlow = remember { loadBirthday(context) }
            var birthday by remember { mutableStateOf<LocalDate?>(null) }
            val scope = rememberCoroutineScope()

            // Flow → State
            LaunchedEffect(Unit) {
                birthdayFlow.collectLatest { birthday = it }
            }

            var currentMonth by remember { mutableStateOf(YearMonth.now()) }
            var showDatePicker by remember { mutableStateOf(false) }

            var selectedEntryDate by remember { mutableStateOf<LocalDate?>(null) }

            val entryFlow = remember { loadEntries(context) }
            var entries by remember { mutableStateOf<Map<LocalDate, String>>(emptyMap()) }

            LaunchedEffect(Unit) {
                entryFlow.collect { loaded ->
                    entries = loaded
                }
            }





            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Text(
                    "Kalender",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Monat Navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Button(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                        Text("<")
                    }
                    Text(
                        "${currentMonth.month} ${currentMonth.year}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Button(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                        Text(">")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (birthday != null) {
                    CalendarGrid(
                        month = currentMonth,
                        birthday = birthday!!,
                        entries = entries,
                        onDayClick = { _, days ->
                            selectedAgeInDays = days
                        },
                        onDayDoubleClick = { date, days ->
                            selectedAgeInDays = days
                            selectedEntryDate = date
                        }
                    )
                } else {
                    Text("Bitte ein Geburtsdatum eingeben")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Geburtstag + Ändern Button
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Geburtsdatum: ${birthday ?: "Nicht gesetzt"}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = { showDatePicker = true }) {
                        Text("Ändern")
                    }
                }

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        onDateSelected = { newDate ->
                            showDatePicker = false
                            scope.launch {
                                saveBirthday(context, newDate)
                            }
                        }
                    )
                }

                selectedAgeInDays?.let { days ->
                    Text(
                        text = "Alter an diesem Tag: $days Tage",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                selectedEntryDate?.let { date ->
                    CalendarEntryDialog(
                        date = date,
                        initialText = entries[date] ?: "",
                        onDismiss = { selectedEntryDate = null },
                        onSaved = { text ->
                            val newMap = entries.toMutableMap()
                            newMap[date] = text
                            entries = newMap

                            scope.launch {
                                saveEntries(context, newMap)
                            }
                        }

                    )
                }

            }
        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarGrid(
    month: YearMonth,
    birthday: LocalDate,
    entries: Map<LocalDate, String> = emptyMap(),
    onDayClick: (LocalDate, Int) -> Unit = { _, _ -> },
    onDayDoubleClick: (LocalDate, Int) -> Unit = { _, _ -> }
) {
    val firstDay = month.atDay(1)
    val daysInMonth = month.lengthOfMonth()
    val firstDayOfWeek = (firstDay.dayOfWeek.value + 6) % 7   // Montag = 0
    val today = LocalDate.now()


    Column {

        // Wochentage
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Mo", "Di", "Mi", "Do", "Fr", "Sa", "So").forEach {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val totalCells = firstDayOfWeek + daysInMonth
        val rows = (totalCells + 6) / 7

        var dayCounter = 1

        Column {
            repeat(rows) { row ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    repeat(7) { col ->
                        val index = row * 7 + col

                        if (index >= firstDayOfWeek && dayCounter <= daysInMonth) {

                            val date = month.atDay(dayCounter)
                            val days = getDays(date, birthday)
                            val isPrime = isPrime(days)
                            val isFib = isFibonacci(days)
                            val isToday = date == today
                            val entryTextForDay = entries[date]

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                                    .then(
                                        if (isToday) {
                                            Modifier.border(2.dp, Color.Red)
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .combinedClickable(
                                        onClick = { onDayClick(date, days) },
                                        onDoubleClick = { onDayDoubleClick(date, days) }
                                    ),
                                contentAlignment = Alignment.Center,

                            ) {

                                // Primzahl-Hintergrund
                                if (isPrime) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(
                                                Color(0xFF4A90E2),
                                                shape = CircleShape
                                            )
                                    )
                                }

                                // Fibonacci-Punkt (Grün, oben links)
                                if (isFib) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(2.dp)
                                            .size(6.dp)
                                            .background(Color(0xFF4CAF50), shape = CircleShape)
                                    )
                                }

                                // Tageszahl
                                Text(text = dayCounter.toString())

                                // Indikator-Punkt für Einträge (Orange, unten mitte)
                                if (!entryTextForDay.isNullOrBlank()) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(bottom = 2.dp)
                                            .size(6.dp)
                                            .background(Color(0xFFFFA000), shape = CircleShape)
                                    )
                                }
                            }


                            dayCounter++

                        } else {
                            // Leere Zelle
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    var yeartext by remember { mutableStateOf("2000") }
    var monthtext by remember { mutableStateOf("1") }
    var daytext by remember { mutableStateOf("1") }
    var year by remember { mutableStateOf(2000) }
    var month by remember { mutableStateOf(1) }
    var day by remember { mutableStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Geburtsdatum wählen") },
        text = {
            Column {
                OutlinedTextField(
                    value = yeartext,
                    onValueChange = { newValue ->
                        yeartext = newValue
                        newValue.toIntOrNull()?.let { year = it }
                    },
                    label = { Text("Jahr") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                OutlinedTextField(
                    value = monthtext,
                    onValueChange = { newValue ->
                        monthtext = newValue
                        newValue.toIntOrNull()?.let { month = it }
                    },
                    label = { Text("Monat") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                OutlinedTextField(
                    value = daytext,
                    onValueChange = {newValue ->
                        daytext = newValue
                        newValue.toIntOrNull()?.let { day = it }
                    },
                    label = { Text("Tag") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onDateSelected(LocalDate.of(year, month, day))
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Abbrechen")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarEntryDialog(
    date: LocalDate,
    initialText: String,
    onDismiss: () -> Unit,
    onSaved: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialText) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Eintrag für ${date.dayOfMonth}.${date.monthValue}.${date.year}:")

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Notiz") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)

            )

            Button(
                onClick = {
                    onSaved(text)
                    onDismiss()
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Speichern")
            }
        }
    }
}
