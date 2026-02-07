package de.kai.kaismod.core;

import de.kai.kaismod.data.ToolType;
import java.util.Set;

public interface ToolCore {
	String id();
	Set<ToolType> compatibleToolTypes();
	String tooltipKey();
}
