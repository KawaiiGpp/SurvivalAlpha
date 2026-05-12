package com.akira.survivalalpha.attribute.transform

import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.meta.ItemMeta

class EnchantTransformer(
    private val enchantment: Enchantment,
    private val attribute: Attribute,
    private val transform: (level: Int) -> Double
) : ModifierTransformer() {
    override fun apply(material: Material, meta: ItemMeta) {
        val name = "Enchant modifier ${enchantment.key}"

        meta.unmodify(name, attribute)
        meta.enchants[enchantment]?.let { level ->
            meta.modify(
                name,
                attribute,
                transform(level),
                Operation.ADD_SCALAR,
                material.equipmentSlot.group
            )
        }
    }
}