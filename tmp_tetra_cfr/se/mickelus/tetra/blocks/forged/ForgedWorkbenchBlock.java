/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.SimpleWaterloggedBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.FluidState
 *  net.minecraft.world.level.material.Fluids
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package se.mickelus.tetra.blocks.forged;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;
import se.mickelus.tetra.blocks.workbench.AbstractWorkbenchBlock;

@ParametersAreNonnullByDefault
public class ForgedWorkbenchBlock
extends AbstractWorkbenchBlock
implements SimpleWaterloggedBlock {
    public static final String identifier = "forged_workbench";
    public static final ResourceLocation unlockId = new ResourceLocation("tetra", "forged_workbench");
    public static final EnumProperty<Direction.Axis> axis = BlockStateProperties.f_61364_;
    private static final VoxelShape zShape = Shapes.m_83124_((VoxelShape)ForgedWorkbenchBlock.m_49796_((double)1.0, (double)0.0, (double)3.0, (double)15.0, (double)2.0, (double)13.0), (VoxelShape[])new VoxelShape[]{ForgedWorkbenchBlock.m_49796_((double)2.0, (double)2.0, (double)4.0, (double)14.0, (double)9.0, (double)12.0), ForgedWorkbenchBlock.m_49796_((double)0.0, (double)9.0, (double)2.0, (double)16.0, (double)16.0, (double)14.0)});
    private static final VoxelShape xShape = Shapes.m_83124_((VoxelShape)ForgedWorkbenchBlock.m_49796_((double)3.0, (double)0.0, (double)1.0, (double)13.0, (double)2.0, (double)15.0), (VoxelShape[])new VoxelShape[]{ForgedWorkbenchBlock.m_49796_((double)4.0, (double)2.0, (double)2.0, (double)12.0, (double)9.0, (double)14.0), ForgedWorkbenchBlock.m_49796_((double)2.0, (double)9.0, (double)0.0, (double)14.0, (double)16.0, (double)16.0)});

    public ForgedWorkbenchBlock() {
        super(ForgedBlockCommon.propertiesSolid);
        this.m_49959_((BlockState)((BlockState)this.m_49966_().m_61124_((Property)BlockStateProperties.f_61362_, (Comparable)Boolean.valueOf(false))).m_61124_(axis, (Comparable)Direction.Axis.X));
    }

    public void m_5871_(ItemStack itemStack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }

    public VoxelShape m_5940_(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction.Axis axis = (Direction.Axis)state.m_61143_(ForgedWorkbenchBlock.axis);
        if (axis == Direction.Axis.Z) {
            return zShape;
        }
        return xShape;
    }

    protected void m_7926_(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.m_61104_(new Property[]{BlockStateProperties.f_61362_, axis});
    }

    public FluidState m_5888_(BlockState state) {
        return (Boolean)state.m_61143_((Property)BlockStateProperties.f_61362_) != false ? Fluids.f_76193_.m_76068_(false) : super.m_5888_(state);
    }

    @Nullable
    public BlockState m_5573_(BlockPlaceContext context) {
        return (BlockState)((BlockState)this.m_49966_().m_61124_((Property)BlockStateProperties.f_61362_, (Comparable)Boolean.valueOf(context.m_43725_().m_6425_(context.m_8083_()).m_76152_() == Fluids.f_76193_))).m_61124_(axis, (Comparable)context.m_8125_().m_122434_());
    }

    public BlockState m_7417_(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (((Boolean)stateIn.m_61143_((Property)BlockStateProperties.f_61362_)).booleanValue()) {
            worldIn.m_186469_(currentPos, (Fluid)Fluids.f_76193_, Fluids.f_76193_.m_6718_((LevelReader)worldIn));
        }
        return super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public BlockState m_6843_(BlockState blockState, Rotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90: 
            case CLOCKWISE_90: {
                switch ((Direction.Axis)blockState.m_61143_(axis)) {
                    case Z: {
                        return (BlockState)blockState.m_61124_(axis, (Comparable)Direction.Axis.X);
                    }
                    case X: {
                        return (BlockState)blockState.m_61124_(axis, (Comparable)Direction.Axis.Z);
                    }
                }
                return blockState;
            }
        }
        return blockState;
    }

    @Override
    public ResourceLocation[] getSchematics(Level world, BlockPos pos, BlockState blockState) {
        return (ResourceLocation[])Stream.concat(Arrays.stream(super.getSchematics(world, pos, blockState)), Stream.of(unlockId)).toArray(ResourceLocation[]::new);
    }

    @Override
    public ResourceLocation[] getCraftingEffects(Level world, BlockPos pos, BlockState blockState) {
        return (ResourceLocation[])Stream.concat(Arrays.stream(super.getCraftingEffects(world, pos, blockState)), Stream.of(unlockId)).toArray(ResourceLocation[]::new);
    }
}
