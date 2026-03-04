package com.android.com.kotlin.one;

/**
 * Simple 2-player turn-based battle game (console only).
 *
 * Goal: kill all characters of the opponent team.
 * Each player builds a team of 3 characters:
 * - Unique name globally (across both players)
 * - Unique type inside the team (Warrior, Magus, Colossus, Dwarf)
 *
 * Concepts used:
 * - Classes: Character, Weapon, Player, Game
 * - Inheritance: Warrior/Magus/Colossus/Dwarf inherit from Character
 * - Interfaces: Attacker, Healer
 * - Abstract class: Character
 * - Polymorphism: List<Character> contains mixed subclasses
 * - Encapsulation: hp is private set; only damage/heal methods can modify it
 * - Composition: Character has a Weapon
 *
 * Run: put this file as Main.kt and run with Kotlin/JVM.
 */

import kotlin.math.max

// ----------------------------- Domain: Weapon -----------------------------

data class Weapon(val name: String, val power: Int)

// ----------------------------- Abilities (Interfaces) -----------------------------

interface Attacker {
    fun attack(target: Character): String
}

interface Healer {
    fun heal(target: Character): String
}

// ----------------------------- Domain: Character (Abstract) -----------------------------

abstract class Character(
    val typeName: String,
    val name: String,
    val weapon: Weapon,
    maxHp: Int
) {
    var hp: Int = maxHp
        private set

    val isAlive: Boolean
        get() = hp > 0

    fun takeDamage(amount: Int): Int {
        if (!isAlive) return 0
        val dmg = max(0, amount)
        val before = hp
        hp = max(0, hp - dmg)
        return before - hp
    }

    fun receiveHeal(amount: Int): Int {
        if (!isAlive) return 0
        val heal = max(0, amount)
        val before = hp
        hp = minOf(maxHpValue(), hp + heal)
        return hp - before
    }

    // Keep max HP accessible only to internal logic.
    private val _maxHp: Int = maxHp
    fun maxHpValue(): Int = _maxHp

    override fun toString(): String {
        val status = if (isAlive) "ALIVE" else "DEAD"
        return "$name [$typeName] HP=$hp/${maxHpValue()} Weapon=${weapon.name}(${weapon.power}) Status=$status"
    }
}

// ----------------------------- Concrete Characters -----------------------------

// Choose values freely (simple balanced numbers):
// Warrior   : medium HP, medium damage
// Magus     : high HP, low damage, can heal
// Colossus  : very high HP, medium damage
// Dwarf     : low HP, very high damage

class Warrior(name: String) : Character(
    typeName = "Warrior",
    name = name,
    weapon = Weapon("Sword", power = 25),
    maxHp = 110
), Attacker {
    override fun attack(target: Character): String {
        val dealt = target.takeDamage(weapon.power)
        return "${name} attacks ${target.name} with ${weapon.name} for $dealt damage."
    }
}

class Magus(name: String) : Character(
    typeName = "Magus",
    name = name,
    weapon = Weapon("Staff", power = 12),
    maxHp = 140
), Attacker, Healer {
    private val healPower = 30

    override fun attack(target: Character): String {
        val dealt = target.takeDamage(weapon.power)
        return "${name} attacks ${target.name} with ${weapon.name} for $dealt damage."
    }

    override fun heal(target: Character): String {
        val healed = target.receiveHeal(healPower)
        return "${name} heals ${target.name} for $healed HP."
    }
}

class Colossus(name: String) : Character(
    typeName = "Colossus",
    name = name,
    weapon = Weapon("Hammer", power = 22),
    maxHp = 180
), Attacker {
    override fun attack(target: Character): String {
        val dealt = target.takeDamage(weapon.power)
        return "${name} smashes ${target.name} with ${weapon.name} for $dealt damage."
    }
}

class Dwarf(name: String) : Character(
    typeName = "Dwarf",
    name = name,
    weapon = Weapon("Axe", power = 40),
    maxHp = 80
), Attacker {
    override fun attack(target: Character): String {
        val dealt = target.takeDamage(weapon.power)
        return "${name} strikes ${target.name} with ${weapon.name} for $dealt damage."
    }
}

// ----------------------------- Player -----------------------------

class Player(val label: String) {
    private val team = mutableListOf<Character>()

    fun addCharacter(c: Character) {
        team.add(c)
    }

    fun livingTeam(): List<Character> = team.filter { it.isAlive }

    fun allTeam(): List<Character> = team.toList()

    fun isDefeated(): Boolean = livingTeam().isEmpty()

    fun printTeam(showDead: Boolean = true) {
        println("=== $label Team ===")
        val list = if (showDead) team else livingTeam()
        list.forEachIndexed { i, c -> println("${i + 1}. $c") }
    }
}

// ----------------------------- Game -----------------------------

class Game {
    private val usedNames = mutableSetOf<String>()
    private val p1 = Player("Player 1")
    private val p2 = Player("Player 2")
    private var turns = 0

    fun run() {
        println("==== Team Battle (Console) ====")
        println("Each player creates 3 characters (unique type per team, unique name globally).")
        println()

        createTeam(p1)
        println()
        createTeam(p2)
        println()

        fightLoop()

        println("\n==== GAME OVER ====")
        val winner = if (p1.isDefeated()) p2.label else p1.label
        println("Winner: $winner")
        println("Turns played: $turns")
        println()

        p1.printTeam(showDead = true)
        println()
        p2.printTeam(showDead = true)
    }

    private fun createTeam(player: Player) {
        println("---- ${player.label}: Create your team ----")
        val availableTypes = mutableSetOf("Warrior", "Magus", "Colossus", "Dwarf")

        while (player.allTeam().size < 3) {
            println("\nChoose a type for character #${player.allTeam().size + 1}")
            println("Available types: ${availableTypes.joinToString(", ")}")
            val type = readChoice(availableTypes)

            println("Enter a unique name for this $type:")
            val name = readUniqueName()

            val character = createCharacter(type, name)
            player.addCharacter(character)
            availableTypes.remove(type)

            println("Added: $character")
        }
    }

    private fun createCharacter(type: String, name: String): Character {
        return when (type) {
            "Warrior" -> Warrior(name)
            "Magus" -> Magus(name)
            "Colossus" -> Colossus(name)
            "Dwarf" -> Dwarf(name)
            else -> error("Unknown type: $type")
        }
    }

    private fun fightLoop() {
        var active = p1
        var passive = p2

        while (!p1.isDefeated() && !p2.isDefeated()) {
            turns += 1
            println("\n==============================")
            println("TURN #$turns - ${active.label} to play")
            println("==============================")

            active.printTeam(showDead = false)
            println()
            passive.printTeam(showDead = false)
            println()

            val actor = chooseLivingCharacter(active, "Choose your character:")
            val action = chooseAction(actor)

            when (action) {
                "ATTACK" -> {
                    val target = chooseLivingCharacter(passive, "Choose enemy target to attack:")
                    val msg = (actor as Attacker).attack(target)
                    println(msg)
                    if (!target.isAlive) println("${target.name} is DEAD!")
                }
                "HEAL" -> {
                    val target = chooseLivingCharacter(active, "Choose ally target to heal:")
                    val msg = (actor as Healer).heal(target)
                    println(msg)
                }
            }

            // swap players
            val tmp = active
            active = passive
            passive = tmp
        }
    }

    // ----------------------------- Input helpers -----------------------------

    private fun readChoice(allowed: Set<String>): String {
        while (true) {
            print("> ")
            val input = readln().trim()
            val normalized = input.replaceFirstChar { it.uppercase() }
            // allow exact match or case-insensitive
            val match = allowed.firstOrNull { it.equals(input, ignoreCase = true) }
                ?: allowed.firstOrNull { it.equals(normalized, ignoreCase = true) }

            if (match != null) return match
            println("Invalid choice. Allowed: ${allowed.joinToString(", ")}")
        }
    }

    private fun readUniqueName(): String {
        while (true) {
            print("> ")
            val name = readln().trim()
            if (name.isBlank()) {
                println("Name cannot be empty.")
                continue
            }
            if (usedNames.contains(name.lowercase())) {
                println("Name already used. Choose another.")
                continue
            }
            usedNames.add(name.lowercase())
            return name
        }
    }

    private fun chooseLivingCharacter(player: Player, prompt: String): Character {
        val living = player.livingTeam()
        while (true) {
            println(prompt)
            living.forEachIndexed { i, c -> println("${i + 1}. ${c.name} [${c.typeName}] HP=${c.hp}/${c.maxHpValue()}") }
            print("> ")
            val input = readln().trim().toIntOrNull()
            if (input == null || input !in 1..living.size) {
                println("Invalid number.")
                continue
            }
            return living[input - 1]
        }
    }

    private fun chooseAction(actor: Character): String {
        val canHeal = actor is Healer
        val canAttack = actor is Attacker

        // In this game all characters can attack, but keep it generic.
        val options = mutableListOf<String>()
        if (canAttack) options.add("ATTACK")
        if (canHeal) options.add("HEAL")

        while (true) {
            println("Choose action for ${actor.name} (${actor.typeName}): ${options.joinToString(" / ")}")
            print("> ")
            val input = readln().trim()
            val match = options.firstOrNull { it.equals(input, ignoreCase = true) }
            if (match != null) return match
            println("Invalid action.")
        }
    }
}

// ----------------------------- Main -----------------------------

fun main() {
    Game().run()
}