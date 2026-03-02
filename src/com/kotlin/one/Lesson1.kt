package com.android.com.kotlin.one

fun main() {

    println("👋 Welcome to the Kotlin Playground!")
    println("Let's start learning step by step.\n")


    val city:String = "Port-au-Prince";
    var temperature: Double = 3.14;
    println("It is "+temperature+"°C in "+city);
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


        val fruits = listOf("mango", "banana", "pineapple", "orange");

        for (fruit in fruits) {
            println(fruit.uppercase());
        }

        println("Total fruits: ${fruits.size}");

        print("Enter a fruit name: ");
        val input = readLine()?.trim()?.lowercase();

        if (input != null && input in fruits) {
            println("Yes, $input is in the list!");
        } else {
            println("No, it's not in the list.");
        }


    val nickname: String? = null;

    val length = nickname?.length ?: 0;
    println("Nickname length: $length");

    val displayName = nickname ?: "No nickname provided";
    println(displayName);



}

