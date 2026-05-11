package com.akira.survivalalpha.listener

import com.akira.survivalalpha.util.transform
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAttemptPickupItemEvent
import org.bukkit.event.player.PlayerJoinEvent

class PlayerDefaultSettingHandler : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        player.shieldBlockingDelay = Int.MAX_VALUE // 禁用原版格挡，使新盾牌格挡机制生效
        player.healthScale = 20.0
    }

    @EventHandler
    fun onPickup(event: PlayerAttemptPickupItemEvent) {
        event.item.itemStack.transform() // debug only: transforming items
    }
}