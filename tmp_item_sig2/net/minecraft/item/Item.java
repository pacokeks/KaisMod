/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.collect.Maps
 *  com.mojang.logging.LogUtils
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DataResult
 *  net.fabricmc.fabric.api.item.v1.FabricItem
 *  net.minecraft.SharedConstants
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockState
 *  net.minecraft.component.ComponentMap
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.component.type.ConsumableComponent
 *  net.minecraft.component.type.EquippableComponent
 *  net.minecraft.component.type.KineticWeaponComponent
 *  net.minecraft.component.type.ToolComponent
 *  net.minecraft.component.type.TooltipDisplayComponent
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.entity.ItemEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.damage.DamageSource
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.inventory.StackReference
 *  net.minecraft.item.Item$Settings
 *  net.minecraft.item.Item$TooltipContext
 *  net.minecraft.item.ItemConvertible
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemUsageContext
 *  net.minecraft.item.Items
 *  net.minecraft.item.consume.UseAction
 *  net.minecraft.item.tooltip.TooltipData
 *  net.minecraft.item.tooltip.TooltipType
 *  net.minecraft.network.RegistryByteBuf
 *  net.minecraft.network.codec.PacketCodec
 *  net.minecraft.network.codec.PacketCodecs
 *  net.minecraft.registry.Registries
 *  net.minecraft.registry.RegistryKey
 *  net.minecraft.registry.RegistryKeys
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.registry.entry.RegistryEntry$Reference
 *  net.minecraft.resource.featuretoggle.FeatureSet
 *  net.minecraft.resource.featuretoggle.ToggleableFeature
 *  net.minecraft.screen.ScreenTexts
 *  net.minecraft.screen.slot.Slot
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.text.Text
 *  net.minecraft.util.ActionResult
 *  net.minecraft.util.ClickType
 *  net.minecraft.util.Hand
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.BlockView
 *  net.minecraft.world.RaycastContext
 *  net.minecraft.world.RaycastContext$FluidHandling
 *  net.minecraft.world.RaycastContext$ShapeType
 *  net.minecraft.world.World
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package net.minecraft.item;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.component.type.KineticWeaponComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.resource.featuretoggle.ToggleableFeature;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class Item
implements ToggleableFeature,
ItemConvertible,
FabricItem {
    public static final Codec<RegistryEntry<Item>> ENTRY_CODEC = Registries.ITEM.getEntryCodec().validate(entry -> entry.matches(Items.AIR.getRegistryEntry()) ? DataResult.error(() -> "Item must not be minecraft:air") : DataResult.success((Object)entry));
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Item>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry((RegistryKey)RegistryKeys.ITEM);
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Map<Block, Item> BLOCK_ITEMS = Maps.newHashMap();
    public static final Identifier BASE_ATTACK_DAMAGE_MODIFIER_ID = Identifier.ofVanilla((String)"base_attack_damage");
    public static final Identifier BASE_ATTACK_SPEED_MODIFIER_ID = Identifier.ofVanilla((String)"base_attack_speed");
    public static final int DEFAULT_MAX_COUNT = 64;
    public static final int MAX_MAX_COUNT = 99;
    public static final int ITEM_BAR_STEPS = 13;
    protected static final int DEFAULT_BLOCKS_ATTACKS_MAX_USE_TIME = 72000;
    private final RegistryEntry.Reference<Item> registryEntry = Registries.ITEM.createEntry((Object)this);
    private final ComponentMap components;
    private final @Nullable Item recipeRemainder;
    protected final String translationKey;
    private final FeatureSet requiredFeatures;

    public static int getRawId(Item item) {
        return item == null ? 0 : Registries.ITEM.getRawId((Object)item);
    }

    public static Item byRawId(int id) {
        return (Item)Registries.ITEM.get(id);
    }

    @Deprecated
    public static Item fromBlock(Block block) {
        return BLOCK_ITEMS.getOrDefault(block, Items.AIR);
    }

    public Item(Settings settings) {
        String string;
        this.translationKey = settings.getTranslationKey();
        this.components = settings.getValidatedComponents((Text)Text.translatable((String)this.translationKey), settings.getModelId());
        this.recipeRemainder = settings.recipeRemainder;
        this.requiredFeatures = settings.requiredFeatures;
        if (SharedConstants.isDevelopment && !(string = this.getClass().getSimpleName()).endsWith("Item")) {
            LOGGER.error("Item classes should end with Item and {} doesn't.", (Object)string);
        }
    }

    @Deprecated
    public RegistryEntry.Reference<Item> getRegistryEntry() {
        return this.registryEntry;
    }

    public ComponentMap getComponents() {
        return this.components;
    }

    public int getMaxCount() {
        return (Integer)this.components.getOrDefault(DataComponentTypes.MAX_STACK_SIZE, (Object)1);
    }

    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
    }

    public void onItemEntityDestroyed(ItemEntity entity) {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity user) {
        ToolComponent toolComponent = (ToolComponent)stack.get(DataComponentTypes.TOOL);
        if (toolComponent == null) return true;
        if (toolComponent.canDestroyBlocksInCreative()) return true;
        if (!(user instanceof PlayerEntity)) return true;
        PlayerEntity playerEntity = (PlayerEntity)user;
        if (playerEntity.getAbilities().creativeMode) return false;
        return true;
    }

    public Item asItem() {
        return this;
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }

    public float getMiningSpeed(ItemStack stack, BlockState state) {
        ToolComponent toolComponent = (ToolComponent)stack.get(DataComponentTypes.TOOL);
        return toolComponent != null ? toolComponent.getSpeed(state) : 1.0f;
    }

    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        ConsumableComponent consumableComponent = (ConsumableComponent)itemStack.get(DataComponentTypes.CONSUMABLE);
        if (consumableComponent != null) {
            return consumableComponent.consume((LivingEntity)user, itemStack, hand);
        }
        EquippableComponent equippableComponent = (EquippableComponent)itemStack.get(DataComponentTypes.EQUIPPABLE);
        if (equippableComponent != null && equippableComponent.swappable()) {
            return equippableComponent.equip(itemStack, user);
        }
        if (itemStack.contains(DataComponentTypes.BLOCKS_ATTACKS)) {
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        }
        KineticWeaponComponent kineticWeaponComponent = (KineticWeaponComponent)itemStack.get(DataComponentTypes.KINETIC_WEAPON);
        if (kineticWeaponComponent != null) {
            user.setCurrentHand(hand);
            kineticWeaponComponent.playSound((Entity)user);
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ConsumableComponent consumableComponent = (ConsumableComponent)stack.get(DataComponentTypes.CONSUMABLE);
        if (consumableComponent != null) {
            return consumableComponent.finishConsumption(world, user, stack);
        }
        return stack;
    }

    public boolean isItemBarVisible(ItemStack stack) {
        return stack.isDamaged();
    }

    public int getItemBarStep(ItemStack stack) {
        return MathHelper.clamp((int)Math.round(13.0f - (float)stack.getDamage() * 13.0f / (float)stack.getMaxDamage()), (int)0, (int)13);
    }

    public int getItemBarColor(ItemStack stack) {
        int i = stack.getMaxDamage();
        float f = Math.max(0.0f, ((float)i - (float)stack.getDamage()) / (float)i);
        return MathHelper.hsvToRgb((float)(f / 3.0f), (float)1.0f, (float)1.0f);
    }

    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        return false;
    }

    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        return false;
    }

    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return 0.0f;
    }

    @Deprecated
    public @Nullable DamageSource getDamageSource(LivingEntity user) {
        return null;
    }

    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    }

    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    }

    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        ToolComponent toolComponent = (ToolComponent)stack.get(DataComponentTypes.TOOL);
        if (toolComponent == null) {
            return false;
        }
        if (!world.isClient() && state.getHardness((BlockView)world, pos) != 0.0f && toolComponent.damagePerBlock() > 0) {
            stack.damage(toolComponent.damagePerBlock(), miner, EquipmentSlot.MAINHAND);
        }
        return true;
    }

    public boolean isCorrectForDrops(ItemStack stack, BlockState state) {
        ToolComponent toolComponent = (ToolComponent)stack.get(DataComponentTypes.TOOL);
        return toolComponent != null && toolComponent.isCorrectForDrops(state);
    }

    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return ActionResult.PASS;
    }

    public String toString() {
        return Registries.ITEM.getEntry((Object)this).getIdAsString();
    }

    public final ItemStack getRecipeRemainder() {
        return this.recipeRemainder == null ? ItemStack.EMPTY : new ItemStack((ItemConvertible)this.recipeRemainder);
    }

    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
    }

    public void onCraftByPlayer(ItemStack stack, PlayerEntity player) {
        this.onCraft(stack, player.getEntityWorld());
    }

    public void onCraft(ItemStack stack, World world) {
    }

    public UseAction getUseAction(ItemStack stack) {
        ConsumableComponent consumableComponent = (ConsumableComponent)stack.get(DataComponentTypes.CONSUMABLE);
        if (consumableComponent != null) {
            return consumableComponent.useAction();
        }
        if (stack.contains(DataComponentTypes.BLOCKS_ATTACKS)) {
            return UseAction.BLOCK;
        }
        if (stack.contains(DataComponentTypes.KINETIC_WEAPON)) {
            return UseAction.SPEAR;
        }
        return UseAction.NONE;
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        ConsumableComponent consumableComponent = (ConsumableComponent)stack.get(DataComponentTypes.CONSUMABLE);
        if (consumableComponent != null) {
            return consumableComponent.getConsumeTicks();
        }
        if (stack.contains(DataComponentTypes.BLOCKS_ATTACKS) || stack.contains(DataComponentTypes.KINETIC_WEAPON)) {
            return 72000;
        }
        return 0;
    }

    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        return false;
    }

    @Deprecated
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
    }

    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return Optional.empty();
    }

    @VisibleForTesting
    public final String getTranslationKey() {
        return this.translationKey;
    }

    public final Text getName() {
        return (Text)this.components.getOrDefault(DataComponentTypes.ITEM_NAME, (Object)ScreenTexts.EMPTY);
    }

    public Text getName(ItemStack stack) {
        return (Text)stack.getComponents().getOrDefault(DataComponentTypes.ITEM_NAME, (Object)ScreenTexts.EMPTY);
    }

    public boolean hasGlint(ItemStack stack) {
        return stack.hasEnchantments();
    }

    protected static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling) {
        Vec3d vec3d = player.getEyePos();
        Vec3d vec3d2 = vec3d.add(player.getRotationVector(player.getPitch(), player.getYaw()).multiply(player.getBlockInteractionRange()));
        return world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, fluidHandling, (Entity)player));
    }

    public boolean isUsedOnRelease(ItemStack stack) {
        return false;
    }

    public ItemStack getDefaultStack() {
        return new ItemStack((ItemConvertible)this);
    }

    public boolean canBeNested() {
        return true;
    }

    public FeatureSet getRequiredFeatures() {
        return this.requiredFeatures;
    }

    public boolean shouldShowOperatorBlockWarnings(ItemStack stack, @Nullable PlayerEntity player) {
        return false;
    }
}
