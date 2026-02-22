package com.example.primetestertest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import  androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun FibonacciScreen(modifier: Modifier = Modifier) {
    var rangeStart by rememberSaveable { mutableStateOf("") }
    var rangeEnd by rememberSaveable { mutableStateOf("") }
    var fibonacciList by rememberSaveable { mutableStateOf(listOf<Int>()) }
    var input1 by rememberSaveable {mutableStateOf("") }
    var input2 by rememberSaveable {mutableStateOf("") }
    var start2 by rememberSaveable { mutableStateOf("") }
    var end2 by rememberSaveable { mutableStateOf("") }
    var fibprimeList by rememberSaveable { mutableStateOf(listOf<Int>()) }
    val scrollStatetotal = rememberScrollState()
    val focusManager = LocalFocusManager.current



    Column(
        modifier = modifier
            .fillMaxSize()          // WICHTIG: Column bekommt eine feste Höhe
            .padding(16.dp)
            .verticalScroll(scrollStatetotal)

    ) {
        OutlinedTextField(
            value = rangeStart,
            onValueChange = { rangeStart = it },
            label = { Text("Start") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        OutlinedTextField(
            value = rangeEnd,
            onValueChange = { rangeEnd = it },
            label = { Text("Ende") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )


        Button(
            onClick = {
            val start = rangeStart.toIntOrNull()
            val end = rangeEnd.toIntOrNull()

                fibonacciList =
                if (start == null || end == null || start > end) {
                    emptyList()
                }
                else {
                    getfibonacci(start, end)
                }
                focusManager.clearFocus()
            },
            modifier = Modifier.padding(top=16.dp)
        ) {
            Text("Bereich prüfen")
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) { Text(
            text = fibonacciList.joinToString(", "),
            modifier = Modifier.padding(4.dp)
        )
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically)
        {

            OutlinedTextField(
                value = input1,
                onValueChange = { input1 = it },
                label = { Text("Fibonacci Zahl") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = input2,
                onValueChange = {input2 = it},
                label = { Text("Index") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {
                    var zahl = input1.toIntOrNull()
                    var index = input2.toIntOrNull()
                    if (index == null && zahl == null){
                        var text = "Geben Sie eine Fibonacci-Zahl oder den Index einer Zahl ein."
                    }
                    else if(index == null && zahl != null) {
                        var text = "$zahl"
                        var fiblist = getfibonacci(0, zahl)
                        if (fiblist.contains(zahl)) {
                            index = fiblist.indexOf(zahl)
                            text = "$zahl ist die $index. Fibonacci-Zahl"
                            input2 = (index+1).toString()
                        }
                        else {
                            text = "$zahl ist keine Fibonacci-Zahl"
                        }

                    }
                    else if(index != null && zahl == null) {
                        var text = "$index"
                        var zahl = getfibInd(index)
                        text = "Die Fibonacci-Zahl an der Stelle $index ist $zahl"
                        input1 = zahl.toString()
                    }
                    focusManager.clearFocus()
                }
            ) {
                Text("Umrechnen")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    input1 = ""
                    input2 = ""
                    focusManager.clearFocus()
                }
            ) {
                Text("Zurücksetzen")
            }

        }

        Spacer(modifier =  Modifier.padding(16.dp))

        Text("Fibonacci Primzahlen")

        OutlinedTextField(
            value = start2,
            onValueChange = { start2 = it },
            label = { Text("Start") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )

        )

        OutlinedTextField(
            value = end2,
            onValueChange = { end2 = it },
            label = { Text("Ende") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )

        )

        Button(
            onClick = {
                val start = start2.toIntOrNull()
                val end = end2.toIntOrNull()

                fibprimeList =
                    if (start == null || end == null || start > end) {
                        emptyList()
                    }
                    else {
                        fibprimes(start, end)
                    }
                focusManager.clearFocus()
            }
        ) { Text("Ausrechnen") }


        Column(
            modifier = Modifier
                .padding(16.dp)
        ) { Text(
            text = fibprimeList.joinToString(", "),
            modifier = Modifier.padding(4.dp)
        )
        }

    }
}