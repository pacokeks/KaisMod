/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.resources.language.I18n
 *  se.mickelus.mutil.gui.GuiButton
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiText
 *  se.mickelus.mutil.gui.GuiTexture
 *  se.mickelus.mutil.gui.animation.AnimationChain
 *  se.mickelus.mutil.gui.animation.Applier
 *  se.mickelus.mutil.gui.animation.Applier$Opacity
 *  se.mickelus.mutil.gui.animation.KeyframeAnimation
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import se.mickelus.mutil.gui.GuiButton;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiText;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.blocks.workbench.gui.GuiSchematicListItem;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class GuiSchematicList
extends GuiElement {
    private static final int pageLength = 8;
    private final Consumer<UpgradeSchematic> schematicSelectionConsumer;
    private final GuiElement listGroup;
    private final GuiButton buttonBack;
    private final GuiButton buttonForward;
    private final GuiText emptyStateText;
    private final AnimationChain flash;
    private int page = 0;
    private UpgradeSchematic[] schematics;

    public GuiSchematicList(int x, int y, Consumer<UpgradeSchematic> schematicSelectionConsumer) {
        super(x, y, 224, 67);
        this.addChild((GuiElement)new GuiTexture(-4, -4, 239, 70, 0, 48, GuiTextures.workbench));
        this.listGroup = new GuiElement(3, 3, this.width - 6, this.height - 6);
        this.addChild(this.listGroup);
        this.buttonBack = new GuiButton(-25, this.height + 4, 45, 12, "< Previous", () -> this.setPage(this.getPage() - 1));
        this.addChild((GuiElement)this.buttonBack);
        this.buttonForward = new GuiButton(this.width - 20, this.height + 4, 30, 12, "Next >", () -> this.setPage(this.getPage() + 1));
        this.addChild((GuiElement)this.buttonForward);
        this.emptyStateText = new GuiText(10, 23, 204, ChatFormatting.GRAY + I18n.m_118938_((String)"tetra.workbench.schematic_list.empty", (Object[])new Object[0]));
        this.addChild((GuiElement)this.emptyStateText);
        this.schematicSelectionConsumer = schematicSelectionConsumer;
        GuiTexture flashOverlay = new GuiTexture(-4, -4, 239, 70, 0, 48, GuiTextures.workbench);
        flashOverlay.setOpacity(0.0f);
        flashOverlay.setColor(0);
        this.addChild((GuiElement)flashOverlay);
        this.flash = new AnimationChain(new KeyframeAnimation[]{new KeyframeAnimation(40, (GuiElement)flashOverlay).applyTo(new Applier[]{new Applier.Opacity(0.3f)}), new KeyframeAnimation(80, (GuiElement)flashOverlay).applyTo(new Applier[]{new Applier.Opacity(0.0f)})});
    }

    public void setSchematics(UpgradeSchematic[] schematics) {
        this.schematics = schematics;
        this.emptyStateText.setVisible(schematics.length == 0);
        this.setPage(0);
    }

    private void updateSchematics() {
        int count = 8;
        int offset = this.page * 8;
        if (count + offset > this.schematics.length) {
            count = this.schematics.length - offset;
        }
        this.listGroup.clearChildren();
        for (int i = 0; i < count; ++i) {
            UpgradeSchematic schematic = this.schematics[i + offset];
            this.listGroup.addChild((GuiElement)new GuiSchematicListItem(i / 4 * 109, i % 4 * 14, schematic, () -> this.schematicSelectionConsumer.accept(schematic)));
        }
    }

    private int getPage() {
        return this.page;
    }

    private void setPage(int page) {
        this.page = page;
        this.buttonBack.setVisible(page > 0);
        this.buttonForward.setVisible(page < this.getNumPages() - 1);
        this.updateSchematics();
    }

    private int getNumPages() {
        return (int)Math.ceil(1.0f * (float)this.schematics.length / 8.0f);
    }

    public void flash() {
        this.flash.stop();
        this.flash.start();
    }
}
