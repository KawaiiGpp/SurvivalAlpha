package com.akira.survivalalpha.attribute.item

import org.bukkit.inventory.ItemStack

object ItemTransform {
    fun transform(item: ItemStack) {
        item.editMeta { meta ->
            ModifierTransform.Vanilla.apply(item.type, meta)
            ModifierTransform.Enchant.registryView.values.forEach { it.apply(item.type, meta) }
        }
    }
}