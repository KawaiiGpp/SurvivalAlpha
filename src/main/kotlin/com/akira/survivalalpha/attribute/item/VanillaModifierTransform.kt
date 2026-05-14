package com.akira.survivalalpha.attribute.item

import com.akira.core.api.util.item.removeAttributeModifier
import org.bukkit.Material
import org.bukkit.inventory.meta.ItemMeta

class VanillaModifierTransform: ItemModifierTransform {
    override fun apply(material: Material, meta: ItemMeta) {
        material.defaultAttributeModifiers.forEach { type, modifier ->
            val name = modifier.name

            meta.removeAttributeModifier(type, name)
            meta.addAttributeModifier(type, name, modifier.amount, modifier.operation, modifier.slotGroup)
        }
    }
}