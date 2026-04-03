package com.akira.survivalalpha.service.damage

import org.bukkit.event.entity.EntityDamageEvent

/**
 * 伤害修饰符
 *
 * 明确对于监听到的 [EntityDamageEvent] 的修饰行为。
 *
 * @property name 修饰符名称，不可为空。不可重复，否则有覆盖风险
 * @property priority 优先级，数字越小优先级越高，不可小于零
 * @property ignoreCancelled 如果事件实例标记为已取消，则不应用修饰符
 */
abstract class DamageModifier(
    val name: String,
    val priority: DamagePriority,
    val ignoreCancelled: Boolean = true,
    val ignoreIfTrueDamage: Boolean = false
) {
    init {
        require(name.isNotEmpty()) { "Name of Damage Modifier cannot be empty." }
    }

    /**
     * 定义对事件的修饰行为。
     *
     * 由 [DamageManager.applyModifiers] 统一调度，
     * 无需另外手动调用该方法。
     *
     * @param event 事件实例
     * @param flag 伤害事件标记，用于记录额外信息
     */
    abstract fun modify(event: EntityDamageEvent, flag: DamageFlag)
}