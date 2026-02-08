package de.kai.kaismod.screen;

import de.kai.kaismod.KaisMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class UpgradeWorkbenchScreen extends HandledScreen<UpgradeWorkbenchScreenHandler> {
	private static final Identifier FORGED_CONTAINER_TEXTURE = Identifier.of(KaisMod.MOD_ID, "textures/gui/forged-container.png");
	private static final Identifier PLAYER_INVENTORY_TEXTURE = Identifier.of(KaisMod.MOD_ID, "textures/gui/player-inventory.png");

	private static final int BACKGROUND_WIDTH = 179;
	private static final int BACKGROUND_HEIGHT = 176;
	private static final int TEXTURE_SIZE = 256;
	private static final int FORGED_CONTAINER_X = 0;
	private static final int FORGED_CONTAINER_Y = -13;
	private static final int FORGED_CONTAINER_WIDTH = 179;
	private static final int FORGED_CONTAINER_HEIGHT = 128;
	private static final int PLAYER_INVENTORY_X = 0;
	private static final int PLAYER_INVENTORY_Y = 103;
	private static final int PLAYER_INVENTORY_WIDTH = 179;
	private static final int PLAYER_INVENTORY_HEIGHT = 106;

	public UpgradeWorkbenchScreen(UpgradeWorkbenchScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		backgroundWidth = BACKGROUND_WIDTH;
		backgroundHeight = BACKGROUND_HEIGHT;
		titleY = -10_000;
		playerInventoryTitleY = -10_000;
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int x = this.x;
		int y = this.y;
		context.drawTexture(
			RenderPipelines.GUI_TEXTURED,
			FORGED_CONTAINER_TEXTURE,
			x + FORGED_CONTAINER_X,
			y + FORGED_CONTAINER_Y,
			0.0F,
			0.0F,
			FORGED_CONTAINER_WIDTH,
			FORGED_CONTAINER_HEIGHT,
			TEXTURE_SIZE,
			TEXTURE_SIZE
		);
		context.drawTexture(
			RenderPipelines.GUI_TEXTURED,
			PLAYER_INVENTORY_TEXTURE,
			x + PLAYER_INVENTORY_X,
			y + PLAYER_INVENTORY_Y,
			0.0F,
			0.0F,
			PLAYER_INVENTORY_WIDTH,
			PLAYER_INVENTORY_HEIGHT,
			TEXTURE_SIZE,
			TEXTURE_SIZE
		);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);
		drawMouseoverTooltip(context, mouseX, mouseY);
	}
}
