package se.mickelus.tetra.registry;

import se.mickelus.tetra.TetraFabricMod;
import se.mickelus.tetra.block.BasicWorkbenchBlock;
import se.mickelus.tetra.block.UpgradeWorkbenchBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class ModBlocks {
	public static final Block BASIC_WORKBENCH = registerBlock(
		"basic_workbench",
		settings -> new BasicWorkbenchBlock(
			settings
				.mapColor(MapColor.OAK_TAN)
				.strength(2.5f)
				.sounds(BlockSoundGroup.WOOD)
		)
	);

	public static final Block FORGED_WORKBENCH = registerBlock(
		"forged_workbench",
		settings -> new UpgradeWorkbenchBlock(
			settings
				.mapColor(MapColor.OAK_TAN)
				.strength(2.5f)
				.sounds(BlockSoundGroup.WOOD)
		)
	);

	public static final Block UPGRADE_WORKBENCH = registerBlock(
		"upgrade_workbench",
		settings -> new UpgradeWorkbenchBlock(
			settings
				.mapColor(MapColor.OAK_TAN)
				.strength(2.5f)
				.sounds(BlockSoundGroup.WOOD)
		)
	);

	private ModBlocks() {
	}

	private static Block registerBlock(String path, Function<AbstractBlock.Settings, Block> blockFactory) {
		Identifier id = Identifier.of(TetraFabricMod.MOD_ID, path);
		RegistryKey<Block> blockRegistryKey = RegistryKey.of(RegistryKeys.BLOCK, id);
		Block block = blockFactory.apply(AbstractBlock.Settings.create().registryKey(blockRegistryKey));
		Registry.register(Registries.BLOCK, id, block);
		RegistryKey<Item> itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, id);
		Registry.register(
			Registries.ITEM,
			id,
			new BlockItem(
				block,
				new Item.Settings()
					.registryKey(itemRegistryKey)
					.useBlockPrefixedTranslationKey()
			)
		);
		return block;
	}

	public static void initialize() {
	}
}
