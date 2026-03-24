package com.akira.survivalalpha

import com.akira.core.api.AkiraPlugin

class SurvivalAlpha : AkiraPlugin() {
    companion object {
        lateinit var instance: SurvivalAlpha
            private set
    }

    init {
        instance = this
    }
}