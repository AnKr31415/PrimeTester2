package com.example.primetestertest

import java.time.LocalDate
import java.time.temporal.ChronoUnit

import kotlin.math.sqrt

fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    if (n == 2) return true
    if (n % 2 == 0) return false

    val limit = sqrt(n.toDouble()).toInt()
    for (i in 3..limit step 2) {
        if (n % i == 0) return false
    }
    return true
}

fun isPalindrome(n: Int): Boolean {
    val s = n.toString()
    return s == s.reversed()
}

fun nextPrime(n: Int): Int {
    var i = n + 1
    while (true) {
        if (isPrime(i)) {
            return i
        }
        i++
    }
}

fun primeBefore(n: Int): Int {
    var i = n - 1
    while (true) {
        if (isPrime(i)) {
            return i
        }
        i--
    }
}

fun generatePrimesInRange(start: Int, end: Int): List<Int> {
    return (start..end).filter { isPrime(it) }
}

fun isPrimeDay(date: LocalDate, birthday: LocalDate): Boolean {
    val days = ChronoUnit.DAYS.between(birthday, date).toInt()
    return isPrime(days)
}

fun getDays(date: LocalDate, birthday: LocalDate): Int {
    return ChronoUnit.DAYS.between(birthday,date).toInt()
}

fun primeFactors(n: Int): List<Int> {
    var num = n
    val factors = mutableListOf<Int>()

    var divisor = 2
    while (divisor * divisor <= num) {
        while (num % divisor == 0) {
            factors.add(divisor)
            num /= divisor
        }
        divisor++
    }
    if (num > 1) factors.add(num)

    return factors
}

fun getfibonacci(start: Int, end: Int): List<Int> {
    val fibonacciSequence = mutableListOf(0, 1)
    var i = 0
    while (fibonacciSequence[i+1]< end){
        val next = fibonacciSequence[i] + fibonacciSequence[i + 1]

        if (next<end){
            fibonacciSequence.add(next)
            i++
        }
        else if(next == end){
            fibonacciSequence.add(next)
            break
        }
        else if(next > end){
            break
        }
    }
    i = 0
    while (fibonacciSequence[i] < start){
        fibonacciSequence.removeAt(0)
    }
    return fibonacciSequence
}

fun getfibInd(index: Int): Int {
    val fibonacciSequence = mutableListOf(0, 1)
    var i = 0
    for (i in 0 until index) {
        val next = fibonacciSequence[i] + fibonacciSequence[i + 1]
        fibonacciSequence.add(next)
    }
    return fibonacciSequence[index-1]
}

fun fibprimes(start: Int, end: Int): List<Int> {
    val list = getfibonacci(start, end)
    val fibprimes: MutableList<Int> = mutableListOf()
    for(i in list){
        if(isPrime(i)){
            fibprimes.add(i)
        }
    }
    return fibprimes
}

fun isFibonacci(n: Int): Boolean {
    if (n < 0) return false
    val isSquare = { x: Long ->
        val s = sqrt(x.toDouble()).toLong()
        s * s == x
    }
    val nLong = n.toLong()
    return isSquare(5 * nLong * nLong + 4) || isSquare(5 * nLong * nLong - 4)
}
