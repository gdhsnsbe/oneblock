package com.oneblock.phase;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;

import java.util.*;

public class PhaseRegistry {

    public record Phase(
        String name,
        String color,       // ANSI/Minecraft formatting code (e.g. "§a")
        List<Block> blocks,
        List<EntityType<?>> mobs,
        List<List<ItemStack>> lootTables, // each inner list = one chest's contents
        int blocksPerPhase
    ) {}

    private static final List<Phase> PHASES = new ArrayList<>();

    static {
        // ─────────────────────────────────────────────
        // PHASE 1 — Plains / Classic Start
        // ─────────────────────────────────────────────
        PHASES.add(new Phase(
            "Plains",
            "§a",
            List.of(
                Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.DIRT, Blocks.COARSE_DIRT,
                Blocks.GRAVEL, Blocks.SAND, Blocks.CLAY,
                Blocks.OAK_LOG, Blocks.OAK_LEAVES, Blocks.OAK_SAPLING,
                Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES,
                Blocks.DANDELION, Blocks.POPPY, Blocks.SHORT_GRASS,
                Blocks.STONE, Blocks.COBBLESTONE
            ),
            List.of(EntityType.COW, EntityType.SHEEP, EntityType.PIG, EntityType.CHICKEN),
            List.of(
                List.of(
                    new ItemStack(Items.OAK_PLANKS, 16),
                    new ItemStack(Items.APPLE, 8),
                    new ItemStack(Items.WHEAT_SEEDS, 16),
                    new ItemStack(Items.BONE_MEAL, 8)
                )
            ),
            100
        ));

        // ─────────────────────────────────────────────
        // PHASE 2 — Underground / Stone Age
        // ─────────────────────────────────────────────
        PHASES.add(new Phase(
            "Underground",
            "§7",
            List.of(
                Blocks.STONE, Blocks.STONE, Blocks.COBBLESTONE, Blocks.COBBLESTONE,
                Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE,
                Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.COPPER_ORE,
                Blocks.GRAVEL, Blocks.TUFF, Blocks.CALCITE,
                Blocks.MOSSY_COBBLESTONE, Blocks.MOSSY_STONE_BRICKS,
                Blocks.INFESTED_STONE
            ),
            List.of(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.CAVE_SPIDER),
            List.of(
                List.of(
                    new ItemStack(Items.IRON_INGOT, 8),
                    new ItemStack(Items.COAL, 16),
                    new ItemStack(Items.STONE_PICKAXE, 1),
                    new ItemStack(Items.BREAD, 6),
                    new ItemStack(Items.TORCH, 32)
                )
            ),
            200
        ));

        // ─────────────────────────────────────────────
        // PHASE 3 — Ocean / Aquatic
        // ─────────────────────────────────────────────
        PHASES.add(new Phase(
            "Ocean",
            "§b",
            List.of(
                Blocks.SAND, Blocks.SAND, Blocks.GRAVEL,
                Blocks.KELP, Blocks.SEAGRASS,
                Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.DARK_PRISMARINE,
                Blocks.SEA_LANTERN, Blocks.SPONGE, Blocks.WET_SPONGE,
                Blocks.TUBE_CORAL_BLOCK, Blocks.BRAIN_CORAL_BLOCK, Blocks.BUBBLE_CORAL_BLOCK,
                Blocks.CLAY, Blocks.MUD
            ),
            List.of(EntityType.DROWNED, EntityType.GUARDIAN, EntityType.SQUID, EntityType.GLOW_SQUID),
            List.of(
                List.of(
                    new ItemStack(Items.COD, 8),
                    new ItemStack(Items.SALMON, 6),
                    new ItemStack(Items.PRISMARINE_SHARD, 12),
                    new ItemStack(Items.HEART_OF_THE_SEA, 1),
                    new ItemStack(Items.IRON_INGOT, 6)
                )
            ),
            200
        ));

        // ─────────────────────────────────────────────
        // PHASE 4 — Desert
        // ─────────────────────────────────────────────
        PHASES.add(new Phase(
            "Desert",
            "§e",
            List.of(
                Blocks.SAND, Blocks.SAND, Blocks.SAND,
                Blocks.RED_SAND, Blocks.SANDSTONE, Blocks.SMOOTH_SANDSTONE,
                Blocks.CUT_SANDSTONE, Blocks.CHISELED_SANDSTONE,
                Blocks.DEAD_BUSH, Blocks.CACTUS,
                Blocks.TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA,
                Blocks.GOLD_ORE, Blocks.LAPIS_ORE
            ),
            List.of(EntityType.HUSK, EntityType.WITCH, EntityType.PILLAGER),
            List.of(
                List.of(
                    new ItemStack(Items.GOLD_INGOT, 8),
                    new ItemStack(Items.LAPIS_LAZULI, 16),
                    new ItemStack(Items.EMERALD, 3),
                    new ItemStack(Items.CACTUS, 8),
                    new ItemStack(Items.TNT, 2)
                )
            ),
            200
        ));

        // ─────────────────────────────────────────────
        // PHASE 5 — Jungle
        // ─────────────────────────────────────────────
        PHASES.add(new Phase(
            "Jungle",
            "§2",
            List.of(
                Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES, Blocks.JUNGLE_SAPLING,
                Blocks.MOSS_BLOCK, Blocks.MOSS_CARPET,
                Blocks.FERN, Blocks.LARGE_FERN, Blocks.VINE,
                Blocks.BAMBOO, Blocks.MELON,
                Blocks.COCOA, Blocks.JUNGLE_WOOD,
                Blocks.EMERALD_ORE, Blocks.DIAMOND_ORE
            ),
            List.of(EntityType.OCELOT, EntityType.PARROT, EntityType.CREEPER, EntityType.ZOMBIE),
            List.of(
                List.of(
                    new ItemStack(Items.DIAMOND, 3),
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(Items.MELON_SLICE, 16),
                    new ItemStack(Items.COCOA_BEANS, 16),
                    new ItemStack(Items.BAMBOO, 32)
                )
            ),
            200
        ));

        // ─────────────────────────────────────────────
        // PHASE 6 — Snowy Tundra
        // ─────────────────────────────────────────────
        PHASES.add(new Phase(
            "Tundra",
            "§f",
            List.of(
                Blocks.SNOW_BLOCK, Blocks.SNOW, Blocks.POWDER_SNOW,
                Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE,
                Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES, Blocks.SPRUCE_SAPLING,
                Blocks.STONE, Blocks.IRON_ORE, Blocks.COAL_ORE,
                Blocks.SWEET_BERRY_BUSH
            ),
            List.of(EntityType.STRAY, EntityType.POLAR_BEAR, EntityType.WOLF, EntityType.FOX),
            List.of(
                List.of(
                    new ItemStack(Items.IRON_INGOT, 12),
                    new ItemStack(Items.LEATHER, 8),
                    new ItemStack(Items.BLUE_ICE, 4),
                    new ItemStack(Items.SWEET_BERRIES, 16),
                    new ItemStack(Items.IRON_SWORD, 1)
                )
            ),
            200
        ));

        // ─────────────────────────────────────────────
        // PHASE 7 — Nether
        // ─────────────────────────────────────────────
        PHASES.add(new Phase(
            "Nether",
            "§c",
            List.of(
                Blocks.NETHERRACK, Blocks.NETHERRACK, Blocks.NETHERRACK,
                Blocks.NETHER_BRICKS, Blocks.CRACKED_NETHER_BRICKS,
                Blocks.BASALT, Blocks.BLACKSTONE, Blocks.GILDED_BLACKSTONE,
                Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_GOLD_ORE,
                Blocks.SOUL_SAND, Blocks.SOUL_SOIL,
                Blocks.MAGMA_BLOCK, Blocks.GLOWSTONE,
                Blocks.WARPED_NYLIUM, Blocks.CRIMSON_NYLIUM,
                Blocks.ANCIENT_DEBRIS
            ),
            List.of(EntityType.BLAZE, EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN, EntityType.GHAST, EntityType.WITHER_SKELETON),
            List.of(
                List.of(
                    new ItemStack(Items.NETHERITE_SCRAP, 2),
                    new ItemStack(Items.BLAZE_ROD, 8),
                    new ItemStack(Items.GOLD_INGOT, 16),
                    new ItemStack(Items.QUARTZ, 32),
                    new ItemStack(Items.OBSIDIAN, 8),
                    new ItemStack(Items.ANCIENT_DEBRIS, 1)
                )
            ),
            300
        ));

        // ─────────────────────────────────────────────
        // PHASE 8 — End
        // ─────────────────────────────────────────────
        PHASES.add(new Phase(
            "The End",
            "§5",
            List.of(
                Blocks.END_STONE, Blocks.END_STONE, Blocks.END_STONE_BRICKS,
                Blocks.PURPUR_BLOCK, Blocks.PURPUR_PILLAR,
                Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN,
                Blocks.CHORUS_PLANT, Blocks.CHORUS_FLOWER,
                Blocks.ENDER_CHEST, Blocks.END_ROD,
                Blocks.DRAGON_EGG
            ),
            List.of(EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.SHULKER),
            List.of(
                List.of(
                    new ItemStack(Items.SHULKER_SHELL, 4),
                    new ItemStack(Items.ELYTRA, 1),
                    new ItemStack(Items.END_CRYSTAL, 2),
                    new ItemStack(Items.ENDER_PEARL, 16),
                    new ItemStack(Items.NETHERITE_INGOT, 2),
                    new ItemStack(Items.DIAMOND, 8)
                )
            ),
            300
        ));
    }

    public static List<Phase> getPhases() {
        return Collections.unmodifiableList(PHASES);
    }

    public static Phase getPhaseForCount(long blocksBroken) {
        long cumulative = 0;
        for (Phase phase : PHASES) {
            cumulative += phase.blocksPerPhase();
            if (blocksBroken < cumulative) return phase;
        }
        // Loop back through End phase forever after completion
        return PHASES.get(PHASES.size() - 1);
    }

    public static int getPhaseIndex(long blocksBroken) {
        long cumulative = 0;
        int index = 0;
        for (Phase phase : PHASES) {
            cumulative += phase.blocksPerPhase();
            if (blocksBroken < cumulative) return index;
            index++;
        }
        return PHASES.size() - 1;
    }
}
