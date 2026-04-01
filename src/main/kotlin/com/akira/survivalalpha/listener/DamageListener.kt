package com.akira.survivalalpha.listener

import com.akira.core.api.util.general.bypassVanillaModifiers
import com.akira.survivalalpha.service.damage.DamageManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class DamageListener : Listener {
    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        DamageManager.applyModifiers(event)
        event.bypassVanillaModifiers()
    }
}