package com.akira.survivalalpha.attribute.item

import com.akira.core.api.util.item.removeAttributeModifier
import com.akira.survivalalpha.attribute.EnchantFormula
import org.bukkit.Material
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.meta.ItemMeta

class EnchantTransform(val formula: EnchantFormula) : ModifierTransform {
    override fun apply(material: Material, meta: ItemMeta) {
        val attribute = formula.attribute
        val enchant = formula.enchant
        val name = "Enchant modifier ${enchant.key}"

        meta.removeAttributeModifier(attribute, name)
        meta.enchants[enchant]?.let { level ->
            meta.addAttributeModifier(
                attribute,
                name,
                formula.transform(level),
                Operation.ADD_SCALAR,
                material.equipmentSlot.group
            )
        }
    }
}