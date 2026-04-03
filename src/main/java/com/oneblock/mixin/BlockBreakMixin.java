package com.oneblock.mixin;

import com.oneblock.util.OneBlockData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.block.Block;

/**
 * This mixin is optional — the main logic runs through the Fabric
 * PlayerBlockBreakEvents.AFTER event in OneBlockMod. This mixin is kept
 * minimal to prevent double-processing.
 */
@Mixin(Block.class)
public class BlockBreakMixin {
    // Reserved for future low-level interception if needed.
    // Currently all logic is handled via Fabric's event API.
}
