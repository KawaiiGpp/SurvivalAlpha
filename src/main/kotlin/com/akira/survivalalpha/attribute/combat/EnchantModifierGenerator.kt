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
    protected fun createModifier(uniqueId: UUID, item: ItemStack): AttributeModifier? {
        val level = item.enchantments[formula.enchant] ?: return null

        return AttributeModifier(
            uniqueId,
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
    private val uniqueIdCaches = List<UUID>(4) { UUID.randomUUID() }

    override fun forVictim(victim: LivingEntity, event: EntityDamageEvent): List<AttributeModifier> {
        if (formula.targetCauses.none { it == event.cause }) return emptyList()

        val armors: Array<ItemStack?> = victim.equipment?.armorContents ?: return emptyList()
        val result = mutableListOf<AttributeModifier>()

        repeat(armors.size) { index ->
            val armor = armors[index]
            if (armor == null || armor.type.isAir) return@repeat

            this.createModifier(uniqueIdCaches[index], armor)
                ?.let { result.add(it) }
        }

        return result
    }
}

class WeaponModifierGenerator internal constructor(
    override val formula: DynamicWeaponEnchant
) : EnchantModifierGenerator(formula) {
    private val uniqueIdCache = UUID.randomUUID()

    override fun forAttacker(attacker: LivingEntity, event: EntityDamageByEntityEvent): List<AttributeModifier> {
        if (!formula.enemyTag.isTagged(event.entityType)) return emptyList()

        val item = attacker.equipment?.itemInMainHand ?: return emptyList()
        if (item.type.isAir) return emptyList()

        return this.createModifier(uniqueIdCache, item)?.let { listOf(it) } ?: emptyList()
    }
}