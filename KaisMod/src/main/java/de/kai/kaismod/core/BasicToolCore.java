package de.kai.kaismod.core;

import de.kai.kaismod.data.ToolType;

import java.util.Objects;
import java.util.Set;

public record BasicToolCore(
	String id,
	Set<ToolType> compatibleToolTypes,
	String tooltipKey,
	int installationCost,
	int switchCost
) implements ToolCore {
	public BasicToolCore {
		Objects.requireNonNull(id, "id");
		Objects.requireNonNull(compatibleToolTypes, "compatibleToolTypes");
		Objects.requireNonNull(tooltipKey, "tooltipKey");
		if (compatibleToolTypes.isEmpty()) {
			throw new IllegalArgumentException("compatibleToolTypes must not be empty");
		}
		if (installationCost < 0) {
			throw new IllegalArgumentException("installationCost must be >= 0");
		}
		if (switchCost < 0) {
			throw new IllegalArgumentException("switchCost must be >= 0");
		}
	}
}
