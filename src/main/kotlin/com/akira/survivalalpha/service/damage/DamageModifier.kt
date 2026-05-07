package com.akira.survivalalpha.service.damage

import com.akira.core.api.util.general.ensure
import com.akira.core.api.util.general.noSuchElm
import com.akira.survivalalpha.SurvivalAlpha
import org.bukkit.event.entity.EntityDamageEvent

/**
 * 伤害修饰符
 *
 * - 覆写 [modify] 以定义对事件实例的修饰行为
 *
 * @property name 名称，也是唯一标识符
 * @property priority 优先级
 * @property ignoreCancelled 若事件取消则忽略
 * @property ignoreIfTrueDamage 若为真伤则忽略
 */
abstract class DamageModifier(
    val name: String,
    val priority: DamagePriority,
    val ignoreCancelled: Boolean = true,
    val ignoreIfTrueDamage: Boolean = false
) {
    private val config get() = SurvivalAlpha.instance.configDamageModifier.getSection(this)

    init {
        require(name.isNotEmpty()) { "Name of damage modifier cannot be empty." }
    }

    protected fun getInt(key: String, check: ((Int) -> Boolean)? = null): Int {
        if (!config.contains(key)) noSuchElm("Int value '$key' for '$name' not found.")

        val result = config.getInt(key)
        if (check != null) result.ensure(check) { "Int value '$key' for '$name' is invalid: $it" }

        return result
    }

    protected fun getDouble(key: String, check: ((Double) -> Boolean)? = null): Double {
        if (!config.contains(key)) noSuchElm("Double value '$key' for '$name' not found.")

        val result = config.getDouble(key)
        if (check != null) result.ensure(check) { "Double value '$key' for '$name' is invalid: $it" }

        return result
    }

    /**
     * 定义对事件的修饰行为。
     *
     * - 由 [DamageManager.applyModifiers] 统一调度
     */
    abstract fun modify(event: EntityDamageEvent, flag: DamageFlag)
}