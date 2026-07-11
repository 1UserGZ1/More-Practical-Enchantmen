package com.dyxiaojiazi.morepracticalenchantments.events;

import com.dyxiaojiazi.morepracticalenchantments.ModEffects;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.damagesource.DamageSource;
import com.dyxiaojiazi.morepracticalenchantments.MorePracticalEnchantments;
import com.dyxiaojiazi.morepracticalenchantments.ModEnchantments;
import com.dyxiaojiazi.morepracticalenchantments.utils.AttributeHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.common.Mod;


import java.lang.reflect.Field;
import java.util.*;

@Mod.EventBusSubscriber(modid = MorePracticalEnchantments.MOD_ID)
public class ModEvents {

    private static final Map<UUID, Integer> swiftnessLevels = new HashMap<>();
    private static final Map<UUID, Long> sprintStartTime = new HashMap<>();
    private static final Map<UUID, Map<Integer, Long>> mendingCooldowns = new HashMap<>();
    private static final Map<UUID, List<SavedItem>> rebirthItems = new HashMap<>();
    private static final Map<UUID, Long> playerLastDamageTime = new HashMap<>();


    private static class SavedItem {
        enum SlotType { INVENTORY, ARMOR, OFFHAND }
        SlotType type;
        int slotIndex;
        ItemStack stack;

        SavedItem(SlotType type, int slotIndex, ItemStack stack) {
            this.type = type;
            this.slotIndex = slotIndex;
            this.stack = stack;
        }
    }

    private static Field JUMPING_FIELD;
    static {
        try {
            JUMPING_FIELD = LivingEntity.class.getDeclaredField("jumping");
            JUMPING_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static boolean isPlayerJumping(Player player) {
        if (JUMPING_FIELD == null) return false;
        try {
            return JUMPING_FIELD.getBoolean(player);
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    //快手
    private static double getBaseAttackSpeed(ItemStack stack) {
        var modifiers = stack.getItem().getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
        var speedMods = modifiers.get(Attributes.ATTACK_SPEED);
        if (!speedMods.isEmpty()) {
            double amount = speedMods.iterator().next().getAmount();
            return 4.0 + amount;
        }
        return 4.0;
    }

    // ---------- 1. 吸血 ----------
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player) {
            Player player = (Player) event.getSource().getEntity();
            ItemStack weapon = player.getMainHandItem();
            int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LIFE_STEAL.get(), weapon);
            if (level > 0 && event.getEntity() instanceof LivingEntity) {
                float damage = event.getAmount();
                float heal = damage * 0.05f * level;
                if (heal > 0) {
                    player.heal(heal);
                }
            }
            // 不破已禁用
            // lockAllItems(player);
        }
        // 不破已禁用
        // if (event.getEntity() instanceof Player) lockAllItems((Player) event.getEntity());
    }

    // ----------2. 跳跃提升：15秒，脱下装备立即移除----------
    @SubscribeEvent
    public static void onPlayerTickJump(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.JUMP_BOOST.get(), player);
        if (level > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 300, 0, false, false, true));
        } else {
            player.removeEffect(MobEffects.JUMP);
        }
    }

    // ---------- 3. 岩浆保护 ----------
    @SubscribeEvent
    public static void onLivingDamageFire(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        String msgId = source.getMsgId();
        boolean isFire = msgId.equals("inFire") || msgId.equals("onFire") || msgId.equals("lava") || msgId.equals("hotFloor");
        if (isFire && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.MAGMA_PROTECTION.get(), player);
            if (level > 0) {
                float reduction = 0.2f * level;
                float newAmount = event.getAmount() * (1 - reduction);
                if (newAmount < 0) newAmount = 0;
                event.setAmount(newAmount);
            }
            // 不破已禁用
            // lockAllItems(player);
        }
        // 不破已禁用
        // if (event.getSource().getEntity() instanceof Player) lockAllItems((Player) event.getSource().getEntity());
    }

    // ---------- 4. 虚空保护（附近安全位置，5秒缓降） ----------
    @SubscribeEvent
    public static void onLivingDamageVoid(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        if (source.getMsgId().equals("outOfWorld") && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.VOID_PROTECTION.get(), player) > 0) {
                event.setCanceled(true);
                Level level = player.getCommandSenderWorld();
                BlockPos playerPos = player.blockPosition();
                BlockPos safePos = findSafePosition(level, playerPos, 5);
                if (safePos == null) {
                    safePos = new BlockPos(level.getLevelData().getXSpawn(),
                            level.getLevelData().getYSpawn(),
                            level.getLevelData().getZSpawn());
                }
                player.teleportTo(safePos.getX() + 0.5, safePos.getY(), safePos.getZ() + 0.5);
                player.setHealth(player.getMaxHealth());
                player.fallDistance = 0;
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0, false, false, true));
            }
        }
        // 不破已禁用
        // if (event.getEntity() instanceof Player) lockAllItems((Player) event.getEntity());
    }

    private static BlockPos findSafePosition(Level level, BlockPos center, int range) {
        for (int dx = -range; dx <= range; dx++) {
            for (int dz = -range; dz <= range; dz++) {
                int x = center.getX() + dx;
                int z = center.getZ() + dz;
                int topY = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                BlockPos candidate = new BlockPos(x, topY, z);
                if (isSafeStanding(level, candidate)) {
                    return candidate.above();
                }
            }
        }
        return null;
    }

    private static boolean isSafeStanding(Level level, BlockPos pos) {
        BlockState ground = level.getBlockState(pos);
        if (!ground.isSolidRender(level, pos)) return false;
        if (!level.getBlockState(pos.above()).isAir()) return false;
        if (!level.getBlockState(pos.above(2)).isAir()) return false;
        if (ground.getBlock() == Blocks.LAVA || ground.getBlock() == Blocks.MAGMA_BLOCK) return false;
        return true;
    }

    // ---------- 5. 快手 ----------
    @SubscribeEvent
    public static void onPlayerTickSwiftness(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        ItemStack weapon = player.getMainHandItem();
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SWIFTNESS.get(), weapon);
        UUID id = player.getUUID();
        AttributeHelper.removeAttackSpeedModifier(player);
        if (level > 0) {
            float bonus = 0.2f * level;
            AttributeHelper.addAttackSpeedModifier(player, bonus);
        }
        swiftnessLevels.put(id, level);
    }

    // ---------- 6. 经验汲取 ----------
    @SubscribeEvent
    public static void onLivingExperienceDrop(LivingExperienceDropEvent event) {
        if (event.getAttackingPlayer() != null) {
            Player player = event.getAttackingPlayer();
            ItemStack weapon = player.getMainHandItem();
            int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.EXPERIENCE_GAIN.get(), weapon);
            if (level > 0) {
                int extra = event.getDroppedExperience() * level;
                event.setDroppedExperience(event.getDroppedExperience() + extra);
            }
        }
    }

    // ---------- 7. 疾跑 ----------
    @SubscribeEvent
    public static void onPlayerTickSprint(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.isSprinting() && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.SPRINT_BOOST.get(), player) > 0) {
            long now = System.currentTimeMillis();
            UUID id = player.getUUID();
            if (!sprintStartTime.containsKey(id)) sprintStartTime.put(id, now);
            long elapsed = now - sprintStartTime.get(id);
            float progress = Math.min(elapsed / 5000.0f, 1.0f);
            float boost = progress * 0.5f;
            AttributeHelper.setSprintSpeedModifier(player, boost);
        } else {
            UUID id = player.getUUID();
            sprintStartTime.remove(id);
            AttributeHelper.removeSprintSpeedModifier(player);
        }
    }

    // ---------- 8. 自然修补 ----------
    @SubscribeEvent
    public static void onPlayerTickMending(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.getCommandSenderWorld().isClientSide) return;
        UUID playerId = player.getUUID();
        Map<Integer, Long> cooldownMap = mendingCooldowns.computeIfAbsent(playerId, k -> new HashMap<>());

        checkAndRepairSlot(player, player.getMainHandItem(), 0, cooldownMap);
        checkAndRepairSlot(player, player.getOffhandItem(), 1, cooldownMap);
        checkAndRepairSlot(player, player.getItemBySlot(EquipmentSlot.HEAD), 2, cooldownMap);
        checkAndRepairSlot(player, player.getItemBySlot(EquipmentSlot.CHEST), 3, cooldownMap);
        checkAndRepairSlot(player, player.getItemBySlot(EquipmentSlot.LEGS), 4, cooldownMap);
        checkAndRepairSlot(player, player.getItemBySlot(EquipmentSlot.FEET), 5, cooldownMap);
    }

    private static void checkAndRepairSlot(Player player, ItemStack stack, int slot, Map<Integer, Long> cooldownMap) {
        if (stack.isEmpty() || !stack.isDamageableItem()) return;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.NATURAL_MENDING.get(), stack);
        if (level <= 0 || stack.getDamageValue() <= 0) return;
        long now = System.currentTimeMillis();
        long cooldownMs = switch (level) {
            case 1 -> 15000L;  // I级：15秒
            case 2 -> 12000L;  // II级：12秒
            case 3 -> 9000L;   // III级：9秒
            case 4 -> 6000L;   // IV级：6秒
            case 5 -> 3000L;   // V级：3秒
            default -> 15000L;
        };
        Long last = cooldownMap.get(slot);
        if (last == null || (now - last) >= cooldownMs) {
            stack.setDamageValue(stack.getDamageValue() - 1);
            cooldownMap.put(slot, now);
        }
    }

    // ---------- 9. 熔炼（配合时运） ----------
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getMainHandItem();
        int smeltLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SMELTING.get(), tool);
        if (smeltLevel <= 0) return;

        BlockState state = event.getState();
        Block block = state.getBlock();
        boolean shouldSmelt = false;
        if (block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE ||
                block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE ||
                block == Blocks.COPPER_ORE || block == Blocks.DEEPSLATE_COPPER_ORE ||
                block == Blocks.SAND || block == Blocks.RED_SAND) {
            shouldSmelt = true;
        }
        if (!shouldSmelt) return;

        event.setCanceled(true);
        Level level = player.getCommandSenderWorld();
        if (level.isClientSide) return;

        List<ItemStack> drops = block.getDrops(state, (ServerLevel) level, event.getPos(), null, player, tool);

        List<ItemStack> newDrops = new ArrayList<>();
        for (ItemStack drop : drops) {
            ItemStack result = null;
            if (drop.getItem() == Items.RAW_IRON) result = new ItemStack(Items.IRON_INGOT, drop.getCount());
            else if (drop.getItem() == Items.RAW_GOLD) result = new ItemStack(Items.GOLD_INGOT, drop.getCount());
            else if (drop.getItem() == Items.RAW_COPPER) result = new ItemStack(Items.COPPER_INGOT, drop.getCount());
            else if (drop.getItem() == Items.SAND || drop.getItem() == Items.RED_SAND)
                result = new ItemStack(Items.GLASS, drop.getCount());
            else result = drop;
            newDrops.add(result);
        }

        level.setBlock(event.getPos(), Blocks.AIR.defaultBlockState(), 3);

        for (ItemStack stack : newDrops) {
            if (!stack.isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(level, event.getPos().getX() + 0.5, event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5, stack);
                level.addFreshEntity(itemEntity);
            }
        }
        // 不破已禁用
        // lockAllItems(player);
    }

    // ----------10. 夜视：15秒，脱下装备立即移除----------
    @SubscribeEvent
    public static void onPlayerTickNightVision(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.NIGHT_VISION.get(), helmet) > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false, true));
        } else {
            player.removeEffect(MobEffects.NIGHT_VISION);
        }
    }

    // ---------- 11. 掠夺者 ----------
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player) {
            Player player = (Player) event.getSource().getEntity();
            ItemStack weapon = player.getMainHandItem();
            int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.PILLAGER.get(), weapon);
            if (level > 0 && event.getEntity() instanceof LivingEntity) {
                Level levelObj = event.getEntity().getCommandSenderWorld();
                if (!levelObj.isClientSide) {
                    int count = level;
                    ItemStack emerald = new ItemStack(Items.EMERALD, count);
                    levelObj.addFreshEntity(new ItemEntity(levelObj,
                            event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                            emerald));
                }
            }
        }
    }

    // ---------- 12. 不破（已禁用，所有代码已注释） ----------
    /*
    @SubscribeEvent
    public static void onPlayerTickIndestructible(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        lockAllItems(player);
        // 鞘翅损坏禁用飞行
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!chest.isEmpty() && chest.getItem() == Items.ELYTRA) {
            int maxDmg = chest.getMaxDamage();
            if (maxDmg > 0) {
                int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.INDESTRUCTIBLE.get(), chest);
                if (level > 0 && chest.getDamageValue() >= maxDmg - 1 && player.isFallFlying()) {
                    player.stopFallFlying();
                }
            }
        }
    }
    */

    /*
    private static void lockAllItems(Player player) {
        if (player == null) return;
        lockItem(player.getMainHandItem());
        lockItem(player.getOffhandItem());
        for (ItemStack stack : player.getArmorSlots()) {
            lockItem(stack);
        }
    }
    */

    /*
    private static void lockItem(ItemStack stack) {
        if (stack.isEmpty()) return;
        int maxDamage = stack.getMaxDamage();
        if (maxDamage <= 0) return;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.INDESTRUCTIBLE.get(), stack);
        if (level <= 0) return;
        if (stack.getDamageValue() >= maxDamage - 1) {
            stack.setDamageValue(maxDamage - 1);
        }
    }
    */

    /*
    @SubscribeEvent
    public static void onItemAttributeModifiers(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;
        int maxDamage = stack.getMaxDamage();
        if (maxDamage <= 0) return;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.INDESTRUCTIBLE.get(), stack);
        if (level > 0 && stack.getDamageValue() >= maxDamage - 1) {
            event.clearModifiers();
            event.addModifier(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier("indestructible_penalty", -0.5, AttributeModifier.Operation.ADDITION));
        }
    }
    */

    /*
    private static boolean isBroken(ItemStack stack) {
        if (stack.isEmpty()) return false;
        int maxDamage = stack.getMaxDamage();
        if (maxDamage <= 0) return false;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.INDESTRUCTIBLE.get(), stack);
        if (level <= 0) return false;
        return stack.getDamageValue() >= maxDamage - 1;
    }
    */

    /*
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (isBroken(event.getItemStack())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (isBroken(event.getItemStack())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBlockBreakCancel(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (isBroken(player.getMainHandItem())) {
            event.setCanceled(true);
        }
    }
    */

    /*
    @SubscribeEvent
    public static void onPlayerDestroyItem(PlayerDestroyItemEvent event) {
        ItemStack stack = event.getOriginal();
        if (stack.isEmpty() || !stack.isDamageableItem()) return;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.INDESTRUCTIBLE.get(), stack);
        if (level > 0) {
            event.setCanceled(true);
            stack.setDamageValue(stack.getMaxDamage() - 1);
            Player player = event.getEntity();
            boolean equipped = false;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR && player.getItemBySlot(slot).isEmpty()) {
                    if (stack.getItem().canEquip(stack, slot, player)) {
                        player.setItemSlot(slot, stack);
                        equipped = true;
                        break;
                    }
                }
            }
            if (!equipped) {
                if (!player.addItem(stack)) {
                    player.drop(stack, false);
                }
            }
        }
    }
    */

    // ---------- 13. 借力 ----------
    @SubscribeEvent
    public static void onPlayerTickLeap(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chest.isEmpty() || chest.getItem() != Items.ELYTRA) return;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LEAP.get(), chest);
        if (level <= 0) return;
        if (player.isFallFlying() && player.zza > 0) {
            Vec3 lookVec = player.getLookAngle();
            double speed = 0.5;
            Vec3 motion = player.getDeltaMovement();
            double newX = motion.x + lookVec.x * speed * 0.1;
            double newZ = motion.z + lookVec.z * speed * 0.1;
            double horizSpeed = Math.sqrt(newX * newX + newZ * newZ);
            double maxSpeed = 3.0;
            if (horizSpeed > maxSpeed) {
                newX = newX / horizSpeed * maxSpeed;
                newZ = newZ / horizSpeed * maxSpeed;
            }
            player.setDeltaMovement(newX, motion.y, newZ);
        }
    }

    // ---------- 14. 重生绑定 ----------
    @SubscribeEvent
    public static void onLivingDeathRebirth(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UUID id = player.getUUID();
            List<SavedItem> keepItems = new ArrayList<>();

            for (int i = 0; i < player.getInventory().items.size(); i++) {
                ItemStack stack = player.getInventory().items.get(i);
                if (!stack.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.REBIRTH_BINDING.get(), stack) > 0) {
                    keepItems.add(new SavedItem(SavedItem.SlotType.INVENTORY, i, stack.copy()));
                }
            }
            for (int i = 0; i < player.getInventory().armor.size(); i++) {
                ItemStack stack = player.getInventory().armor.get(i);
                if (!stack.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.REBIRTH_BINDING.get(), stack) > 0) {
                    keepItems.add(new SavedItem(SavedItem.SlotType.ARMOR, i, stack.copy()));
                }
            }
            ItemStack offhand = player.getInventory().offhand.get(0);
            if (!offhand.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.REBIRTH_BINDING.get(), offhand) > 0) {
                keepItems.add(new SavedItem(SavedItem.SlotType.OFFHAND, 0, offhand.copy()));
            }

            if (!keepItems.isEmpty()) {
                rebirthItems.put(id, keepItems);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UUID id = player.getUUID();
            List<SavedItem> keepItems = rebirthItems.get(id);
            if (keepItems == null || keepItems.isEmpty()) return;
            Iterator<ItemEntity> iterator = event.getDrops().iterator();
            while (iterator.hasNext()) {
                ItemEntity entity = iterator.next();
                ItemStack stack = entity.getItem();
                for (SavedItem kept : keepItems) {
                    if (ItemStack.isSameItemSameTags(stack, kept.stack)) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player newPlayer = (Player) event.getEntity();
        UUID id = original.getUUID();
        List<SavedItem> kept = rebirthItems.get(id);
        if (kept != null && !kept.isEmpty()) {
            for (SavedItem item : kept) {
                switch (item.type) {
                    case INVENTORY:
                        newPlayer.getInventory().items.set(item.slotIndex, item.stack);
                        break;
                    case ARMOR:
                        newPlayer.getInventory().armor.set(item.slotIndex, item.stack);
                        break;
                    case OFFHAND:
                        newPlayer.getInventory().offhand.set(0, item.stack);
                        break;
                }
            }
            rebirthItems.remove(id);
        }
    }

    // ---------- 15. 工具提示（移除不破相关，保留快手攻速） ----------
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        // 不破已禁用，不再添加 (损坏) 标记和过滤属性行

        // 处理快手攻速
        int swiftLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SWIFTNESS.get(), stack);
        if (swiftLevel > 0) {
            double baseSpeed = getBaseAttackSpeed(stack);
            double totalSpeed = baseSpeed + 0.2 * swiftLevel;
            List<Component> tooltip = event.getToolTip();
            for (int i = 0; i < tooltip.size(); i++) {
                Component line = tooltip.get(i);
                String lineStr = line.getString();
                if (lineStr.contains("attribute.name.generic.attack_speed") || lineStr.contains("攻击速度")) {
                    tooltip.set(i, Component.literal(" ")
                            .append(Component.literal(String.format("%.1f", totalSpeed))
                                    .withStyle(ChatFormatting.WHITE))
                            .append(Component.literal(" "))
                            .append(Component.translatable("attribute.name.generic.attack_speed")
                                    .withStyle(ChatFormatting.DARK_GREEN)));
                    break;
                }
            }
        }
    }

    // ---------- 14. 猫薄荷（苦力怕逃离，猫主动跟随） ----------
    @SubscribeEvent
    public static void onCatMintTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.getCommandSenderWorld().isClientSide) return;
        if (!hasCatMintArmor(player)) return;
        double range = 12.0;
        AABB area = player.getBoundingBox().inflate(range);

        // 苦力怕：远离玩家
        for (Creeper creeper : player.getCommandSenderWorld().getEntitiesOfClass(Creeper.class, area)) {
            if (creeper.getTarget() == player) {
                creeper.setTarget(null);
            }
            Vec3 toPlayer = player.position().subtract(creeper.position());
            Vec3 awayPos = creeper.position().subtract(toPlayer.normalize().scale(10));
            creeper.getNavigation().moveTo(awayPos.x, awayPos.y, awayPos.z, 1.5);
        }

        // 猫：坐下的不跟随，未坐下的主动靠近
        for (Cat cat : player.getCommandSenderWorld().getEntitiesOfClass(Cat.class, area)) {
            cat.setLastHurtByMob(null);
            cat.setLastHurtByPlayer(null);

            if (cat.isOrderedToSit()) {
                continue;
            }

            double dist = cat.distanceToSqr(player);
            double minFollowDist = 2.25;
            if (dist > minFollowDist) {
                cat.getNavigation().moveTo(player, 1.5);
            } else {
                cat.getNavigation().stop();
            }
        }
    }

    private static boolean hasCatMintArmor(Player player) {
        for (ItemStack stack : player.getArmorSlots()) {
            if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.CAT_MINT.get(), stack) > 0) return true;
        }
        return false;
    }

    // ---------- 15. 压制 ----------
    @SubscribeEvent
    public static void onSuppress(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player) {
            Player player = (Player) event.getSource().getEntity();
            ItemStack weapon = player.getMainHandItem();
            if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SUPPRESS.get(), weapon) > 0) {
                if (event.getEntity() instanceof LivingEntity) {
                    LivingEntity target = (LivingEntity) event.getEntity();
                    target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
                }
            }
        }
    }

    // ---------- 16. 霜冻 ----------
    private static final Map<UUID, Integer> frostDamageMap = new HashMap<>();

    @SubscribeEvent
    public static void onFrostHit(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getProjectile();
        if (!(arrow.getOwner() instanceof Player)) return;
        Player player = (Player) arrow.getOwner();
        ItemStack weapon = player.getMainHandItem();
        if (weapon.getItem() != Items.BOW && weapon.getItem() != Items.CROSSBOW) return;
        if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.FROST.get(), weapon) <= 0) return;

        if (event.getRayTraceResult() instanceof EntityHitResult) {
            EntityHitResult entityHit = (EntityHitResult) event.getRayTraceResult();
            if (entityHit.getEntity() instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) entityHit.getEntity();
                // 缓慢III 3秒
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
                // 自定义霜冻效果 3秒
                living.addEffect(new MobEffectInstance(ModEffects.FROST.get(), 60, 0));
                UUID id = living.getUUID();
                frostDamageMap.put(id, 3);
            }
        }
    }

    @SubscribeEvent
    public static void onFrostTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        ServerLevel level = event.getServer().overworld();
        if (level.getGameTime() % 20 != 0) return;
        Iterator<Map.Entry<UUID, Integer>> it = frostDamageMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, Integer> entry = it.next();
            Entity entity = level.getEntity(entry.getKey());
            if (entity instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) entity;
                if (living.isAlive()) {
                    living.hurt(living.damageSources().freeze(), 0.5f);
                    int remain = entry.getValue() - 1;
                    if (remain <= 0) it.remove();
                    else entry.setValue(remain);
                } else {
                    it.remove();
                }
            } else {
                it.remove();
            }
        }
    }

    // ---------- 17. 深海洞察（客户端） ----------
    @SubscribeEvent
    public static void onFogRender(ViewportEvent.RenderFog event) {
        Player player = net.minecraft.client.Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.DEEP_INSIGHT.get(), helmet) <= 0) return;
        if (player.isEyeInFluid(FluidTags.WATER) || player.isEyeInFluid(FluidTags.LAVA)) {
            event.setFarPlaneDistance(1000.0f);
            event.setNearPlaneDistance(0.0f);
            event.setCanceled(true);
        }
    }

    // 18. ---------- 耐寒：免疫冰冻伤害 ----------
    @SubscribeEvent
    public static void cancelFreezeDamage(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        if (!source.getMsgId().equals("freeze")) return;
        if (!(event.getEntity() instanceof Player player)) return;

        // 检查玩家所有盔甲是否有耐寒附魔
        for (ItemStack armor : player.getArmorSlots()) {
            if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.COLD_RESISTANCE.get(), armor) > 0) {
                event.setCanceled(true);
                return;
            }
        }
    }

    // 19. ---------- 斩首：概率掉落头颅 ----------
    @SubscribeEvent
    public static void onLivingDeathBeheading(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        if (!(source.getEntity() instanceof Player attacker)) return;
        ItemStack weapon = attacker.getMainHandItem();
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.BEHEADING.get(), weapon);
        if (level <= 0) return;

        // 概率判定 (I:10% ~ V:50%)
        if (attacker.getRandom().nextFloat() > level * 0.1f) return;

        Entity killed = event.getEntity();
        ItemStack head = null;
        // 判断生物类型
        if (killed instanceof Skeleton) {
            head = new ItemStack(Items.SKELETON_SKULL);
        } else if (killed instanceof WitherSkeleton) {
            head = new ItemStack(Items.WITHER_SKELETON_SKULL);
        } else if (killed instanceof Zombie) {
            head = new ItemStack(Items.ZOMBIE_HEAD);
        } else if (killed instanceof Player) {
            // 玩家头：获取被击杀玩家的游戏档案
            Player playerKilled = (Player) killed;
            GameProfile profile = playerKilled.getGameProfile();
            head = new ItemStack(Items.PLAYER_HEAD);
            // 设置头颅的GameProfile (需要NBT)
            head.getOrCreateTag().putString("SkullOwner", profile.getName());
            // 也可使用更完整的序列化，但简单写名字也行
        } else if (killed instanceof Creeper) {
            head = new ItemStack(Items.CREEPER_HEAD);
        } else if (killed instanceof EnderDragon) {
            head = new ItemStack(Items.DRAGON_HEAD);
        } else if (killed instanceof Piglin) {
            head = new ItemStack(Items.PIGLIN_HEAD);
        }

        if (head != null) {
            killed.spawnAtLocation(head);
        }
    }

    // 20. ---------- 稳固：盾牌冷却时立即重置（若盾牌附魔稳固） ----------
    @SubscribeEvent
    public static void onPlayerTickSteadfast(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        ItemStack offhand = player.getOffhandItem();
        if (offhand.getItem() != Items.SHIELD) return;
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.STEADFAST.get(), offhand);
        if (level <= 0) return;

        // 如果盾牌正处于冷却中，重置冷却
        if (player.getCooldowns().isOnCooldown(offhand.getItem())) {
            player.getCooldowns().removeCooldown(offhand.getItem());
        }
    }

    // 21. ---------- 幸运：根据防具等级提供 Luck 效果 ----------
    @SubscribeEvent
    public static void onPlayerTickLuck(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        int maxLevel = 0;
        for (ItemStack armor : player.getArmorSlots()) {
            int lvl = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LUCK.get(), armor);
            if (lvl > maxLevel) maxLevel = lvl;
        }
        if (maxLevel > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.LUCK, 300, maxLevel - 1, false, false, true));
        } else {
            player.removeEffect(MobEffects.LUCK);
        }
    }

    // 22. ---------- 盔甲保护Ⅴ：记录受伤时间 ----------
    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            UUID id = player.getUUID();
            playerLastDamageTime.put(id, System.currentTimeMillis());
            // 受伤后清空当前吸收值（可选，原版会自动扣除，但为了重置，我们不强刷，让补充逻辑处理）
        }
    }

    // 22. ---------- 盔甲保护Ⅴ：补充吸收值 ----------
    @SubscribeEvent
    public static void onPlayerTickArmorProtection(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.getCommandSenderWorld().isClientSide) return;

        // 获取最高等级的盔甲保护附魔
        int maxLevel = 0;
        for (ItemStack armor : player.getArmorSlots()) {
            int lvl = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.ARMOR_PROTECTION.get(), armor);
            if (lvl > maxLevel) maxLevel = lvl;
        }
        if (maxLevel == 0) {
            // 如果没有该附魔，移除可能残留的吸收值（防止脱下装备后残留）
            if (player.getAbsorptionAmount() > 0) {
                player.setAbsorptionAmount(0);
            }
            return;
        }

        UUID id = player.getUUID();
        long lastDamage = playerLastDamageTime.getOrDefault(id, 0L);
        long now = System.currentTimeMillis();

        // 如果超过5秒未受伤，补充吸收值
        if (now - lastDamage >= 5000) {
            float maxAbsorption = maxLevel * 2.0f; // 每级2点吸收
            float current = player.getAbsorptionAmount();
            if (current < maxAbsorption) {
                player.setAbsorptionAmount(maxAbsorption);
            }
        }
    }




}