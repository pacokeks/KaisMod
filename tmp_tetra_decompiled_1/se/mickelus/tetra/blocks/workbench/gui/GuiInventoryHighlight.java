/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  se.mickelus.mutil.gui.GuiAttachment
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiRect
 *  se.mickelus.mutil.gui.GuiTexture
 *  se.mickelus.mutil.gui.animation.AnimationChain
 *  se.mickelus.mutil.gui.animation.Applier
 *  se.mickelus.mutil.gui.animation.Applier$Opacity
 *  se.mickelus.mutil.gui.animation.KeyframeAnimation
 */
package se.mickelus.tetra.blocks.workbench.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class GuiInventoryHighlight
extends GuiElement {
    private final AnimationChain animation;
    GuiElement dots;

    public GuiInventoryHighlight(int x, int y, int offset) {
        super(x, y, 16, 16);
        GuiTexture texture = new GuiTexture(0, 0, 16, 16, 80, 16, GuiTextures.workbench);
        this.addChild((GuiElement)texture);
        this.dots = new GuiElement(2, 2, 12, 12);
        this.dots.addChild((GuiElement)new GuiRect(0, 0, 1, 1, 0xFFFFFF));
        this.dots.addChild(new GuiRect(0, 0, 1, 1, 0xFFFFFF).setAttachment(GuiAttachment.topRight));
        this.dots.addChild(new GuiRect(0, 0, 1, 1, 0xFFFFFF).setAttachment(GuiAttachment.bottomLeft));
        this.dots.addChild(new GuiRect(0, 0, 1, 1, 0xFFFFFF).setAttachment(GuiAttachment.bottomRight));
        this.addChild(this.dots);
        this.animation = new AnimationChain(new KeyframeAnimation[]{new KeyframeAnimation(200, (GuiElement)texture).withDelay(offset * 50).applyTo(new Applier[]{new Applier.Opacity(0.0f, 1.0f)}), new KeyframeAnimation(300, (GuiElement)texture).applyTo(new Applier[]{new Applier.Opacity(0.4f)}), new KeyframeAnimation(400, this.dots).applyTo(new Applier[]{new Applier.Opacity(1.0f)})});
    }

    protected void onShow() {
        this.dots.setOpacity(0.0f);
        this.animation.start();
    }

    protected boolean onHide() {
        this.animation.stop();
        return super.onHide();
    }
}
