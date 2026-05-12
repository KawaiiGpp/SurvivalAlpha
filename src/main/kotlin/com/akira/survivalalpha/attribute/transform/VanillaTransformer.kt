package com.akira.survivalalpha.attribute.transform

import org.bukkit.Material
import org.bukkit.inventory.meta.ItemMeta

class VanillaTransformer : ModifierTransformer() {
    override fun apply(material: Material, meta: ItemMeta) {
        material.defaultAttributeModifiers.forEach { type, modifier ->
            val name = modifier.name

            meta.unmodify(name, type)
            meta.modify(name, type, modifier.amount, modifier.operation, modifier.slotGroup)
        }
    }
}