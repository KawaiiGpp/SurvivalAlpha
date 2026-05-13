package com.akira.survivalalpha.attribute.temp.enchant

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.inventory.ItemStack

class ArmorEnchantTempModifier(
    name: String,
    enchantment: Enchantment,
    multiplier: Double,
    private vararg val causes: DamageCause,
) : EnchantTempModifier(
    name,
    Attribute.GENERIC_ARMOR,
    enchantment,
    multiplier
) {
    override fun generateFor(target: LivingEntity): List<AttributeModifier> {
        val armors: Array<ItemStack?> = target.equipment?.armorContents ?: return listOf()

        return armors.mapNotNull(this::createModifier)
    }

    override fun isApplicable(event: EntityDamageEvent): Boolean {
        return causes.any { it == event.cause }
    }

    companion object {
        val FIRE_PROTECTION = ArmorEnchantTempModifier(
            "fire_protection",
            Enchantment.FIRE_PROTECTION,
            0.15,
            DamageCause.FIRE,
            DamageCause.FIRE_TICK,
            DamageCause.LAVA,
            DamageCause.HOT_FLOOR
        )

        val BLAST_PROTECTION = ArmorEnchantTempModifier(
            "blast_protection",
            Enchantment.BLAST_PROTECTION,
            0.15,
            DamageCause.ENTITY_EXPLOSION,
            DamageCause.BLOCK_EXPLOSION,
            DamageCause.SONIC_BOOM
        )

        val PROJECTILE_PROTECTION = ArmorEnchantTempModifier(
            "projectile_protection",
            Enchantment.PROJECTILE_PROTECTION,
            0.15,
            DamageCause.PROJECTILE,
        )

        val FEATHER_FALLING = ArmorEnchantTempModifier(
            "feather_falling",
            Enchantment.FEATHER_FALLING,
            0.25,
            DamageCause.FALL
        )
    }
}