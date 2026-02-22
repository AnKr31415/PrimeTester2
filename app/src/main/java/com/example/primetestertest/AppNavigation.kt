package com.example.primetestertest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AppNavigation {
    var currentDestination by mutableStateOf(AppDestinations.HOME)
}
