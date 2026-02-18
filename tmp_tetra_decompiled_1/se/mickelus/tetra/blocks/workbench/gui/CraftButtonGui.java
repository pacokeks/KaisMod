/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.common.ToolAction
 *  se.mickelus.mutil.gui.GuiAttachment
 *  se.mickelus.mutil.gui.GuiClickable
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiStringOutline
 *  se.mickelus.mutil.gui.GuiTexture
 *  se.mickelus.mutil.util.CastOptional
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class CraftButtonGui
extends GuiClickable {
    private final GuiStringOutline label;
    private final GuiTexture backdrop = new GuiTexture(0, 0, this.width, this.height, 176, 16, GuiTextures.workbench);
    private boolean enabled = true;
    private List<Component> tooltip;
    private int labelColor = 0xFFFFFF;
    private int backdropColor = 0xFFFFFF;

    public CraftButtonGui(int x, int y, Runnable onClickHandler) {
        super(x, y, 46, 15, onClickHandler);
        this.backdrop.setAttachment(GuiAttachment.middleCenter);
        this.addChild((GuiElement)this.backdrop);
        this.label = new GuiStringOutline(0, 1, I18n.m_118938_((String)"tetra.workbench.schematic_detail.craft", (Object[])new Object[0]));
        this.label.setAttachment(GuiAttachment.middleCenter);
        this.addChild((GuiElement)this.label);
    }

    public boolean onMouseClick(int x, int y, int button) {
        return this.enabled && super.onMouseClick(x, y, button);
    }

    public void update(UpgradeSchematic schematic, Player player, ItemStack itemStack, ItemStack previewStack, ItemStack[] materials, String slot, Map<ToolAction, Integer> availableTools) {
        this.enabled = schematic.canApplyUpgrade(player, itemStack, materials, slot, availableTools);
        this.tooltip = new ArrayList<Component>();
        if (this.enabled) {
            boolean willRepair;
            this.labelColor = 0xFFFFFF;
            this.backdropColor = 0xFFFFFF;
            boolean willReplace = schematic.willReplace(itemStack, materials, slot);
            float severity = schematic.getSeverity(itemStack, materials, slot);
            List<String> destabilizationChance = this.getDestabilizationChance(previewStack.m_41619_() ? itemStack : previewStack, severity, willReplace ? slot : null);
            if (!destabilizationChance.isEmpty()) {
                this.backdropColor = 0xEE5599;
                this.tooltip.add((Component)Component.m_237115_((String)"tetra.workbench.schematic_detail.destabilize_tooltip"));
                destabilizationChance.stream().map(Component::m_237113_).forEach(this.tooltip::add);
            }
            boolean bl = willRepair = CastOptional.cast((Object)itemStack.m_41720_(), IModularItem.class).map(item -> item.getRepairSlot(itemStack)).map(repairSlot -> repairSlot.equals(slot)).orElse(false) != false && willReplace && itemStack.m_41763_() && (double)itemStack.m_41773_() * 1.0 / (double)itemStack.m_41776_() > 0.0;
            if (willRepair) {
                if (!this.tooltip.isEmpty()) {
                    this.tooltip.add((Component)Component.m_237113_((String)" "));
                }
                this.tooltip.add((Component)Component.m_237115_((String)"tetra.workbench.schematic_detail.repair_tooltip"));
            }
        } else {
            this.labelColor = 0x7F7F7F;
            this.backdropColor = 0xFF5555;
            if (!schematic.isMaterialsValid(itemStack, slot, materials)) {
                if (this.hasEmptyMaterial(schematic, materials)) {
                    this.tooltip.add((Component)Component.m_237115_((String)"tetra.workbench.schematic_detail.no_material_tooltip"));
                    this.backdropColor = 0x7F7F7F;
                } else if (this.hasInsufficientQuantities(schematic, itemStack, slot, materials)) {
                    this.tooltip.add((Component)Component.m_237115_((String)"tetra.workbench.schematic_detail.material_count_tooltip"));
                } else {
                    this.tooltip.add((Component)Component.m_237115_((String)"tetra.workbench.schematic_detail.material_tooltip"));
                }
            } else {
                if (schematic.isIntegrityViolation(player, itemStack, materials, slot)) {
                    this.tooltip.add((Component)Component.m_237115_((String)"tetra.workbench.schematic_detail.integrity_tooltip"));
                }
                if (!schematic.checkTools(itemStack, materials, availableTools)) {
                    this.tooltip.add((Component)Component.m_237115_((String)"tetra.workbench.schematic_detail.tools_tooltip"));
                }
                if (!player.m_7500_() && player.f_36078_ < schematic.getExperienceCost(itemStack, materials, slot)) {
                    this.tooltip.add((Component)Component.m_237115_((String)"tetra.workbench.schematic_detail.level_tooltip"));
                }
            }
        }
        this.updateColors();
    }

    private List<String> getDestabilizationChance(ItemStack itemStack, float severity, @Nullable String ignoredSlot) {
        return CastOptional.cast((Object)itemStack.m_41720_(), IModularItem.class).map(item -> item.getMajorModules(itemStack)).stream().flatMap(Arrays::stream).filter(Objects::nonNull).filter(module -> !module.getSlot().equals(ignoredSlot)).filter(module -> module.getDestabilizationChance(itemStack, severity) > 0.0f).map(module -> String.format("  %s%s: %s%.0f%%", ChatFormatting.WHITE, module.getName(itemStack), ChatFormatting.YELLOW, Float.valueOf(module.getDestabilizationChance(itemStack, severity) * 100.0f))).collect(Collectors.toList());
    }

    private boolean hasEmptyMaterial(UpgradeSchematic schematic, ItemStack[] materials) {
        for (int i = 0; i < schematic.getNumMaterialSlots(); ++i) {
            if (!materials[i].m_41619_()) continue;
            return true;
        }
        return false;
    }

    private boolean hasInsufficientQuantities(UpgradeSchematic schematic, ItemStack itemStack, String slot, ItemStack[] materials) {
        for (int i = 0; i < schematic.getNumMaterialSlots(); ++i) {
            if (!schematic.acceptsMaterial(itemStack, slot, i, materials[i])) continue;
            int requiredCount = schematic.getRequiredQuantity(itemStack, i, materials[i]);
            if (materials[i].m_41619_() || requiredCount <= materials[i].m_41613_()) continue;
            return true;
        }
        return false;
    }

    protected void onFocus() {
        this.updateColors();
    }

    protected void onBlur() {
        this.updateColors();
    }

    private void updateColors() {
        if (this.enabled && this.hasFocus()) {
            this.label.setColor(0xFFFFCC);
            this.backdrop.setColor(0xFFFFCC);
        } else {
            this.label.setColor(this.labelColor);
            this.backdrop.setColor(this.backdropColor);
        }
    }

    public List<Component> getTooltipLines() {
        if (this.hasFocus()) {
            return this.tooltip;
        }
        return null;
    }
}
