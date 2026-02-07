package de.kai.kaismod.data;

import net.minecraft.nbt.NbtCompound;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public record ToolState(
	ToolType toolType,
	String headMaterial,
	String handleMaterial,
	Optional<String> coreId,
	int headUpgradeLevel,
	int coreUpgradeLevel
) {
	private static final String TOOL_TYPE_KEY = "tool_type";
	private static final String HEAD_MATERIAL_KEY = "head_material";
	private static final String HANDLE_MATERIAL_KEY = "handle_material";
	private static final String CORE_ID_KEY = "core_id";
	private static final String HEAD_UPGRADE_LEVEL_KEY = "head_upgrade_level";
	private static final String CORE_UPGRADE_LEVEL_KEY = "core_upgrade_level";

	public ToolState {
		Objects.requireNonNull(toolType, "toolType");
		Objects.requireNonNull(headMaterial, "headMaterial");
		Objects.requireNonNull(handleMaterial, "handleMaterial");
		Objects.requireNonNull(coreId, "coreId");
		if (headUpgradeLevel < 0) {
			throw new IllegalArgumentException("headUpgradeLevel must be >= 0");
		}
		if (coreUpgradeLevel < 0) {
			throw new IllegalArgumentException("coreUpgradeLevel must be >= 0");
		}
	}

	public static ToolState base(ToolType toolType, String headMaterial, String handleMaterial) {
		return new ToolState(toolType, headMaterial, handleMaterial, Optional.empty(), 0, 0);
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putString(TOOL_TYPE_KEY, toolType.name().toLowerCase(Locale.ROOT));
		nbt.putString(HEAD_MATERIAL_KEY, headMaterial);
		nbt.putString(HANDLE_MATERIAL_KEY, handleMaterial);
		coreId.ifPresent(value -> nbt.putString(CORE_ID_KEY, value));
		nbt.putInt(HEAD_UPGRADE_LEVEL_KEY, headUpgradeLevel);
		nbt.putInt(CORE_UPGRADE_LEVEL_KEY, coreUpgradeLevel);
		return nbt;
	}

	public static Optional<ToolState> fromNbt(NbtCompound nbt) {
		String typeName = nbt.getString(TOOL_TYPE_KEY).orElse("");
		String head = nbt.getString(HEAD_MATERIAL_KEY).orElse("");
		String handle = nbt.getString(HANDLE_MATERIAL_KEY).orElse("");

		if (typeName.isBlank() || head.isBlank() || handle.isBlank()) {
			return Optional.empty();
		}

		ToolType parsedType;
		try {
			parsedType = ToolType.valueOf(typeName.toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException ex) {
			return Optional.empty();
		}

		Optional<String> core = Optional.empty();
		core = nbt.getString(CORE_ID_KEY).filter(value -> !value.isBlank());

		int headLevel = Math.max(0, nbt.getInt(HEAD_UPGRADE_LEVEL_KEY).orElse(0));
		int coreLevel = Math.max(0, nbt.getInt(CORE_UPGRADE_LEVEL_KEY).orElse(0));
		return Optional.of(new ToolState(parsedType, head, handle, core, headLevel, coreLevel));
	}
}
