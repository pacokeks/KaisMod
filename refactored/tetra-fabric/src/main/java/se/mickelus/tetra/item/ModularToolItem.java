package se.mickelus.tetra.item;

import se.mickelus.tetra.data.ToolState;
import se.mickelus.tetra.data.ToolStateOperations;
import se.mickelus.tetra.data.ToolType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public abstract class ModularToolItem extends Item {
	private static final String TOOL_STATE_NBT_KEY = "tetra_tool_state";

	private final ToolType toolType;

	protected ModularToolItem(Settings settings, ToolType toolType) {
		super(settings);
		this.toolType = toolType;
	}

	public ToolType getToolType() {
		return toolType;
	}

	public ToolState createDefaultState() {
		return ToolState.base(toolType, "stone", "wood");
	}

	public Optional<ToolState> getState(ItemStack stack) {
		NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
		if (customData == null) {
			return Optional.empty();
		}

		NbtCompound root = customData.copyNbt();
		if (!root.contains(TOOL_STATE_NBT_KEY)) {
			return Optional.empty();
		}

		Optional<ToolState> parsedState = root
			.getCompound(TOOL_STATE_NBT_KEY)
			.flatMap(ToolState::fromNbt);
		if (parsedState.isEmpty()) {
			return Optional.empty();
		}

		return parsedState
			.filter(state -> state.toolType() == toolType)
			.filter(state -> ToolStateOperations.validate(state).isSuccess());
	}

	public ToolState getOrCreateState(ItemStack stack) {
		return getState(stack).orElseGet(() -> {
			ToolState defaultState = createDefaultState();
			setState(stack, defaultState);
			return defaultState;
		});
	}

	public void setState(ItemStack stack, ToolState state) {
		if (state.toolType() != toolType) {
			throw new IllegalArgumentException("ToolState type does not match item type");
		}

		NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
		NbtCompound root = customData != null ? customData.copyNbt() : new NbtCompound();
		root.put(TOOL_STATE_NBT_KEY, state.toNbt());
		stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(root));
	}

	@Override
	public ItemStack getDefaultStack() {
		ItemStack stack = super.getDefaultStack();
		setState(stack, createDefaultState());
		return stack;
	}
}
