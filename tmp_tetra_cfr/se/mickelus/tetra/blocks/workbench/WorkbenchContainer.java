/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.common.capabilities.ForgeCapabilities
 *  net.minecraftforge.items.IItemHandler
 *  net.minecraftforge.items.SlotItemHandler
 *  net.minecraftforge.items.wrapper.InvWrapper
 *  net.minecraftforge.registries.RegistryObject
 *  se.mickelus.mutil.gui.ToggleableSlot
 */
package se.mickelus.tetra.blocks.workbench;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.mutil.gui.ToggleableSlot;
import se.mickelus.tetra.blocks.workbench.AbstractWorkbenchBlock;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class WorkbenchContainer
extends AbstractContainerMenu {
    public static RegistryObject<MenuType<WorkbenchContainer>> containerType;
    private final WorkbenchTile workbench;
    private ToggleableSlot[] materialSlots = new ToggleableSlot[0];

    public WorkbenchContainer(int windowId, WorkbenchTile workbench, Container playerInventory, Player player) {
        super((MenuType)containerType.get(), windowId);
        this.workbench = workbench;
        workbench.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.m_38897_((Slot)new SlotItemHandler(handler, 0, 152, 58));
            this.materialSlots = new ToggleableSlot[3];
            for (int i = 0; i < this.materialSlots.length; ++i) {
                this.materialSlots[i] = new ToggleableSlot(handler, i + 1, 167 + 28 * i, 108);
                this.m_38897_((Slot)this.materialSlots[i]);
            }
        });
        InvWrapper playerInventoryHandler = new InvWrapper(playerInventory);
        for (int x = 0; x < 9; ++x) {
            for (int y = 0; y < 3; ++y) {
                this.m_38897_((Slot)new SlotItemHandler((IItemHandler)playerInventoryHandler, y * 9 + x + 9, x * 17 + 84, y * 17 + 166));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.m_38897_((Slot)new SlotItemHandler((IItemHandler)playerInventoryHandler, i, i * 17 + 84, 221));
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public static WorkbenchContainer create(int windowId, BlockPos pos, Inventory inv) {
        WorkbenchTile te = (WorkbenchTile)Minecraft.m_91087_().f_91073_.m_7702_(pos);
        return new WorkbenchContainer(windowId, te, (Container)inv, (Player)Minecraft.m_91087_().f_91074_);
    }

    private int getSlots() {
        return this.workbench.getCapability(ForgeCapabilities.ITEM_HANDLER).map(IItemHandler::getSlots).orElse(0);
    }

    public boolean m_6875_(Player player) {
        BlockPos pos = this.workbench.m_58899_();
        if (this.workbench.m_58904_().m_8055_(this.workbench.m_58899_()).m_60734_() instanceof AbstractWorkbenchBlock) {
            return player.m_20275_((double)pos.m_123341_() + 0.5, (double)pos.m_123342_() + 0.5, (double)pos.m_123343_() + 0.5) <= 64.0;
        }
        return false;
    }

    public ItemStack m_7648_(Player player, int index) {
        ItemStack resultStack = ItemStack.f_41583_;
        Slot slot = (Slot)this.f_38839_.get(index);
        if (slot != null && slot.m_6657_()) {
            ItemStack slotStack = slot.m_7993_();
            resultStack = slotStack.m_41777_();
            if (index < this.getSlots() ? !this.m_38903_(slotStack, this.getSlots(), this.f_38839_.size(), true) : !this.m_38903_(slotStack, 0, this.getSlots(), false)) {
                return ItemStack.f_41583_;
            }
            if (slotStack.m_41619_()) {
                slot.m_5852_(ItemStack.f_41583_);
            } else {
                slot.m_6654_();
            }
        }
        this.workbench.m_6596_();
        return resultStack;
    }

    public void updateSlots() {
        int numMaterialSlots = Optional.ofNullable(this.workbench.getCurrentSchematic()).map(UpgradeSchematic::getNumMaterialSlots).orElse(0);
        for (int i = 0; i < this.materialSlots.length; ++i) {
            this.materialSlots[i].toggle(i < numMaterialSlots);
            this.materialSlots[i].f_40220_ = 194 + WorkbenchContainer.getSlotOffsetY(i, numMaterialSlots);
        }
    }

    public static int getSlotOffsetY(int index, int numMaterialSlots) {
        if (numMaterialSlots == 2) {
            return 11 + 32 * index;
        }
        return 28 * index;
    }

    public WorkbenchTile getTileEntity() {
        return this.workbench;
    }
}
