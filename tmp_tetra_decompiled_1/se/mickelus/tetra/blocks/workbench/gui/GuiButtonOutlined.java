/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.client.Minecraft
 *  se.mickelus.mutil.gui.GuiAlignment
 *  se.mickelus.mutil.gui.GuiClickable
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiRect
 *  se.mickelus.mutil.gui.GuiStringOutline
 *  se.mickelus.mutil.gui.GuiTexture
 */
package se.mickelus.tetra.blocks.workbench.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import se.mickelus.mutil.gui.GuiAlignment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class GuiButtonOutlined
extends GuiClickable {
    private final GuiTexture borderLeft;
    private final GuiTexture borderRight;
    private final GuiRect borderTop;
    private final GuiRect borderBottom;

    public GuiButtonOutlined(int x, int y, String label, Runnable onClickHandler) {
        this(x, y, label, GuiAlignment.left, onClickHandler);
    }

    public GuiButtonOutlined(int x, int y, String label, GuiAlignment alignment, Runnable onClickHandler) {
        super(x, y, 0, 11, onClickHandler);
        this.width = Minecraft.m_91087_().f_91062_.m_92895_(label) + 18;
        this.addChild((GuiElement)new GuiRect(9, 0, this.width - 18, 11, 0));
        this.borderLeft = new GuiTexture(0, 0, 9, 11, 79, 0, GuiTextures.workbench).setColor(0x7F7F7F);
        this.addChild((GuiElement)this.borderLeft);
        this.borderRight = new GuiTexture(this.width - 9, 0, 9, 11, 88, 0, GuiTextures.workbench).setColor(0x7F7F7F);
        this.addChild((GuiElement)this.borderRight);
        this.borderTop = new GuiRect(9, 1, this.width - 18, 1, 0x7F7F7F);
        this.addChild((GuiElement)this.borderTop);
        this.borderBottom = new GuiRect(9, 9, this.width - 18, 1, 0x7F7F7F);
        this.addChild((GuiElement)this.borderBottom);
        this.addChild((GuiElement)new GuiStringOutline(9, 1, label));
    }

    private void setBorderColors(int color) {
        this.borderLeft.setColor(color);
        this.borderRight.setColor(color);
        this.borderTop.setColor(color);
        this.borderBottom.setColor(color);
    }

    protected void onFocus() {
        this.setBorderColors(0x8F8F6F);
    }

    protected void onBlur() {
        this.setBorderColors(0x7F7F7F);
    }
}
