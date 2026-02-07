package de.kai.kaismod;

import de.kai.kaismod.registry.RegistryBootstrap;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class KaisMod implements ModInitializer {
	public static final String MOD_ID = "kaismod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		RegistryBootstrap.initialize();
		LOGGER.info("KaisMod wurde initialisiert.");
	}
}
