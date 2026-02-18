package se.mickelus.tetra.block;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import se.mickelus.tetra.registry.ModBlocks;
import se.mickelus.tetra.registry.ModItems;

public final class WorkbenchInteractions {
	private WorkbenchInteractions() {
	}

	public static void initialize() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> tryUpgradeCraftingTable(player, world, hand, hitResult));
	}

	private static ActionResult tryUpgradeCraftingTable(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
		if (hand != Hand.MAIN_HAND) {
			return ActionResult.PASS;
		}

		BlockPos blockPos = hitResult.getBlockPos();
		if (!world.getBlockState(blockPos).isOf(Blocks.CRAFTING_TABLE)) {
			return ActionResult.PASS;
		}

		ItemStack heldStack = player.getStackInHand(hand);
		if (!heldStack.isOf(ModItems.HAMMER)) {
			return ActionResult.PASS;
		}

		Direction side = hitResult.getSide();
		if (!player.canPlaceOn(blockPos.offset(side), side, heldStack)) {
			return ActionResult.FAIL;
		}

		if (!world.isClient()) {
			world.setBlockState(blockPos, ModBlocks.BASIC_WORKBENCH.getDefaultState());
			world.playSound(null, blockPos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 0.5F);
		}
		return ActionResult.SUCCESS;
	}
}
