package com.akira.survivalalpha

import com.akira.core.api.AkiraPlugin
import com.akira.survivalalpha.listener.DamageListener
import com.akira.survivalalpha.service.damage.DamageManager
import com.akira.survivalalpha.service.damage.modifier.DistanceScaling

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

        setupDamageModifiers()
        setupListener(DamageListener())
    }

    private fun setupDamageModifiers() {
        DamageManager.register(DistanceScaling(0))
    }
}