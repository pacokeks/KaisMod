/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiItem$CountMode
 *  se.mickelus.mutil.gui.GuiString
 *  se.mickelus.mutil.gui.GuiStringSmall
 *  se.mickelus.mutil.gui.GuiTexture
 */
package se.mickelus.tetra.blocks.workbench.gui;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiItem;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.gui.GuiItemRolling;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.gui.ZOffsetGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.schematic.HoloMaterialApplicable;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.schematic.HoloMaterialTranslationGui;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

public class SchemaSlotGui
extends GuiElement {
    private GuiString label;
    private GuiString quantity;
    private GuiItemRolling placeholder;
    private GuiElement placeholderBorder;
    private GuiTexture border;
    private HoloMaterialTranslationGui materialTranslation;
    private HoloMaterialApplicable applicableMaterials;
    private final int index;
    private final int fullWidth;
    private static final int compactWidth = 26;
    private List<Component> labelTooltip = null;

    public SchemaSlotGui(int x, int y, int width, int index) {
        super(x, y, width, 18);
        this.fullWidth = width;
        this.label = new GuiString(28, 5, width - 30, "");
        this.addChild((GuiElement)this.label);
        this.quantity = new GuiStringSmall(27, 14, "");
        this.addChild((GuiElement)this.quantity);
        this.placeholder = new GuiItemRolling(10, 1);
        this.placeholder.setCountVisibility(GuiItem.CountMode.never);
        this.addChild(this.placeholder);
        this.placeholderBorder = new ZOffsetGui(0, 0, 160.0);
        this.placeholderBorder.addChild(new GuiTexture(10, 1, 16, 16, 52, 16, GuiTextures.workbench).setOpacity(0.8f));
        this.addChild(this.placeholderBorder);
        this.border = new GuiTexture(10, 1, 16, 16, 52, 16, GuiTextures.workbench);
        this.border.setOpacity(0.8f);
        this.addChild((GuiElement)this.border);
        this.materialTranslation = new HoloMaterialTranslationGui(1, 1);
        this.addChild(this.materialTranslation);
        this.applicableMaterials = new HoloMaterialApplicable(1, 9);
        this.addChild(this.applicableMaterials);
        this.index = index;
    }

    public void update(UpgradeSchematic schematic, Player player, Level level, BlockPos pos, WorkbenchTile blockEntity, ItemStack targetStack, String slot, ItemStack[] materials) {
        int slotCount = schematic.getNumMaterialSlots();
        if (slotCount > this.index) {
            boolean slotHoldsMaterial = this.index < materials.length && materials[this.index].m_41619_();
            this.setWidth(slotCount > 1 ? 26 : this.fullWidth);
            this.label.setVisible(slotCount == 1);
            this.materialTranslation.setVisible(this.index == 0);
            this.applicableMaterials.setVisible(this.index == 0);
            if (this.index == 0) {
                this.materialTranslation.update(schematic);
                this.applicableMaterials.update(level, pos, blockEntity, targetStack, slot, schematic, player);
            }
            this.border.setVisible(!slotHoldsMaterial);
            this.placeholderBorder.setVisible(slotHoldsMaterial);
            this.placeholder.setVisible(slotHoldsMaterial);
            this.placeholder.setItems(schematic.getSlotPlaceholders(targetStack, this.index));
            String labelString = schematic.getSlotName(targetStack, this.index);
            this.label.setString(labelString);
            Object object = this.labelTooltip = slotHoldsMaterial ? ImmutableList.of((Object)Component.m_237113_((String)labelString)) : null;
            if (this.index < materials.length && schematic.acceptsMaterial(targetStack, slot, this.index, materials[this.index])) {
                int requiredCount = schematic.getRequiredQuantity(targetStack, this.index, materials[this.index]);
                if (!materials[this.index].m_41619_() && requiredCount > 1) {
                    this.quantity.setString("/" + requiredCount);
                    this.quantity.setColor(materials[this.index].m_41613_() < requiredCount ? 0xFF5555 : 0xFFFFFF);
                }
                this.quantity.setVisible(!materials[this.index].m_41619_() && requiredCount > 1);
            } else {
                this.quantity.setVisible(false);
            }
            this.setVisible(true);
        } else {
            this.setVisible(false);
        }
    }

    public List<Component> getTooltipLines() {
        List tooltipLines = super.getTooltipLines();
        if (tooltipLines == null && this.hasFocus()) {
            return this.labelTooltip;
        }
        return tooltipLines;
    }
}
