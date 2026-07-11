package com.dyxiaojiazi.morepracticalenchantments.utils;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class AttributeHelper {
    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("af8b7e9c-9d3a-4b6a-9e1e-5b5c6d7e8f9a");
    private static final UUID SPRINT_SPEED_UUID = UUID.fromString("bf8b7e9c-9d3a-4b6a-9e1e-5b5c6d7e8f9b");
    private static final UUID INDESTRUCTIBLE_PENALTY_UUID = UUID.fromString("cf8b7e9c-9d3a-4b6a-9e1e-5b5c6d7e8f9c");

    public static void addAttackSpeedModifier(Player player, float bonus) {
        var attr = player.getAttribute(Attributes.ATTACK_SPEED);
        if (attr != null) {
            attr.removeModifier(ATTACK_SPEED_UUID);
            attr.addTransientModifier(new AttributeModifier(ATTACK_SPEED_UUID, "swiftness_bonus", bonus,
                    AttributeModifier.Operation.ADDITION));
        }
    }

    public static void removeAttackSpeedModifier(Player player) {
        var attr = player.getAttribute(Attributes.ATTACK_SPEED);
        if (attr != null) attr.removeModifier(ATTACK_SPEED_UUID);
    }

    public static void setSprintSpeedModifier(Player player, float boost) {
        var attr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr != null) {
            attr.removeModifier(SPRINT_SPEED_UUID);
            attr.addTransientModifier(new AttributeModifier(SPRINT_SPEED_UUID, "sprint_boost", boost,
                    AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    public static void removeSprintSpeedModifier(Player player) {
        var attr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr != null) attr.removeModifier(SPRINT_SPEED_UUID);
    }

    public static void applyIndestructiblePenalty(Player player) {
        removeIndestructiblePenalty(player);
        var armorAttr = player.getAttribute(Attributes.ARMOR);
        if (armorAttr != null) {
            armorAttr.addTransientModifier(new AttributeModifier(INDESTRUCTIBLE_PENALTY_UUID, "indestructible_penalty",
                    -1000, AttributeModifier.Operation.ADDITION));
        }
        var toughnessAttr = player.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (toughnessAttr != null) {
            toughnessAttr.addTransientModifier(new AttributeModifier(INDESTRUCTIBLE_PENALTY_UUID, "indestructible_penalty",
                    -1000, AttributeModifier.Operation.ADDITION));
        }
        var attackAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackAttr != null) {
            attackAttr.addTransientModifier(new AttributeModifier(INDESTRUCTIBLE_PENALTY_UUID, "indestructible_penalty",
                    -1000, AttributeModifier.Operation.ADDITION));
        }
    }

    public static void removeIndestructiblePenalty(Player player) {
        var armorAttr = player.getAttribute(Attributes.ARMOR);
        if (armorAttr != null) armorAttr.removeModifier(INDESTRUCTIBLE_PENALTY_UUID);
        var toughnessAttr = player.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (toughnessAttr != null) toughnessAttr.removeModifier(INDESTRUCTIBLE_PENALTY_UUID);
        var attackAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackAttr != null) attackAttr.removeModifier(INDESTRUCTIBLE_PENALTY_UUID);
    }
}