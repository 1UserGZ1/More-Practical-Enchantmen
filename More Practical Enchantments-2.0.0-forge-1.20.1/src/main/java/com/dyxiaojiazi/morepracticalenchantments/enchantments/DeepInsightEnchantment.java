package com.dyxiaojiazi.morepracticalenchantments.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class DeepInsightEnchantment extends Enchantment {
    public DeepInsightEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_HEAD,
                new EquipmentSlot[]{EquipmentSlot.HEAD});
    }
    @Override public int getMaxLevel() { return 1; }
    @Override public int getMinCost(int lv) { return 15; }
    @Override public int getMaxCost(int lv) { return 30; }
}