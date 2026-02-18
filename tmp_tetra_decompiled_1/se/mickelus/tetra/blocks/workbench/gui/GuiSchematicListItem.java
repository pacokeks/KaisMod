/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.network.chat.Component
 *  se.mickelus.mutil.gui.GuiClickable
 *  se.mickelus.mutil.gui.GuiElement
 *  se.mickelus.mutil.gui.GuiString
 *  se.mickelus.mutil.gui.GuiTexture
 */
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Collections;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.chat.Component;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.schematic.SchematicRarity;
import se.mickelus.tetra.module.schematic.SchematicType;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class GuiSchematicListItem
extends GuiClickable {
    private final GuiString label;
    private final SchematicRarity rarity;
    private GuiTexture border;
    private GuiTexture glyph;

    public GuiSchematicListItem(int x, int y, UpgradeSchematic schematic, Runnable onClickHandler) {
        this(x, y, 109, schematic, onClickHandler);
    }

    public GuiSchematicListItem(int x, int y, int width, UpgradeSchematic schematic, Runnable onClickHandler) {
        super(x, y, width, 14, onClickHandler);
        this.rarity = schematic.getRarity();
        this.label = new GuiString(16, 3, width - 16, schematic.getName());
        this.label.setColor(this.rarity.tint);
        this.addChild((GuiElement)this.label);
        GlyphData glyphData = schematic.getGlyph();
        if (schematic.getType() == SchematicType.major) {
            this.border = new GuiTexture(0, 2, 16, 9, 52, 3, GuiTextures.workbench);
            this.glyph = new GuiTexture(-1, -1, 16, 16, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
        } else if (schematic.getType() == SchematicType.minor) {
            this.border = new GuiTexture(2, 1, 11, 11, 68, 0, GuiTextures.workbench);
            this.glyph = new GuiTexture(4, 3, 8, 8, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
        } else if (schematic.getType() == SchematicType.improvement) {
            this.border = new GuiTexture(0, 2, 16, 9, 52, 3, GuiTextures.workbench);
            this.glyph = new GuiTexture(-1, -1, 16, 16, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
        } else if (schematic.getType() == SchematicType.other) {
            this.glyph = new GuiTexture(-1, -1, 16, 16, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
        }
        if (this.border != null) {
            this.border.setOpacity(0.3f);
            this.border.setColor(this.rarity.tint);
            this.addChild((GuiElement)this.border);
        }
        this.glyph.setColor(this.rarity.tint);
        this.addChild((GuiElement)this.glyph);
        if (schematic.getType() == SchematicType.improvement) {
            this.addChild((GuiElement)new GuiTexture(7, 7, 7, 7, 68, 16, GuiTextures.workbench).setColor(0x7F7F7F));
        }
    }

    protected void onFocus() {
        if (this.border != null) {
            this.border.setOpacity(0.6f);
        }
        this.label.setColor(0xFFFFCC);
    }

    protected void onBlur() {
        if (this.border != null) {
            this.border.setOpacity(0.3f);
        }
        this.label.setColor(this.rarity.tint);
    }

    public List<Component> getTooltipLines() {
        if (this.hasFocus() && this.rarity.equals((Object)SchematicRarity.temporary)) {
            return Collections.singletonList(Component.m_237115_((String)"tetra.workbench.schematic_list.temporary"));
        }
        return null;
    }
}
