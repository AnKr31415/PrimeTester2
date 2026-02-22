package com.example.primetestertest

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import android.content.Context

val Context.entryDataStore by preferencesDataStore("calender_entries")
private val ENTRIES_KEY = stringPreferencesKey("entries_json")

suspend fun saveEntries(context: Context, entries: Map<LocalDate, String>) {
    val json = entries.mapKeys { it.key.toString() }.toString()
    context.entryDataStore.edit { preferences ->
        preferences[ENTRIES_KEY] = json
    }
}

fun loadEntries(context: Context): Flow<Map<LocalDate, String>> =
    context.entryDataStore.data.map { preferences ->
        val json = preferences[ENTRIES_KEY] ?: "{}"

        json
            .removePrefix("{")
            .removeSuffix("}")
            .split(", ")
            .filter { it.contains("=")  }
            .associate {
                val (date, text ) = it.split("=")
                LocalDate.parse(date) to text

            }
}