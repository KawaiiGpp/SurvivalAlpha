package com.akira.survivalalpha.service.damage.modifier

import com.akira.survivalalpha.service.damage.DamageFlag
import com.akira.survivalalpha.service.damage.DamageModifier
import com.akira.survivalalpha.service.damage.DamagePriority
import com.akira.survivalalpha.util.recalculatedValue
import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import org.bukkit.event.entity.EntityDamageEvent

class ArmorOverride(priority: DamagePriority) : DamageModifier(
    "armor_override", priority,
    ignoreIfTrueDamage = true
) {
    override fun modify(event: EntityDamageEvent, flag: DamageFlag) {
        val entity = event.entity as? Attributable ?: return
        val armor = entity.getAttributeValueOrZero(Attribute.GENERIC_ARMOR)
        val toughness = entity.getAttributeValueOrZero(Attribute.GENERIC_ARMOR_TOUGHNESS)

        val armorReduction = armor / 30 * 0.5
        val toughnessReduction = toughness / (toughness + 20) * 0.5

        event.damage *= 1 - (armorReduction + toughnessReduction)
    }

    private fun Attributable.getAttributeValueOrZero(attribute: Attribute): Double {
        return this.getAttribute(attribute)?.recalculatedValue ?: 0.0
    }
}