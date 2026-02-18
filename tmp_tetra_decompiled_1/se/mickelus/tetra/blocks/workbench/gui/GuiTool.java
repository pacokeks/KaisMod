/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraftforge.common.ToolAction
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiString
 *  se.mickelus.mutil.gui.GuiStringOutline
 *  se.mickelus.mutil.gui.GuiTexture
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.client.ToolActionIconStore;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.data.GlyphData;

@ParametersAreNonnullByDefault
public class GuiTool
extends GuiElement {
    public static final int width = 16;
    private final GuiString levelIndicator;
    protected GuiElement iconContainer;
    protected ToolAction toolAction;
    private GlyphData fallback = new GlyphData(GuiTextures.toolActions, 240, 0);

    public GuiTool(int x, int y, ToolAction toolAction) {
        super(x, y, 16, 16);
        this.toolAction = toolAction;
        this.iconContainer = new GuiElement(0, 0, 16, 16);
        this.addChild(this.iconContainer);
        this.updateIcon();
        this.levelIndicator = new GuiStringOutline(10, 8, "");
        this.addChild((GuiElement)this.levelIndicator);
    }

    public void update(int level, int color) {
        this.levelIndicator.setVisible(level >= 0);
        this.levelIndicator.setString("" + level);
        this.levelIndicator.setColor(color);
        this.updateIcon();
    }

    protected void updateIcon() {
        this.iconContainer.clearChildren();
        GlyphData glyph = Optional.ofNullable(ToolActionIconStore.instance.getIcon(this.toolAction)).orElse(this.fallback);
        this.iconContainer.addChild((GuiElement)new GuiTexture(0, 0, 16, 16, glyph.textureX, glyph.textureY, glyph.textureLocation));
    }

    public ToolAction getToolAction() {
        return this.toolAction;
    }
}
