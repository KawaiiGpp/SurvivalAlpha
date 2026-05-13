package com.akira.survivalalpha.attribute.item

import com.akira.core.api.Registry
import com.akira.core.api.util.item.removeAttributeModifier
import com.akira.survivalalpha.attribute.EnchantFormula
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

interface ModifierTransform {
    fun ItemMeta.addAttributeModifier(
        attribute: Attribute,
        name: String,
        amount: Double,
        operation: Operation,
        slot: EquipmentSlotGroup
    ) {
        this.addAttributeModifier(
            attribute,
            AttributeModifier(UUID.randomUUID(), name, amount, operation, slot)
        )
    }

    fun apply(material: Material, meta: ItemMeta)

    object Vanilla : ModifierTransform {
        override fun apply(material: Material, meta: ItemMeta) {
            material.defaultAttributeModifiers.forEach { type, modifier ->
                val name = modifier.name

                meta.removeAttributeModifier(type, name)
                meta.addAttributeModifier(type, name, modifier.amount, modifier.operation, modifier.slotGroup)
            }
        }
    }

    object Enchant : Registry<EnchantFormula, EnchantTransform>() {
        init {
            EnchantFormula.registryView.values
                .filter { it.isInherent }
                .forEach { this.register(it, EnchantTransform(it)) }
        }
    }
}