/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraftforge.registries.ObjectHolder
 *  se.mickelus.tetra.advancements.BlockUseCriterion
 */
package se.mickelus.tetra.blocks.workbench;

import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.tetra.advancements.BlockUseCriterion;
import se.mickelus.tetra.blocks.workbench.AbstractWorkbenchBlock;

@ParametersAreNonnullByDefault
public class BasicWorkbenchBlock
extends AbstractWorkbenchBlock {
    public static final String identifier = "basic_workbench";
    @ObjectHolder(registryName="block", value="tetra:basic_workbench")
    public static AbstractWorkbenchBlock instance;

    public BasicWorkbenchBlock() {
        super(BlockBehaviour.Properties.m_284310_().m_60978_(2.5f).m_60918_(SoundType.f_56736_));
    }

    public static InteractionResult upgradeWorkbench(Player player, Level world, BlockPos pos, InteractionHand hand, Direction facing) {
        ItemStack itemStack = player.m_21120_(hand);
        if (!player.m_36204_(pos.m_121945_(facing), facing, itemStack)) {
            return InteractionResult.FAIL;
        }
        if (world.m_8055_(pos).m_60734_().equals(Blocks.f_50091_)) {
            world.m_5594_(player, pos, SoundEvents.f_12635_, SoundSource.BLOCKS, 1.0f, 0.5f);
            if (!world.f_46443_) {
                world.m_46597_(pos, instance.m_49966_());
                BlockUseCriterion.trigger((ServerPlayer)((ServerPlayer)player), (BlockState)instance.m_49966_(), (ItemStack)ItemStack.f_41583_);
            }
            return InteractionResult.m_19078_((boolean)world.f_46443_);
        }
        return InteractionResult.PASS;
    }

    public void m_5871_(ItemStack itemStack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add((Component)Component.m_237115_((String)"block.tetra.basic_workbench.description").m_130940_(ChatFormatting.GRAY));
    }
}
