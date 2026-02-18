package de.kai.kaismod.screen;

import de.kai.kaismod.KaisMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class UpgradeWorkbenchScreen extends HandledScreen<UpgradeWorkbenchScreenHandler> {
	private static final Identifier WORKBENCH_TEXTURE = Identifier.of(KaisMod.MOD_ID, "textures/gui/workbench.png");
	private static final Identifier PLAYER_INVENTORY_TEXTURE = Identifier.of(KaisMod.MOD_ID, "textures/gui/player-inventory.png");

	private static final int BACKGROUND_WIDTH = 320;
	private static final int BACKGROUND_HEIGHT = 240;
	private static final int TEXTURE_SIZE = 256;
	private static final int WORKBENCH_X = 136;
	private static final int WORKBENCH_Y = 42;
	private static final int WORKBENCH_U = 0;
	private static final int WORKBENCH_V = 0;
	private static final int WORKBENCH_WIDTH = 48;
	private static final int WORKBENCH_HEIGHT = 48;
	private static final int PLAYER_INVENTORY_X = 72;
	private static final int PLAYER_INVENTORY_Y = 153;
	private static final int PLAYER_INVENTORY_WIDTH = 179;
	private static final int PLAYER_INVENTORY_HEIGHT = 106;
	private static final int SLOT_SIZE = 18;
	private static final int TOOL_SLOT_X = 152;
	private static final int TOOL_SLOT_Y = 58;
	private static final int CORE_SLOT_X = 161;
	private static final int CORE_SLOT_Y = 108;
	private static final int UPGRADE_SLOT_X = CORE_SLOT_X + 42;
	private static final int UPGRADE_SLOT_Y = CORE_SLOT_Y;
	private static final int CONFIRM_SLOT_X = UPGRADE_SLOT_X + 42;
	private static final int CONFIRM_SLOT_Y = CORE_SLOT_Y;
	private static final int DECOR_SIZE = 36;
	private static final int DECOR_PADDING = (DECOR_SIZE - SLOT_SIZE) / 2;
	private static final int DECOR_U = (TOOL_SLOT_X - WORKBENCH_X) - DECOR_PADDING;
	private static final int DECOR_V = (TOOL_SLOT_Y - WORKBENCH_Y) - DECOR_PADDING;

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
			WORKBENCH_TEXTURE,
			x + WORKBENCH_X,
			y + WORKBENCH_Y,
			WORKBENCH_U,
			WORKBENCH_V,
			WORKBENCH_WIDTH,
			WORKBENCH_HEIGHT,
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

		drawCustomSlots(context, x, y);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);
		drawMouseoverTooltip(context, mouseX, mouseY);
	}

	private void drawCustomSlots(DrawContext context, int x, int y) {
		drawSlotSurrounding(context, x + CORE_SLOT_X, y + CORE_SLOT_Y);
		drawSlotSurrounding(context, x + UPGRADE_SLOT_X, y + UPGRADE_SLOT_Y);
		drawSlotSurrounding(context, x + CONFIRM_SLOT_X, y + CONFIRM_SLOT_Y);
	}

	private void drawSlotSurrounding(DrawContext context, int left, int top) {
		int outerLeft = left - DECOR_PADDING;
		int outerTop = top - DECOR_PADDING;
		context.drawTexture(
			RenderPipelines.GUI_TEXTURED,
			WORKBENCH_TEXTURE,
			outerLeft,
			outerTop,
			DECOR_U,
			DECOR_V,
			DECOR_SIZE,
			DECOR_SIZE,
			TEXTURE_SIZE,
			TEXTURE_SIZE
		);
	}
}
