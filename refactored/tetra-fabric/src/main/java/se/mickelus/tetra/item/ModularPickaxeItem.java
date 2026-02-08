package se.mickelus.tetra.item;

import se.mickelus.tetra.data.ToolType;
import net.minecraft.item.Item;

public final class ModularPickaxeItem extends ModularToolItem {
	public ModularPickaxeItem(Item.Settings settings) {
		super(settings.maxCount(1), ToolType.PICKAXE);
	}
}
