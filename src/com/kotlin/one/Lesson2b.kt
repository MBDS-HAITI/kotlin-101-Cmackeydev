package com.android.com.kotlin.one


var passed = 0
var failed = 0

fun verify(name: String, block: () -> Boolean) {
    try {
        check(block()) { "❌ Test failed: $name" }
        println("✅ $name")
        passed++
    } catch (e: Throwable) {
        println("❌ $name → ${e.message}")
        failed++
    }
}

fun ex1_createImmutableList(): List<Int> {
    return listOf(1, 2, 3, 4, 5)
}

fun ex2_createMutableList(): MutableList<String> {
    val list = mutableListOf("one", "two", "three")
    list.add("four")
    return list
}

fun ex3_filterEvenNumbers(): List<Int> {
    return (1..10).toList().filter { it % 2 == 0 }
}

fun ex4_filterAndMapAges(ages: List<Int>): List<String> {
    return ages
        .filter { it >= 18 }
        .map { "Adult: $it" }
}

fun ex5_flattenList(): List<Int> {
    val nested = listOf(listOf(1, 2), listOf(3, 4), listOf(5))
    return nested.flatten()
}

fun ex6_flatMapWords(): List<String> {
    val phrases = listOf("Kotlin is fun", "I love lists")
    return phrases.flatMap { it.split(" ").filter { w -> w.isNotBlank() } }
}

fun ex7_eagerProcessing(): List<Long> {
    val start = System.currentTimeMillis()

    val result = (1..1_000_000)
        .toList() // eager
        .filter { it % 3 == 0 }
        .map { n -> n.toLong() * n.toLong() }
        .take(5)

    val end = System.currentTimeMillis()
    println("Time ex7 (eager): ${end - start} ms")

    return result
}

fun ex8_lazyProcessing(): List<Long> {
    val start = System.currentTimeMillis()

    val result = (1..1_000_000)
        .asSequence() // lazy
        .filter { it % 3 == 0 }
        .map { n -> n.toLong() * n.toLong() }
        .take(5)
        .toList()

    val end = System.currentTimeMillis()
    println("Time ex8 (lazy): ${end - start} ms")

    return result
}

fun ex9_filterAndSortNames(names: List<String>): List<String> {
    return names
        .filter { it.startsWith("A", ignoreCase = true) }
        .map { it.uppercase() }
        .sorted()
}

fun main() {

    verify("ex1_createImmutableList returns 5 ints") {
        ex1_createImmutableList() == listOf(1, 2, 3, 4, 5)
    }

    verify("ex2_createMutableList adds one element") {
        ex2_createMutableList() == mutableListOf("one", "two", "three", "four")
    }

    verify("ex3_filterEvenNumbers returns evens") {
        ex3_filterEvenNumbers() == listOf(2, 4, 6, 8, 10)
    }

    verify("ex4_filterAndMapAges filters >=18 and maps") {
        val ages = listOf(10, 18, 25, 17, 40)
        ex4_filterAndMapAges(ages) == listOf("Adult: 18", "Adult: 25", "Adult: 40")
    }

    verify("ex5_flattenList flattens correctly") {
        ex5_flattenList() == listOf(1, 2, 3, 4, 5)
    }

    verify("ex6_flatMapWords extracts all words") {
        ex6_flatMapWords() == listOf("Kotlin", "is", "fun", "I", "love", "lists")
    }

    verify("ex7_eagerProcessing first 5 squares of multiples of 3") {
        ex7_eagerProcessing() == listOf(9L, 36L, 81L, 144L, 225L)
    }

    verify("ex8_lazyProcessing first 5 squares of multiples of 3") {
        ex8_lazyProcessing() == listOf(9L, 36L, 81L, 144L, 225L)
    }

    verify("ex9_filterAndSortNames filters A* and sorts uppercase") {
        val names = listOf("Bob", "alice", "Anna", "Mackey", "adam", "Zoe", "Alex")
        ex9_filterAndSortNames(names) == listOf("ADAM", "ALEX", "ALICE", "ANNA")
    }

    println("\n==== SUMMARY ====")
    println("Passed: $passed")
    println("Failed: $failed")
}