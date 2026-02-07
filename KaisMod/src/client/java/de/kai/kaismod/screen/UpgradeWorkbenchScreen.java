package de.kai.kaismod.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class UpgradeWorkbenchScreen extends HandledScreen<UpgradeWorkbenchScreenHandler> {
	public UpgradeWorkbenchScreen(UpgradeWorkbenchScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		backgroundWidth = 176;
		backgroundHeight = 140;
		playerInventoryTitleY = backgroundHeight - 94;
	}

	@Override
	protected void init() {
		super.init();
		titleX = 8;
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int x = this.x;
		int y = this.y;
		context.fill(x, y, x + backgroundWidth, y + backgroundHeight, 0xFF2A2A2A);
		context.fill(x + 4, y + 16, x + backgroundWidth - 4, y + 52, 0xFF1C1C1C);
		context.fill(x + 7, y + 57, x + backgroundWidth - 7, y + backgroundHeight - 7, 0xFF171717);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);
		if (mouseY >= y + 2 && mouseY <= y + 14 && mouseX >= x && mouseX <= x + backgroundWidth) {
			context.drawTooltip(
				textRenderer,
				Text.literal("Werkzeug | Kern | Material | Confirm").formatted(Formatting.GRAY),
				mouseX,
				mouseY
			);
		}
		drawMouseoverTooltip(context, mouseX, mouseY);
	}
}
