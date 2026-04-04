package com.akira.survivalalpha.config

import com.akira.core.api.config.ConfigFile
import com.akira.survivalalpha.SurvivalAlpha
import com.akira.survivalalpha.service.damage.DamageModifier

/**
 * 伤害修饰符配置
 *
 * 用于调节伤害修饰符中的可配置参数。
 *
 * @param plugin 插件实例
 * @param templatePath 模板路径
 */
class DamageModifierConfig(plugin: SurvivalAlpha, templatePath: String) :
    ConfigFile("damage_modifier", plugin, templatePath) {
    /**
     * 获取修饰符的专属配置节点
     *
     * @param modifier 修饰符实例
     * @return 修饰符专属节点
     * @throws NullPointerException 当配置文件不存在与修饰符相关节点时抛出
     */
    fun getSection(modifier: DamageModifier) = this.config.getConfigurationSection(modifier.name)
        ?: throw NullPointerException("No config section for damage modifier ${modifier.name} was found.")
}