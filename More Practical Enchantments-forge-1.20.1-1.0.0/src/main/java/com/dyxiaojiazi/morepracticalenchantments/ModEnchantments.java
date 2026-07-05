package com.dyxiaojiazi.morepracticalenchantments;

import com.dyxiaojiazi.morepracticalenchantments.enchantments.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MorePracticalEnchantments.MOD_ID);

    public static final RegistryObject<Enchantment> LIFE_STEAL =
            ENCHANTMENTS.register("life_steal", LifeStealEnchantment::new);
    public static final RegistryObject<Enchantment> JUMP_BOOST =
            ENCHANTMENTS.register("jump_boost", JumpBoostEnchantment::new);
    public static final RegistryObject<Enchantment> MAGMA_PROTECTION =
            ENCHANTMENTS.register("magma_protection", MagmaProtectionEnchantment::new);
    public static final RegistryObject<Enchantment> VOID_PROTECTION =
            ENCHANTMENTS.register("void_protection", VoidProtectionEnchantment::new);
    public static final RegistryObject<Enchantment> SWIFTNESS =
            ENCHANTMENTS.register("swiftness", SwiftnessEnchantment::new);
    public static final RegistryObject<Enchantment> EXPERIENCE_GAIN =
            ENCHANTMENTS.register("experience_gain", ExperienceGainEnchantment::new);
    public static final RegistryObject<Enchantment> SPRINT_BOOST =
            ENCHANTMENTS.register("sprint_boost", SprintBoostEnchantment::new);
    public static final RegistryObject<Enchantment> NATURAL_MENDING =
            ENCHANTMENTS.register("natural_mending", NaturalMendingEnchantment::new);
    public static final RegistryObject<Enchantment> SMELTING =
            ENCHANTMENTS.register("smelting", SmeltingEnchantment::new);
    public static final RegistryObject<Enchantment> NIGHT_VISION =
            ENCHANTMENTS.register("night_vision", NightVisionEnchantment::new);
    public static final RegistryObject<Enchantment> PILLAGER =
            ENCHANTMENTS.register("pillager", PillagerEnchantment::new);
    // 不破已移除
    // INDESTRUCTIBLE 不再注册
    public static final RegistryObject<Enchantment> REBIRTH_BINDING =
            ENCHANTMENTS.register("rebirth_binding", RebirthBindingEnchantment::new);
    public static final RegistryObject<Enchantment> LEAP =
            ENCHANTMENTS.register("leap", LeapEnchantment::new);
}