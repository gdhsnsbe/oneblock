package com.oneblock.util;

import com.oneblock.phase.PhaseRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.*;

public class OneBlockData extends PersistentState {

    private static final String DATA_ID = "oneblock_data";

    // Map from BlockPos string -> blocks broken count
    private final Map<String, Long> blockCounters = new HashMap<>();
    // Map from BlockPos string -> phase index at last announcement
    private final Map<String, Integer> lastAnnouncedPhase = new HashMap<>();

    public static OneBlockData get(MinecraftServer server) {
        PersistentStateManager manager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        return manager.getOrCreate(
            new PersistentState.Type<>(OneBlockData::new, OneBlockData::fromNbt, null),
            DATA_ID
        );
    }

    private static OneBlockData fromNbt(NbtCompound nbt) {
        OneBlockData data = new OneBlockData();
        NbtCompound counters = nbt.getCompound("counters");
        for (String key : counters.getKeys()) {
            data.blockCounters.put(key, counters.getLong(key));
        }
        NbtCompound phases = nbt.getCompound("lastPhase");
        for (String key : phases.getKeys()) {
            data.lastAnnouncedPhase.put(key, phases.getInt(key));
        }
        return data;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound counters = new NbtCompound();
        blockCounters.forEach(counters::putLong);
        nbt.put("counters", counters);

        NbtCompound phases = new NbtCompound();
        lastAnnouncedPhase.forEach(phases::putInt);
        nbt.put("lastPhase", phases);
        return nbt;
    }

    public static String posKey(BlockPos pos) {
        return pos.getX() + "," + pos.getY() + "," + pos.getZ();
    }

    public long getCount(BlockPos pos) {
        return blockCounters.getOrDefault(posKey(pos), 0L);
    }

    public long increment(BlockPos pos) {
        String key = posKey(pos);
        long newVal = blockCounters.getOrDefault(key, 0L) + 1;
        blockCounters.put(key, newVal);
        markDirty();
        return newVal;
    }

    public int getLastAnnouncedPhase(BlockPos pos) {
        return lastAnnouncedPhase.getOrDefault(posKey(pos), -1);
    }

    public void setLastAnnouncedPhase(BlockPos pos, int phase) {
        lastAnnouncedPhase.put(posKey(pos), phase);
        markDirty();
    }

    public boolean isOneBlock(BlockPos pos) {
        return blockCounters.containsKey(posKey(pos));
    }

    public void register(BlockPos pos) {
        blockCounters.putIfAbsent(posKey(pos), 0L);
        markDirty();
    }

    public Set<String> getAllKeys() {
        return blockCounters.keySet();
    }
}
