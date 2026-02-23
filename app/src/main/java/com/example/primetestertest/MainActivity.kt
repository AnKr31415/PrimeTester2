package com.example.primetestertest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.primetestertest.ui.theme.PrimeTesterTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val themeMode by loadThemeMode(context).collectAsState(initial = ThemeMode.SYSTEM)
            val fontScale by loadFontScale(context).collectAsState(initial = FontScale.NORMAL)

            PrimeTesterTestTheme(
                themeMode = themeMode,
                fontScale = fontScale
            ) {
                PrimeTesterTestApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun PrimeTesterTestApp() {
    val context = LocalContext.current
    val achievementManager = remember { AchievementManager(context.dataStore) }

    // Popup-Logik für freigeschaltete Achievements
    LaunchedEffect(Unit) {
        achievementManager.newlyUnlocked.collect { achievement ->
            Toast.makeText(
                context,
                "🏆 Achievement freigeschaltet: ${achievement.title}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Sichtbarkeiten aus DataStore laden
    val showHome by loadVisibility(context, UserPreferences.SHOW_HOME, true).collectAsState(initial = true)
    val showCalendar by loadVisibility(context, UserPreferences.SHOW_CALENDAR, true).collectAsState(initial = true)
    val showFeatures by loadVisibility(context, UserPreferences.SHOW_FEATURES, true).collectAsState(initial = true)
    val showDiagramm by loadVisibility(context, UserPreferences.SHOW_DIAGRAMM, false).collectAsState(initial = false)
    val showFactorisation by loadVisibility(context, UserPreferences.SHOW_FACTORISATION, false).collectAsState(initial = false)
    val showPrimeDays by loadVisibility(context, UserPreferences.SHOW_PRIME_DAYS, false).collectAsState(initial = false)
    val showQuiz by loadVisibility(context, UserPreferences.SHOW_QUIZ, false).collectAsState(initial = false)
    val showFibonacci by loadVisibility(context, UserPreferences.SHOW_FIBONACCI, false).collectAsState(initial = false)
    val showSettings by loadVisibility(context, UserPreferences.SHOW_SETTINGS, false).collectAsState(initial = false)
    val showAchievements by loadVisibility(context, UserPreferences.SHOW_ACHIEVEMENTS, false).collectAsState(initial = false)


    // Map zur schnellen Prüfung der Sichtbarkeit
    val visibilityMap = remember(showHome, showCalendar, showFeatures, showDiagramm, showFactorisation, showPrimeDays, showQuiz, showFibonacci, showAchievements) {
        mapOf(
            AppDestinations.HOME to showHome,
            AppDestinations.CALENDAR to showCalendar,
            AppDestinations.FEATURES to showFeatures,
            AppDestinations.DIAGRAMM to showDiagramm,
            AppDestinations.FACTORISATION to showFactorisation,
            AppDestinations.PRIME_DAYS to showPrimeDays,
            AppDestinations.QUIZ to showQuiz,
            AppDestinations.FIBONACCI to showFibonacci,
            AppDestinations.SETTINGS to showSettings,
            AppDestinations.ACHIEVEMENTS to showAchievements
        )
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                if (visibilityMap[destination] == true) {
                    item(
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                        selected = destination == AppNavigation.currentDestination,
                        onClick = { AppNavigation.currentDestination = destination }
                    )
                }
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when (AppNavigation.currentDestination) {
                AppDestinations.HOME -> PrimeCheckerScreen(achievementManager = achievementManager, modifier = Modifier.padding(innerPadding))
                AppDestinations.PRIME_DAYS -> PrimeDaysScreen(context = LocalContext.current, modifier = Modifier.padding(innerPadding))
                AppDestinations.FEATURES -> FeaturesScreen(modifier = Modifier.padding(innerPadding))
                AppDestinations.DIAGRAMM -> DiagrammScreen(modifier = Modifier.padding(innerPadding))
                AppDestinations.FACTORISATION -> FactorisationScreen(context = LocalContext.current, modifier = Modifier.padding(innerPadding))
                AppDestinations.CALENDAR -> CalendarScreen(context = LocalContext.current, modifier = Modifier.padding(innerPadding))
                AppDestinations.QUIZ -> QuizScreen(context = LocalContext.current, modifier = Modifier.padding(innerPadding))
                AppDestinations.FIBONACCI -> FibonacciScreen(modifier = Modifier.padding(innerPadding))
                AppDestinations.SETTINGS -> SettingsScreen(modifier = Modifier.padding(innerPadding))
                AppDestinations.ACHIEVEMENTS -> AchievementList(manager = achievementManager)
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector
) {
    HOME("Home", Icons.Default.Home),
    CALENDAR("Kalender", Icons.Default.DateRange),
    FEATURES("Weiteres", Icons.Default.AccountBox),
    DIAGRAMM("Diagramm", Icons.Default.Star),
    FACTORISATION("Primfaktoren", Icons.Default.Star),
    PRIME_DAYS("Primzahltage", Icons.Default.Favorite),
    QUIZ("Quiz", Icons.Default.Star),
    FIBONACCI("Fibonacci", Icons.Default.Star),
    SETTINGS("Einstellungen", Icons.Default.Settings),
    ACHIEVEMENTS("Achievements", Icons.Default.Star)
}
