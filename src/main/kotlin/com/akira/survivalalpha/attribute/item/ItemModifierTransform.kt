package com.akira.survivalalpha.attribute.item

import com.akira.core.api.util.item.removeAttributeModifier
import com.akira.survivalalpha.attribute.TransformableEnchant
import com.akira.survivalalpha.util.ProtectedStorage
import com.akira.survivalalpha.util.matches
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

interface ItemModifierTransform {
    fun ItemMeta.setAttributeModifier(
        attribute: Attribute,
        name: String,
        amount: Double,
        operation: Operation,
        slotGroup: EquipmentSlotGroup
    ) {
        val modifiers = this.getAttributeModifiers(attribute)
        val dirty = modifiers?.none { it.matches(name, amount, operation, slotGroup) } ?: true

        if (!dirty) return
        this.removeAttributeModifier(attribute, name)

        val modifier = AttributeModifier(UUID.randomUUID(), name, amount, operation, slotGroup)
        this.addAttributeModifier(attribute, modifier)
    }

    fun apply(material: Material, meta: ItemMeta)

    companion object : ProtectedStorage<ItemModifierTransform>() {
        init {
            this.add(VanillaModifierTransform())

            TransformableEnchant.registryView.values
                .filter { it.isInherent }
                .forEach { this.add(EnchantModifierTransform(it)) }
        }

        fun transform(item: ItemStack) {
            item.editMeta { meta ->
                this.forEach { it.apply(item.type, meta) }
            }
        }
    }
}