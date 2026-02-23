package com.example.primetestertest

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val unlocked: Boolean
)

data class Stats(
    val numbersChecked: Int = 0,
    val primesFound: Int = 0,
    val largestPrime: Long = 0
)