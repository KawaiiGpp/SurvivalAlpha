package com.akira.survivalalpha.attribute.item

import org.bukkit.Material
import org.bukkit.inventory.meta.ItemMeta

class VanillaModifierTransform: ItemModifierTransform {
    override fun apply(material: Material, meta: ItemMeta) {
        material.defaultAttributeModifiers.forEach { attribute, modifier ->
            meta.setAttributeModifier(
                attribute,
                modifier.name,
                modifier.amount,
                modifier.operation,
                modifier.slotGroup
            )
        }
    }
}