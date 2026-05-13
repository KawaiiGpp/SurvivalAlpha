package com.akira.survivalalpha.attribute.temp.enchant

import org.bukkit.Tag
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent

class WeaponEnchantTempModifier(
    name: String,
    enchantment: Enchantment,
    multiplier: Double,
    private val entityTypeTag: Tag<EntityType>
) : EnchantTempModifier(
    name,
    Attribute.GENERIC_ATTACK_DAMAGE,
    enchantment,
    multiplier
) {
    override fun generateFor(target: LivingEntity): List<AttributeModifier> {
        val item = target.equipment?.itemInMainHand ?: return emptyList()

        return this.createModifier(item)?.let { listOf(it) } ?: emptyList()
    }

    override fun isApplicable(event: EntityDamageEvent): Boolean {
        return entityTypeTag.isTagged(event.entityType)
    }

    companion object {
        val SMITE = WeaponEnchantTempModifier(
            "smite",
            Enchantment.SMITE,
            0.2,
            Tag.ENTITY_TYPES_SENSITIVE_TO_SMITE
        )

        val BANE_OF_ARTHROPODS = WeaponEnchantTempModifier(
            "bane_of_arthropods",
            Enchantment.BANE_OF_ARTHROPODS,
            0.2,
            Tag.ENTITY_TYPES_SENSITIVE_TO_BANE_OF_ARTHROPODS
        )

        val IMPALING = WeaponEnchantTempModifier(
            "impaling",
            Enchantment.IMPALING,
            0.2,
            Tag.ENTITY_TYPES_SENSITIVE_TO_IMPALING
        )
    }
}