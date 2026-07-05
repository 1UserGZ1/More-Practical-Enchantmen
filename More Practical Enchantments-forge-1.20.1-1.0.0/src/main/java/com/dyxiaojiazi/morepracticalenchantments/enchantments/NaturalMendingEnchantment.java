package com.dyxiaojiazi.morepracticalenchantments.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class NaturalMendingEnchantment extends Enchantment {
    public NaturalMendingEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.BREAKABLE,
                new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND,
                        EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
    }

    @Override
    public int getMinCost(int level) { return 10 + 5 * (level - 1); }
    @Override
    public int getMaxCost(int level) { return getMinCost(level) + 15; }
    @Override
    public int getMaxLevel() { return 5; }

    // 移除冲突：允许与经验修补同时附魔
}