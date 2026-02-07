package de.kai.kaismod.screen;

import de.kai.kaismod.data.ToolState;
import de.kai.kaismod.item.ModularToolItem;
import de.kai.kaismod.registry.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.Optional;

public final class UpgradeWorkbenchScreenHandler extends ScreenHandler {
	private static final int TOOL_SLOT = 0;
	private static final int CORE_SLOT = 1;
	private static final int UPGRADE_SLOT = 2;
	private static final int CONFIRM_SLOT = 3;

	private static final int INVENTORY_START = 4;
	private static final int INVENTORY_END = 31;
	private static final int HOTBAR_START = 31;
	private static final int HOTBAR_END = 40;

	private final Inventory inventory;

	public UpgradeWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, null);
	}

	public UpgradeWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
		super(ModScreenHandlers.UPGRADE_WORKBENCH, syncId);
		Inventory actualInventory = inventory != null ? inventory : new SimpleInventory(4) {
			@Override
			public void markDirty() {
				super.markDirty();
				UpgradeWorkbenchScreenHandler.this.onContentChanged(this);
			}
		};

		checkSize(actualInventory, 4);
		this.inventory = actualInventory;
		this.inventory.onOpen(playerInventory.player);

		addSlot(new Slot(this.inventory, TOOL_SLOT, 26, 20) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return stack.getItem() instanceof ModularToolItem;
			}
		});

		addSlot(new Slot(this.inventory, CORE_SLOT, 62, 20) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return stack.isOf(ModItems.CORE_PLACEHOLDER);
			}
		});

		addSlot(new Slot(this.inventory, UPGRADE_SLOT, 98, 20) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return isUpgradeMaterial(stack);
			}
		});

		addSlot(new Slot(this.inventory, CONFIRM_SLOT, 134, 20) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return false;
			}

			@Override
			public boolean canTakeItems(PlayerEntity playerEntity) {
				return hasStack();
			}

			@Override
			public void onTakeItem(PlayerEntity player, ItemStack stack) {
				consumeInputForResult();
				updateResult();
				super.onTakeItem(player, stack);
			}
		});

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 58 + row * 18));
			}
		}

		for (int col = 0; col < 9; col++) {
			addSlot(new Slot(playerInventory, col, 8 + col * 18, 116));
		}

		updateResult();
	}

	@Override
	public void onContentChanged(Inventory inventory) {
		super.onContentChanged(inventory);
		updateResult();
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slotIndex) {
		Slot slot = slots.get(slotIndex);
		if (slot == null || !slot.hasStack()) {
			return ItemStack.EMPTY;
		}

		ItemStack originalStack = slot.getStack();
		ItemStack newStack = originalStack.copy();

		if (slotIndex == CONFIRM_SLOT) {
			originalStack.getItem().onCraftByPlayer(originalStack, player);
			if (!insertItem(originalStack, INVENTORY_START, HOTBAR_END, true)) {
				return ItemStack.EMPTY;
			}
			slot.onQuickTransfer(originalStack, newStack);
		} else if (slotIndex < INVENTORY_START) {
			if (!insertItem(originalStack, INVENTORY_START, HOTBAR_END, true)) {
				return ItemStack.EMPTY;
			}
		} else if (slots.get(TOOL_SLOT).canInsert(originalStack) && !slots.get(TOOL_SLOT).hasStack()) {
			if (!insertItem(originalStack, TOOL_SLOT, TOOL_SLOT + 1, false)) {
				return ItemStack.EMPTY;
			}
		} else if (slots.get(CORE_SLOT).canInsert(originalStack) && !slots.get(CORE_SLOT).hasStack()) {
			if (!insertItem(originalStack, CORE_SLOT, CORE_SLOT + 1, false)) {
				return ItemStack.EMPTY;
			}
		} else if (slots.get(UPGRADE_SLOT).canInsert(originalStack) && !slots.get(UPGRADE_SLOT).hasStack()) {
			if (!insertItem(originalStack, UPGRADE_SLOT, UPGRADE_SLOT + 1, false)) {
				return ItemStack.EMPTY;
			}
		} else if (slotIndex < INVENTORY_END) {
			if (!insertItem(originalStack, HOTBAR_START, HOTBAR_END, false)) {
				return ItemStack.EMPTY;
			}
		} else if (!insertItem(originalStack, INVENTORY_START, INVENTORY_END, false)) {
			return ItemStack.EMPTY;
		}

		if (originalStack.isEmpty()) {
			slot.setStack(ItemStack.EMPTY);
		} else {
			slot.markDirty();
		}

		if (originalStack.getCount() == newStack.getCount()) {
			return ItemStack.EMPTY;
		}

		slot.onTakeItem(player, originalStack);
		updateResult();
		return newStack;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return inventory.canPlayerUse(player);
	}

	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
		dropInventory(player, inventory);
	}

	private void updateResult() {
		ItemStack toolStack = inventory.getStack(TOOL_SLOT);
		ItemStack coreStack = inventory.getStack(CORE_SLOT);
		ItemStack upgradeStack = inventory.getStack(UPGRADE_SLOT);

		ItemStack preview = ItemStack.EMPTY;
		if (toolStack.getItem() instanceof ModularToolItem modularToolItem) {
			ItemStack candidate = toolStack.copy();
			ToolState state = modularToolItem.getOrCreateState(candidate);
			boolean changed = false;

			if (!coreStack.isEmpty() && state.coreId().isEmpty()) {
				state = new ToolState(
					state.toolType(),
					state.headMaterial(),
					state.handleMaterial(),
					Optional.of("kaismod:core_placeholder"),
					state.headUpgradeLevel(),
					Math.max(1, state.coreUpgradeLevel())
				);
				changed = true;
			}

			String upgradedMaterial = toHeadMaterial(upgradeStack);
			if (upgradedMaterial != null) {
				state = new ToolState(
					state.toolType(),
					upgradedMaterial,
					state.handleMaterial(),
					state.coreId(),
					state.headUpgradeLevel() + 1,
					state.coreUpgradeLevel()
				);
				changed = true;
			}

			if (changed) {
				modularToolItem.setState(candidate, state);
				preview = candidate;
			}
		}

		inventory.setStack(CONFIRM_SLOT, preview);
		sendContentUpdates();
	}

	private void consumeInputForResult() {
		ItemStack toolStack = inventory.getStack(TOOL_SLOT);
		if (toolStack.isEmpty()) {
			return;
		}

		if (!inventory.getStack(CORE_SLOT).isEmpty()) {
			inventory.getStack(CORE_SLOT).decrement(1);
		}

		if (!inventory.getStack(UPGRADE_SLOT).isEmpty()) {
			inventory.getStack(UPGRADE_SLOT).decrement(1);
		}

		toolStack.decrement(1);
	}

	private static boolean isUpgradeMaterial(ItemStack stack) {
		return stack.isOf(Items.IRON_INGOT)
			|| stack.isOf(Items.GOLD_INGOT)
			|| stack.isOf(Items.DIAMOND)
			|| stack.isOf(Items.NETHERITE_INGOT);
	}

	private static String toHeadMaterial(ItemStack stack) {
		if (stack.isOf(Items.IRON_INGOT)) {
			return "iron";
		}
		if (stack.isOf(Items.GOLD_INGOT)) {
			return "gold";
		}
		if (stack.isOf(Items.DIAMOND)) {
			return "diamond";
		}
		if (stack.isOf(Items.NETHERITE_INGOT)) {
			return "netherite";
		}
		return null;
	}
}
