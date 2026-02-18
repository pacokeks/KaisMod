/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.world.item.ItemStack
 *  se.mickelus.mutil.gui.GuiAttachment
 *  se.mickelus.mutil.gui.GuiElement
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.tetra.blocks.workbench.gui.GuiModule;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleMajor;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.ItemModuleMajor;

@ParametersAreNonnullByDefault
public class GuiModuleList
extends GuiElement {
    private final Consumer<String> slotClickHandler;
    private final BiConsumer<String, String> hoverHandler;
    private GuiModuleMajor[] majorModuleElements = new GuiModuleMajor[0];
    private GuiModule[] minorModuleElements = new GuiModule[0];

    public GuiModuleList(int x, int y, Consumer<String> slotClickHandler, BiConsumer<String, String> hoverHandler) {
        super(x, y, 0, 0);
        this.slotClickHandler = slotClickHandler;
        this.hoverHandler = hoverHandler;
    }

    public void update(ItemStack itemStack, ItemStack previewStack, String focusSlot) {
        this.clearChildren();
        if (!itemStack.m_41619_() && itemStack.m_41720_() instanceof IModularItem) {
            IModularItem item = (IModularItem)itemStack.m_41720_();
            this.updateMajorModules(item, itemStack, previewStack);
            this.updateMinorModules(item, itemStack, previewStack);
            this.setFocus(focusSlot);
        }
    }

    public void showAnimation() {
        int i;
        Random rand = new Random();
        for (i = 0; i < this.majorModuleElements.length; ++i) {
            this.majorModuleElements[i].showAnimation(rand.nextInt(this.minorModuleElements.length + this.majorModuleElements.length));
        }
        for (i = 0; i < this.minorModuleElements.length; ++i) {
            this.minorModuleElements[i].showAnimation(rand.nextInt(this.minorModuleElements.length + this.majorModuleElements.length));
        }
    }

    public void setFocus(String slotKey) {
        for (GuiModuleMajor guiModuleMajor : this.majorModuleElements) {
            guiModuleMajor.updateSelectedHighlight(slotKey);
        }
        for (GuiModule guiModule : this.minorModuleElements) {
            guiModule.updateSelectedHighlight(slotKey);
        }
    }

    private void updateMajorModules(IModularItem item, ItemStack itemStack, ItemStack previewStack) {
        String[] majorModuleNames = item.getMajorModuleNames(itemStack);
        String[] majorModuleKeys = item.getMajorModuleKeys(itemStack);
        ItemModuleMajor[] majorModules = item.getMajorModules(itemStack);
        GuiModuleOffsets offsets = item.getMajorGuiOffsets(itemStack);
        this.majorModuleElements = new GuiModuleMajor[majorModules.length];
        if (!previewStack.m_41619_()) {
            ItemModuleMajor[] majorModulesPreview = item.getMajorModules(previewStack);
            for (int i = 0; i < majorModuleNames.length; ++i) {
                int x = offsets.getX(i);
                this.majorModuleElements[i] = new GuiModuleMajor(x, offsets.getY(i), x > 0 ? GuiAttachment.topLeft : GuiAttachment.topRight, itemStack, previewStack, majorModuleKeys[i], majorModuleNames[i], majorModules[i], majorModulesPreview[i], this.slotClickHandler, this.hoverHandler);
                this.addChild((GuiElement)this.majorModuleElements[i]);
            }
        } else {
            for (int i = 0; i < majorModuleNames.length; ++i) {
                int x = offsets.getX(i);
                this.majorModuleElements[i] = new GuiModuleMajor(x, offsets.getY(i), x > 0 ? GuiAttachment.topLeft : GuiAttachment.topRight, itemStack, itemStack, majorModuleKeys[i], majorModuleNames[i], majorModules[i], majorModules[i], this.slotClickHandler, this.hoverHandler);
                this.addChild((GuiElement)this.majorModuleElements[i]);
            }
        }
    }

    private void updateMinorModules(IModularItem item, ItemStack itemStack, ItemStack previewStack) {
        String[] minorModuleNames = item.getMinorModuleNames(itemStack);
        String[] minorModuleKeys = item.getMinorModuleKeys(itemStack);
        ItemModule[] minorModules = item.getMinorModules(itemStack);
        GuiModuleOffsets offsets = item.getMinorGuiOffsets(itemStack);
        this.minorModuleElements = new GuiModule[minorModules.length];
        if (!previewStack.m_41619_()) {
            ItemModule[] minorModulesPreview = item.getMinorModules(previewStack);
            for (int i = 0; i < minorModuleNames.length; ++i) {
                this.minorModuleElements[i] = this.getMinorModule(i, offsets, itemStack, previewStack, minorModuleKeys[i], minorModuleNames[i], minorModules[i], minorModulesPreview[i]);
                this.addChild((GuiElement)this.minorModuleElements[i]);
            }
        } else {
            for (int i = 0; i < minorModuleNames.length; ++i) {
                this.minorModuleElements[i] = this.getMinorModule(i, offsets, itemStack, itemStack, minorModuleKeys[i], minorModuleNames[i], minorModules[i], minorModules[i]);
                this.addChild((GuiElement)this.minorModuleElements[i]);
            }
        }
    }

    private GuiModule getMinorModule(int index, GuiModuleOffsets offsets, ItemStack itemStack, ItemStack previewStack, String slotKey, String slotName, ItemModule module, ItemModule previewModule) {
        int x = offsets.getX(index);
        return new GuiModule(x, offsets.getY(index), x > 0 ? GuiAttachment.topLeft : GuiAttachment.topRight, itemStack, previewStack, slotKey, slotName, module, previewModule, this.slotClickHandler, this.hoverHandler);
    }
}
