package com.akira.survivalalpha.service.damage.modifier

import com.akira.core.api.util.entity.isShieldRaised
import com.akira.core.api.util.world.SoundPack
import com.akira.survivalalpha.service.damage.DamageFlag
import com.akira.survivalalpha.service.damage.DamageModifier
import org.bukkit.Sound
import org.bukkit.Tag
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.math.roundToInt

/**
 * 盾牌削弱修饰符
 *
 * 盾牌只能格挡一部分攻击伤害，并根据伤害扣除耐久度。
 * 当攻击者手持斧头，耐久度的扣除将大幅增加。
 *
 * 为禁用原版格挡，需设 [Player.shieldBlockingDelay] 为 [Int.MAX_VALUE]
 *
 * @property defaultDamageMultiplier 普通攻击扣除的耐久度倍数
 * @property axeDamageMultiplier 斧头攻击扣除的耐久度倍数
 * @property damageAfterBlockingMultiplier 盾牌格挡后剩余的伤害比例
 */
class ShieldNerf(priority: Int) : DamageModifier("shield_nerf", priority) {
    private val defaultDamageMultiplier = 2.0
    private val axeDamageMultiplier = 5.0
    private val damageAfterBlockingMultiplier = 0.5

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