package com.akira.survivalalpha.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerDefaultSettingHandler : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        player.shieldBlockingDelay = Int.MAX_VALUE
        player.healthScale = 20.0
    }
}