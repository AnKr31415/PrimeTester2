package com.example.primetestertest

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material3.Checkbox


@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val currentThemeMode by loadThemeMode(context).collectAsState(initial = ThemeMode.SYSTEM)
    val currentFontScale by loadFontScale(context).collectAsState(initial = FontScale.NORMAL)

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Einstellungen",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Design",
            style = MaterialTheme.typography.titleMedium,
        )

        ThemeSelectionRow(
            label = "Dunkler Modus",
            selected = currentThemeMode == ThemeMode.DARK,
            onClick = {
                scope.launch { saveThemeMode(context, ThemeMode.DARK) }
            }
        )

        ThemeSelectionRow(
            label = "Heller Modus",
            selected = currentThemeMode == ThemeMode.LIGHT,
            onClick = {
                scope.launch { saveThemeMode(context, ThemeMode.LIGHT) }
            }
        )

        ThemeSelectionRow(
            label = "System Standard",
            selected = currentThemeMode == ThemeMode.SYSTEM,
            onClick = {
                scope.launch { saveThemeMode(context, ThemeMode.SYSTEM) }
            }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = "Schriftgröße",
            style = MaterialTheme.typography.titleMedium,
        )

        ThemeSelectionRow(
            label = "Normal",
            selected = currentFontScale == FontScale.NORMAL,
            onClick = {
                scope.launch { saveFontScale(context, FontScale.NORMAL) }
            }
        )

        ThemeSelectionRow(
            label = "Groß",
            selected = currentFontScale == FontScale.LARGE,
            onClick = {
                scope.launch { saveFontScale(context, FontScale.LARGE) }
            }
        )

        Spacer(modifier = Modifier.padding(16.dp))

        ExpandableCard(
            "Funktionen Sortieren",
            content = {
                val showHome by loadVisibility(context, UserPreferences.SHOW_HOME, true).collectAsState(initial = true)
                val showFeatures by loadVisibility(context, UserPreferences.SHOW_FEATURES, true).collectAsState(initial = true)
                val showCalendar by loadVisibility(context, UserPreferences.SHOW_CALENDAR, true).collectAsState(initial = true)
                val showDiagramm by loadVisibility(context, UserPreferences.SHOW_DIAGRAMM, false).collectAsState(initial = false)
                val showFactorisation by loadVisibility(context, UserPreferences.SHOW_FACTORISATION, false).collectAsState(initial = false)
                val showPrimeDays by loadVisibility(context, UserPreferences.SHOW_PRIME_DAYS, false).collectAsState(initial = false)
                val showQuiz by loadVisibility(context, UserPreferences.SHOW_QUIZ, false).collectAsState(initial = false)
                val showFibonacci by loadVisibility(context, UserPreferences.SHOW_FIBONACCI, false).collectAsState(initial = false)
                val showSettings by loadVisibility(context, UserPreferences.SHOW_SETTINGS, false).collectAsState(initial = false)


                Text("Anwendungen, die in der Navigationsleiste angezeigt werden")

                Spacer(modifier = Modifier.padding(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = showHome,
                        onCheckedChange = { scope.launch { saveVisibility(context, UserPreferences.SHOW_HOME, it) } }
                    )
                    Text(text = "Home", modifier = Modifier.padding(start = 4.dp))
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = showCalendar,
                        onCheckedChange = { scope.launch { saveVisibility(context, UserPreferences.SHOW_CALENDAR, it) } }
                    )
                    Text(text = "Kalender", modifier = Modifier.padding(start = 4.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = showFeatures,
                        onCheckedChange = { scope.launch { saveVisibility(context, UserPreferences.SHOW_FEATURES, true) } }
                    )
                    Text(text = "Weiteres", modifier = Modifier.padding(start = 4.dp))
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = showFibonacci,
                        onCheckedChange = { scope.launch { saveVisibility(context, UserPreferences.SHOW_FIBONACCI, it) } }
                    )
                    Text(text = "Fibonacci-Zahlen", modifier = Modifier.padding(start = 4.dp))
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = showQuiz,
                        onCheckedChange = { scope.launch { saveVisibility(context, UserPreferences.SHOW_QUIZ, it) } }
                    )
                    Text(text = "Quiz", modifier = Modifier.padding(start = 4.dp))
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = showDiagramm,
                        onCheckedChange = { scope.launch { saveVisibility(context, UserPreferences.SHOW_DIAGRAMM, it) } }
                    )
                    Text(text = "Diagramm", modifier = Modifier.padding(start = 4.dp))
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = showFactorisation,
                        onCheckedChange = { scope.launch { saveVisibility(context, UserPreferences.SHOW_FACTORISATION, it) } }
                    )
                    Text(text = "Primfaktoren", modifier = Modifier.padding(start = 4.dp))
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = showPrimeDays,
                        onCheckedChange = { scope.launch { saveVisibility(context, UserPreferences.SHOW_PRIME_DAYS, it) } }
                    )
                    Text(text = "Primzahltage", modifier = Modifier.padding(start = 4.dp))
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically){
                    Checkbox(
                        checked = showSettings,
                        onCheckedChange = { scope.launch { saveVisibility(context, UserPreferences.SHOW_SETTINGS, it) } }
                    )
                    Text(text = "Einstellungen", modifier = Modifier.padding(start = 4.dp))
                }
            }
        )
    }
}


@Composable
fun ThemeSelectionRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = label, modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
fun ExpandableCard(title: String, content: @Composable () -> Unit) {

    var expanded by remember{mutableStateOf(false)}

    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp)

    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }

        }


    }
}
