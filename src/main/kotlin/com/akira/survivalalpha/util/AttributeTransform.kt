package com.akira.survivalalpha.util

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

fun ItemStack.transform() {
    this.editMeta { meta ->
        fun transformEnchant(enchant: Enchantment, type: Attribute, func: (Int) -> Double) {
            val name = "Enchant modifier ${enchant.key}"
            meta.removeModifier(name, type)

            meta.enchants[enchant]?.let { level ->
                meta.addModifier(
                    name,
                    type,
                    func(level),
                    Operation.ADD_SCALAR,
                    this.type.equipmentSlot.group
                )
            }
        }

        this.type.defaultAttributeModifiers.forEach { type, modifier ->
            val name = modifier.name

            meta.removeModifier(name, type)
            meta.addModifier(name, type, modifier.amount, modifier.operation, modifier.slotGroup)
        }

        transformEnchant(Enchantment.PROTECTION, Attribute.GENERIC_ARMOR) { it * 0.05 }
        transformEnchant(Enchantment.SHARPNESS, Attribute.GENERIC_ATTACK_DAMAGE) { it * 0.12 }
    }
}

private fun ItemMeta.removeModifier(name: String, type: Attribute) {
    val modifiers = this.attributeModifiers

    modifiers?.let {
        modifiers[type]
            .filter { it.name == name }
            .forEach { this.removeAttributeModifier(type, it) }
    }
}

private fun ItemMeta.addModifier(name: String, type: Attribute, value: Double, op: Operation, slot: EquipmentSlotGroup) {
    this.addAttributeModifier(
        type,
        AttributeModifier(
            UUID.randomUUID(),
            name,
            value,
            op,
            slot
        )
    )
}