/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiTexture
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.IModularItem;

public class GuiModuleImprovement
extends GuiElement {
    private final List<Component> tooltipLines;
    private final Runnable hoverHandler;
    private final Runnable blurHandler;
    private int color;
    private GuiTexture texture;

    public GuiModuleImprovement(int x, int y, String improvement, int level, int color, Runnable hoverHandler, Runnable blurHandler) {
        super(x, y, 5, 4);
        this.color = color;
        this.texture = new GuiTexture(0, 0, 5, 4, 68, 23, GuiTextures.workbench).setColor(color);
        this.addChild((GuiElement)this.texture);
        this.tooltipLines = new ArrayList<Component>();
        if (level < 0) {
            this.tooltipLines.add((Component)Component.m_237113_((String)("-" + IModularItem.getImprovementName(improvement, 0))).m_130940_(ChatFormatting.DARK_RED));
        } else {
            this.tooltipLines.add((Component)Component.m_237113_((String)IModularItem.getImprovementName(improvement, level)));
        }
        Arrays.stream(IModularItem.getImprovementDescription(improvement).split("\\\\n")).map(line -> Component.m_237113_((String)line).m_130940_(ChatFormatting.DARK_GRAY)).forEachOrdered(this.tooltipLines::add);
        this.hoverHandler = hoverHandler;
        this.blurHandler = blurHandler;
    }

    public List<Component> getTooltipLines() {
        if (this.hasFocus()) {
            return this.tooltipLines;
        }
        return null;
    }

    protected void onFocus() {
        super.onFocus();
        this.hoverHandler.run();
        this.texture.setColor(0xFFFFCC);
    }

    protected void onBlur() {
        super.onBlur();
        this.blurHandler.run();
        this.texture.setColor(this.color);
    }
}
