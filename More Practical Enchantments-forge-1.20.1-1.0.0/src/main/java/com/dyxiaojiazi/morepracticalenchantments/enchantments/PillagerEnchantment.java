package com.dyxiaojiazi.morepracticalenchantments.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class PillagerEnchantment extends Enchantment {
    public PillagerEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) { return 10 + 8 * (level - 1); }
    @Override
    public int getMaxCost(int level) { return getMinCost(level) + 20; }
    @Override
    public int getMaxLevel() { return 5; }  // 改为 5
}