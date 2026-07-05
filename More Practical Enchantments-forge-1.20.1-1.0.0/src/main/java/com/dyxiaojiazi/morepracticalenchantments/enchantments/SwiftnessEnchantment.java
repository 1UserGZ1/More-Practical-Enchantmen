package com.dyxiaojiazi.morepracticalenchantments.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SwiftnessEnchantment extends Enchantment {
    public SwiftnessEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) { return 5 + 8 * (level - 1); }
    @Override
    public int getMaxCost(int level) { return getMinCost(level) + 15; }
    @Override
    public int getMaxLevel() { return 5; }
}