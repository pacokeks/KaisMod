package de.kai.kaismod.item;

import de.kai.kaismod.data.ToolType;
import net.minecraft.item.Item;

public final class ModularAxeItem extends ModularToolItem {
	public ModularAxeItem(Item.Settings settings) {
		super(settings.maxCount(1), ToolType.AXE);
	}
}
