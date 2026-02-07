package de.kai.kaismod.item;

import de.kai.kaismod.data.ToolType;
import net.minecraft.item.Item;

public final class ModularSpearItem extends ModularToolItem {
	public ModularSpearItem(Item.Settings settings) {
		super(settings.maxCount(1), ToolType.SPEAR);
	}
}
