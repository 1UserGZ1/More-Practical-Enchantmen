package com.dyxiaojiazi.morepracticalenchantments;

import com.dyxiaojiazi.morepracticalenchantments.enchantments.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MorePracticalEnchantments.MOD_ID);

    // 1. 吸血 (Life Steal)
    public static final RegistryObject<Enchantment> LIFE_STEAL =
            ENCHANTMENTS.register("life_steal", LifeStealEnchantment::new);

    // 2. 跳跃提升 (Jump Boost)
    public static final RegistryObject<Enchantment> JUMP_BOOST =
            ENCHANTMENTS.register("jump_boost", JumpBoostEnchantment::new);

    // 3. 岩浆保护 (Magma Protection)
    public static final RegistryObject<Enchantment> MAGMA_PROTECTION =
            ENCHANTMENTS.register("magma_protection", MagmaProtectionEnchantment::new);

    // 4. 虚空保护 (Void Protection)
    public static final RegistryObject<Enchantment> VOID_PROTECTION =
            ENCHANTMENTS.register("void_protection", VoidProtectionEnchantment::new);

    // 5. 快手 (Swiftness)
    public static final RegistryObject<Enchantment> SWIFTNESS =
            ENCHANTMENTS.register("swiftness", SwiftnessEnchantment::new);

    // 6. 经验汲取 (Experience Gain)
    public static final RegistryObject<Enchantment> EXPERIENCE_GAIN =
            ENCHANTMENTS.register("experience_gain", ExperienceGainEnchantment::new);

    // 7. 疾跑 (Sprint Boost)
    public static final RegistryObject<Enchantment> SPRINT_BOOST =
            ENCHANTMENTS.register("sprint_boost", SprintBoostEnchantment::new);

    // 8. 自然修补 (Natural Mending)
    public static final RegistryObject<Enchantment> NATURAL_MENDING =
            ENCHANTMENTS.register("natural_mending", NaturalMendingEnchantment::new);

    // 9. 熔炼 (Smelting)
    public static final RegistryObject<Enchantment> SMELTING =
            ENCHANTMENTS.register("smelting", SmeltingEnchantment::new);

    // 10. 夜视 (Night Vision)
    public static final RegistryObject<Enchantment> NIGHT_VISION =
            ENCHANTMENTS.register("night_vision", NightVisionEnchantment::new);

    // 11. 掠夺者 (Pillager)
    public static final RegistryObject<Enchantment> PILLAGER =
            ENCHANTMENTS.register("pillager", PillagerEnchantment::new);

    // 12. 重生绑定 (Rebirth Binding)
    public static final RegistryObject<Enchantment> REBIRTH_BINDING =
            ENCHANTMENTS.register("rebirth_binding", RebirthBindingEnchantment::new);

    // 13. 借力 (Leap)
    public static final RegistryObject<Enchantment> LEAP =
            ENCHANTMENTS.register("leap", LeapEnchantment::new);

    // 14. 猫薄荷 (Cat Mint)
    public static final RegistryObject<Enchantment> CAT_MINT =
            ENCHANTMENTS.register("cat_mint", CatMintEnchantment::new);

    // 15. 压制 (Suppress)
    public static final RegistryObject<Enchantment> SUPPRESS =
            ENCHANTMENTS.register("suppress", SuppressEnchantment::new);

    // 16. 霜冻 (Frost)
    public static final RegistryObject<Enchantment> FROST =
            ENCHANTMENTS.register("frost", FrostEnchantment::new);

    // 17. 深海洞察 (Deep Insight)
    public static final RegistryObject<Enchantment> DEEP_INSIGHT =
            ENCHANTMENTS.register("deep_insight", DeepInsightEnchantment::new);

    // 18. 耐寒 (Cold Resistance)
    public static final RegistryObject<Enchantment> COLD_RESISTANCE =
            ENCHANTMENTS.register("cold_resistance", ColdResistanceEnchantment::new);

    // 19. 斩首 (Beheading) - 新增
    public static final RegistryObject<Enchantment> BEHEADING =
            ENCHANTMENTS.register("beheading", BeheadingEnchantment::new);

    // 20. 稳固 (Steadfast) - 新增
    public static final RegistryObject<Enchantment> STEADFAST =
            ENCHANTMENTS.register("steadfast", SteadfastEnchantment::new);

    // 21. 幸运 (Luck) - 新增
    public static final RegistryObject<Enchantment> LUCK =
            ENCHANTMENTS.register("luck", LuckEnchantment::new);

    // 22. 盔甲保护 (Armor Protection) - 新增
    public static final RegistryObject<Enchantment> ARMOR_PROTECTION =
            ENCHANTMENTS.register("armor_protection", ArmorProtectionEnchantment::new);
}