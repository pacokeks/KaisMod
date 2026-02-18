/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.common.ToolAction
 *  se.mickelus.mutil.gui.GuiAttachment
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.tetra.blocks.workbench.gui.ToolRequirementGui;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class ToolRequirementListGui
extends GuiElement {
    private Map<ToolAction, Integer> requiredTools = Collections.emptyMap();

    public ToolRequirementListGui(int x, int y) {
        super(x, y, 0, 0);
        this.setAttachmentPoint(GuiAttachment.topCenter);
    }

    public void update(UpgradeSchematic schematic, ItemStack targetStack, String slot, ItemStack[] materials, Map<ToolAction, Integer> availableTools) {
        boolean hasValidMaterials = schematic.isMaterialsValid(targetStack, slot, materials);
        this.setVisible(hasValidMaterials);
        if (hasValidMaterials) {
            this.clearChildren();
            this.requiredTools = schematic.getRequiredToolLevels(targetStack, materials);
            GuiHorizontalLayoutGroup layout = new GuiHorizontalLayoutGroup(0, 0, 16, -2 - this.requiredTools.size() * 2);
            layout.setAttachmentPoint(GuiAttachment.topCenter);
            this.addChild((GuiElement)layout);
            int spacing = -3 - this.requiredTools.size() * 2;
            AtomicInteger i = new AtomicInteger(0);
            this.requiredTools.entrySet().stream().map(entry -> new ToolRequirementGui(-1 * i.getAndIncrement() * (spacing + 16), 0, (ToolAction)entry.getKey()).updateRequirement((Integer)entry.getValue(), availableTools.getOrDefault(entry.getKey(), 0)).setAttachment(GuiAttachment.topRight)).forEach(arg_0 -> ((ToolRequirementListGui)this).addChild(arg_0));
            this.setWidth(this.requiredTools.size() * 16 + (this.requiredTools.size() - 1) * spacing);
        }
    }

    public void updateAvailableTools(Map<ToolAction, Integer> availableTools) {
        this.getChildren(ToolRequirementGui.class).forEach(indicator -> indicator.updateRequirement(this.requiredTools.getOrDefault(indicator.getToolAction(), 0), availableTools.getOrDefault(indicator.getToolAction(), 0)));
    }
}
