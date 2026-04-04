package com.akira.survivalalpha

import com.akira.core.api.AkiraPlugin
import com.akira.core.api.config.ConfigManager
import com.akira.survivalalpha.config.DamageModifierConfig
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

    val configManager = ConfigManager()

    override fun onEnable() {
        super.onEnable()

        val templatePath = "com/akira/survivalalpha/config/template"
        configManager.register(DamageModifierConfig(this, templatePath))
        configManager.initializeAll()

        DamageManager.setupModifiers()

        setupListener(DamageListener())
        setupListener(PlayerDefaultSettingHandler())
    }

    override fun onDisable() {
        super.onDisable()

        configManager.saveAll()
    }
}