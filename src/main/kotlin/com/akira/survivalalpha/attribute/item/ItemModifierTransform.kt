package com.akira.survivalalpha.attribute.item

import com.akira.survivalalpha.attribute.TransformableEnchant
import com.akira.survivalalpha.util.ProtectedStorage
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

interface ItemModifierTransform {
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