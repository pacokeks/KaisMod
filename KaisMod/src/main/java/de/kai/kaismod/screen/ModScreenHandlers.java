package de.kai.kaismod.screen;

import de.kai.kaismod.KaisMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public final class ModScreenHandlers {
	public static final ScreenHandlerType<UpgradeWorkbenchScreenHandler> UPGRADE_WORKBENCH = Registry.register(
		Registries.SCREEN_HANDLER,
		Identifier.of(KaisMod.MOD_ID, "upgrade_workbench"),
		new ScreenHandlerType<>(UpgradeWorkbenchScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
	);

	private ModScreenHandlers() {
	}

	public static void initialize() {
		// static init
	}
}
