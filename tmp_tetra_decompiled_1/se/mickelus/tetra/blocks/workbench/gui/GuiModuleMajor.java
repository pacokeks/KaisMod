/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraftforge.registries.ForgeRegistries
 *  se.mickelus.mutil.gui.GuiAttachment
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiString
 *  se.mickelus.mutil.gui.GuiStringSmall
 *  se.mickelus.mutil.gui.GuiTextureOffset
 *  se.mickelus.mutil.gui.animation.Applier
 *  se.mickelus.mutil.gui.animation.Applier$Opacity
 *  se.mickelus.mutil.gui.animation.Applier$TranslateX
 *  se.mickelus.mutil.gui.animation.KeyframeAnimation
 *  se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.mutil.gui.GuiTextureOffset;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.tetra.blocks.workbench.gui.GuiModule;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleBackdrop;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleEnchantment;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleGlyph;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleImprovement;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.data.ImprovementData;

@ParametersAreNonnullByDefault
public class GuiModuleMajor
extends GuiModule {
    private GuiStringSmall slotString;
    private GuiHorizontalLayoutGroup improvementGroup;

    public GuiModuleMajor(int x, int y, GuiAttachment attachmentPoint, ItemStack itemStack, ItemStack previewStack, String slotKey, String slotName, ItemModuleMajor module, ItemModuleMajor previewModule, Consumer<String> slotClickHandler, BiConsumer<String, String> hoverHandler) {
        super(x, y, attachmentPoint, itemStack, previewStack, slotKey, slotName, module, previewModule, slotClickHandler, hoverHandler);
        this.height = 17;
        this.improvementGroup = new GuiHorizontalLayoutGroup(GuiAttachment.topRight.equals((Object)attachmentPoint) ? -17 : 19, "".equals(slotName) ? 12 : 13, 3, 1);
        this.improvementGroup.setAttachment(attachmentPoint);
        this.addChild((GuiElement)this.improvementGroup);
        if (module != null && previewModule != null) {
            this.setupImprovements(previewModule, previewStack, module, itemStack);
        }
    }

    public static String[] getImprovementUnion(ImprovementData[] improvements, ImprovementData[] previewImprovements) {
        return (String[])Stream.concat(Arrays.stream(improvements), Arrays.stream(previewImprovements)).map(improvement -> improvement.key).distinct().toArray(String[]::new);
    }

    public static List<Enchantment> getEnchantmentUnion(Set<Enchantment> enchantments, Set<Enchantment> previewEnchantments) {
        return Stream.concat(enchantments.stream(), previewEnchantments.stream()).distinct().collect(Collectors.toList());
    }

    @Override
    protected void setupChildren(String moduleName, GlyphData glyphData, String slotName, boolean tweakable) {
        this.backdrop = new GuiModuleBackdrop(1, 0, 0xFFFFFF);
        this.backdrop.setAttachment(this.attachmentPoint);
        this.addChild((GuiElement)this.backdrop);
        if (tweakable) {
            this.tweakingIndicator = new GuiTextureOffset(1, 0, 15, 15, 176, 32, GuiTextures.workbench);
            this.tweakingIndicator.setAttachment(this.attachmentPoint);
            this.addChild((GuiElement)this.tweakingIndicator);
        }
        this.moduleString = new GuiString(19, "".equals(slotName) ? 4 : 5, "");
        if (moduleName != null) {
            this.moduleString.setString(moduleName);
        } else {
            this.moduleString.setString(I18n.m_118938_((String)"item.tetra.modular.empty_slot", (Object[])new Object[0]));
        }
        if (GuiAttachment.topRight.equals((Object)this.attachmentPoint)) {
            this.moduleString.setX(-16);
        }
        this.moduleString.setAttachment(this.attachmentPoint);
        this.addChild((GuiElement)this.moduleString);
        this.slotString = new GuiStringSmall(19, 0, slotName);
        if (GuiAttachment.topRight.equals((Object)this.attachmentPoint)) {
            this.slotString.setX(-16);
        }
        this.slotString.setAttachment(this.attachmentPoint);
        this.addChild((GuiElement)this.slotString);
        this.width = this.moduleString.getWidth() + 19;
        if (glyphData != null) {
            this.glyph = new GuiModuleGlyph(0, 0, 16, 16, glyphData.tint, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
            if (GuiAttachment.topRight.equals((Object)this.attachmentPoint)) {
                this.glyph.setX(1);
            }
            this.glyph.setAttachment(this.attachmentPoint);
            this.addChild((GuiElement)this.glyph);
        }
    }

    @Override
    public void showAnimation(int offset) {
        if (this.isVisible()) {
            int direction = this.attachmentPoint == GuiAttachment.topLeft ? -2 : 2;
            new KeyframeAnimation(100, (GuiElement)this.backdrop).withDelay(offset * 80).applyTo(new Applier[]{new Applier.Opacity(0.0f, 1.0f), new Applier.TranslateX((float)direction, 0.0f, true)}).start();
            if (this.glyph != null) {
                new KeyframeAnimation(100, (GuiElement)this.glyph).withDelay(offset * 80 + 100).applyTo(new Applier[]{new Applier.Opacity(0.0f, 1.0f)}).start();
            }
            if (this.tweakingIndicator != null) {
                new KeyframeAnimation(100, (GuiElement)this.tweakingIndicator).withDelay(offset * 80 + 100).applyTo(new Applier[]{new Applier.Opacity(0.0f, 1.0f)}).start();
            }
            new KeyframeAnimation(100, (GuiElement)this.moduleString).withDelay(offset * 80 + 200).applyTo(new Applier[]{new Applier.Opacity(0.0f, 1.0f), new Applier.TranslateX((float)(direction * 2), 0.0f, true)}).start();
            new KeyframeAnimation(100, (GuiElement)this.slotString).withDelay(offset * 80 + 100).applyTo(new Applier[]{new Applier.Opacity(0.0f, 1.0f), new Applier.TranslateX((float)(direction * 2), 0.0f, true)}).start();
            for (int i = 0; i < this.improvementGroup.getNumChildren(); ++i) {
                GuiElement element = this.improvementGroup.getChild(i);
                element.setOpacity(0.0f);
                new KeyframeAnimation(100, element).withDelay(offset * 200 + 280 + i * 80).applyTo(new Applier[]{new Applier.Opacity(1.0f)}).start();
            }
        }
    }

    private void setupImprovements(ItemModuleMajor previewModule, ItemStack previewStack, ItemModuleMajor module, ItemStack itemStack) {
        String[] improvements;
        this.improvementGroup.clearChildren();
        for (String improvementKey : improvements = GuiModuleMajor.getImprovementUnion(module.getImprovements(itemStack), previewModule.getImprovements(previewStack))) {
            int currentValue = module.getImprovementLevel(itemStack, improvementKey);
            int previewValue = previewModule.getImprovementLevel(previewStack, improvementKey);
            int color = currentValue == -1 ? 0xAAFFAA : (previewValue == -1 ? 0xFFAAAA : (currentValue != previewValue ? 0xAAAAFF : module.getImprovement((ItemStack)itemStack, (String)improvementKey).glyph.tint));
            GuiModuleImprovement improvement = new GuiModuleImprovement(0, 0, improvementKey, previewValue, color, () -> this.hoverHandler.accept(this.slotKey, improvementKey), () -> {
                if (this.hasFocus()) {
                    this.hoverHandler.accept(this.slotKey, null);
                }
            });
            this.improvementGroup.addChild((GuiElement)improvement);
        }
        Map<Enchantment, Integer> currentEnchantments = module.getEnchantments(itemStack);
        Map<Enchantment, Integer> previewEnchantments = module.getEnchantments(previewStack);
        GuiModuleMajor.getEnchantmentUnion(currentEnchantments.keySet(), previewEnchantments.keySet()).forEach(enchantment -> {
            int currentLevel = currentEnchantments.getOrDefault(enchantment, 0);
            int previewLevel = previewEnchantments.getOrDefault(enchantment, 0);
            int color = currentLevel == 0 ? 0xAAFFAA : (previewLevel == 0 ? 0xFFAAAA : (currentLevel != previewLevel ? 0xAAAAFF : 0xFFFFFF));
            String enchantmentKey = "enchantment:" + ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString();
            this.improvementGroup.addChild((GuiElement)new GuiModuleEnchantment(0, 0, (Enchantment)enchantment, previewLevel, color, () -> this.hoverHandler.accept(this.slotKey, enchantmentKey), () -> {
                if (this.hasFocus()) {
                    this.hoverHandler.accept(this.slotKey, null);
                }
            }));
        });
        this.improvementGroup.forceLayout();
    }

    @Override
    protected void setColor(int color) {
        super.setColor(color);
        this.slotString.setColor(color);
        this.improvementGroup.setOpacity(color == 0x7F7F7F ? 0.5f : 1.0f);
    }
}
