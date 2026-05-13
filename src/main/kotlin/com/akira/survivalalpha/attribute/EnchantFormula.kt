package com.akira.survivalalpha.attribute

import com.akira.core.api.Registry
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment

sealed class EnchantFormula(
    val enchant: Enchantment,
    val attribute: Attribute,
    val isInherent: Boolean = false,
    val transform: (Int) -> Double
) {
    val isDynamic = !isInherent

    init {
        register(enchant, this)
    }

    private class ArmorFormula(
        enchant: Enchantment,
        isInherent: Boolean = false,
        transform: (Int) -> Double
    ) : EnchantFormula(enchant, Attribute.GENERIC_ARMOR, isInherent, transform)

    private class WeaponFormula(
        enchant: Enchantment,
        isInherent: Boolean = false,
        transform: (Int) -> Double
    ) : EnchantFormula(enchant, Attribute.GENERIC_ATTACK_DAMAGE, isInherent, transform)

    companion object : Registry<Enchantment, EnchantFormula>() {
        val PROTECTION: EnchantFormula = ArmorFormula(Enchantment.PROTECTION, true) { it * 0.05 }

        val FIRE_PROTECTION: EnchantFormula = ArmorFormula(Enchantment.FIRE_PROTECTION) { it * 0.15 }

        val BLAST_PROTECTION: EnchantFormula = ArmorFormula(Enchantment.BLAST_PROTECTION) { it * 0.15 }

        val PROJECTILE_PROTECTION: EnchantFormula = ArmorFormula(Enchantment.PROJECTILE_PROTECTION) { it * 0.15 }

        val FEATHER_FALLING: EnchantFormula = ArmorFormula(Enchantment.FEATHER_FALLING) { it * 0.25 }

        val SHARPNESS: EnchantFormula = WeaponFormula(Enchantment.SHARPNESS, true) { it * 0.1 }

        val BANE_OF_ARTHROPODS: EnchantFormula = WeaponFormula(Enchantment.BANE_OF_ARTHROPODS) { it * 0.2 }

        val SMITE: EnchantFormula = WeaponFormula(Enchantment.SMITE) { it * 0.2 }

        val IMPALING: EnchantFormula = WeaponFormula(Enchantment.IMPALING) { it * 0.2 }
    }
}