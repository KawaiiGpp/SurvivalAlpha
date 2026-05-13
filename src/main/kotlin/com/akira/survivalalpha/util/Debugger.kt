package com.akira.survivalalpha.util

import com.akira.core.api.util.text.debug
import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute

object Debugger {
    fun attributeModifiers(target: Attributable, attribute: Attribute) {
        val instance = target.getAttribute(attribute)

        if (instance == null) {
            debug("Target has no attribute instance for $attribute.")
            return
        }

        val modifiers = instance.modifiers.sortedBy { it.operation.ordinal }

        if (modifiers.isNotEmpty()) {
            debug("Existing modifiers for $attribute (§e${modifiers.size}§f): ")

            modifiers.forEach { debug("§7${it.name} §8- §a${it.amount} §8- §2${it.operation.ordinal}") }
        } else {
            debug("Target has no modifiers applied for $attribute.")
        }

        debug("Attribute value: §b${instance.uncappedValue}")
    }
}