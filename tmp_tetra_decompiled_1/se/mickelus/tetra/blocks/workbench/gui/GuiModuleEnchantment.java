/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.enchantment.Enchantment
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiTexture
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.aspect.TetraEnchantmentHelper;
import se.mickelus.tetra.gui.GuiTextures;

public class GuiModuleEnchantment
extends GuiElement {
    private final List<Component> tooltipLines;
    private Runnable hoverHandler;
    private Runnable blurHandler;
    private int color;
    private GuiTexture texture;

    public GuiModuleEnchantment(int x, int y, Enchantment enchantment, int level, int color, Runnable hoverHandler, Runnable blurHandler) {
        super(x, y, 5, 4);
        this.color = color;
        this.texture = new GuiTexture(0, 0, 5, 4, 68, 27, GuiTextures.workbench).setColor(color);
        this.addChild((GuiElement)this.texture);
        this.tooltipLines = new ArrayList<Component>();
        if (level < 0) {
            this.tooltipLines.add((Component)Component.m_237113_((String)"-").m_130946_(TetraEnchantmentHelper.getEnchantmentName(enchantment, 0)).m_130940_(ChatFormatting.DARK_RED));
        } else {
            this.tooltipLines.add((Component)Component.m_237113_((String)TetraEnchantmentHelper.getEnchantmentName(enchantment, level)));
        }
        Optional.ofNullable(TetraEnchantmentHelper.getEnchantmentDescription(enchantment)).map(description -> Component.m_237113_((String)description).m_130940_(ChatFormatting.DARK_GRAY)).ifPresent(this.tooltipLines::add);
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
