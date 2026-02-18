/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.world.item.ItemStack
 *  se.mickelus.mutil.gui.GuiAttachment
 *  se.mickelus.mutil.gui.GuiClickable
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiString
 *  se.mickelus.mutil.gui.GuiTextureOffset
 *  se.mickelus.mutil.gui.animation.Applier
 *  se.mickelus.mutil.gui.animation.Applier$Opacity
 *  se.mickelus.mutil.gui.animation.Applier$TranslateX
 *  se.mickelus.mutil.gui.animation.KeyframeAnimation
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTextureOffset;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleBackdrop;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleGlyph;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleMinorBackdrop;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.data.VariantData;

@ParametersAreNonnullByDefault
public class GuiModule
extends GuiClickable {
    protected String slotKey = null;
    protected GuiModuleBackdrop backdrop;
    protected GuiString moduleString;
    protected GuiModuleGlyph glyph;
    protected GuiTextureOffset tweakingIndicator;
    protected boolean isEmpty;
    protected boolean isHovered;
    protected boolean isUnselected;
    protected boolean isSelected;
    protected boolean isPreview;
    protected boolean isRemoving;
    protected boolean isAdding;
    protected BiConsumer<String, String> hoverHandler;

    public GuiModule(int x, int y, GuiAttachment attachmentPoint, ItemStack itemStack, ItemStack previewStack, String slotKey, String slotName, ItemModule module, ItemModule previewModule, Consumer<String> slotClickHandler, BiConsumer<String, String> hoverHandler) {
        super(x, y, 0, 11, () -> slotClickHandler.accept(slotKey));
        this.slotKey = slotKey;
        this.setAttachmentPoint(attachmentPoint);
        if (module == null && previewModule == null) {
            this.isEmpty = true;
            this.setupChildren(null, null, slotName, false);
        } else if (previewModule == null) {
            this.isRemoving = true;
            VariantData data = module.getVariantData(itemStack);
            this.setupChildren(null, data.glyph, slotName, module.isTweakable(itemStack));
        } else if (module == null) {
            this.isAdding = true;
            VariantData previewData = previewModule.getVariantData(previewStack);
            this.setupChildren(previewModule.getName(previewStack), previewData.glyph, slotName, previewModule.isTweakable(itemStack));
        } else {
            VariantData previewData;
            VariantData data = module.getVariantData(itemStack);
            if (data.equals(previewData = previewModule.getVariantData(previewStack))) {
                this.setupChildren(module.getName(itemStack), data.glyph, slotName, module.isTweakable(itemStack));
            } else {
                this.isPreview = true;
                this.setupChildren(previewModule.getName(previewStack), previewData.glyph, slotName, previewModule.isTweakable(itemStack));
            }
        }
        this.hoverHandler = hoverHandler;
    }

    public void showAnimation(int offset) {
        if (this.isVisible()) {
            int direction = this.attachmentPoint == GuiAttachment.topLeft ? -2 : 2;
            new KeyframeAnimation(100, (GuiElement)this.backdrop).withDelay(offset * 80).applyTo(new Applier[]{new Applier.Opacity(0.0f, 1.0f), new Applier.TranslateX((float)direction, 0.0f, true)}).start();
            if (this.glyph != null) {
                new KeyframeAnimation(100, (GuiElement)this.glyph).withDelay(offset * 80 + 100).applyTo(new Applier[]{new Applier.Opacity(0.0f, 1.0f)}).start();
            }
            if (this.tweakingIndicator != null) {
                new KeyframeAnimation(100, (GuiElement)this.tweakingIndicator).withDelay(offset * 80 + 100).applyTo(new Applier[]{new Applier.Opacity(0.0f, 1.0f)}).start();
            }
            new KeyframeAnimation(100, (GuiElement)this.moduleString).withDelay(offset * 80 + 200).applyTo(new Applier[]{new Applier.Opacity(0.0f, 1.0f), new Applier.TranslateX((float)(direction * 2), 0.0f, true)}).start();
        }
    }

    protected void setupChildren(String moduleName, GlyphData glyphData, String slotName, boolean tweakable) {
        this.backdrop = new GuiModuleMinorBackdrop(1, -1, 0xFFFFFF);
        if (GuiAttachment.topLeft.equals((Object)this.attachmentPoint)) {
            this.backdrop.setX(-1);
        }
        this.backdrop.setAttachment(this.attachmentPoint);
        this.addChild((GuiElement)this.backdrop);
        if (tweakable) {
            this.tweakingIndicator = new GuiTextureOffset(1, -1, 11, 11, 192, 32, GuiTextures.workbench);
            if (GuiAttachment.topLeft.equals((Object)this.attachmentPoint)) {
                this.tweakingIndicator.setX(-1);
            }
            this.tweakingIndicator.setAttachment(this.attachmentPoint);
            this.addChild((GuiElement)this.tweakingIndicator);
        }
        this.moduleString = new GuiString(-12, 1, moduleName != null ? moduleName : slotName);
        if (GuiAttachment.topLeft.equals((Object)this.attachmentPoint)) {
            this.moduleString.setX(12);
        }
        this.moduleString.setAttachment(this.attachmentPoint);
        this.addChild((GuiElement)this.moduleString);
        this.width = this.moduleString.getWidth() + 12;
        if (glyphData != null) {
            this.glyph = new GuiModuleGlyph(0, 1, 8, 8, glyphData.tint, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
            if (GuiAttachment.topLeft.equals((Object)this.attachmentPoint)) {
                this.glyph.setX(1);
            }
            this.glyph.setAttachment(this.attachmentPoint);
            this.addChild((GuiElement)this.glyph);
        }
    }

    public void updateSelectedHighlight(String selectedSlot) {
        this.isUnselected = selectedSlot != null && !this.slotKey.equals(selectedSlot);
        this.isSelected = selectedSlot != null && this.slotKey.equals(selectedSlot);
        this.updateColors();
    }

    private void updateColors() {
        if (this.isPreview) {
            this.setColor(0xAAAAFF);
        } else if (this.isAdding) {
            this.setColor(0xAAFFAA);
        } else if (this.isRemoving) {
            this.setColor(0xFFAAAA);
        } else if (this.isHovered && this.isEmpty) {
            this.setColor(0x8F8F6F);
        } else if (this.isHovered) {
            this.setColor(0xFFFFCC);
        } else if (this.isSelected) {
            this.setColor(0xFFFFFF);
        } else if (this.isEmpty || this.isUnselected) {
            this.setColor(0x7F7F7F);
        } else {
            this.setColor(0xFFFFFF);
        }
    }

    protected void setColor(int color) {
        this.backdrop.setColor(color);
        this.moduleString.setColor(color);
    }

    protected void onFocus() {
        this.isHovered = true;
        this.updateColors();
        this.hoverHandler.accept(this.slotKey, null);
    }

    protected void onBlur() {
        this.isHovered = false;
        this.updateColors();
        this.hoverHandler.accept(null, null);
    }
}
