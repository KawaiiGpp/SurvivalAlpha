package com.akira.survivalalpha.service.damage.modifier

import com.akira.core.api.util.entity.getFinalMaxHealth
import com.akira.survivalalpha.service.damage.DamageModifier
import org.bukkit.World.Environment
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause

/**
 * 地狱火焰伤害修饰符
 *
 * 在地狱玩家所受到的火焰相关伤害将大幅增加。
 * 其中岩浆与火焰直接伤害均改真伤。
 *
 * 对于火焰直接伤害、持续伤害以及岩浆伤害可有不同的增强幅度。
 *
 * @property lavaMultiplier 岩浆伤害的倍乘值
 * @property fireMultiplier 火焰直接伤害的倍乘值
 * @property fireTickMultiplier 火焰持续伤害的倍乘值
 */
class NetherAmplifier(priority: Int) : DamageModifier("nether_amplifier", priority) {
    private val lavaMultiplier = 1.5
    private val fireMultiplier = 3.0
    private val fireTickMultiplier = 3.0

    override fun modify(event: EntityDamageEvent) {
        if (event.entity.world.environment != Environment.NETHER) return

        val player = event.entity as? Player ?: return
        val cause = event.cause

        if (cause == DamageCause.LAVA || cause == DamageCause.FIRE) {
            val multiplier = if (cause == DamageCause.LAVA) lavaMultiplier else fireMultiplier
            val damage = event.damage * multiplier

            val maxHealth = player.getFinalMaxHealth()
            if (player.health > maxHealth) player.health = maxHealth

            if (damage < player.health) {
                event.damage = 0.0
                player.health -= damage
            } else {
                event.damage = Long.MAX_VALUE.toDouble()
            }
        }

        if (cause == DamageCause.FIRE_TICK) event.damage *= fireTickMultiplier
    }
}