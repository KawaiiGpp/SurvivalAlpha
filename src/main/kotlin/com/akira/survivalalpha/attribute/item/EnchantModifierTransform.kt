package com.akira.survivalalpha.attribute.item

import com.akira.core.api.util.item.removeAttributeModifier
import com.akira.survivalalpha.attribute.TransformableEnchant
import org.bukkit.Material
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.meta.ItemMeta

class EnchantModifierTransform(val formula: TransformableEnchant) : ItemModifierTransform {
    override fun apply(material: Material, meta: ItemMeta) {
        val enchant = formula.enchant
        val name = "Enchant modifier ${enchant.key}"
        val attribute = formula.attribute
        val level = meta.enchants[enchant]

        if (level != null) {
            meta.setAttributeModifier(
                attribute,
                name,
                level * formula.multiplier,
                Operation.ADD_SCALAR,
                material.equipmentSlot.group
            )
        } else {
            meta.removeAttributeModifier(attribute, name)
        }
    }
}