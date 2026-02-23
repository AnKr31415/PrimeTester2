package com.example.primetestertest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AchievementManager(
    private val dataStore: DataStore<Preferences>
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    // Flow für neu freigeschaltete Achievements
    private val _newlyUnlocked = MutableSharedFlow<Achievement>(extraBufferCapacity = 5)
    val newlyUnlocked: SharedFlow<Achievement> = _newlyUnlocked.asSharedFlow()

    // DataStore-Schlüssel
    private val NUMBERS_CHECKED_KEY = intPreferencesKey("numbers_checked")
    private val PRIMES_FOUND_KEY = intPreferencesKey("primes_found")
    private val LARGEST_PRIME_KEY = longPreferencesKey("largest_prime")
    private val UNLOCKED_ACHIEVEMENTS_KEY = stringSetPreferencesKey("unlocked_achievements")

    var stats by mutableStateOf(Stats())
        private set

    var achievements by mutableStateOf(
        listOf(
            // Achievements für geprüfte Zahlen
            Achievement("check_10", "Anfänger", "10 Zahlen geprüft", false),
            Achievement("check_100", "Primzahl-Novize", "100 Zahlen geprüft", false),
            Achievement("check_1000", "Fleißiger Prüfer", "1.000 Zahlen geprüft", false),
            Achievement("check_10000", "Prüf-Meister", "10.000 Zahlen geprüft", false),

            // Achievements für gefundene Primzahlen
            Achievement("prime_1", "Erste Primzahl", "1. Primzahl gefunden", false),
            Achievement("prime_10", "Zehnerpack", "10 Primzahlen gefunden", false),
            Achievement("prime_50", "Primjäger", "50 Primzahlen gefunden", false),
            Achievement("prime_100", "Primzahl-Sammler", "100 Primzahlen gefunden", false),
            Achievement("prime_1000", "Primzahl-Experte", "1.000 Primzahlen gefunden", false),
            Achievement("prime_10000", "Primzahl-Legende", "10.000 Primzahlen gefunden", false)
        )
    )
        private set

    init {
        load()
    }

    fun onNumberChecked(isPrime: Boolean, value: Long) {
        stats = stats.copy(
            numbersChecked = stats.numbersChecked + 1,
            primesFound = stats.primesFound + if (isPrime) 1 else 0,
            largestPrime = maxOf(stats.largestPrime, if (isPrime) value else 0)
        )

        checkAchievements()
        save()
    }

    private fun checkAchievements() {
        val oldAchievements = achievements
        val newAchievements = oldAchievements.map { achievement ->
            val isUnlockedNow = when (achievement.id) {
                // geprüfte Zahlen
                "check_10" -> stats.numbersChecked >= 10
                "check_100" -> stats.numbersChecked >= 100
                "check_1000" -> stats.numbersChecked >= 1000
                "check_10000" -> stats.numbersChecked >= 10000

                // gefundene Primzahlen
                "prime_1" -> stats.primesFound >= 1
                "prime_10" -> stats.primesFound >= 10
                "prime_50" -> stats.primesFound >= 50
                "prime_100" -> stats.primesFound >= 100
                "prime_1000" -> stats.primesFound >= 1000
                "prime_10000" -> stats.primesFound >= 10000

                else -> achievement.unlocked
            }
            
            // Wenn es vorher gesperrt war und jetzt freigeschaltet ist -> Emit Event
            if (isUnlockedNow && !achievement.unlocked) {
                val unlockedAchievement = achievement.copy(unlocked = true)
                scope.launch {
                    _newlyUnlocked.emit(unlockedAchievement)
                }
                unlockedAchievement
            } else {
                achievement
            }
        }
        achievements = newAchievements
    }

    private fun save() {
        scope.launch {
            dataStore.edit { prefs ->
                prefs[NUMBERS_CHECKED_KEY] = stats.numbersChecked
                prefs[PRIMES_FOUND_KEY] = stats.primesFound
                prefs[LARGEST_PRIME_KEY] = stats.largestPrime
                
                val unlockedIds = achievements.filter { it.unlocked }.map { it.id }.toSet()
                prefs[UNLOCKED_ACHIEVEMENTS_KEY] = unlockedIds
            }
        }
    }

    private fun load() {
        scope.launch {
            val prefs = dataStore.data.first()
            
            stats = Stats(
                numbersChecked = prefs[NUMBERS_CHECKED_KEY] ?: 0,
                primesFound = prefs[PRIMES_FOUND_KEY] ?: 0,
                largestPrime = prefs[LARGEST_PRIME_KEY] ?: 0L
            )

            val unlockedIds = prefs[UNLOCKED_ACHIEVEMENTS_KEY] ?: emptySet()
            achievements = achievements.map { 
                it.copy(unlocked = it.id in unlockedIds)
            }
            
            // Wir führen checkAchievements hier NICHT aus, um Popups beim Start zu vermeiden
        }
    }
}
