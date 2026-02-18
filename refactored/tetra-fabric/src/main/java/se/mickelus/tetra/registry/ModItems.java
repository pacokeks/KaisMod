package se.mickelus.tetra.registry;

import se.mickelus.tetra.TetraFabricMod;
import se.mickelus.tetra.item.ModularAxeItem;
import se.mickelus.tetra.item.ModularMaceItem;
import se.mickelus.tetra.item.ModularPickaxeItem;
import se.mickelus.tetra.item.ModularSpearItem;
import se.mickelus.tetra.item.ModularSwordItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class ModItems {
	public static final Item HAMMER = register("hammer", Item::new);
	public static final Item CORE_PLACEHOLDER = register("core_placeholder", Item::new);
	public static final Item MODULAR_SWORD = register("modular_sword", ModularSwordItem::new);
	public static final Item MODULAR_AXE = register("modular_axe", ModularAxeItem::new);
	public static final Item MODULAR_PICKAXE = register("modular_pickaxe", ModularPickaxeItem::new);
	public static final Item MODULAR_SPEAR = register("modular_spear", ModularSpearItem::new);
	public static final Item MODULAR_MACE = register("modular_mace", ModularMaceItem::new);

	private ModItems() {
	}

	private static Item register(String path, Function<Item.Settings, Item> itemFactory) {
		Identifier id = Identifier.of(TetraFabricMod.MOD_ID, path);
		RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, id);
		Item item = itemFactory.apply(new Item.Settings().registryKey(registryKey));
		return Registry.register(Registries.ITEM, id, item);
	}

	public static void initialize() {
	}
}
