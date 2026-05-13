package com.akira.survivalalpha.attribute.temp

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

class TempModifierSession(private val target: Attributable) {
    private val registry: Multimap<Attribute, AttributeModifier> = ArrayListMultimap.create()

    fun set(attribute: Attribute, modifiers: List<AttributeModifier>) {
        registry.putAll(attribute, modifiers)
    }

    fun applyToTarget() {
        registry.forEach { attribute, modifier ->
            target.getAttribute(attribute)?.addModifier(modifier)
        }
    }

    fun removeFromTarget() {
        registry.forEach { attribute, modifier ->
            target.getAttribute(attribute)?.removeModifier(modifier)
        }
    }
}