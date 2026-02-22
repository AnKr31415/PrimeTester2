package com.example.primetestertest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType




@Composable
fun PrimeCheckerScreen(modifier: Modifier = Modifier) {
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.background) {


        var input by rememberSaveable { mutableStateOf("") }
        var result by rememberSaveable { mutableStateOf("") }

        var rangeStart by rememberSaveable { mutableStateOf("") }
        var rangeEnd by rememberSaveable { mutableStateOf("") }

        // Liste der Primzahlen
        var primesList by rememberSaveable { mutableStateOf(listOf<Int>()) }

        val focusManager = LocalFocusManager.current
        val scrollState = rememberScrollState()


        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)

        ) {

            Text(
                "Startseite",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                "Primzahl Überprüfung",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Einzelne Zahl prüfen
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Zahl eingeben") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(modifier = Modifier.padding(4.dp))

                Button(
                    onClick = {
                        val number = input.toIntOrNull()
                        result = if (number == null) {
                            "Bitte eine gültige Zahl eingeben"
                        } else if (isPrime(number)) {
                            "$number ist eine Primzahl"
                        } else {
                            "$number ist keine Primzahl"
                        }
                        focusManager.clearFocus()
                    }
                ) {
                    Text("Prüfen")
                }

            Row(
                modifier = Modifier.padding(top = 8.dp)
            ){

                Button(
                    onClick = {
                        val number = input.toIntOrNull()
                        if (number != null) {
                            var resultInt: Int = nextPrime(number)
                            result = resultInt.toString()
                            focusManager.clearFocus()
                        }
                    }
                ) {
                    Text("Nächste")
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Button(
                    onClick = {
                        val number = input.toIntOrNull()
                        if (number != null) {
                            var resultInt: Int = primeBefore(number)
                            result = resultInt.toString()
                            focusManager.clearFocus()
                        }
                    }
                ) {
                    Text("Vorherige")
                }
            }

            Text(
                text = result,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Text(
                "Bereichsüberprüfung",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Bereichsberechnung
            OutlinedTextField(
                value = rangeStart,
                onValueChange = { rangeStart = it },
                label = { Text("Von") },
                modifier = Modifier.padding(top = 8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            OutlinedTextField(
                value = rangeEnd,
                onValueChange = { rangeEnd = it },
                label = { Text("Bis") },
                modifier = Modifier.padding(top = 8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Row(modifier = Modifier.padding(top = 16.dp)) {

                Button(
                    onClick = {
                        val start = rangeStart.toIntOrNull()
                        val end = rangeEnd.toIntOrNull()

                        primesList =
                            if (start == null || end == null || start > end) {
                                emptyList()
                            } else {
                                (start..end).filter { isPrime(it) }
                            }
                        focusManager.clearFocus()
                    }
                ) {
                    Text("Bereich prüfen")
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = {
                        val start = rangeStart.toIntOrNull()
                        val end = rangeEnd.toIntOrNull()

                        primesList =
                            if (start == null || end == null || start > end) {
                                emptyList()
                            } else {
                                (start..end)
                                    .filter { isPrime(it) }
                                    .filter { isPalindrome(it) }
                            }
                        focusManager.clearFocus()
                    }
                ) {
                    Text("Palindrome")
                }
            }



            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = primesList.joinToString(", "),
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}
