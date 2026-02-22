package com.example.primetestertest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import android.content.Context

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimeDaysScreen(modifier: Modifier = Modifier, context: Context) {

    // --- States für ausgewählte Daten ---
    val birthdayFlow = remember { loadBirthday(context) }
    var birthday by remember { mutableStateOf<LocalDate?>(null) }

    // Flow → State
    LaunchedEffect(Unit) {
        birthdayFlow.collectLatest { birthday = it }
    }
    var startDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }
    var endDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }

    // --- DatePicker States ---
    val birthdayPickerState = rememberDatePickerState()
    val startPickerState = rememberDatePickerState()
    val endPickerState = rememberDatePickerState()

    var showBirthdayPicker by remember { mutableStateOf(false) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    // --- Ergebnisliste ---
    var results by rememberSaveable { mutableStateOf(listOf<Triple<LocalDate, Int, Int>>()) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        // -------------------------
        // DATUMSAUSWAHL BUTTONS
        // -------------------------

        /*Button(
            onClick = { showBirthdayPicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Geburtstag: ${birthday ?: "---"}")
        }*/

        Button(
            onClick = { showStartPicker = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Startdatum: ${startDate ?: "---"}")
        }

        Button(
            onClick = { showEndPicker = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Enddatum: ${endDate ?: "---"}")
        }

        // -------------------------
        // BERECHNEN BUTTON
        // -------------------------

        Button(
            onClick = {
                if (birthday != null && startDate != null && endDate != null) {

                    val list = mutableListOf<Triple<LocalDate, Int, Int>>()
                    var current = startDate!!

                    while (!current.isAfter(endDate)) {

                        val livedDays = ChronoUnit.DAYS.between(birthday, current).toInt()
                        val daysFromToday = ChronoUnit.DAYS.between(LocalDate.now(), current).toInt()

                        if (livedDays > 0 && isPrime(livedDays)) {
                            list.add(Triple(current, livedDays, daysFromToday))
                        }

                        current = current.plusDays(1)
                    }

                    results = list
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Berechnen")
        }

        // -------------------------
        // ERGEBNIS-TABELLE
        // -------------------------

        Column(
            modifier = Modifier
                .padding(top = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {

            if (results.isNotEmpty()) {

                // Tabellenkopf
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Datum", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                    Text("Gelebt", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                    Text("Noch", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                }

                Divider()

                // Einträge
                results.forEach { (date, lived, until) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth()
                        ) {
                            Text(date.toString(), modifier = Modifier.weight(1f))
                            Text("$lived", modifier = Modifier.weight(1f))
                            Text("$until", modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }

    // -------------------------
    // DATE PICKER DIALOGE
    // -------------------------

    if (showBirthdayPicker) {
        DatePickerDialog(
            onDismissRequest = { showBirthdayPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    birthdayPickerState.selectedDateMillis?.let { millis ->
                        birthday = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showBirthdayPicker = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = birthdayPickerState)
        }
    }

    if (showStartPicker) {
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startPickerState.selectedDateMillis?.let { millis ->
                        startDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showStartPicker = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = startPickerState)
        }
    }

    if (showEndPicker) {
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    endPickerState.selectedDateMillis?.let { millis ->
                        endDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showEndPicker = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = endPickerState)
        }
    }
}