package com.dyxiaojiazi.morepracticalenchantments.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {

    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(Type.COMMON, COMMON_SPEC, "morepracticalenchantments-common.toml");
    }

    // ---------- 配置类 ----------
    public static class CommonConfig {

        public final ForgeConfigSpec.DoubleValue lifeStealPercentPerLevel;
        public final ForgeConfigSpec.IntValue magmaProtectionPercentPerLevel;
        public final ForgeConfigSpec.DoubleValue swiftnessAttackSpeedPerLevel;
        public final ForgeConfigSpec.IntValue experienceGainMultiplierPerLevel;
        public final ForgeConfigSpec.DoubleValue sprintBoostMaxSpeedPercent;
        public final ForgeConfigSpec.IntValue sprintBoostRampUpTimeSeconds;
        public final ForgeConfigSpec.IntValue naturalMendingCooldownLevel1;
        public final ForgeConfigSpec.IntValue naturalMendingCooldownLevel2;
        public final ForgeConfigSpec.IntValue naturalMendingCooldownLevel3;
        public final ForgeConfigSpec.IntValue naturalMendingCooldownLevel4;
        public final ForgeConfigSpec.IntValue naturalMendingCooldownLevel5;
        public final ForgeConfigSpec.IntValue pillagerEmeraldCountPerLevel;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("life_steal");
            lifeStealPercentPerLevel = builder
                    .comment("吸血每级生命恢复百分比（例如 0.05 表示 5%）")
                    .defineInRange("percentPerLevel", 0.05, 0.0, 1.0);
            builder.pop();

            builder.push("magma_protection");
            magmaProtectionPercentPerLevel = builder
                    .comment("岩浆保护每级减伤百分比（例如 20 表示 20%）")
                    .defineInRange("percentPerLevel", 20, 0, 100);
            builder.pop();

            builder.push("swiftness");
            swiftnessAttackSpeedPerLevel = builder
                    .comment("快手每级增加的攻击速度值（例如 0.2）")
                    .defineInRange("attackSpeedPerLevel", 0.2, 0.0, 10.0);
            builder.pop();

            builder.push("experience_gain");
            experienceGainMultiplierPerLevel = builder
                    .comment("经验汲取每级额外经验倍率（例如 1 表示 +100%）")
                    .defineInRange("multiplierPerLevel", 1, 0, 100);
            builder.pop();

            builder.push("sprint_boost");
            sprintBoostMaxSpeedPercent = builder
                    .comment("疾跑最终移速增幅百分比（例如 0.5 表示 50%）")
                    .defineInRange("maxSpeedPercent", 0.5, 0.0, 1.0);
            sprintBoostRampUpTimeSeconds = builder
                    .comment("疾跑达到最大移速所需时间（秒）")
                    .defineInRange("rampUpTimeSeconds", 5, 1, 60);
            builder.pop();

            builder.push("natural_mending");
            naturalMendingCooldownLevel1 = builder
                    .comment("自然修补 I 级冷却时间（毫秒）")
                    .defineInRange("cooldownLevel1", 15000, 1000, 60000);
            naturalMendingCooldownLevel2 = builder
                    .comment("自然修补 II 级冷却时间（毫秒）")
                    .defineInRange("cooldownLevel2", 12000, 1000, 60000);
            naturalMendingCooldownLevel3 = builder
                    .comment("自然修补 III 级冷却时间（毫秒）")
                    .defineInRange("cooldownLevel3", 9000, 1000, 60000);
            naturalMendingCooldownLevel4 = builder
                    .comment("自然修补 IV 级冷却时间（毫秒）")
                    .defineInRange("cooldownLevel4", 3000, 1000, 60000);
            naturalMendingCooldownLevel5 = builder
                    .comment("自然修补 V 级冷却时间（毫秒）")
                    .defineInRange("cooldownLevel5", 1000, 500, 60000);
            builder.pop();

            builder.push("pillager");
            pillagerEmeraldCountPerLevel = builder
                    .comment("掠夺者每级掉落绿宝石数量（例如 1 表示 I 级 1 颗）")
                    .defineInRange("emeraldCountPerLevel", 1, 0, 64);
            builder.pop();
        }
    }
}