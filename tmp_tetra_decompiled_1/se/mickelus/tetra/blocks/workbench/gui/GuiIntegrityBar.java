/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.ItemStack
 *  se.mickelus.mutil.gui.GuiAttachment
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiString
 *  se.mickelus.mutil.gui.GuiStringSmall
 *  se.mickelus.mutil.gui.animation.Applier
 *  se.mickelus.mutil.gui.animation.Applier$Opacity
 *  se.mickelus.mutil.gui.animation.Applier$TranslateY
 *  se.mickelus.mutil.gui.animation.KeyframeAnimation
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Collections;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
public class GuiIntegrityBar
extends GuiElement {
    private static final int segmentHeight = 2;
    private static final int segmentOffset = 6;
    private static final int gainColor = 0x22FFFFFF;
    private static final float gainOpacity = 0.15f;
    private static final int costColor = -1;
    private static final int overuseColor = -1996532395;
    private static final float overuseOpacity = 0.55f;
    private final GuiString label = new GuiStringSmall(0, 0, "");
    private final List<Component> tooltip;
    private int segmentWidth = 8;
    private int integrityGain;
    private int integrityCost;

    public GuiIntegrityBar(int x, int y) {
        super(x, y, 0, 8);
        this.label.setAttachment(GuiAttachment.topCenter);
        this.addChild((GuiElement)this.label);
        this.setAttachmentPoint(GuiAttachment.topCenter);
        this.tooltip = Collections.singletonList(Component.m_237115_((String)"tetra.stats.integrity_usage.tooltip"));
    }

    public void setItemStack(ItemStack itemStack, ItemStack previewStack) {
        boolean shouldShow = !itemStack.m_41619_() && itemStack.m_41720_() instanceof IModularItem;
        this.setVisible(shouldShow);
        if (shouldShow) {
            if (!previewStack.m_41619_()) {
                this.integrityGain = IModularItem.getIntegrityGain(previewStack);
                this.integrityCost = IModularItem.getIntegrityCost(previewStack);
            } else {
                this.integrityGain = IModularItem.getIntegrityGain(itemStack);
                this.integrityCost = IModularItem.getIntegrityCost(itemStack);
            }
            if (this.integrityGain - this.integrityCost < 0) {
                this.label.setString(ChatFormatting.RED + I18n.m_118938_((String)"tetra.stats.integrity_usage", (Object[])new Object[]{this.integrityCost, this.integrityGain}));
            } else {
                this.label.setString(I18n.m_118938_((String)"tetra.stats.integrity_usage", (Object[])new Object[]{this.integrityCost, this.integrityGain}));
            }
            this.segmentWidth = this.integrityGain > 7 ? Math.max(64 / this.integrityGain - 1, 1) : 8;
            this.width = this.integrityGain * (this.segmentWidth + 1);
        }
    }

    public void showAnimation() {
        if (this.isVisible()) {
            new KeyframeAnimation(100, (GuiElement)this).withDelay(200).applyTo(new Applier[]{new Applier.Opacity(0.0f, 1.0f), new Applier.TranslateY(-3.0f, 0.0f, true)}).start();
        }
    }

    public List<Component> getTooltipLines() {
        if (this.hasFocus()) {
            return this.tooltip;
        }
        return super.getTooltipLines();
    }

    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        int i;
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        for (i = 0; i < this.integrityCost; ++i) {
            if (i < this.integrityGain) {
                this.drawSegment(graphics, refX + this.x + i * (this.segmentWidth + 1), refY + this.y + 6, -1, opacity * this.getOpacity());
                continue;
            }
            this.drawSegment(graphics, refX + this.x + i * (this.segmentWidth + 1), refY + this.y + 6, -1996532395, opacity * this.getOpacity());
        }
        for (i = this.integrityCost; i < this.integrityGain; ++i) {
            this.drawSegment(graphics, refX + this.x + i * (this.segmentWidth + 1), refY + this.y + 6, 0x22FFFFFF, opacity * this.getOpacity());
        }
    }

    private void drawSegment(GuiGraphics graphics, int x, int y, int color, float opacity) {
        GuiIntegrityBar.drawRect((GuiGraphics)graphics, (int)x, (int)y, (int)(x + this.segmentWidth), (int)(y + 2), (int)color, (float)opacity);
    }
}
