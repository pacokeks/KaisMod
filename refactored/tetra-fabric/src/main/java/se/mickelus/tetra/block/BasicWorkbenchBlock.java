package se.mickelus.tetra.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import se.mickelus.tetra.screen.UpgradeWorkbenchScreenHandler;

public final class BasicWorkbenchBlock extends Block {
	private static final Text TITLE = Text.translatable("block.tetra.basic_workbench");

	public BasicWorkbenchBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (world.isClient()) {
			return ActionResult.SUCCESS;
		}

		player.openHandledScreen(
			new SimpleNamedScreenHandlerFactory(
				(syncId, playerInventory, ignoredPlayer) -> new UpgradeWorkbenchScreenHandler(syncId, playerInventory),
				TITLE
			)
		);
		return ActionResult.CONSUME;
	}
}
