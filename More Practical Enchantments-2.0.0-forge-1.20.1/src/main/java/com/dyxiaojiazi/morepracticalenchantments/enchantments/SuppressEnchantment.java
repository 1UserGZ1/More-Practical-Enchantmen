package com.dyxiaojiazi.morepracticalenchantments.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SuppressEnchantment extends Enchantment {
    public SuppressEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON,
                new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }
    @Override public int getMaxLevel() { return 1; }
    @Override public int getMinCost(int lv) { return 10; }
    @Override public int getMaxCost(int lv) { return 25; }
}