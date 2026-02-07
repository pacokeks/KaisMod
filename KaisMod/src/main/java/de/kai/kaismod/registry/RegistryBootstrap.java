package de.kai.kaismod.registry;

import de.kai.kaismod.network.ModNetworking;
import de.kai.kaismod.screen.ModScreenHandlers;

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
		ModNetworking.initialize();
		initialized = true;
	}
}
