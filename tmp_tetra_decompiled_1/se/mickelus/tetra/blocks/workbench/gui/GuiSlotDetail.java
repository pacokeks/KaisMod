/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraftforge.common.ToolAction
 *  se.mickelus.mutil.gui.GuiButton
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiRect
 *  se.mickelus.mutil.gui.animation.AnimationChain
 *  se.mickelus.mutil.gui.animation.Applier
 *  se.mickelus.mutil.gui.animation.Applier$Opacity
 *  se.mickelus.mutil.gui.animation.KeyframeAnimation
 *  se.mickelus.mutil.util.CastOptional
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiButton;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleDetails;
import se.mickelus.tetra.blocks.workbench.gui.GuiSchematicDetail;
import se.mickelus.tetra.blocks.workbench.gui.GuiSchematicList;
import se.mickelus.tetra.blocks.workbench.gui.GuiTweakControls;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.gui.VerticalTabGroupGui;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.CraftingContext;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;
import se.mickelus.tetra.properties.PropertyHelper;

@ParametersAreNonnullByDefault
public class GuiSlotDetail
extends GuiElement {
    private static final char[] keybindings = new char[]{'a', 's', 'd'};
    private static final String[] labels = new String[]{"tetra.workbench.slot_detail.details_tab", "tetra.workbench.slot_detail.craft_tab", "tetra.workbench.slot_detail.tweak_tab"};
    private final AnimationChain slotTransition;
    private final VerticalTabGroupGui tabGroup;
    private final GuiModuleDetails moduleDetails;
    private final GuiElement schematicGroup;
    private final GuiSchematicList schematicList;
    private final GuiSchematicDetail schematicDetail;
    private final GuiTweakControls tweakControls;
    private final Consumer<UpgradeSchematic> selectSchematicHandler;
    private int tab = 1;

    public GuiSlotDetail(int x, int y, Consumer<UpgradeSchematic> selectSchematicHandler, Runnable closeHandler, Runnable craftHandler, Consumer<Map<String, Integer>> previewTweak, Consumer<Map<String, Integer>> applyTweak) {
        super(x, y, 224, 67);
        this.selectSchematicHandler = selectSchematicHandler;
        this.moduleDetails = new GuiModuleDetails(0, 0);
        this.addChild(this.moduleDetails);
        this.schematicGroup = new GuiElement(0, 0, this.width, this.height);
        this.addChild(this.schematicGroup);
        this.schematicList = new GuiSchematicList(0, 0, selectSchematicHandler);
        this.schematicList.setVisible(false);
        this.schematicGroup.addChild((GuiElement)this.schematicList);
        this.schematicDetail = new GuiSchematicDetail(0, 0, () -> selectSchematicHandler.accept(null), craftHandler);
        this.schematicDetail.setVisible(false);
        this.schematicGroup.addChild((GuiElement)this.schematicDetail);
        this.tweakControls = new GuiTweakControls(0, 0, previewTweak, applyTweak);
        this.addChild(this.tweakControls);
        this.addChild((GuiElement)new GuiRect(1, 6, 2, 49, 0));
        this.tabGroup = new VerticalTabGroupGui(1, 6, this::changeTab, GuiTextures.workbench, 128, 32, (String[])IntStream.range(0, 3).mapToObj(i -> I18n.m_118938_((String)labels[i], (Object[])new Object[0])).toArray(String[]::new));
        this.tabGroup.setHasContent(1, true);
        this.addChild(this.tabGroup);
        GuiRect slotTransitionElement = new GuiRect(3, 3, 218, 56, 0);
        slotTransitionElement.setOpacity(0.0f);
        this.addChild((GuiElement)slotTransitionElement);
        this.slotTransition = new AnimationChain(new KeyframeAnimation[]{new KeyframeAnimation(60, (GuiElement)slotTransitionElement).applyTo(new Applier[]{new Applier.Opacity(0.3f)}), new KeyframeAnimation(100, (GuiElement)slotTransitionElement).applyTo(new Applier[]{new Applier.Opacity(0.0f)})});
        GuiButton buttonClose = new GuiButton(215, -4, "x", closeHandler);
        this.addChild((GuiElement)buttonClose);
        this.setVisible(false);
    }

    private void changeTab(int index) {
        this.selectSchematicHandler.accept(null);
        this.tab = index;
        this.updateTabVisibility();
        if (this.moduleDetails.isVisible()) {
            this.moduleDetails.flash();
        }
        if (this.schematicGroup.isVisible()) {
            if (this.schematicDetail.isVisible()) {
                this.schematicDetail.flash();
            }
            if (this.schematicList.isVisible()) {
                this.schematicList.flash();
            }
        }
        if (this.tweakControls.isVisible()) {
            this.tweakControls.flash();
        }
    }

    private void updateTabVisibility() {
        this.moduleDetails.setVisible(this.tab == 0);
        this.schematicGroup.setVisible(this.tab == 1);
        this.tweakControls.setVisible(this.tab == 2);
    }

    public void onTileEntityChange(Player player, WorkbenchTile tileEntity, ItemStack itemStack, String selectedSlot, UpgradeSchematic currentSchematic) {
        ItemModule module = CastOptional.cast((Object)itemStack.m_41720_(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, selectedSlot)).orElse(null);
        if (currentSchematic == null) {
            this.updateSchematicList(player, tileEntity, selectedSlot);
        } else {
            Level world = tileEntity.m_58904_();
            BlockPos pos = tileEntity.m_58899_();
            Map<ToolAction, Integer> availableTools = PropertyHelper.getCombinedToolLevels(player, world, pos, world.m_8055_(pos));
            ItemStack[] materials = tileEntity.getMaterials();
            ItemStack previewStack = currentSchematic.applyUpgrade(itemStack.m_41777_(), materials, false, selectedSlot, player);
            this.schematicDetail.update(world, pos, tileEntity, currentSchematic, itemStack, selectedSlot, materials, availableTools, player);
            this.schematicDetail.updateMagicCapacity(currentSchematic, selectedSlot, itemStack, previewStack);
            this.schematicDetail.updateButton(currentSchematic, player, itemStack, previewStack, materials, selectedSlot, availableTools);
            this.tab = 1;
        }
        this.moduleDetails.update(module, itemStack);
        this.tabGroup.setHasContent(0, module != null);
        this.tweakControls.update(module, itemStack);
        this.tabGroup.setHasContent(2, module != null && module.isTweakable(itemStack));
        this.schematicDetail.setVisible(currentSchematic != null);
        this.schematicList.setVisible(currentSchematic == null);
        this.updateTabVisibility();
        this.tabGroup.setActive(this.tab);
    }

    public void update(Player player, WorkbenchTile tileEntity, Map<ToolAction, Integer> availableTools) {
        this.schematicDetail.updateAvailableTools(availableTools);
        ItemStack currentStack = tileEntity.getTargetItemStack().m_41777_();
        UpgradeSchematic currentSchematic = tileEntity.getCurrentSchematic();
        ItemStack previewStack = currentSchematic.applyUpgrade(currentStack, tileEntity.getMaterials(), false, tileEntity.getCurrentSlot(), player);
        this.schematicDetail.updateButton(tileEntity.getCurrentSchematic(), player, currentStack, previewStack, tileEntity.getMaterials(), tileEntity.getCurrentSlot(), availableTools);
    }

    public void updatePreview(UpgradeSchematic schematic, String slot, ItemStack itemStack, ItemStack previewStack) {
        this.schematicDetail.updateMagicCapacity(schematic, slot, itemStack, previewStack);
    }

    private void updateSchematicList(Player player, WorkbenchTile tileEntity, String selectedSlot) {
        CraftingContext context = new CraftingContext(tileEntity.m_58904_(), tileEntity.m_58899_(), tileEntity.m_58900_(), player, tileEntity.getTargetItemStack(), selectedSlot, tileEntity.getUnlockedSchematics());
        UpgradeSchematic[] schematics = (UpgradeSchematic[])Arrays.stream(SchematicRegistry.getSchematics(context)).sorted(Comparator.comparing(UpgradeSchematic::getRarity).thenComparing(UpgradeSchematic::getType).thenComparing(UpgradeSchematic::getKey)).toArray(UpgradeSchematic[]::new);
        this.schematicList.setSchematics(schematics);
    }

    public void keyTyped(char typedChar) {
        this.tabGroup.keyTyped(typedChar);
    }
}
