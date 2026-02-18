/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraftforge.common.ToolAction
 *  se.mickelus.mutil.util.CastOptional
 *  se.mickelus.tetra.blocks.salvage.BlockInteraction
 *  se.mickelus.tetra.blocks.salvage.InteractionOutcome
 */
package se.mickelus.tetra.blocks.workbench;

import java.util.Arrays;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.InteractionOutcome;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.blocks.workbench.action.WorkbenchAction;

@ParametersAreNonnullByDefault
public class ActionInteraction
extends BlockInteraction {
    private final String actionKey;

    public ActionInteraction(ToolAction requiredType, int requiredLevel, String actionKey) {
        super(requiredType, requiredLevel, Direction.UP, 5.0f, 11.0f, 5.0f, 11.0f, InteractionOutcome.EMPTY);
        this.actionKey = actionKey;
        this.applyUsageEffects = false;
    }

    public static ActionInteraction create(WorkbenchTile tile) {
        ItemStack targetStack = tile.getTargetItemStack();
        return Arrays.stream(tile.getAvailableActions(null)).filter(WorkbenchAction::allowInWorldInteraction).filter(action -> action.getRequiredTools(targetStack).entrySet().size() == 1).findFirst().map(action -> {
            Map.Entry requirementPair = (Map.Entry)action.getRequiredTools(targetStack).entrySet().stream().findFirst().get();
            return new ActionInteraction((ToolAction)requirementPair.getKey(), (Integer)requirementPair.getValue(), action.getKey());
        }).orElse(null);
    }

    public boolean applicableForBlock(Level world, BlockPos pos, BlockState blockState) {
        return this.actionKey != null;
    }

    public void applyOutcome(Level world, BlockPos pos, BlockState blockState, @Nullable Player player, @Nullable InteractionHand hand, Direction hitFace) {
        if (!world.f_46443_) {
            CastOptional.cast((Object)world.m_7702_(pos), WorkbenchTile.class).ifPresent(tile -> {
                if (player != null) {
                    tile.performAction(player, this.actionKey);
                } else {
                    tile.performAction(this.actionKey);
                }
            });
        }
    }
}
