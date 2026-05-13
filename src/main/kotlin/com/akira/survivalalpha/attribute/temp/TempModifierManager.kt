package com.akira.survivalalpha.attribute.temp

import com.akira.core.api.Manager
import com.akira.survivalalpha.attribute.temp.enchant.ArmorEnchantTempModifier
import com.akira.survivalalpha.attribute.temp.enchant.WeaponEnchantTempModifier
import org.bukkit.attribute.Attributable
import org.bukkit.event.entity.EntityDamageEvent

object TempModifierManager : Manager<TempModifier>() {
    fun setupTempModifiers() {
        register(ArmorEnchantTempModifier.FIRE_PROTECTION)
        register(ArmorEnchantTempModifier.BLAST_PROTECTION)
        register(ArmorEnchantTempModifier.PROJECTILE_PROTECTION)
        register(ArmorEnchantTempModifier.FEATHER_FALLING)

        register(WeaponEnchantTempModifier.SMITE)
        register(WeaponEnchantTempModifier.BANE_OF_ARTHROPODS)
        register(WeaponEnchantTempModifier.IMPALING)
    }

    fun createSessionFor(target: Attributable, event: EntityDamageEvent): TempModifierSession {
        val session = TempModifierSession(target)

        for (modifier in registry.values) {
            if (!modifier.isApplicable(event)) continue

            session.set(modifier.attribute, modifier.generateFor(target))
        }

        return session
    }

    override fun transform(element: TempModifier) = element.name
}