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

class WeaponOverride(priority: DamagePriority) : DamageModifier("weapon_override", priority) {
    override fun modify(event: EntityDamageEvent, flag: DamageFlag) {
        if (event !is EntityDamageByEntityEvent) return

        val entity = event.damager as? Attributable ?: return
        val damage = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.recalculatedValue ?: return
        val cooldown = (entity as? Player)?.attackCooldown ?: 1.0F

        if (cooldown >= 0.5) event.damage = damage * cooldown
        else event.isCancelled = true
    }
}