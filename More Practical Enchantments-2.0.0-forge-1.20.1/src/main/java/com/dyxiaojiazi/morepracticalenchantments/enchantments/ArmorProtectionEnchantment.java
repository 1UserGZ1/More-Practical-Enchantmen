package com.dyxiaojiazi.morepracticalenchantments.enchantments;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class ArmorProtectionEnchantment extends Enchantment {
    public ArmorProtectionEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.ARMOR,
                new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
    }

    @Override
    public int getMinCost(int level) {
        return 10 + 8 * (level - 1);
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    // 原版保护类减伤效果（每级4%，与保护附魔相同）
    @Override
    public int getDamageProtection(int level, DamageSource source) {
        return level * 4;
    }

    // 与所有原版保护类附魔互斥
    @Override
    public boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other)
                && other != Enchantments.ALL_DAMAGE_PROTECTION
                && other != Enchantments.FIRE_PROTECTION
                && other != Enchantments.BLAST_PROTECTION
                && other != Enchantments.PROJECTILE_PROTECTION
                && other != Enchantments.FALL_PROTECTION;
    }
}