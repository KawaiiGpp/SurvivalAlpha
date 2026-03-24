package com.akira.survivalalpha.service.damage.modifier

import com.akira.survivalalpha.service.damage.DamageModifier
import org.bukkit.World.Environment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

/**
 * 距离伤害修饰符
 *
 * 距离出生点越远，受到攻击的伤害就越高。
 *
 * @param scaling 每距100米的增伤百分比
 */
class DistanceScaling(val scaling: Int) : DamageModifier("distance_scaling", 0) {
    override fun modify(event: EntityDamageEvent) {
        val victim = event.entity

        if (victim !is Player) return
        if (victim.world.environment != Environment.NORMAL) return
        if (event !is EntityDamageByEntityEvent) return

        val damager = event.damager
        val attacker = if (damager is Projectile) damager.shooter else damager

        if (attacker !is LivingEntity) return
        if (attacker is Player) return

        val distance = victim.location.distance(victim.world.spawnLocation)
        val multiplier = (distance / 100) * (scaling / 100.0)

        event.damage *= 1 + multiplier
    }
}