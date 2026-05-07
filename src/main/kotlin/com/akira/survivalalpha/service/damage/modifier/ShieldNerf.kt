package com.akira.survivalalpha.service.damage.modifier

import com.akira.core.api.util.entity.isShieldRaised
import com.akira.core.api.util.world.SoundPack
import com.akira.survivalalpha.service.damage.DamageFlag
import com.akira.survivalalpha.service.damage.DamageModifier
import com.akira.survivalalpha.service.damage.DamagePriority
import org.bukkit.Sound
import org.bukkit.Tag
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.math.roundToInt

/**
 * 盾牌削弱
 * - 盾牌特性改为格挡部分攻击伤害
 * - 根据伤害扣除盾牌耐久。若武器为斧头，耐久扣除大幅增加
 *
 * 需禁用原版格挡行为以避免冲突：
 * - 设 [Player.shieldBlockingDelay] 为 [Int.MAX_VALUE]
 */
class ShieldNerf(priority: DamagePriority) : DamageModifier("shield_nerf", priority) {
    private val defaultDamageMultiplier = this.getDouble("default_damage_multiplier") { it > 0 }
    private val axeDamageMultiplier = this.getDouble("axe_damage_multiplier") { it > 0 }
    private val damageAfterBlockingMultiplier = this.getDouble("damage_after_blocking_multiplier") { it >= 0 }

    override fun modify(event: EntityDamageEvent, flag: DamageFlag) {
        if (event !is EntityDamageByEntityEvent) return

        val player = event.entity as? Player ?: return
        if (!player.isShieldRaised) return

        val item = player.activeItem
        val multiplier = calculateItemDamageMultiplier(event)

        val damage = event.damage * multiplier
        item.damage(damage.roundToInt(), player)

        event.damage *= damageAfterBlockingMultiplier
        SoundPack(Sound.ITEM_SHIELD_BLOCK, 1.0F, 1.0F).playEnvironment(player)
    }

    private fun calculateItemDamageMultiplier(event: EntityDamageByEntityEvent): Double {
        return if (isDamagerHoldingAxe(event)) axeDamageMultiplier else defaultDamageMultiplier
    }

    private fun isDamagerHoldingAxe(event: EntityDamageByEntityEvent): Boolean {
        val damager = event.damager as? LivingEntity ?: return false
        val item = damager.equipment?.itemInMainHand ?: return false

        return Tag.ITEMS_AXES.isTagged(item.type)
    }
}