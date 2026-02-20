package de.kai.kaismod.data;

import de.kai.kaismod.balance.HeadMaterialTier;
import de.kai.kaismod.core.ToolCoreRegistry;

import java.util.Optional;

public final class ToolStateOperations {
	private ToolStateOperations() {
	}

	public static ToolStateOperationResult validate(ToolState state) {
		if (HeadMaterialTier.fromId(state.headMaterial()).isEmpty()) {
			return ToolStateOperationResult.failure(state, ToolStateError.UNKNOWN_HEAD_MATERIAL);
		}

		if (state.coreId().isPresent()) {
			String coreId = state.coreId().get();
			Optional<ToolStateOperationResult> failure = validateCoreCompatibility(state, coreId);
			if (failure.isPresent()) {
				return failure.get();
			}
		}

		return ToolStateOperationResult.success(state);
	}

	public static ToolStateOperationResult installCore(ToolState state, String coreId) {
		if (state.coreId().isPresent()) {
			return ToolStateOperationResult.failure(state, ToolStateError.CORE_ALREADY_INSTALLED);
		}

		Optional<ToolStateOperationResult> failure = validateCoreCompatibility(state, coreId);
		if (failure.isPresent()) {
			return failure.get();
		}

		return ToolStateOperationResult.success(new ToolState(
			state.toolType(),
			state.headMaterial(),
			state.handleMaterial(),
			Optional.of(coreId),
			state.headUpgradeLevel(),
			Math.max(1, state.coreUpgradeLevel())
		));
	}

	public static ToolStateOperationResult switchCore(ToolState state, String coreId) {
		if (state.coreId().isEmpty()) {
			return ToolStateOperationResult.failure(state, ToolStateError.CORE_NOT_INSTALLED);
		}
		if (state.coreId().get().equals(coreId)) {
			return ToolStateOperationResult.failure(state, ToolStateError.CORE_SAME_AS_CURRENT);
		}

		Optional<ToolStateOperationResult> failure = validateCoreCompatibility(state, coreId);
		if (failure.isPresent()) {
			return failure.get();
		}

		return ToolStateOperationResult.success(new ToolState(
			state.toolType(),
			state.headMaterial(),
			state.handleMaterial(),
			Optional.of(coreId),
			state.headUpgradeLevel(),
			state.coreUpgradeLevel() + 1
		));
	}

	public static ToolStateOperationResult upgradeHead(ToolState state, String materialId) {
		Optional<HeadMaterialTier> currentTier = HeadMaterialTier.fromId(state.headMaterial());
		if (currentTier.isEmpty()) {
			return ToolStateOperationResult.failure(state, ToolStateError.UNKNOWN_HEAD_MATERIAL);
		}

		Optional<HeadMaterialTier> targetTier = HeadMaterialTier.fromId(materialId);
		if (targetTier.isEmpty()) {
			return ToolStateOperationResult.failure(state, ToolStateError.UNKNOWN_HEAD_MATERIAL);
		}

		if (currentTier.get() == targetTier.get()) {
			return ToolStateOperationResult.failure(state, ToolStateError.INVALID_HEAD_UPGRADE_PATH);
		}

		return ToolStateOperationResult.success(new ToolState(
			state.toolType(),
			targetTier.get().id(),
			state.handleMaterial(),
			state.coreId(),
			state.headUpgradeLevel() + 1,
			state.coreUpgradeLevel()
		));
	}

	private static Optional<ToolStateOperationResult> validateCoreCompatibility(ToolState state, String coreId) {
		if (ToolCoreRegistry.find(coreId).isEmpty()) {
			return Optional.of(ToolStateOperationResult.failure(state, ToolStateError.UNKNOWN_CORE));
		}
		if (!ToolCoreRegistry.isCompatible(coreId, state.toolType())) {
			return Optional.of(ToolStateOperationResult.failure(state, ToolStateError.INCOMPATIBLE_CORE));
		}
		return Optional.empty();
	}
}
