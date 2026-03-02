package com.android.com.kotlin.one

fun main() {

    println("👋 Welcome to the Kotlin Playground!")
    println("Let's start learning step by step.\n")


    val city:String = "Port-au-Prince";
    var temperature: Double = 3.14;
    println("It is "+temperature+"°C in "+city)
    // ✅ EXERCISE 1 Variables:
    // Create two variables: `city` (String) and `temperature` (Double)
    // Then print: "It is {temperature}°C in {city}"
    // Enforce `city` to be immutable and `temperature` mutable
    // Then print the sentence again after changing `temperature`


    var score:Int = 53;
    var message:String="";
    if(score==100)
        message="Perfect score";
    else if(score <0 || score > 100)
        message="Invalid score";
    else if(score>=0 && score <= 49)
        message="You failed!";
    else if(score>=50 && score <= 60)
        message="Just passed!";
    else if(score>=61 && score <= 99)
        message="Well done!";
    println(message);



    // ✅ EXERCISE 3 list and Loops:
    // Create a list of your favorite fruits
    // Loop through the list and print each fruit in uppercase
    // Then, print the total number of fruits in the list
    // Ask the user to enter a fruit name and check if it's in the list

    TODO(
        "Exercise 3 List and Loops implementation"
    )

    // ✅EXERCISE 4 Elvis Operator:
    // Create a nullable variable `nickname` of type String? and assign it null
    // Print the number of characters in `nickname`
    // Print the nickname or "No nickname provided" if it's null using the Elvis operator

    TODO(
        "Exercise 4 Elvis Operator implementation"
    )

}

