package com.akira.survivalalpha.util

import org.bukkit.attribute.AttributeInstance
import org.bukkit.attribute.AttributeModifier.Operation
import kotlin.math.max

val AttributeInstance.recalculatedValue: Double
    get() {
        val modifiers = this.modifiers.asSequence()

        val flat = modifiers
            .filter { it.operation == Operation.ADD_NUMBER }
            .fold(baseValue) { acc, modifier -> acc + modifier.amount }

        val addition = modifiers
            .filter { it.operation == Operation.ADD_SCALAR }
            .fold(flat) { acc, modifier -> acc + (flat * modifier.amount) }

        val multiplication = modifiers
            .filter { it.operation == Operation.MULTIPLY_SCALAR_1 }
            .fold(addition) { acc, modifier -> acc * (1 + modifier.amount) }

        return max(multiplication, 0.0)
    }