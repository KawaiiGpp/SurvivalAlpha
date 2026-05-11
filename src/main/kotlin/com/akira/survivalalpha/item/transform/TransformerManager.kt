package com.akira.survivalalpha.item.transform

import com.akira.core.api.Registry
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object TransformerManager : Registry<String, ModifierTransformer>() {
    fun setupTransformers() {
        register("vanilla", VanillaTransformer())
        register("sharpness", EnchantTransformer(Enchantment.SHARPNESS, Attribute.GENERIC_ATTACK_DAMAGE) { it * 0.12 })
        register("protection", EnchantTransformer(Enchantment.PROTECTION, Attribute.GENERIC_ARMOR) { it * 0.05 })
    }

    fun transform(item: ItemStack) {
        item.editMeta { meta ->
            for (transformer in registry.values) {
                transformer.apply(item.type, meta)
            }
        }
    }
}