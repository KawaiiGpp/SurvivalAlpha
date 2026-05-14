package com.akira.survivalalpha.attribute.combat

import com.akira.survivalalpha.attribute.DynamicArmorEnchant
import com.akira.survivalalpha.attribute.DynamicWeaponEnchant
import com.akira.survivalalpha.attribute.TransformableEnchant
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import java.util.*

sealed class EnchantModifierGenerator(open val formula: TransformableEnchant) : DynamicModifierGenerator {
    protected fun createModifier(item: ItemStack?): AttributeModifier? {
        val level = item?.enchantments[formula.enchant] ?: return null

        return AttributeModifier(
            UUID.randomUUID(),
            "Temp enchant modifier ${formula.enchant.key}",
            level * formula.multiplier,
            Operation.ADD_SCALAR,
            item.type.equipmentSlot.group
        )
    }

    override val targetAttribute: Attribute get() = formula.attribute
}

class ArmorModifierGenerator internal constructor(
    override val formula: DynamicArmorEnchant
) : EnchantModifierGenerator(formula) {
    override fun forVictim(victim: LivingEntity, event: EntityDamageEvent): List<AttributeModifier> {
        if (formula.targetCauses.none { it == event.cause }) return emptyList()
        val armors: Array<ItemStack?> = victim.equipment?.armorContents ?: return emptyList()

        return armors.mapNotNull {
            return@mapNotNull if (it == null || it.type.isAir) null
            else this.createModifier(it)
        }
    }
}

class WeaponModifierGenerator internal constructor(
    override val formula: DynamicWeaponEnchant
) : EnchantModifierGenerator(formula) {
    override fun forAttacker(attacker: LivingEntity, event: EntityDamageByEntityEvent): List<AttributeModifier> {
        if (!formula.enemyTag.isTagged(event.entityType)) return emptyList()

        val item = attacker.equipment?.itemInMainHand ?: return emptyList()
        if (item.type.isAir) return emptyList()

        return this.createModifier(item)?.let { listOf(it) } ?: emptyList()
    }
}