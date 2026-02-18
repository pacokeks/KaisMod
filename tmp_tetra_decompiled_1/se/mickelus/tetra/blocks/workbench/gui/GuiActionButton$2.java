/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  se.mickelus.mutil.gui.GuiClickable
 */
package se.mickelus.tetra.blocks.workbench.gui;

import se.mickelus.mutil.gui.GuiClickable;

class GuiActionButton.2
extends GuiClickable {
    GuiActionButton.2(int x, int y, int width, int height, Runnable onClickHandler) {
        super(x, y, width, height, onClickHandler);
    }

    protected void onFocus() {
        GuiActionButton.this.setBorderColors(0x8F8F6F);
    }

    protected void onBlur() {
        if (!GuiActionButton.this.labelClickable.hasFocus()) {
            GuiActionButton.this.setBorderColors(0x7F7F7F);
        }
    }
}
