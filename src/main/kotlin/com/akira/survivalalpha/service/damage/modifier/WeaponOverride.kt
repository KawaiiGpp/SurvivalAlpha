package com.akira.survivalalpha.service.damage.modifier

import com.akira.survivalalpha.service.damage.DamageFlag
import com.akira.survivalalpha.service.damage.DamageModifier
import com.akira.survivalalpha.service.damage.DamagePriority
import com.akira.survivalalpha.util.recalculatedValue
import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

/**
 * 武器伤害修饰符
 *
 * 覆写原版的攻击伤害计算逻辑，
 * 同时无效化玩家在未充分蓄力时做出的攻击。
 *
 * @property cooldownThreshold 冷却低于此值，攻击无效
 */
class WeaponOverride(priority: DamagePriority) : DamageModifier("weapon_override", priority) {
    private val cooldownThreshold = 0.5

    override fun modify(event: EntityDamageEvent, flag: DamageFlag) {
        if (event !is EntityDamageByEntityEvent) return

        val entity = event.damager as? Attributable ?: return
        val damage = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.recalculatedValue ?: return
        val cooldown = (entity as? Player)?.attackCooldown ?: 1.0F

        if (cooldown >= cooldownThreshold) event.damage = damage * cooldown
        else event.isCancelled = true
    }
}