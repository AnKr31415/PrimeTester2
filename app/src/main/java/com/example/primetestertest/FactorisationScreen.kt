package com.example.primetestertest

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun FactorisationScreen(
                        context: Context,
                        modifier: Modifier = Modifier
) {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 48.dp,   // 👈 deutlich mehr Abstand nach oben
                bottom = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){

        Text(
            text = "Primfaktoren Rechner",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Zahl eingeben") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()

        )

        Button(
            onClick = {
                val number = input.toIntOrNull()
                result = if (number != null && number > 1) {
                    primeFactors(number).joinToString(" × ")
                } else {
                    "Bitte eine gültige Zahl > 1 eingeben"
                }
                focusManager.clearFocus()
            }
        ) {
            Text("Berechnen")
        }

        Text(text = "Ergebnis: $result")
    }
}

