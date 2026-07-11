package com.dyxiaojiazi.morepracticalenchantments.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FrostEffect extends MobEffect {
    public FrostEffect() {
        super(MobEffectCategory.HARMFUL, 0x88DDFF);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // 伤害逻辑在 ModEvents 中处理
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}