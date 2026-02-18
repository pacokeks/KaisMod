/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.world.item.ItemStack
 *  se.mickelus.mutil.gui.GuiAttachment
 *  se.mickelus.mutil.gui.GuiButton
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiString
 *  se.mickelus.mutil.gui.GuiTexture
 *  se.mickelus.mutil.gui.animation.AnimationChain
 *  se.mickelus.mutil.gui.animation.Applier
 *  se.mickelus.mutil.gui.animation.Applier$Opacity
 *  se.mickelus.mutil.gui.animation.KeyframeAnimation
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiButton;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.blocks.workbench.gui.GuiTweakSlider;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.data.TweakData;

@ParametersAreNonnullByDefault
public class GuiTweakControls
extends GuiElement {
    private final GuiString untweakableLabel;
    private final GuiElement tweakControls;
    private final GuiButton applyButton;
    private final Consumer<Map<String, Integer>> previewTweak;
    private final AnimationChain flash;
    private Map<String, Integer> tweaks;

    public GuiTweakControls(int x, int y, Consumer<Map<String, Integer>> previewTweak, Consumer<Map<String, Integer>> applyTweak) {
        super(x, y, 224, 67);
        this.addChild((GuiElement)new GuiTexture(-4, -4, 239, 70, 0, 48, GuiTextures.workbench));
        this.untweakableLabel = new GuiString(0, -3, ChatFormatting.DARK_GRAY + I18n.m_118938_((String)"tetra.workbench.module_detail.not_tweakable", (Object[])new Object[0]));
        this.untweakableLabel.setAttachment(GuiAttachment.middleCenter);
        this.addChild((GuiElement)this.untweakableLabel);
        this.tweakControls = new GuiElement(0, -4, this.width, this.height - 20);
        this.tweakControls.setAttachment(GuiAttachment.middleLeft);
        this.addChild(this.tweakControls);
        this.applyButton = new GuiButton(0, -10, I18n.m_118938_((String)"tetra.workbench.slot_detail.tweak_apply", (Object[])new Object[0]), () -> applyTweak.accept(this.tweaks));
        this.applyButton.setAttachment(GuiAttachment.bottomCenter);
        this.addChild((GuiElement)this.applyButton);
        this.previewTweak = previewTweak;
        this.tweaks = new HashMap<String, Integer>();
        GuiTexture flashOverlay = new GuiTexture(-4, -4, 239, 70, 0, 48, GuiTextures.workbench);
        flashOverlay.setOpacity(0.0f);
        flashOverlay.setColor(0);
        this.addChild((GuiElement)flashOverlay);
        this.flash = new AnimationChain(new KeyframeAnimation[]{new KeyframeAnimation(40, (GuiElement)flashOverlay).applyTo(new Applier[]{new Applier.Opacity(0.3f)}), new KeyframeAnimation(80, (GuiElement)flashOverlay).applyTo(new Applier[]{new Applier.Opacity(0.0f)})});
    }

    public void update(ItemModule module, ItemStack itemStack) {
        this.tweakControls.clearChildren();
        if (module != null && module.isTweakable(itemStack)) {
            TweakData[] data = module.getTweaks(itemStack);
            this.tweakControls.setHeight(data.length * 22);
            for (int i = 0; i < data.length; ++i) {
                TweakData tweak = data[i];
                GuiTweakSlider slider = new GuiTweakSlider(0, i * 22, 200, tweak, step -> this.applyTweak(tweak.key, (int)step));
                slider.setAttachment(GuiAttachment.topCenter);
                slider.setValue(module.getTweakStep(itemStack, tweak));
                this.tweakControls.addChild((GuiElement)slider);
            }
            this.applyButton.setVisible(true);
            this.untweakableLabel.setVisible(false);
        } else {
            this.applyButton.setVisible(false);
            this.untweakableLabel.setVisible(true);
        }
    }

    private void applyTweak(String key, int step) {
        this.tweaks.put(key, step);
        this.previewTweak.accept(this.tweaks);
    }

    public void flash() {
        this.flash.stop();
        this.flash.start();
    }
}
