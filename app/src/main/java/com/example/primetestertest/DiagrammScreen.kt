package com.example.primetestertest

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun DiagrammScreen(modifier: Modifier = Modifier) {
    var startText by remember { mutableStateOf("1") }
    var endText by remember { mutableStateOf("100") }

    var primes by remember { mutableStateOf(listOf<Int>()) }

    val focusManager = LocalFocusManager.current


    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        Text(
            "Diagramm",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = startText,
            onValueChange = { startText = it },
            label = { Text("Startwert") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = endText,
            onValueChange = { endText = it },
            label = { Text("Endwert") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        Button(
            onClick = {
                val start = startText.toIntOrNull() ?: 1
                val end = endText.toIntOrNull() ?: 100
                primes = generatePrimesInRange(start, end)
                focusManager.clearFocus()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Diagramm anzeigen")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (primes.isNotEmpty()) {
            PrimeScatterWithLabels(
                primes = primes,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun PrimeScatterWithLabels(
    primes: List<Int>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        // Y-Achsentitel
        Text(
            "Primzahl",
            modifier = Modifier.padding(start = 32.dp, bottom = 4.dp)
        )

        Row(
            modifier = Modifier.weight(1f)
        ) {
            // Keine Y-Achsenwerte mehr
            Spacer(modifier = Modifier.width(16.dp))

            // Diagramm
            PrimeScatterPlot(
                primes = primes,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }

        // Keine X-Achsenwerte mehr
        Spacer(modifier = Modifier.height(8.dp))

        // X-Achsentitel
        Text(
            "Index",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp)
        )
    }
}
@Composable
fun PrimeScatterPlot(
    primes: List<Int>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {

        if (primes.isEmpty()) return@Canvas

        val maxX = primes.size.toFloat()
        val maxY = primes.maxOrNull()?.toFloat() ?: 1f

        val padding = 40f
        val width = size.width - padding * 2
        val height = size.height - padding * 2

        // Gitterlinien (optional)
        val steps = 4
        for (i in 0..steps) {
            val y = padding + height - (i / steps.toFloat()) * height
            drawLine(Color.LightGray, Offset(padding, y), Offset(padding + width, y))
        }
        for (i in 0..steps) {
            val x = padding + (i / steps.toFloat()) * width
            drawLine(Color.LightGray, Offset(x, padding), Offset(x, padding + height))
        }

        // Achsen
        drawLine(Color.Black, Offset(padding, padding), Offset(padding, padding + height), 3f)
        drawLine(Color.Black, Offset(padding, padding + height), Offset(padding + width, padding + height), 3f)

        // Punkte
        primes.forEachIndexed { index, prime ->
            val x = padding + (index / maxX) * width
            val y = padding + height - (prime / maxY) * height

            drawCircle(Color.Red, radius = 6f, center = Offset(x, y))
        }
    }
}