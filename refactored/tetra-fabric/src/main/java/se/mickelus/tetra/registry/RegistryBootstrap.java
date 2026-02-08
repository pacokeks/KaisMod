package se.mickelus.tetra.registry;

import se.mickelus.tetra.core.ToolCoreRegistry;
import se.mickelus.tetra.network.ModNetworking;
import se.mickelus.tetra.screen.ModScreenHandlers;

public final class RegistryBootstrap {
	private static boolean initialized;

	private RegistryBootstrap() {
	}

	public static void initialize() {
		if (initialized) {
			return;
		}

		ModItems.initialize();
		ModBlocks.initialize();
		ModScreenHandlers.initialize();
		ToolCoreRegistry.initialize();
		ModNetworking.initialize();
		initialized = true;
	}
}
