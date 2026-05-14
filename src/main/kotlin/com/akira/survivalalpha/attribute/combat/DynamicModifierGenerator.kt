package com.akira.survivalalpha.attribute.combat

import com.akira.core.api.util.general.illegalState
import com.akira.survivalalpha.attribute.DynamicArmorEnchant
import com.akira.survivalalpha.attribute.DynamicWeaponEnchant
import com.akira.survivalalpha.attribute.TransformableEnchant
import com.akira.survivalalpha.util.ProtectedStorage
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

interface DynamicModifierGenerator {
    fun forVictim(victim: LivingEntity, event: EntityDamageEvent) = emptyList<AttributeModifier>()

    fun forAttacker(attacker: LivingEntity, event: EntityDamageByEntityEvent) = emptyList<AttributeModifier>()

    val targetAttribute: Attribute

    companion object : ProtectedStorage<DynamicModifierGenerator>() {
        init {
            TransformableEnchant.registryView.values
                .filter { it.isDynamic }
                .forEach {
                    if (it is DynamicArmorEnchant)
                        this.add(ArmorModifierGenerator(it))

                    if (it is DynamicWeaponEnchant)
                        this.add(WeaponModifierGenerator(it))
                }
        }

        fun createSession(event: EntityDamageEvent): Session? {
            val victim = event.entity as? LivingEntity ?: return null
            val attacker = (event as? EntityDamageByEntityEvent)?.damager as? LivingEntity
            val session = Session(victim, attacker)

            content.forEach {
                session.setVictim(it.targetAttribute, it.forVictim(victim, event))

                if (attacker == null) return@forEach
                session.setAttacker(it.targetAttribute, it.forAttacker(attacker, event))
            }

            return session
        }
    }

    class Session(val victim: LivingEntity, val attacker: LivingEntity?) {
        private val victimMap: Multimap<Attribute, AttributeModifier> = ArrayListMultimap.create()
        private val attackerMap: Multimap<Attribute, AttributeModifier> = ArrayListMultimap.create()

        fun setVictim(attribute: Attribute, modifiers: Collection<AttributeModifier>) {
            if (modifiers.isEmpty()) return

            this.victimMap.putAll(attribute, modifiers)
        }

        fun setAttacker(attribute: Attribute, modifiers: Collection<AttributeModifier>) {
            if (attacker == null) illegalState("Attacker is not set for this session.")
            if (modifiers.isEmpty()) return

            this.attackerMap.putAll(attribute, modifiers)
        }

        fun applyDynamicModifiers() {
            victimMap.forEach { type, modifier -> victim.getAttribute(type)?.addModifier(modifier) }

            if (attacker == null) return
            attackerMap.forEach { type, modifier -> attacker.getAttribute(type)?.addModifier(modifier) }
        }

        fun removeDynamicModifiers() {
            victimMap.forEach { type, modifier -> victim.getAttribute(type)?.removeModifier(modifier) }

            if (attacker == null) return
            attackerMap.forEach { type, modifier -> attacker.getAttribute(type)?.removeModifier(modifier) }
        }
    }
}