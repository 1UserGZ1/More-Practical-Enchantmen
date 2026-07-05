package com.dyxiaojiazi.morepracticalenchantments;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MorePracticalEnchantments.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("main_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MorePracticalEnchantments.MOD_ID + ".main"))
                    .icon(() -> new ItemStack(Items.ENCHANTED_BOOK))  // 改为普通附魔书
                    .displayItems((params, output) -> {
                        ModEnchantments.ENCHANTMENTS.getEntries().forEach(entry -> {
                            Enchantment enchant = entry.get();
                            int maxLevel = enchant.getMaxLevel();
                            for (int lvl = 1; lvl <= maxLevel; lvl++) {
                                ItemStack enchantedBook = EnchantedBookItem.createForEnchantment(
                                        new net.minecraft.world.item.enchantment.EnchantmentInstance(enchant, lvl));
                                output.accept(enchantedBook);
                            }
                        });
                    })
                    .build()
    );
}