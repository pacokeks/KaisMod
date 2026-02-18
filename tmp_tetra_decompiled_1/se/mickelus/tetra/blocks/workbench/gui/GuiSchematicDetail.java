/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraftforge.common.ToolAction
 *  se.mickelus.mutil.gui.GuiButton
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiString
 *  se.mickelus.mutil.gui.GuiTextSmall
 *  se.mickelus.mutil.gui.GuiTexture
 *  se.mickelus.mutil.gui.animation.AnimationChain
 *  se.mickelus.mutil.gui.animation.Applier
 *  se.mickelus.mutil.gui.animation.Applier$Opacity
 *  se.mickelus.mutil.gui.animation.KeyframeAnimation
 */
package se.mickelus.tetra.blocks.workbench.gui;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiButton;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTextSmall;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.blocks.workbench.WorkbenchContainer;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.blocks.workbench.gui.CraftButtonGui;
import se.mickelus.tetra.blocks.workbench.gui.GuiExperience;
import se.mickelus.tetra.blocks.workbench.gui.GuiSources;
import se.mickelus.tetra.blocks.workbench.gui.SchemaSlotGui;
import se.mickelus.tetra.blocks.workbench.gui.ToolRequirementListGui;
import se.mickelus.tetra.gui.GuiMagicUsage;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.schematic.SchematicType;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class GuiSchematicDetail
extends GuiElement {
    private final GuiElement glyph;
    private final GuiString title;
    private final GuiSources sources;
    private final GuiTextSmall description;
    private final CraftButtonGui craftButton;
    private final SchemaSlotGui[] slots;
    private final GuiElement emptySlotsIndicator;
    private final GuiElement hasSlotsIndicator;
    private final GuiMagicUsage magicCapacity;
    private final ToolRequirementListGui toolRequirementList;
    private final GuiExperience experienceIndicator;
    private final AnimationChain flash;
    private UpgradeSchematic schematic;
    private List<Component> descriptionTooltip;

    public GuiSchematicDetail(int x, int y, Runnable backListener, Runnable craftListener) {
        super(x, y, 224, 67);
        this.addChild((GuiElement)new GuiTexture(-4, -4, 239, 69, 0, 187, GuiTextures.workbench));
        this.addChild((GuiElement)new GuiButton(-4, this.height - 2, 40, 8, "< " + I18n.m_118938_((String)"tetra.workbench.schematic_detail.back", (Object[])new Object[0]), backListener));
        this.glyph = new GuiElement(3, 3, 16, 16);
        this.addChild(this.glyph);
        this.title = new GuiString(19, 6, 100, "");
        this.addChild((GuiElement)this.title);
        this.sources = new GuiSources(19, 15, 81);
        this.addChild(this.sources);
        this.description = new GuiTextSmall(5, 22, 125, "");
        this.addChild((GuiElement)this.description);
        this.slots = new SchemaSlotGui[3];
        for (int i = 0; i < 3; ++i) {
            this.slots[i] = new SchemaSlotGui(125, 5, 82, i);
            this.addChild(this.slots[i]);
        }
        this.emptySlotsIndicator = new GuiTexture(146, 6, 64, 16, 48, 32, GuiTextures.workbench);
        this.addChild(this.emptySlotsIndicator);
        this.hasSlotsIndicator = new GuiElement(0, 0, 0, 0);
        this.hasSlotsIndicator.addChild((GuiElement)new GuiTexture(132, 3, 4, 22, 240, 192, GuiTextures.workbench));
        this.hasSlotsIndicator.addChild((GuiElement)new GuiTexture(220, 3, 5, 22, 244, 192, GuiTextures.workbench));
        this.addChild(this.hasSlotsIndicator);
        this.magicCapacity = new GuiMagicUsage(138, 30, 80);
        this.addChild(this.magicCapacity);
        this.experienceIndicator = new GuiExperience(205, 41, "tetra.workbench.schematic_detail.experience");
        this.addChild(this.experienceIndicator);
        this.craftButton = new CraftButtonGui(155, 41, craftListener);
        this.addChild((GuiElement)this.craftButton);
        this.toolRequirementList = new ToolRequirementListGui(143, 40);
        this.addChild(this.toolRequirementList);
        GuiTexture flashOverlay = new GuiTexture(-4, -4, 239, 69, 0, 187, GuiTextures.workbench);
        flashOverlay.setOpacity(0.0f);
        flashOverlay.setColor(0);
        this.addChild((GuiElement)flashOverlay);
        this.flash = new AnimationChain(new KeyframeAnimation[]{new KeyframeAnimation(60, (GuiElement)flashOverlay).applyTo(new Applier[]{new Applier.Opacity(0.3f)}), new KeyframeAnimation(120, (GuiElement)flashOverlay).applyTo(new Applier[]{new Applier.Opacity(0.0f)})});
    }

    public void update(Level level, BlockPos pos, WorkbenchTile blockEntity, UpgradeSchematic schematic, ItemStack itemStack, String slot, ItemStack[] materials, Map<ToolAction, Integer> availableTools, Player player) {
        GuiTexture glyphTexture;
        this.schematic = schematic;
        this.title.setString(schematic.getName());
        this.title.setColor(schematic.getRarity().tint);
        this.sources.update(schematic);
        String descriptionString = schematic.getDescription(itemStack);
        this.description.setString(ChatFormatting.GRAY + descriptionString.replace(ChatFormatting.RESET.toString(), ChatFormatting.RESET.toString() + ChatFormatting.GRAY));
        this.descriptionTooltip = ImmutableList.of((Object)Component.m_237113_((String)descriptionString));
        this.glyph.clearChildren();
        GlyphData glyphData = schematic.getGlyph();
        GuiTexture border = null;
        if (schematic.getType() == SchematicType.major) {
            border = new GuiTexture(0, 2, 16, 9, 52, 3, GuiTextures.workbench);
            glyphTexture = new GuiTexture(-1, -1, 16, 16, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
        } else if (schematic.getType() == SchematicType.minor) {
            border = new GuiTexture(2, 1, 11, 11, 68, 0, GuiTextures.workbench);
            glyphTexture = new GuiTexture(4, 3, 8, 8, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
        } else if (schematic.getType() == SchematicType.improvement) {
            border = new GuiTexture(0, 2, 16, 9, 52, 3, GuiTextures.workbench);
            glyphTexture = new GuiTexture(-1, -1, 16, 16, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
        } else {
            glyphTexture = new GuiTexture(-1, -1, 16, 16, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
        }
        if (border != null) {
            border.setOpacity(0.3f);
            border.setColor(schematic.getRarity().tint);
            this.glyph.addChild((GuiElement)border);
        }
        glyphTexture.setColor(schematic.getRarity().tint);
        this.glyph.addChild((GuiElement)glyphTexture);
        if (schematic.getType() == SchematicType.improvement) {
            this.glyph.addChild((GuiElement)new GuiTexture(7, 7, 7, 7, 68, 16, GuiTextures.workbench).setColor(0x7F7F7F));
        }
        int numMaterialSlots = schematic.getNumMaterialSlots();
        for (int i = 0; i < 3; ++i) {
            this.slots[i].update(schematic, player, level, pos, blockEntity, itemStack, slot, materials);
            this.slots[i].setX(136 + WorkbenchContainer.getSlotOffsetY(i, numMaterialSlots));
        }
        this.toolRequirementList.update(schematic, itemStack, slot, materials, availableTools);
        this.emptySlotsIndicator.setVisible(numMaterialSlots == 0);
        this.hasSlotsIndicator.setVisible(numMaterialSlots != 0);
        int xpCost = schematic.getExperienceCost(itemStack, materials, slot);
        this.experienceIndicator.setVisible(xpCost > 0);
        if (xpCost > 0) {
            if (!player.m_7500_()) {
                this.experienceIndicator.update(xpCost, xpCost <= player.f_36078_);
            } else {
                this.experienceIndicator.update(xpCost, true);
            }
        }
        this.flash();
    }

    public void updateMagicCapacity(UpgradeSchematic schematic, String slot, ItemStack itemStack, ItemStack previewStack) {
        if (slot != null && (schematic != null && SchematicType.major.equals((Object)schematic.getType()) && this.magicCapacity.providesCapacity(itemStack, previewStack, slot) || this.magicCapacity.hasChanged(itemStack, previewStack, slot))) {
            this.magicCapacity.update(itemStack, previewStack, slot);
            this.magicCapacity.setVisible(true);
        } else {
            this.magicCapacity.setVisible(false);
        }
    }

    public void updateAvailableTools(Map<ToolAction, Integer> availableTools) {
        this.toolRequirementList.updateAvailableTools(availableTools);
    }

    public void updateButton(UpgradeSchematic schematic, Player player, ItemStack itemStack, ItemStack previewStack, ItemStack[] materials, String slot, Map<ToolAction, Integer> availableTools) {
        this.craftButton.update(schematic, player, itemStack, previewStack, materials, slot, availableTools);
    }

    public List<Component> getTooltipLines() {
        if (this.description.hasFocus()) {
            return this.descriptionTooltip;
        }
        return super.getTooltipLines();
    }

    public void flash() {
        this.flash.stop();
        this.flash.start();
    }
}
