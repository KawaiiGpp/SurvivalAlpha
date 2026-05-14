package com.akira.survivalalpha.service.damage

import com.akira.core.api.Manager
import com.akira.survivalalpha.SurvivalAlpha
import com.akira.survivalalpha.attribute.combat.DynamicModifierGenerator
import com.akira.survivalalpha.service.damage.modifier.ArmorOverride
import com.akira.survivalalpha.service.damage.modifier.ShieldNerf
import com.akira.survivalalpha.service.damage.modifier.WeaponOverride
import org.bukkit.event.entity.EntityDamageEvent

/**
 * 伤害修饰符管理器
 *
 * - 存储已注册的修饰符
 * - 封装对实体伤害事件应用修饰符的逻辑
 */
object DamageManager : Manager<DamageModifier>() {
    private val sorted = mutableListOf<DamageModifier>()

    /**
     * 将已注册的修饰符依次应用至事件实例。
     */
    fun applyModifiers(event: EntityDamageEvent) {
        val plugin = SurvivalAlpha.instance
        val flag = DamageFlag()

        val session = DynamicModifierGenerator.createSession(event)
        session?.applyDynamicModifiers() // 若 event.entity 不为 LivingEntity 实例则跳过

        try {
            sorted.forEach { modifier ->
                if (event.isCancelled && modifier.ignoreCancelled) return@forEach
                if (flag.isTrueDamage && modifier.ignoreIfTrueDamage) return@forEach

                runCatching { modifier.modify(event, flag) }
                    .onFailure { throwable ->
                        plugin.logError("处理伤害修饰符 ${modifier.name} 发生异常。")
                        plugin.logError("该修饰符的优先级属于：${modifier.priority.name}")
                        throwable.printStackTrace()
                    }
            }
        } finally {
            session?.removeDynamicModifiers()
        }
    }

    /**
     * 注册伤害修饰符，由开发者手动新增。
     */
    fun setupModifiers() {
        register(WeaponOverride(DamagePriority.PRE_PROCESS))
        register(ShieldNerf(DamagePriority.HIGHEST))
        register(ArmorOverride(DamagePriority.HIGHEST))
    }

    override fun register(element: DamageModifier): String {
        sorted.add(element)
        sorted.sortBy { it.priority }

        return super.register(element)
    }

    override fun unregister(key: String) {
        this.get(key)?.let { sorted.remove(it) }

        super.unregister(key)
    }

    override fun clear() {
        super.clear()
        sorted.clear()
    }

    override fun transform(element: DamageModifier) = element.name
}