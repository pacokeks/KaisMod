package se.mickelus.tetra;

import se.mickelus.tetra.registry.RegistryBootstrap;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TetraFabricMod implements ModInitializer {
	public static final String MOD_ID = "tetra";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		RegistryBootstrap.initialize();
		LOGGER.info("TetraFabricMod wurde initialisiert.");
	}
}
