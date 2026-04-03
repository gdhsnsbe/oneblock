package com.oneblock;

import com.oneblock.phase.PhaseRegistry;
import com.oneblock.phase.PhaseRegistry.Phase;
import com.oneblock.util.OneBlockData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

import static net.minecraft.server.command.CommandManager.literal;

public class OneBlockMod implements ModInitializer {

    public static final String MOD_ID = "oneblock";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final Random RANDOM = new Random();

    // Chance a loot chest spawns after breaking (1 in N)
    private static final int CHEST_CHANCE = 25;
    // Chance a mob spawns after breaking (1 in N)
    private static final int MOB_CHANCE = 10;

    @Override
    public void onInitialize() {
        LOGGER.info("[OneBlock] Initializing OneBlock mod...");
        registerBlockBreakEvent();
        registerCommands();
    }

    // ─────────────────────────────────────────────
    // Block Break Event
    // ─────────────────────────────────────────────
    private void registerBlockBreakEvent() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient() || !(world instanceof ServerWorld serverWorld)) return;

            OneBlockData data = OneBlockData.get(serverWorld.getServer());

            // Only process registered OneBlock positions
            if (!data.isOneBlock(pos)) return;

            long count = data.increment(pos);
            Phase currentPhase = PhaseRegistry.getPhaseForCount(count);
            int phaseIndex = PhaseRegistry.getPhaseIndex(count);

            // Announce phase transitions
            int lastPhase = data.getLastAnnouncedPhase(pos);
            if (phaseIndex != lastPhase) {
                data.setLastAnnouncedPhase(pos, phaseIndex);
                broadcastPhaseChange(serverWorld.getServer(), currentPhase, phaseIndex + 1);
            }

            // Place the next block
            Block nextBlock = pickBlock(currentPhase);
            serverWorld.setBlockState(pos, nextBlock.getDefaultState());

            // Maybe spawn a mob above the block
            if (RANDOM.nextInt(MOB_CHANCE) == 0 && !currentPhase.mobs().isEmpty()) {
                spawnMob(serverWorld, pos, currentPhase);
            }

            // Maybe spawn a loot chest
            if (RANDOM.nextInt(CHEST_CHANCE) == 0 && !currentPhase.lootTables().isEmpty()) {
                spawnLootChest(serverWorld, pos, currentPhase);
            }
        });
    }

    // ─────────────────────────────────────────────
    // Commands
    // ─────────────────────────────────────────────
    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            // /oneblock set — marks the block you're standing on as a OneBlock
            dispatcher.register(literal("oneblock")
                .requires(src -> src.hasPermissionLevel(2))
                .then(literal("set")
                    .executes(ctx -> {
                        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
                        BlockPos pos = player.getBlockPos().down();
                        OneBlockData data = OneBlockData.get(ctx.getSource().getServer());
                        data.register(pos);
                        ServerWorld world = (ServerWorld) player.getWorld();
                        // Place grass block as the starting block
                        world.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState());
                        player.sendMessage(Text.literal("§a[OneBlock] §fRegistered OneBlock at " + pos.toShortString()), false);
                        return 1;
                    })
                )
                // /oneblock info — shows current phase for the block below
                .then(literal("info")
                    .executes(ctx -> {
                        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
                        BlockPos pos = player.getBlockPos().down();
                        OneBlockData data = OneBlockData.get(ctx.getSource().getServer());
                        if (!data.isOneBlock(pos)) {
                            player.sendMessage(Text.literal("§c[OneBlock] No OneBlock registered at " + pos.toShortString()), false);
                            return 0;
                        }
                        long count = data.getCount(pos);
                        Phase phase = PhaseRegistry.getPhaseForCount(count);
                        int phaseIndex = PhaseRegistry.getPhaseIndex(count);
                        player.sendMessage(Text.literal(
                            "§6[OneBlock] §fPosition: §e" + pos.toShortString()
                            + "\n§fBlocks broken: §e" + count
                            + "\n§fPhase: " + phase.color() + (phaseIndex + 1) + " - " + phase.name()
                        ), false);
                        return 1;
                    })
                )
                // /oneblock list — lists all registered OneBlock positions
                .then(literal("list")
                    .executes(ctx -> {
                        OneBlockData data = OneBlockData.get(ctx.getSource().getServer());
                        var keys = data.getAllKeys();
                        if (keys.isEmpty()) {
                            ctx.getSource().sendFeedback(() -> Text.literal("§c[OneBlock] No OneBlocks registered."), false);
                        } else {
                            ctx.getSource().sendFeedback(() -> Text.literal("§6[OneBlock] §fRegistered positions:"), false);
                            keys.forEach(k -> ctx.getSource().sendFeedback(() -> Text.literal("  §7- §f" + k), false));
                        }
                        return 1;
                    })
                )
            );
        });
    }

    // ─────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────

    private Block pickBlock(Phase phase) {
        List<Block> blocks = phase.blocks();
        return blocks.get(RANDOM.nextInt(blocks.size()));
    }

    private void broadcastPhaseChange(MinecraftServer server, Phase phase, int phaseNumber) {
        String message = phase.color() + "✦ OneBlock Phase " + phaseNumber + ": " + phase.name() + " §fhas begun!";
        server.getPlayerManager().getPlayerList().forEach(p ->
            p.sendMessage(Text.literal(message), false)
        );
        LOGGER.info("[OneBlock] Phase changed to: {} ({})", phaseNumber, phase.name());
    }

    private void spawnMob(ServerWorld world, BlockPos pos, Phase phase) {
        List<EntityType<?>> mobs = phase.mobs();
        EntityType<?> mobType = mobs.get(RANDOM.nextInt(mobs.size()));
        BlockPos spawnPos = pos.up();

        // Make sure there's space above
        if (!world.getBlockState(spawnPos).isAir()) return;

        Entity mob = mobType.create(world);
        if (mob == null) return;
        mob.refreshPositionAndAngles(
            spawnPos.getX() + 0.5,
            spawnPos.getY(),
            spawnPos.getZ() + 0.5,
            RANDOM.nextFloat() * 360f, 0f
        );
        world.spawnEntity(mob);
    }

    private void spawnLootChest(ServerWorld world, BlockPos pos, Phase phase) {
        BlockPos chestPos = pos.up();

        // Don't overwrite air — place chest one block above the oneblock
        if (!world.getBlockState(chestPos).isAir()) {
            chestPos = chestPos.up(); // try two up
        }
        if (!world.getBlockState(chestPos).isAir()) return;

        world.setBlockState(chestPos, Blocks.CHEST.getDefaultState());

        ChestBlockEntity chest = (ChestBlockEntity) world.getBlockEntity(chestPos);
        if (chest == null) return;

        List<List<ItemStack>> lootTables = phase.lootTables();
        List<ItemStack> loot = lootTables.get(RANDOM.nextInt(lootTables.size()));

        // Place items in random slots
        List<Integer> slots = new java.util.ArrayList<>();
        for (int i = 0; i < 27; i++) slots.add(i);
        java.util.Collections.shuffle(slots, RANDOM);

        for (int i = 0; i < loot.size() && i < slots.size(); i++) {
            chest.setStack(slots.get(i), loot.get(i).copy());
        }
    }
}
