package com.akira.survivalalpha.attribute

import com.akira.core.api.Registry
import org.bukkit.Tag
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDamageEvent.DamageCause

sealed class TransformableEnchant(
    val enchant: Enchantment,
    val attribute: Attribute,
    val isInherent: Boolean,
    val multiplier: Double
) {
    val isDynamic = !isInherent

    init {
        register(enchant, this)
    }

    companion object : Registry<Enchantment, TransformableEnchant>() {
        val PROTECTION = InerentArmorEnchant(Enchantment.PROTECTION, 0.05)

        val SHARPNESS = InerentWeaponEnchant(Enchantment.SHARPNESS, 0.1)

        val FIRE_PROTECTION = DynamicArmorEnchant(
            Enchantment.FIRE_PROTECTION,
            0.15,
            DamageCause.FIRE,
            DamageCause.FIRE_TICK,
            DamageCause.LAVA,
            DamageCause.HOT_FLOOR
        )

        val BLAST_PROTECTION = DynamicArmorEnchant(
            Enchantment.BLAST_PROTECTION,
            0.15,
            DamageCause.ENTITY_EXPLOSION,
            DamageCause.BLOCK_EXPLOSION
        )

        val PROJECTILE_PROTECTION = DynamicArmorEnchant(Enchantment.PROJECTILE_PROTECTION, 0.15, DamageCause.PROJECTILE)

        val FEATHER_FALLING = DynamicArmorEnchant(Enchantment.FEATHER_FALLING, 0.25, DamageCause.FALL)

        val BANE_OF_ARTHROPODS = DynamicWeaponEnchant(
            Enchantment.BANE_OF_ARTHROPODS,
            0.2,
            Tag.ENTITY_TYPES_SENSITIVE_TO_BANE_OF_ARTHROPODS
        )

        val SMITE = DynamicWeaponEnchant(
            Enchantment.SMITE,
            0.2,
            Tag.ENTITY_TYPES_SENSITIVE_TO_SMITE
        )

        val IMPALING = DynamicWeaponEnchant(
            Enchantment.IMPALING,
            0.2,
            Tag.ENTITY_TYPES_SENSITIVE_TO_IMPALING
        )
    }
}

class InerentArmorEnchant internal constructor(
    enchant: Enchantment,
    multiplier: Double
) : TransformableEnchant(enchant, Attribute.GENERIC_ARMOR, true, multiplier)

class DynamicArmorEnchant internal constructor(
    enchant: Enchantment,
    multiplier: Double,
    vararg val targetCauses: DamageCause
) : TransformableEnchant(enchant, Attribute.GENERIC_ARMOR, false, multiplier)

class InerentWeaponEnchant internal constructor(
    enchant: Enchantment,
    multiplier: Double
) : TransformableEnchant(enchant, Attribute.GENERIC_ATTACK_DAMAGE, true, multiplier)

class DynamicWeaponEnchant internal constructor(
    enchant: Enchantment,
    multiplier: Double,
    val enemyTag: Tag<EntityType>
) : TransformableEnchant(enchant, Attribute.GENERIC_ATTACK_DAMAGE, false, multiplier)