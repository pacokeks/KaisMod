/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraftforge.common.ToolAction
 */
package se.mickelus.tetra.blocks.workbench.gui;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.blocks.workbench.gui.GuiTool;

@ParametersAreNonnullByDefault
public class ToolRequirementGui
extends GuiTool {
    String requirementTooltip;
    private int requiredLevel;
    private int availableLevel;
    private boolean showTooltip = true;
    private boolean showTooltipRequirement = true;

    public ToolRequirementGui(int x, int y, ToolAction toolAction) {
        this(x, y, toolAction, "tetra.tool." + toolAction.name() + ".requirement");
    }

    public ToolRequirementGui(int x, int y, ToolAction toolAction, String requirementTooltip) {
        super(x, y, toolAction);
        this.requirementTooltip = requirementTooltip;
    }

    public ToolRequirementGui setTooltipVisibility(boolean shouldShow) {
        this.showTooltip = shouldShow;
        return this;
    }

    public ToolRequirementGui setTooltipRequirementVisibility(boolean shouldShow) {
        this.showTooltipRequirement = shouldShow;
        return this;
    }

    public ToolRequirementGui updateRequirement(int requiredLevel, int availableLevel) {
        this.setVisible(requiredLevel != 0);
        this.requiredLevel = requiredLevel;
        this.availableLevel = availableLevel;
        if (this.isVisible()) {
            if (requiredLevel > availableLevel) {
                this.update(requiredLevel, 0xFFAAAA);
            } else {
                this.update(requiredLevel, 0xAAFFAA);
            }
        }
        return this;
    }

    public List<Component> getTooltipLines() {
        if (this.hasFocus() && this.showTooltip) {
            if (this.showTooltipRequirement) {
                return ImmutableList.of((Object)Component.m_237110_((String)this.requirementTooltip, (Object[])new Object[]{this.requiredLevel}), (Object)Component.m_237113_((String)""), (Object)Component.m_237110_((String)"tetra.tool.available", (Object[])new Object[]{this.availableLevel}).m_130940_(this.requiredLevel > this.availableLevel ? ChatFormatting.RED : ChatFormatting.GREEN));
            }
            return Collections.singletonList(Component.m_237110_((String)this.requirementTooltip, (Object[])new Object[]{this.requiredLevel}));
        }
        return super.getTooltipLines();
    }
}
