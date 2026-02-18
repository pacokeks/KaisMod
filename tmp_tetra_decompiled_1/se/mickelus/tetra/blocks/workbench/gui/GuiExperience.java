/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.network.chat.Component
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiString
 *  se.mickelus.mutil.gui.GuiStringOutline
 *  se.mickelus.mutil.gui.GuiTexture
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Collections;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.chat.Component;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class GuiExperience
extends GuiElement {
    private static final int positiveColor = 0xC8FF8F;
    private static final int negativeColor = 9199709;
    private final GuiTexture indicator = new GuiTexture(0, 0, 16, 16, 0, 0, GuiTextures.workbench);
    private final GuiString levelString;
    private final String unlocalizedTooltip;
    private List<Component> formattedTooltip;

    public GuiExperience(int x, int y) {
        this(x, y, null);
    }

    public GuiExperience(int x, int y, String unlocalizedTooltip) {
        super(x, y, 16, 16);
        this.addChild((GuiElement)this.indicator);
        this.levelString = new GuiStringOutline(10, 4, "");
        this.addChild((GuiElement)this.levelString);
        this.unlocalizedTooltip = unlocalizedTooltip;
    }

    public void update(int level, boolean positive) {
        this.indicator.setTextureCoordinates(Math.min(level, 3) * 16 + 112, positive ? 0 : 16);
        this.levelString.setString("" + level);
        this.levelString.setColor(positive ? 0xC8FF8F : 9199709);
        if (this.unlocalizedTooltip != null) {
            this.formattedTooltip = Collections.singletonList(Component.m_237110_((String)this.unlocalizedTooltip, (Object[])new Object[]{level}));
        }
    }

    public List<Component> getTooltipLines() {
        if (this.formattedTooltip != null && this.hasFocus()) {
            return this.formattedTooltip;
        }
        return super.getTooltipLines();
    }
}
