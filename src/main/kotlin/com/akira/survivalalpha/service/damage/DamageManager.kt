package com.akira.survivalalpha.service.damage

import com.akira.core.api.EnhancedManager
import com.akira.survivalalpha.SurvivalAlpha
import com.akira.survivalalpha.service.damage.DamageManager.applyModifiers
import com.akira.survivalalpha.service.damage.DamageManager.sorted
import org.bukkit.event.entity.EntityDamageEvent

/**
 * 伤害修饰符管理器
 *
 * 内部维护一个伤害修饰符的集合。
 *
 * 当监听到 [EntityDamageEvent] 将调用 [applyModifiers] 方法，
 * 根据修饰符的优先级依次对事件实例进行作用。
 *
 * 私有属性 [sorted] 用于缓存已排序的修饰符。
 */
object DamageManager : EnhancedManager<DamageModifier>() {
    private val sorted = mutableListOf<DamageModifier>()

    /**
     * 对接收到的事件实例进行修饰。
     *
     * @param event 事件实例
     */
    fun applyModifiers(event: EntityDamageEvent) {
        sorted.forEach { modifier ->
            runCatching { modifier.modify(event) }
                .onFailure { throwable ->
                    val plugin = SurvivalAlpha.instance

                    plugin.logError("处理伤害修饰符 ${modifier.name} 发生异常。")
                    throwable.printStackTrace()
                }
        }
    }

    override fun register(element: DamageModifier) {
        super.register(element)

        sorted.add(element)
        sorted.sortBy { it.priority }
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