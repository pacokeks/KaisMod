/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 */
package se.mickelus.tetra.blocks.forged;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

@ParametersAreNonnullByDefault
public class ForgedBlockCommon {
    public static final BlockBehaviour.Properties propertiesSolid = BlockBehaviour.Properties.m_284310_().m_60999_().m_60918_(SoundType.f_56725_).m_60913_(12.0f, 2400.0f);
    public static final BlockBehaviour.Properties propertiesNotSolid = BlockBehaviour.Properties.m_284310_().m_60999_().m_60955_().m_60918_(SoundType.f_56725_).m_60924_(ForgedBlockCommon::notSolid).m_60960_(ForgedBlockCommon::notSolid).m_60971_(ForgedBlockCommon::notSolid).m_60913_(12.0f, 600.0f);
    public static final Component locationTooltip = Component.m_237115_((String)"item.tetra.forged_description").m_130940_(ChatFormatting.GRAY);
    public static final Component unsettlingTooltip = Component.m_237115_((String)"item.tetra.forged_unsettling").m_130944_(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC});

    private static boolean notSolid(BlockState state, BlockGetter reader, BlockPos pos) {
        return false;
    }
}
