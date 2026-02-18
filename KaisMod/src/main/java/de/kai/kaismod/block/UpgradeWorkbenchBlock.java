package de.kai.kaismod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import de.kai.kaismod.screen.UpgradeWorkbenchScreenHandler;

public class UpgradeWorkbenchBlock extends Block {
	private static final Text TITLE = Text.translatable("screen.kaismod.upgrade_workbench");
	public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;
	private static final VoxelShape Z_SHAPE = VoxelShapes.union(
		Block.createCuboidShape(1, 0, 3, 15, 2, 13),
		Block.createCuboidShape(2, 2, 4, 14, 9, 12),
		Block.createCuboidShape(0, 9, 2, 16, 16, 14)
	);
	private static final VoxelShape X_SHAPE = VoxelShapes.union(
		Block.createCuboidShape(3, 0, 1, 13, 2, 15),
		Block.createCuboidShape(4, 2, 2, 12, 9, 14),
		Block.createCuboidShape(2, 9, 0, 14, 16, 16)
	);

	public UpgradeWorkbenchBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(AXIS, Direction.Axis.X));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AXIS);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(AXIS, ctx.getHorizontalPlayerFacing().getAxis());
	}

	@Override
	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		if (rotation == BlockRotation.CLOCKWISE_90 || rotation == BlockRotation.COUNTERCLOCKWISE_90) {
			Direction.Axis axis = state.get(AXIS);
			return state.with(AXIS, axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
		}
		return state;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(AXIS) == Direction.Axis.Z ? Z_SHAPE : X_SHAPE;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
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
