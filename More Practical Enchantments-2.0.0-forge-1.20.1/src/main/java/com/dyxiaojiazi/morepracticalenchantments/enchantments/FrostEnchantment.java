package com.dyxiaojiazi.morepracticalenchantments.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class FrostEnchantment extends Enchantment {
    public FrostEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.BOW,
                new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }
    @Override public int getMaxLevel() { return 1; }
    @Override public int getMinCost(int lv) { return 20; }
    @Override public int getMaxCost(int lv) { return 40; }
    @Override public boolean canEnchant(ItemStack stack) {
        return stack.getItem() == Items.BOW || stack.getItem() == Items.CROSSBOW;
    }
    @Override public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return canEnchant(stack);
    }
    @Override
    public boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other != Enchantments.FLAMING_ARROWS;
    }

}