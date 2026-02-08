package se.mickelus.tetra;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import se.mickelus.tetra.screen.ModScreenHandlers;
import se.mickelus.tetra.screen.UpgradeWorkbenchScreen;

public final class TetraFabricModClient implements ClientModInitializer {
	private static int overlayTicksRemaining;
	private static Text overlayText = Text.empty();

	@Override
	public void onInitializeClient() {
		HandledScreens.register(ModScreenHandlers.UPGRADE_WORKBENCH, UpgradeWorkbenchScreen::new);

		final String version = FabricLoader.getInstance()
			.getModContainer(TetraFabricMod.MOD_ID)
			.map(container -> container.getMetadata().getVersion().getFriendlyString())
			.orElse("unknown");

		final Text loadMessage = Text.literal("TetraFabricMod v" + version + " loaded").formatted(Formatting.GREEN);

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

		TetraFabricMod.LOGGER.info("TetraFabricMod Client wurde initialisiert.");
	}
}
