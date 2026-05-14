package com.akira.survivalalpha.util

import org.bukkit.attribute.AttributeInstance
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.EquipmentSlotGroup
import kotlin.math.max

/**
 * 根据基础值和修饰符重新计算出的属性值
 *
 * - 可绕过原版根据属性类型施加的上限
 */
val AttributeInstance.uncappedValue: Double
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

/**
 * 判断该修饰符是否除 `UUID` 外的内容均相同。
 */
fun AttributeModifier.matches(
    name: String,
    amount: Double,
    operation: Operation,
    slotGroup: EquipmentSlotGroup
): Boolean {
    return this.name == name
            && this.amount == amount
            && this.operation == operation
            && this.slotGroup == slotGroup
}