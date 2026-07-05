package com.dyxiaojiazi.morepracticalenchantments.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class MagmaProtectionEnchantment extends Enchantment {
    public MagmaProtectionEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
    }

    @Override
    public int getMinCost(int level) { return 10 + 8 * (level - 1); }
    @Override
    public int getMaxCost(int level) { return getMinCost(level) + 20; }
    @Override
    public int getMaxLevel() { return 5; }

    @Override
    public boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other != Enchantments.FIRE_PROTECTION;
    }
}