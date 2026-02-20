package de.kai.kaismod.item;

import de.kai.kaismod.balance.ToolStatCalculator;
import de.kai.kaismod.data.ToolState;
import de.kai.kaismod.data.ToolStateOperations;
import de.kai.kaismod.data.ToolType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Locale;
import java.util.function.Consumer;

import java.util.Optional;

public abstract class ModularToolItem extends Item {
	private static final String TOOL_STATE_NBT_KEY = "kaismod_tool_state";

	private final ToolType toolType;

	protected ModularToolItem(Settings settings, ToolType toolType) {
		super(settings);
		this.toolType = toolType;
	}

	public ToolType getToolType() {
		return toolType;
	}

	public ToolState createDefaultState() {
		return ToolState.base(toolType, "stone", "wood");
	}

	public Optional<ToolState> getState(ItemStack stack) {
		NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
		if (customData == null) {
			return Optional.empty();
		}

		NbtCompound root = customData.copyNbt();
		if (!root.contains(TOOL_STATE_NBT_KEY)) {
			return Optional.empty();
		}

		Optional<ToolState> parsedState = root
			.getCompound(TOOL_STATE_NBT_KEY)
			.flatMap(ToolState::fromNbt);
		if (parsedState.isEmpty()) {
			return Optional.empty();
		}

		return parsedState
			.filter(state -> state.toolType() == toolType)
			.filter(state -> ToolStateOperations.validate(state).isSuccess());
	}

	public ToolState getOrCreateState(ItemStack stack) {
		return getState(stack).orElseGet(() -> {
			ToolState defaultState = createDefaultState();
			setState(stack, defaultState);
			return defaultState;
		});
	}

	public void setState(ItemStack stack, ToolState state) {
		if (state.toolType() != toolType) {
			throw new IllegalArgumentException("ToolState type does not match item type");
		}

		NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
		NbtCompound root = customData != null ? customData.copyNbt() : new NbtCompound();
		root.put(TOOL_STATE_NBT_KEY, state.toNbt());
		stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(root));
	}

	@Override
	public ItemStack getDefaultStack() {
		ItemStack stack = super.getDefaultStack();
		setState(stack, createDefaultState());
		return stack;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
		ToolState state = getOrCreateState(stack);
		double attackDamage = ToolStatCalculator.attackDamage(toolType, state);
		double attackSpeed = ToolStatCalculator.attackSpeed(toolType, state);

		textConsumer.accept(Text.literal(String.format(Locale.ROOT, "Attack Damage: %.2f", attackDamage)).formatted(Formatting.GRAY));
		textConsumer.accept(Text.literal(String.format(Locale.ROOT, "Attack Speed: %.2f", attackSpeed)).formatted(Formatting.GRAY));

		String coreName = state.coreId().map(this::shortCoreName).orElse("none");
		textConsumer.accept(Text.literal("Core: " + coreName).formatted(Formatting.DARK_AQUA));
		textConsumer.accept(Text.literal("Kopf: " + state.headMaterial()).formatted(Formatting.BLUE));
		textConsumer.accept(Text.literal("Griff: " + state.handleMaterial()).formatted(Formatting.BLUE));
	}

	@Override
	public float getMiningSpeed(ItemStack stack, BlockState state) {
		ToolState toolState = getOrCreateState(stack);
		float speed = ToolStatCalculator.miningSpeed(toolType, toolState);

		return switch (toolType) {
			case PICKAXE -> state.isIn(BlockTags.PICKAXE_MINEABLE) ? speed : 1.0f;
			case AXE -> state.isIn(BlockTags.AXE_MINEABLE) ? speed : 1.0f;
			default -> 1.0f;
		};
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (world instanceof ServerWorld && !state.isAir()) {
			stack.damage(ToolStatCalculator.durabilityLossOnMine(toolType), miner, EquipmentSlot.MAINHAND);
		}
		return true;
	}

	@Override
	public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		ToolState state = getOrCreateState(stack);
		float totalDamage = (float) ToolStatCalculator.attackDamage(toolType, state);
		float extraDamage = Math.max(0.0f, totalDamage - 1.0f);

		if (extraDamage > 0.0f) {
			DamageSource source = attacker instanceof PlayerEntity player
				? attacker.getDamageSources().playerAttack(player)
				: attacker.getDamageSources().mobAttack(attacker);
			if (attacker.getEntityWorld() instanceof ServerWorld serverWorld) {
				target.damage(serverWorld, source, extraDamage);
			}
		}

		if (attacker instanceof PlayerEntity player) {
			double attackSpeed = ToolStatCalculator.attackSpeed(toolType, state);
			int cooldownTicks = Math.max(0, (int) Math.round(20.0 / Math.max(0.1, attackSpeed)) - 5);
			if (cooldownTicks > 0) {
				player.getItemCooldownManager().set(stack, cooldownTicks);
			}
		}

		stack.damage(ToolStatCalculator.durabilityLossOnHit(toolType), attacker, EquipmentSlot.MAINHAND);
	}

	private String shortCoreName(String coreId) {
		int separator = coreId.indexOf(':');
		String value = separator >= 0 && separator + 1 < coreId.length() ? coreId.substring(separator + 1) : coreId;
		return value.replace('_', ' ');
	}
}
