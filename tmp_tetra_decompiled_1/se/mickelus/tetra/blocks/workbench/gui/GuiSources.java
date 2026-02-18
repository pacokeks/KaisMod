/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiStringSmall
 *  se.mickelus.mutil.gui.GuiTexture
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

public class GuiSources
extends GuiElement {
    private List<Component> tooltip = Collections.singletonList(Component.m_237115_((String)"tetra.sources.unknown_tooltip"));
    private GuiTexture icon = new GuiTexture(0, 0, 4, 4, 214, 33, GuiTextures.workbench);
    private GuiStringSmall label = new GuiStringSmall(0, 0, "");

    public GuiSources(int x, int y, int width) {
        super(x, y, width, 5);
        this.label.setColor(0x5555FF);
        this.addChild((GuiElement)this.label);
    }

    public void update(UpgradeSchematic schematic) {
        String[] sources = schematic.getSources();
        this.label.setColor(0x5555FF);
        if (sources.length > 0) {
            if (sources.length > 1) {
                String modifiers = String.join((CharSequence)", ", Arrays.copyOfRange(sources, 0, sources.length - 1));
                this.tooltip = Collections.singletonList(Component.m_237113_((String)I18n.m_118938_((String)"tetra.sources.multi_tooltip", (Object[])new Object[]{sources[sources.length - 1], modifiers})));
            } else {
                this.tooltip = Collections.singletonList(Component.m_237113_((String)I18n.m_118938_((String)"tetra.sources.single_tooltip", (Object[])new Object[]{sources[0]})));
            }
            Font font = Minecraft.m_91087_().f_91062_;
            for (int i = sources.length; i > 0; --i) {
                String labelString = String.join((CharSequence)", ", Arrays.copyOfRange(sources, 0, i));
                String overflowSuffix = I18n.m_118938_((String)"tetra.sources.overflow_suffix_label", (Object[])new Object[]{sources.length - i});
                int width = font.m_92895_(labelString);
                if (i == sources.length && width < this.width - 7) {
                    this.label.setString(labelString);
                    return;
                }
                if (width >= this.width - 7 - font.m_92895_(overflowSuffix)) continue;
                this.label.setString(labelString + overflowSuffix);
                return;
            }
            this.label.setString(I18n.m_118938_((String)"tetra.sources.overflow_label", (Object[])new Object[]{sources.length}));
        } else {
            this.label.setString(I18n.m_118938_((String)"tetra.sources.unknown_label", (Object[])new Object[0]));
            this.tooltip = Collections.singletonList(Component.m_237115_((String)"tetra.sources.unknown_tooltip"));
        }
    }

    public List<Component> getTooltipLines() {
        if (this.hasFocus()) {
            return this.tooltip;
        }
        return null;
    }
}
