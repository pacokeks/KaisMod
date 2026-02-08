package se.mickelus.tetra.core;

import se.mickelus.tetra.data.ToolType;

import java.util.Set;

public interface ToolCore {
	String id();

	Set<ToolType> compatibleToolTypes();

	String tooltipKey();

	int installationCost();

	int switchCost();

	default boolean isCompatible(ToolType toolType) {
		return compatibleToolTypes().contains(toolType);
	}
}
