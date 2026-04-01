package com.akira.survivalalpha

import com.akira.core.api.AkiraPlugin
import com.akira.survivalalpha.listener.DamageListener
import com.akira.survivalalpha.listener.PlayerDefaultSettingHandler
import com.akira.survivalalpha.service.damage.DamageManager

class SurvivalAlpha : AkiraPlugin() {
    companion object {
        lateinit var instance: SurvivalAlpha
            private set
    }

    init {
        instance = this
    }

    override fun onEnable() {
        super.onEnable()

        DamageManager.setupModifiers()

        setupListener(DamageListener())
        setupListener(PlayerDefaultSettingHandler())
    }
}