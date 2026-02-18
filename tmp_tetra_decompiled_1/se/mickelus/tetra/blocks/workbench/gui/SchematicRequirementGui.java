/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiTexture
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.List;
import net.minecraft.network.chat.Component;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

public class SchematicRequirementGui
extends GuiElement {
    private List<Component> tooltip;

    public SchematicRequirementGui(int x, int y) {
        super(x, y, 9, 9);
        this.addChild((GuiElement)new GuiTexture(0, 0, 9, 9, 224, 32, GuiTextures.workbench));
    }

    public List<Component> getTooltipLines() {
        if (this.hasFocus()) {
            return this.tooltip;
        }
        return null;
    }

    public SchematicRequirementGui update(UpgradeSchematic schematic) {
        this.tooltip = schematic.getRequirementDescription();
        this.setVisible(this.tooltip != null);
        return this;
    }

    public List<Component> getTooltip() {
        if (this.hasFocus()) {
            return this.tooltip;
        }
        return null;
    }
}
