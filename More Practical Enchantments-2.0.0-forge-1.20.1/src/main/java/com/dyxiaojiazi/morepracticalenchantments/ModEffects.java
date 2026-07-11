package com.dyxiaojiazi.morepracticalenchantments;

import com.dyxiaojiazi.morepracticalenchantments.effects.FrostEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MorePracticalEnchantments.MOD_ID);

    public static final RegistryObject<MobEffect> FROST =
            EFFECTS.register("frost", FrostEffect::new);
}