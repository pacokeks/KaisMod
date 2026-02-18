/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.ItemStack
 *  se.mickelus.mutil.gui.GuiAttachment
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiRect
 *  se.mickelus.mutil.gui.GuiString
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Collections;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.tetra.gui.GuiItemRolling;

public class RepairInfoGui
extends GuiElement {
    private final GuiString repairTitle = new GuiString(-8, 0, I18n.m_118938_((String)"item.tetra.modular.repair_material.label", (Object[])new Object[0]));
    private final GuiString noRepairLabel;
    private final GuiRect noRepairBackdrop;
    private final GuiItemRolling repairMaterial;
    private boolean canRepair;
    private final List<Component> tooltip;
    private final List<Component> noRepairtooltip;

    public RepairInfoGui(int x, int y) {
        super(x, y, 49, 16);
        this.repairTitle.setAttachment(GuiAttachment.middleCenter);
        this.addChild((GuiElement)this.repairTitle);
        this.noRepairBackdrop = new GuiRect(0, 0, 49, 16, 0);
        this.noRepairBackdrop.setVisible(false);
        this.addChild((GuiElement)this.noRepairBackdrop);
        this.noRepairLabel = new GuiString(1, 0, ChatFormatting.DARK_GRAY + I18n.m_118938_((String)"item.tetra.modular.repair_material.empty", (Object[])new Object[0]));
        this.noRepairLabel.setAttachment(GuiAttachment.middleCenter);
        this.noRepairLabel.setVisible(false);
        this.addChild((GuiElement)this.noRepairLabel);
        this.repairMaterial = new GuiItemRolling(0, 0);
        this.repairMaterial.setAttachment(GuiAttachment.topRight);
        this.addChild(this.repairMaterial);
        this.tooltip = Collections.singletonList(Component.m_237115_((String)"item.tetra.modular.repair_material.tooltip"));
        this.noRepairtooltip = Collections.singletonList(Component.m_237115_((String)"item.tetra.modular.repair_material.empty_tooltip"));
    }

    public void update(ItemStack[] repairItemStacks) {
        this.repairMaterial.setItems(repairItemStacks);
        this.canRepair = repairItemStacks.length > 0;
        this.repairTitle.setVisible(this.canRepair);
        this.repairMaterial.setVisible(this.canRepair);
        this.noRepairLabel.setVisible(!this.canRepair);
        this.noRepairBackdrop.setVisible(!this.canRepair);
    }

    public List<Component> getTooltipLines() {
        List childTooltip = super.getTooltipLines();
        if (this.hasFocus() && childTooltip == null) {
            return this.canRepair ? this.tooltip : this.noRepairtooltip;
        }
        return childTooltip;
    }
}
