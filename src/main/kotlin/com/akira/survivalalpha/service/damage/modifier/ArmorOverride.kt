package com.akira.survivalalpha.service.damage.modifier

import com.akira.survivalalpha.attribute.temp.TempModifierManager
import com.akira.survivalalpha.service.damage.DamageFlag
import com.akira.survivalalpha.service.damage.DamageModifier
import com.akira.survivalalpha.service.damage.DamagePriority
import com.akira.survivalalpha.util.uncappedValue
import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import org.bukkit.event.entity.EntityDamageEvent

/**
 * 盔甲减伤
 *
 * - 覆写原版的盔甲减伤逻辑
 * - 采用 `n / (n + const)` 非线性减伤公式
 * - 伤害减免由盔甲值与韧性值共同决定
 */
class ArmorOverride(priority: DamagePriority) : DamageModifier(
    "armor_override",
    priority,
    ignoreIfTrueDamage = true
) {
    private val armorReductionWeight = this.getDouble("armor_reduction_weight") { it >= 0 && it <= 1 }
    private val reductionThreshold = this.getInt("reduction_threshold") { it > 0 }
    private val toughnessReductionWeight = 1 - armorReductionWeight

    override fun modify(event: EntityDamageEvent, flag: DamageFlag) {
        val entity = event.entity as? Attributable ?: return

        val tempModifierSession = TempModifierManager.createSessionFor(entity, event)
        tempModifierSession.applyToTarget()

        val armor = entity.getAttributeValueOrZero(Attribute.GENERIC_ARMOR)
        val toughness = entity.getAttributeValueOrZero(Attribute.GENERIC_ARMOR_TOUGHNESS)

        tempModifierSession.removeFromTarget()

        val armorReduction = armor / (armor + reductionThreshold) * armorReductionWeight
        val toughnessReduction = toughness / (toughness + reductionThreshold) * toughnessReductionWeight

        event.damage *= 1 - (armorReduction + toughnessReduction)
    }

    private fun Attributable.getAttributeValueOrZero(attribute: Attribute): Double {
        return this.getAttribute(attribute)?.uncappedValue ?: 0.0
    }
}