package de.kai.kaismod.screen;

import de.kai.kaismod.balance.HeadUpgradeRules;
import de.kai.kaismod.data.ToolState;
import de.kai.kaismod.data.ToolStateOperationResult;
import de.kai.kaismod.data.ToolStateOperations;
import de.kai.kaismod.data.ToolType;
import de.kai.kaismod.core.ToolCore;
import de.kai.kaismod.item.ModularToolItem;
import de.kai.kaismod.core.ToolCoreRegistry;
import de.kai.kaismod.registry.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.Optional;

public final class UpgradeWorkbenchScreenHandler extends ScreenHandler {
	private static final int TOOL_SLOT = 0;
	private static final int CORE_SLOT = 1;
	private static final int UPGRADE_SLOT = 2;
	private static final int CONFIRM_SLOT = 3;
	private static final int SLOT_SPACING = 17;
	private static final int TOOL_SLOT_X = 152;
	private static final int TOOL_SLOT_Y = 58;
	private static final int CORE_SLOT_X = 161;
	private static final int CORE_SLOT_Y = 108;
	private static final int UPGRADE_SLOT_X = CORE_SLOT_X + 42;
	private static final int UPGRADE_SLOT_Y = CORE_SLOT_Y;
	private static final int CONFIRM_SLOT_X = UPGRADE_SLOT_X + 42;
	private static final int CONFIRM_SLOT_Y = CORE_SLOT_Y;
	private static final int PLAYER_INV_X = 84;
	private static final int PLAYER_INV_Y = 166;
	private static final int HOTBAR_Y = 221;

	private static final int INVENTORY_START = 4;
	private static final int INVENTORY_END = 31;
	private static final int HOTBAR_START = 31;
	private static final int HOTBAR_END = 40;

	private final Inventory inventory;
	private boolean updatingResult;

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

		addSlot(new Slot(this.inventory, TOOL_SLOT, TOOL_SLOT_X, TOOL_SLOT_Y) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return isSupportedToolInput(stack);
			}
		});

		addSlot(new Slot(this.inventory, CORE_SLOT, CORE_SLOT_X, CORE_SLOT_Y) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return hasToolInput() && isCoreItem(stack);
			}
		});

		addSlot(new Slot(this.inventory, UPGRADE_SLOT, UPGRADE_SLOT_X, UPGRADE_SLOT_Y) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return hasToolInput() && (isUpgradeMaterial(stack) || isSwitchCatalyst(stack));
			}
		});

		addSlot(new Slot(this.inventory, CONFIRM_SLOT, CONFIRM_SLOT_X, CONFIRM_SLOT_Y) {
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
				addSlot(new Slot(playerInventory, col + row * 9 + 9, PLAYER_INV_X + col * SLOT_SPACING, PLAYER_INV_Y + row * SLOT_SPACING));
			}
		}

		for (int col = 0; col < 9; col++) {
			addSlot(new Slot(playerInventory, col, PLAYER_INV_X + col * SLOT_SPACING, HOTBAR_Y));
		}

		updateResult();
	}

	@Override
	public void onContentChanged(Inventory inventory) {
		if (updatingResult) {
			return;
		}
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
		if (updatingResult) {
			return;
		}

		ItemStack toolStack = inventory.getStack(TOOL_SLOT);
		ItemStack coreStack = inventory.getStack(CORE_SLOT);
		ItemStack upgradeStack = inventory.getStack(UPGRADE_SLOT);

		ItemStack preview = ItemStack.EMPTY;
		Optional<ToolInputResolution> inputResolution = resolveToolInput(toolStack);
		if (inputResolution.isPresent()) {
			ToolInputResolution resolution = inputResolution.get();
			ItemStack candidate = resolution.outputStack().copy();
			PendingOperation operation = resolveOperation(resolution.state(), coreStack, upgradeStack);
			if (operation.changed()) {
				resolution.modularItem().setState(candidate, operation.state());
				preview = candidate;
			}
		}

		updatingResult = true;
		try {
			ItemStack currentResult = inventory.getStack(CONFIRM_SLOT);
			if (!ItemStack.areEqual(currentResult, preview)) {
				inventory.setStack(CONFIRM_SLOT, preview);
			}
			sendContentUpdates();
		} finally {
			updatingResult = false;
		}
	}

	private boolean hasToolInput() {
		return inventory != null && !inventory.getStack(TOOL_SLOT).isEmpty();
	}

	private void consumeInputForResult() {
		ItemStack toolStack = inventory.getStack(TOOL_SLOT);
		if (toolStack.isEmpty()) {
			return;
		}

		Optional<ToolInputResolution> inputResolution = resolveToolInput(toolStack);
		if (inputResolution.isEmpty()) {
			return;
		}

		ToolState currentState = inputResolution.get().state();
		ItemStack coreStack = inventory.getStack(CORE_SLOT);
		ItemStack upgradeStack = inventory.getStack(UPGRADE_SLOT);
		PendingOperation operation = resolveOperation(currentState, coreStack, upgradeStack);
		if (!operation.changed()) {
			return;
		}

		if (operation.coreCost() > 0 && !coreStack.isEmpty()) {
			coreStack.decrement(operation.coreCost());
		}

		if (operation.upgradeCost() > 0 && !upgradeStack.isEmpty()) {
			upgradeStack.decrement(operation.upgradeCost());
		}

		toolStack.decrement(1);
	}

	private static PendingOperation resolveOperation(ToolState initialState, ItemStack coreStack, ItemStack upgradeStack) {
		ToolState state = initialState;
		int coreCost = 0;
		int upgradeCost = 0;
		boolean changed = false;

		String targetCoreId = toCoreId(coreStack);
		if (targetCoreId != null) {
			ToolCore core = ToolCoreRegistry.find(targetCoreId).orElse(null);
			if (core != null) {
				if (state.coreId().isEmpty()) {
					int installCost = Math.max(1, core.installationCost());
					if (coreStack.getCount() >= installCost) {
						ToolStateOperationResult result = ToolStateOperations.installCore(state, targetCoreId);
						if (result.isSuccess()) {
							state = result.state();
							coreCost = installCost;
							changed = true;
						}
					}
				} else if (!state.coreId().get().equals(targetCoreId)) {
					int switchCost = Math.max(1, core.switchCost());
					if (hasSwitchCatalyst(upgradeStack, switchCost)) {
						ToolStateOperationResult result = ToolStateOperations.switchCore(state, targetCoreId);
						if (result.isSuccess()) {
							state = result.state();
							coreCost = 1;
							upgradeCost = switchCost;
							changed = true;
						}
					}
				}
			}
		}

		// Head-Upgrade nur dann, wenn kein Kernwechsel in derselben Aktion stattfindet.
		if (upgradeCost == 0) {
			String upgradedMaterial = toHeadMaterial(upgradeStack);
			if (upgradedMaterial != null) {
				int targetCost = HeadUpgradeRules.costForTargetMaterial(upgradedMaterial);
				if (upgradeStack.getCount() >= targetCost) {
					ToolStateOperationResult upgradeResult = ToolStateOperations.upgradeHead(state, upgradedMaterial);
					if (upgradeResult.isSuccess()) {
						state = upgradeResult.state();
						upgradeCost = targetCost;
						changed = true;
					}
				}
			}
		}

		return new PendingOperation(state, changed, coreCost, upgradeCost);
	}

	private static boolean isUpgradeMaterial(ItemStack stack) {
		return stack.isIn(ItemTags.PLANKS)
			|| stack.isOf(Items.COBBLESTONE)
			|| stack.isOf(Items.STONE)
			|| stack.isOf(Items.COPPER_INGOT)
			|| stack.isOf(Items.IRON_INGOT)
			|| stack.isOf(Items.GOLD_INGOT)
			|| stack.isOf(Items.DIAMOND)
			|| stack.isOf(Items.NETHERITE_INGOT);
	}

	private static boolean isSwitchCatalyst(ItemStack stack) {
		return stack.isOf(Items.AMETHYST_SHARD);
	}

	private static boolean hasSwitchCatalyst(ItemStack stack, int requiredCount) {
		return isSwitchCatalyst(stack) && stack.getCount() >= requiredCount;
	}

	private static boolean isCoreItem(ItemStack stack) {
		return toCoreId(stack) != null;
	}

	private static boolean isSupportedToolInput(ItemStack stack) {
		return resolveToolInput(stack).isPresent();
	}

	private static Optional<ToolInputResolution> resolveToolInput(ItemStack inputStack) {
		Item item = inputStack.getItem();

		if (item instanceof ModularToolItem modularToolItem) {
			ItemStack output = inputStack.copy();
			ToolState state = modularToolItem.getOrCreateState(output);
			return Optional.of(new ToolInputResolution(modularToolItem, output, state));
		}

		ToolState vanillaState = stateFromVanillaTool(item);
		if (vanillaState == null) {
			return Optional.empty();
		}

		ModularToolItem modularItem = modularItemFor(vanillaState.toolType());
		if (modularItem == null) {
			return Optional.empty();
		}

		ItemStack convertedOutput = new ItemStack(modularItem);
		modularItem.setState(convertedOutput, vanillaState);
		return Optional.of(new ToolInputResolution(modularItem, convertedOutput, vanillaState));
	}

	private static ToolState stateFromVanillaTool(Item item) {
		if (item == Items.WOODEN_PICKAXE) return ToolState.base(ToolType.PICKAXE, "wood", "wood");
		if (item == Items.STONE_PICKAXE) return ToolState.base(ToolType.PICKAXE, "stone", "wood");
		if (item == Items.IRON_PICKAXE) return ToolState.base(ToolType.PICKAXE, "iron", "wood");
		if (item == Items.GOLDEN_PICKAXE) return ToolState.base(ToolType.PICKAXE, "gold", "wood");
		if (item == Items.DIAMOND_PICKAXE) return ToolState.base(ToolType.PICKAXE, "diamond", "wood");
		if (item == Items.NETHERITE_PICKAXE) return ToolState.base(ToolType.PICKAXE, "netherite", "wood");

		if (item == Items.WOODEN_SWORD) return ToolState.base(ToolType.SWORD, "wood", "wood");
		if (item == Items.STONE_SWORD) return ToolState.base(ToolType.SWORD, "stone", "wood");
		if (item == Items.IRON_SWORD) return ToolState.base(ToolType.SWORD, "iron", "wood");
		if (item == Items.GOLDEN_SWORD) return ToolState.base(ToolType.SWORD, "gold", "wood");
		if (item == Items.DIAMOND_SWORD) return ToolState.base(ToolType.SWORD, "diamond", "wood");
		if (item == Items.NETHERITE_SWORD) return ToolState.base(ToolType.SWORD, "netherite", "wood");

		if (item == Items.WOODEN_AXE) return ToolState.base(ToolType.AXE, "wood", "wood");
		if (item == Items.STONE_AXE) return ToolState.base(ToolType.AXE, "stone", "wood");
		if (item == Items.IRON_AXE) return ToolState.base(ToolType.AXE, "iron", "wood");
		if (item == Items.GOLDEN_AXE) return ToolState.base(ToolType.AXE, "gold", "wood");
		if (item == Items.DIAMOND_AXE) return ToolState.base(ToolType.AXE, "diamond", "wood");
		if (item == Items.NETHERITE_AXE) return ToolState.base(ToolType.AXE, "netherite", "wood");

		if (item == Items.TRIDENT) return ToolState.base(ToolType.SPEAR, "diamond", "wood");
		String itemPath = Registries.ITEM.getId(item).getPath();
		if ("spear".equals(itemPath)) return ToolState.base(ToolType.SPEAR, "diamond", "wood");

		return null;
	}

	private static ModularToolItem modularItemFor(ToolType toolType) {
		Item item = switch (toolType) {
			case SWORD -> ModItems.MODULAR_SWORD;
			case AXE -> ModItems.MODULAR_AXE;
			case PICKAXE -> ModItems.MODULAR_PICKAXE;
			case SPEAR -> ModItems.MODULAR_SPEAR;
			case MACE -> ModItems.MODULAR_MACE;
		};

		return item instanceof ModularToolItem modularToolItem ? modularToolItem : null;
	}

	private static String toCoreId(ItemStack stack) {
		if (stack.isOf(ModItems.CORE_PLACEHOLDER)) {
			return ToolCoreRegistry.CORE_PLACEHOLDER_ID;
		}
		if (stack.isOf(ModItems.EFFICIENCY_CORE)) {
			return "kaismod:efficiency_core";
		}
		if (stack.isOf(ModItems.BLOOD_CORE)) {
			return "kaismod:blood_core";
		}
		if (stack.isOf(ModItems.BREAKER_CORE)) {
			return "kaismod:breaker_core";
		}
		if (stack.isOf(ModItems.PRECISION_CORE)) {
			return "kaismod:precision_core";
		}
		return null;
	}

	private static String toHeadMaterial(ItemStack stack) {
		if (stack.isIn(ItemTags.PLANKS)) {
			return "wood";
		}
		if (stack.isOf(Items.COBBLESTONE) || stack.isOf(Items.STONE)) {
			return "stone";
		}
		if (stack.isOf(Items.COPPER_INGOT)) {
			return "copper";
		}
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

	private record PendingOperation(
		ToolState state,
		boolean changed,
		int coreCost,
		int upgradeCost
	) {
	}

	private record ToolInputResolution(
		ModularToolItem modularItem,
		ItemStack outputStack,
		ToolState state
	) {
	}
}
