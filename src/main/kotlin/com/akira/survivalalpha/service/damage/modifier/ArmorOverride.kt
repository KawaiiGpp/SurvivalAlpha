package com.akira.survivalalpha.service.damage.modifier

import com.akira.survivalalpha.service.damage.DamageFlag
import com.akira.survivalalpha.service.damage.DamageModifier
import com.akira.survivalalpha.service.damage.DamagePriority
import com.akira.survivalalpha.util.recalculatedValue
import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import org.bukkit.event.entity.EntityDamageEvent

/**
 * 盔甲减伤修饰符
 *
 * 覆写原版的盔甲防御逻辑。
 * 当玩家受到真伤攻击时忽略。
 *
 * @property armorReductionCap 护甲值的减伤上限，需大于0小于1
 */
class ArmorOverride(priority: DamagePriority) : DamageModifier(
    "armor_override", priority,
    ignoreIfTrueDamage = true
) {
    private val armorReductionCap = 0.5

    override fun modify(event: EntityDamageEvent, flag: DamageFlag) {
        val entity = event.entity as? Attributable ?: return
        val armor = entity.getAttributeValueOrZero(Attribute.GENERIC_ARMOR)
        val toughness = entity.getAttributeValueOrZero(Attribute.GENERIC_ARMOR_TOUGHNESS)
        val toughnessReductionCap = 1 - armorReductionCap

        val armorReduction = armor / 30 * armorReductionCap
        val toughnessReduction = toughness / (toughness + 20) * toughnessReductionCap

        event.damage *= 1 - (armorReduction + toughnessReduction)
    }

    private fun Attributable.getAttributeValueOrZero(attribute: Attribute): Double {
        return this.getAttribute(attribute)?.recalculatedValue ?: 0.0
    }
}