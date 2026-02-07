package de.kai.kaismod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import de.kai.kaismod.screen.ModScreenHandlers;
import de.kai.kaismod.screen.UpgradeWorkbenchScreen;

public final class KaisModClient implements ClientModInitializer {
	private static int overlayTicksRemaining;
	private static Text overlayText = Text.empty();

	@Override
	public void onInitializeClient() {
		HandledScreens.register(ModScreenHandlers.UPGRADE_WORKBENCH, UpgradeWorkbenchScreen::new);

		final String version = FabricLoader.getInstance()
			.getModContainer(KaisMod.MOD_ID)
			.map(container -> container.getMetadata().getVersion().getFriendlyString())
			.orElse("unknown");

		final Text loadMessage = Text.literal("KaisMod v" + version + " loaded").formatted(Formatting.GREEN);

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			client.execute(() -> {
				if (client.player != null) {
					client.player.sendMessage(loadMessage, false);
				}
				overlayText = loadMessage;
				overlayTicksRemaining = 100;
			});
		});

		HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
			if (overlayTicksRemaining <= 0) {
				return;
			}

			MinecraftClient client = MinecraftClient.getInstance();
			if (client.textRenderer == null) {
				return;
			}

			drawContext.drawTextWithShadow(client.textRenderer, overlayText, 6, 6, 0xFFFFFF);
			overlayTicksRemaining--;
		});

		KaisMod.LOGGER.info("KaisMod Client wurde initialisiert.");
	}
}
