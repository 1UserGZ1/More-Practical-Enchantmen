package com.dyxiaojiazi.morepracticalenchantments.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LeapEnchantment extends Enchantment {
    public LeapEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.ARMOR_CHEST, new EquipmentSlot[]{EquipmentSlot.CHEST});
    }

    @Override
    public int getMinCost(int level) { return 20; }
    @Override
    public int getMaxCost(int level) { return 40; }
    @Override
    public int getMaxLevel() { return 1; }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() == Items.ELYTRA;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() == Items.ELYTRA;
    }
}