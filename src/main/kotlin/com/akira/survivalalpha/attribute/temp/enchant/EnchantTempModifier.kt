package com.akira.survivalalpha.attribute.temp.enchant

import com.akira.survivalalpha.attribute.temp.TempModifier
import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import java.util.*

abstract class EnchantTempModifier(
    name: String,
    attribute: Attribute,
    protected val enchantment: Enchantment,
    protected val multiplier: Double
) : TempModifier(
    name,
    attribute
) {
    protected val modifierName = "Temp enchant modifier ${enchantment.key}"

    protected fun createModifier(item: ItemStack?): AttributeModifier? {
        val level = item?.enchantments[enchantment] ?: return null

        return AttributeModifier(
            UUID.randomUUID(),
            modifierName,
            level * multiplier,
            Operation.ADD_SCALAR,
            item.type.equipmentSlot.group
        )
    }

    final override fun generateFor(target: Attributable): List<AttributeModifier> {
        return if (target !is LivingEntity) listOf()
        else this.generateFor(target)
    }

    protected abstract fun generateFor(target: LivingEntity): List<AttributeModifier>
}