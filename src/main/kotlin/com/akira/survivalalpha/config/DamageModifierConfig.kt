package com.akira.survivalalpha.config

import com.akira.core.api.config.ConfigFile
import com.akira.core.api.util.general.noSuchElm
import com.akira.survivalalpha.SurvivalAlpha
import com.akira.survivalalpha.service.damage.DamageModifier
import org.bukkit.configuration.ConfigurationSection

class DamageModifierConfig(plugin: SurvivalAlpha, templatePath: String) :
    ConfigFile("damage_modifier", plugin, templatePath) {

    /**
     * 获取修饰符的专属配置节点。
     *
     * @throws NoSuchElementException 找不到修饰符专属节点时抛出
     */
    fun getSection(modifier: DamageModifier): ConfigurationSection {
        return this.config.getConfigurationSection(modifier.name)
            ?: noSuchElm("No config section for damage modifier ${modifier.name} was found.")
    }
}