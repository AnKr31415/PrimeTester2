package com.example.primetestertest

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import java.time.LocalDate

val Context.dataStore by preferencesDataStore(name = "user_prefs")

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

enum class FontScale {
    NORMAL, LARGE
}

object UserPreferences {
    val BIRTHDAY = stringPreferencesKey("birthday")
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val FONT_SCALE = stringPreferencesKey("font_scale")
    val SHOW_HOME = booleanPreferencesKey("show_home")
    val SHOW_FEATURES = booleanPreferencesKey("show_features")
    val SHOW_CALENDAR = booleanPreferencesKey("show_calendar")
    val SHOW_DIAGRAMM = booleanPreferencesKey("show_diagramm")
    val SHOW_FACTORISATION = booleanPreferencesKey("show_factorisation")
    val SHOW_PRIME_DAYS = booleanPreferencesKey("show_prime_days")
    val SHOW_QUIZ = booleanPreferencesKey("show_quiz")
    val SHOW_FIBONACCI = booleanPreferencesKey("show_fibonacci")
    val SHOW_SETTINGS = booleanPreferencesKey("show_settings")
}

suspend fun saveBirthday(context: Context, date: LocalDate) {
    context.dataStore.edit { prefs ->
        prefs[UserPreferences.BIRTHDAY] = date.toString()
    }
}

fun loadBirthday(context: Context) =
    context.dataStore.data.map { prefs ->
        prefs[UserPreferences.BIRTHDAY]?.let { LocalDate.parse(it) }
    }

suspend fun saveThemeMode(context: Context, mode: ThemeMode) {
    context.dataStore.edit { prefs ->
        prefs[UserPreferences.THEME_MODE] = mode.name
    }
}

fun loadThemeMode(context: Context) =
    context.dataStore.data.map { prefs ->
        val modeName = prefs[UserPreferences.THEME_MODE] ?: ThemeMode.SYSTEM.name
        ThemeMode.valueOf(modeName)
    }

suspend fun saveFontScale(context: Context, scale: FontScale) {
    context.dataStore.edit { prefs ->
        prefs[UserPreferences.FONT_SCALE] = scale.name
    }
}

fun loadFontScale(context: Context) =
    context.dataStore.data.map { prefs ->
        val scaleName = prefs[UserPreferences.FONT_SCALE] ?: FontScale.NORMAL.name
        FontScale.valueOf(scaleName)
    }

suspend fun saveVisibility(context: Context, key: androidx.datastore.preferences.core.Preferences.Key<Boolean>, visible: Boolean) {
    context.dataStore.edit { prefs ->
        prefs[key] = visible
    }
}

fun loadVisibility(context: Context, key: androidx.datastore.preferences.core.Preferences.Key<Boolean>, defaultValue: Boolean = true) =
    context.dataStore.data.map { prefs ->
        prefs[key] ?: defaultValue
    }
