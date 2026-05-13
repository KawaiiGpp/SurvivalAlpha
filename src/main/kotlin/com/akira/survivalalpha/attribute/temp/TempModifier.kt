package com.akira.survivalalpha.attribute.temp

import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.entity.EntityDamageEvent

abstract class TempModifier(
    val name: String,
    val attribute: Attribute
) {
    abstract fun generateFor(target: Attributable): List<AttributeModifier>

    abstract fun isApplicable(event: EntityDamageEvent): Boolean
}