package com.dyxiaojiazi.morepracticalenchantments;

import com.dyxiaojiazi.morepracticalenchantments.config.ModConfig;
import com.dyxiaojiazi.morepracticalenchantments.events.ModEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MorePracticalEnchantments.MOD_ID)
public class MorePracticalEnchantments {
    public static final String MOD_ID = "morepracticalenchantments";
    public static final Logger LOGGER = LogManager.getLogger();

    public MorePracticalEnchantments() {
        // 注册配置
        ModConfig.register();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEnchantments.ENCHANTMENTS.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(new ModEvents());
        LOGGER.info("More Practical Enchantments loaded!");
    }
}