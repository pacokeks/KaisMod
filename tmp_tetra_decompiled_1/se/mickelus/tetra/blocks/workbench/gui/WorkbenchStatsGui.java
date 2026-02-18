/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Streams
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.common.ToolActions
 *  se.mickelus.mutil.gui.GuiAlignment
 *  se.mickelus.mutil.gui.GuiAttachment
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.animation.Applier
 *  se.mickelus.mutil.gui.animation.Applier$Opacity
 *  se.mickelus.mutil.gui.animation.Applier$TranslateY
 *  se.mickelus.mutil.gui.animation.KeyframeAnimation
 */
package se.mickelus.tetra.blocks.workbench.gui;

import com.google.common.collect.Streams;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolActions;
import se.mickelus.mutil.gui.GuiAlignment;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.gui.stats.AbilityStats;
import se.mickelus.tetra.gui.stats.GuiStats;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.bar.GuiStatBarTool;
import se.mickelus.tetra.gui.stats.bar.GuiStatBase;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
public class WorkbenchStatsGui
extends GuiElement {
    private static final List<GuiStatBase> staticBars = new ArrayList<GuiStatBase>();
    private static List<GuiStatBase> bars = new ArrayList<GuiStatBase>();
    private final GuiElement barGroup;

    public WorkbenchStatsGui(int x, int y) {
        super(x, y, 200, 52);
        this.barGroup = new GuiElement(0, 0, this.width, this.height);
        this.addChild(this.barGroup);
    }

    public static void initializeStaticBars() {
        Arrays.asList(new GuiStatBar[]{GuiStats.attackSpeed, GuiStats.drawStrength, GuiStats.drawSpeed, GuiStats.abilityDamage, GuiStats.abilityCooldown, GuiStats.reach, GuiStats.attackRange, GuiStats.durability, GuiStats.armor, GuiStats.toughness, GuiStats.shieldbreaker, GuiStats.blocking, GuiStats.bashing, GuiStats.throwable, GuiStats.ricochet, GuiStats.piercing, GuiStats.jab, GuiStats.quickslot, GuiStats.potionStorage, GuiStats.storage, GuiStats.quiver, GuiStats.booster, GuiStats.suspendSelf, GuiStats.sweeping, GuiStats.bleeding, GuiStats.backstab, GuiStats.armorPenetration, GuiStats.crushing, GuiStats.skewering, GuiStats.howling, GuiStats.knockback, AbilityStats.execute, GuiStats.severing, GuiStats.stun, AbilityStats.lunge, AbilityStats.slam, AbilityStats.puncture, AbilityStats.pry, AbilityStats.overpower, AbilityStats.reap, GuiStats.looting, GuiStats.fiery, GuiStats.smite, GuiStats.arthropod, GuiStats.unbreaking, GuiStats.mending, GuiStats.silkTouch, GuiStats.fortune, GuiStats.infinity, GuiStats.flame, GuiStats.punch, GuiStats.quickStrike, GuiStats.softStrike, GuiStats.fierySelf, GuiStats.enderReverb, GuiStats.sculkTaint, GuiStats.criticalStrike, GuiStats.medialLimit, GuiStats.lateralLimit, GuiStats.axialLimit, GuiStats.earthbind, GuiStats.reaching, GuiStats.janking, GuiStats.releaseLatch, GuiStats.overbowed, GuiStats.multishot, GuiStats.zoom, GuiStats.spread, GuiStats.velocity, GuiStats.sweeperRange, GuiStats.sweeperHorizontalSpread, GuiStats.sweeperVerticalSpread, GuiStats.percussionScanner, GuiStats.intuit, GuiStats.workable, GuiStats.stability, new GuiStatBarTool(0, 0, 59, TetraToolActions.hammer), new GuiStatBarTool(0, 0, 59, TetraToolActions.cut), new GuiStatBarTool(0, 0, 59, ToolActions.AXE_DIG), new GuiStatBarTool(0, 0, 59, ToolActions.PICKAXE_DIG), new GuiStatBarTool(0, 0, 59, ToolActions.SHOVEL_DIG), new GuiStatBarTool(0, 0, 59, ToolActions.SWORD_DIG), new GuiStatBarTool(0, 0, 59, TetraToolActions.pry), new GuiStatBarTool(0, 0, 59, ToolActions.HOE_DIG)}).forEach(WorkbenchStatsGui::addBar);
    }

    public static void addBar(GuiStatBase statBar) {
        staticBars.add(statBar);
    }

    public static void setDataBars(GuiStatBase ... statBars) {
        bars = Arrays.asList(statBars);
    }

    public void update(ItemStack itemStack, ItemStack previewStack, String slot, String improvement, Player player) {
        boolean shouldShow = !itemStack.m_41619_() && itemStack.m_41720_() instanceof IModularItem;
        this.setVisible(shouldShow);
        if (shouldShow) {
            this.barGroup.clearChildren();
            Streams.concat((Stream[])new Stream[]{staticBars.stream(), bars.stream()}).filter(bar -> bar.shouldShow(player, itemStack, previewStack, slot, improvement)).forEach(bar -> {
                bar.update(player, itemStack, previewStack, slot, improvement);
                this.realignBar((GuiStatBase)((Object)bar), this.barGroup.getNumChildren());
                this.barGroup.addChild((GuiElement)bar);
            });
        }
    }

    public void showAnimation() {
        if (this.isVisible()) {
            for (int i = 0; i < this.barGroup.getNumChildren(); ++i) {
                this.barGroup.getChild(i).updateAnimations();
                new KeyframeAnimation(100, this.barGroup.getChild(i)).withDelay(i * 60 + 400).applyTo(new Applier[]{new Applier.Opacity(0.0f, 1.0f), new Applier.TranslateY(3.0f, 0.0f, true)}).start();
            }
        }
    }

    public void realignBars() {
        List bars = this.barGroup.getChildren(GuiStatBase.class);
        for (int i = 0; i < bars.size(); ++i) {
            this.realignBar((GuiStatBase)((Object)bars.get(i)), i);
        }
    }

    private void realignBar(GuiStatBase bar, int index) {
        bar.setY(-17 * (index % 6 / 2) - 3);
        bar.setAttachmentAnchor(GuiAttachment.bottomCenter);
        int xOffset = 3 + index / 6 * 62;
        if (index % 2 == 0) {
            bar.setX(xOffset);
            bar.setAttachmentPoint(GuiAttachment.bottomLeft);
            bar.setAlignment(GuiAlignment.left);
        } else {
            bar.setX(-xOffset);
            bar.setAttachmentPoint(GuiAttachment.bottomRight);
            bar.setAlignment(GuiAlignment.right);
        }
    }
}
