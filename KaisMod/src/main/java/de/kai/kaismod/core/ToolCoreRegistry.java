package de.kai.kaismod.core;

import de.kai.kaismod.data.ToolType;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class ToolCoreRegistry {
	public static final String CORE_PLACEHOLDER_ID = "kaismod:core_placeholder";

	private static final Map<String, ToolCore> CORES = new LinkedHashMap<>();

	public static final ToolCore CORE_PLACEHOLDER = register(new BasicToolCore(
		CORE_PLACEHOLDER_ID,
		EnumSet.allOf(ToolType.class),
		"core.kaismod.core_placeholder.tooltip",
		1,
		2
	));
	public static final ToolCore EFFICIENCY_CORE = register(new BasicToolCore(
		"kaismod:efficiency_core",
		Set.of(ToolType.PICKAXE),
		"core.kaismod.efficiency_core.tooltip",
		2,
		4
	));
	public static final ToolCore BLOOD_CORE = register(new BasicToolCore(
		"kaismod:blood_core",
		Set.of(ToolType.SWORD),
		"core.kaismod.blood_core.tooltip",
		2,
		4
	));
	public static final ToolCore BREAKER_CORE = register(new BasicToolCore(
		"kaismod:breaker_core",
		Set.of(ToolType.AXE, ToolType.MACE),
		"core.kaismod.breaker_core.tooltip",
		2,
		4
	));
	public static final ToolCore PRECISION_CORE = register(new BasicToolCore(
		"kaismod:precision_core",
		Set.of(ToolType.SPEAR),
		"core.kaismod.precision_core.tooltip",
		2,
		4
	));

	private ToolCoreRegistry() {
	}

	private static ToolCore register(ToolCore core) {
		ToolCore previous = CORES.putIfAbsent(core.id(), core);
		if (previous != null) {
			throw new IllegalStateException("Duplicate core id: " + core.id());
		}
		return core;
	}

	public static Optional<ToolCore> find(String id) {
		return Optional.ofNullable(CORES.get(id));
	}

	public static boolean isCompatible(String id, ToolType toolType) {
		return find(id)
			.map(core -> core.isCompatible(toolType))
			.orElse(false);
	}

	public static Collection<ToolCore> values() {
		return Collections.unmodifiableCollection(CORES.values());
	}

	public static void initialize() {
		// static init
	}
}
