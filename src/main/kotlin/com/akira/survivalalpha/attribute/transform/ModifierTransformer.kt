package com.akira.survivalalpha.attribute.transform

import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

abstract class ModifierTransformer {
    abstract fun apply(material: Material, meta: ItemMeta)

    protected fun ItemMeta.unmodify(name: String, type: Attribute) {
        val modifiers = this.getAttributeModifiers(type) ?: return

        for (modifier in modifiers) {
            if (modifier.name != name) continue
            this.removeAttributeModifier(type, modifier)
        }
    }

    protected fun ItemMeta.modify(name: String, type: Attribute, value: Double, op: Operation, slot: EquipmentSlotGroup) {
        this.addAttributeModifier(
            type,
            AttributeModifier(UUID.randomUUID(), name, value, op, slot)
        )
    }
}