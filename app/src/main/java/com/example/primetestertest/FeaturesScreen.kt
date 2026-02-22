package com.example.primetestertest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import com.example.primetestertest.ui.theme.DeepBlue


@Composable
fun FeaturesScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        Text(
            "Weitere Anwendungen:",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = { AppNavigation.currentDestination = AppDestinations.DIAGRAMM },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Diagramm")
        }

        Button(
            onClick = { AppNavigation.currentDestination = AppDestinations.FACTORISATION },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Primfaktoren")
        }

        Button(
            onClick = { AppNavigation.currentDestination = AppDestinations.PRIME_DAYS },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Primzahltage")
        }

        Button(
            onClick = { AppNavigation.currentDestination = AppDestinations.QUIZ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Quiz")
        }

        Button(
            onClick = { AppNavigation.currentDestination = AppDestinations.FIBONACCI},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) { Text("Fibonacci") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            FloatingActionButton(
                onClick = {
                    AppNavigation.currentDestination = AppDestinations.SETTINGS
                },
                containerColor = DeepBlue,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }

    }
}