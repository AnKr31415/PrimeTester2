package com.example.primetestertest

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun QuizScreen(
    context: Context,
    modifier: Modifier = Modifier
){
    // Eingabefelder für Range
    var minInput by remember { mutableStateOf("1") }
    var maxInput by remember { mutableStateOf("200") }

    // SharedPreferences für Highscore
    val prefs = context.getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)

    // Score-System
    var score by remember { mutableStateOf(0) }  // aktueller Score
    var highscore by remember { mutableStateOf(prefs.getInt("highscore", 0)) } // gespeicherter Highscore
    var feedback by remember { mutableStateOf("") }

    // Aktuelle Quiz-Zahl
    var currentNumber by remember { mutableStateOf(0) }

    // Fokus-Manager
    val focusManager = LocalFocusManager.current

    // Erste Zahl generieren
    LaunchedEffect(Unit) {
        currentNumber = generateBiasedNumber(1, 200)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Text(
            text = "Primzahlen‑Quiz",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Score: $score",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Highscore: $highscore",
            style = MaterialTheme.typography.titleMedium
        )

        // Range-Eingabe
        OutlinedTextField(
            value = minInput,
            onValueChange = { minInput = it },
            label = { Text("Minimum") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = maxInput,
            onValueChange = { maxInput = it },
            label = { Text("Maximum") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Fokus entfernen
        Button(onClick = { focusManager.clearFocus() }) {
            Text("Eingabe verlassen")
        }

        Text(
            text = "Ist die Zahl $currentNumber eine Primzahl?",
            style = MaterialTheme.typography.titleLarge
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            // JA-Button
            Button(onClick = {
                val min = minInput.toIntOrNull() ?: 1
                val max = maxInput.toIntOrNull() ?: 500

                if (isPrime(currentNumber)) {
                    score++

                    // Highscore aktualisieren
                    if (score > highscore) {
                        highscore = score
                        prefs.edit().putInt("highscore", highscore).apply()
                    }

                    feedback = "Richtig! $currentNumber ist eine Primzahl."
                } else {
                    feedback = "Falsch! $currentNumber ist keine Primzahl."
                    score = 0
                }

                currentNumber = generateBiasedNumber(min, max)
            }) {
                Text("Ja")
            }

            // NEIN-Button
            Button(onClick = {
                val min = minInput.toIntOrNull() ?: 1
                val max = maxInput.toIntOrNull() ?: 200

                if (!isPrime(currentNumber)) {
                    score++

                    // Highscore aktualisieren
                    if (score > highscore) {
                        highscore = score
                        prefs.edit().putInt("highscore", highscore).apply()
                    }

                    feedback = "Richtig! $currentNumber ist keine Primzahl."
                } else {
                    feedback = "Falsch! $currentNumber ist eine Primzahl."
                    score = 0
                }

                currentNumber = generateBiasedNumber(min, max)
            }) {
                Text("Nein")
            }
        }

        Text(
            text = feedback,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

// 30% Primzahlen, 70% Nicht-Primzahlen (angepasst an Bedingung)
fun generateBiasedNumber(min: Int, max: Int): Int {
    val wantPrime = (1..100).random() <= 30
    val range = (min..max)

    return if (wantPrime) {
        val primes = range.filter { isPrime(it) }
        if (primes.isNotEmpty()) primes.random() else range.random()
    } else {
        // Nur zusammengesetzte Zahlen, die nicht durch 2-9 teilbar sind (z.B. 121, 143, 169...)
        val nonPrimes = range.filter { !isPrime(it) && (2..9).none { d -> it % d == 0 } }
        if (nonPrimes.isNotEmpty()) nonPrimes.random() else range.random()
    }
}
