package com.akira.survivalalpha.util

import org.bukkit.attribute.AttributeInstance
import org.bukkit.attribute.AttributeModifier.Operation
import kotlin.math.max

/**
 * 根据属性实例的基础值和修饰符重新计算出的属性值。
 *
 * 在原版，属性最终值 [AttributeInstance.value] 的上限受到限制，
 * 通过此属性可还原出限制前的原始属性值。
 */
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