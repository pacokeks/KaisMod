package se.mickelus.tetra.item;

import se.mickelus.tetra.data.ToolType;
import net.minecraft.item.Item;

public final class ModularMaceItem extends ModularToolItem {
	public ModularMaceItem(Item.Settings settings) {
		super(settings.maxCount(1), ToolType.MACE);
	}
}
