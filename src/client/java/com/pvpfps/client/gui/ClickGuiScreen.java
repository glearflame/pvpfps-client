package com.pvpfps.client.gui;

import com.pvpfps.client.PvpFpsClient;
import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.modules.hud.HudModule;
import com.pvpfps.client.settings.ColorSetting;
import com.pvpfps.client.settings.IntSetting;
import com.pvpfps.client.settings.Setting;
import com.pvpfps.client.utils.DrawUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {
    private static final int PANEL_WIDTH = 460;
    private static final int PANEL_HEIGHT = 280;
    private static final int CATEGORY_WIDTH = 115;

    private Category selectedCategory = Category.COMBAT;

    // Cores percorridas ao clicar no botao de cor de um ColorSetting.
    private static final int[] COLOR_CYCLE = {
            0xFFFF0000, // vermelho
            0xFF00FF00, // verde
            0xFF0000FF, // azul
            0xFFFFFF00, // amarelo
            0xFF00FFFF, // ciano
            0xFFFF00FF, // magenta
            0xFFFFFFFF, // branco
    };

    public ClickGuiScreen() {
        super(Text.literal("PVP FPS Client"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0xB0101115);

        int panelX = width / 2 - PANEL_WIDTH / 2;
        int panelY = height / 2 - PANEL_HEIGHT / 2;

        DrawUtils.drawPanel(context, panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0xEE181B22, 0xFF2A2D38);
        context.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + 22, 0xFF10131A);

        context.drawTextWithShadow(textRenderer, "PVP FPS Client", panelX + 10, panelY + 7, 0xFFFFFFFF);
        context.drawTextWithShadow(textRenderer, "Perfil: " + PvpFpsClient.getConfigManager().getActiveProfile(), panelX + PANEL_WIDTH - 150, panelY + 7, 0xFF9CA3AF);

        renderCategories(context, panelX, panelY, mouseX, mouseY);
        renderModules(context, panelX, panelY, mouseX, mouseY);

        context.drawTextWithShadow(textRenderer, "LMB: Toggle | Clique nos botoes X/Y para mover HUD | F6/F7: Perfil", panelX + 10, panelY + PANEL_HEIGHT - 12, 0xFF9CA3AF);

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderCategories(DrawContext context, int panelX, int panelY, int mouseX, int mouseY) {
        int startX = panelX + 8;
        int startY = panelY + 32;

        int i = 0;
        for (Category category : Category.values()) {
            int x1 = startX;
            int y1 = startY + i * 24;
            int x2 = x1 + CATEGORY_WIDTH - 16;
            int y2 = y1 + 20;

            boolean selected = category == selectedCategory;
            boolean hovered = isInside(mouseX, mouseY, x1, y1, x2, y2);
            int bg = selected ? 0xFF2563EB : hovered ? 0xCC242833 : 0xAA1D202A;
            DrawUtils.drawPanel(context, x1, y1, x2, y2, bg, 0xFF2D3748);
            context.drawTextWithShadow(textRenderer, category.getDisplayName(), x1 + 8, y1 + 6, 0xFFFFFFFF);
            i++;
        }
    }

    private void renderModules(DrawContext context, int panelX, int panelY, int mouseX, int mouseY) {
        int listX = panelX + CATEGORY_WIDTH;
        int listY = panelY + 32;
        int listW = PANEL_WIDTH - CATEGORY_WIDTH - 10;

        List<Module> modules = new ArrayList<>(PvpFpsClient.getModuleManager().getModulesByCategory(selectedCategory));
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            int y1 = listY + i * 22;
            int y2 = y1 + 19;
            int x1 = listX;
            int x2 = listX + listW;

            boolean hovered = isInside(mouseX, mouseY, x1, y1, x2, y2);
            int bg = module.isEnabled() ? 0xCC1E3A2A : 0xAA1B1E27;
            if (hovered) {
                bg = module.isEnabled() ? 0xFF27543A : 0xCC2A2E3A;
            }

            DrawUtils.drawPanel(context, x1, y1, x2, y2, bg, 0xFF2D3748);
            context.drawTextWithShadow(textRenderer, module.getName(), x1 + 8, y1 + 6, 0xFFFFFFFF);
            context.drawTextWithShadow(textRenderer, module.isEnabled() ? "ON" : "OFF", x2 - 28, y1 + 6, module.isEnabled() ? 0xFF86EFAC : 0xFFFCA5A5);

            ColorSetting colorSetting = getColorSetting(module);
            if (colorSetting != null && !(module instanceof HudModule)) {
                drawColorButton(context, x2 - 48, y1 + 3, colorSetting.get());
            }

            if (module instanceof HudModule hudModule) {
                renderHudPositionControls(context, hudModule, x2 - 62, y1 + 3);
            }
        }
    }

    private ColorSetting getColorSetting(Module module) {
        for (Setting<?> setting : module.getSettings()) {
            if (setting instanceof ColorSetting colorSetting) {
                return colorSetting;
            }
        }
        return null;
    }

    private void drawColorButton(DrawContext context, int x, int y, int color) {
        DrawUtils.drawPanel(context, x, y, x + 15, y + 14, color, 0xFF000000);
    }

    private void renderHudPositionControls(DrawContext context, HudModule module, int firstButtonX, int y) {
        context.drawTextWithShadow(textRenderer,
                "X:" + module.getXSetting().get() + " Y:" + module.getYSetting().get(),
                firstButtonX - 72,
                y + 7,
                0xFFD1D5DB);

        drawTinyButton(context, firstButtonX, y, "-X");
        drawTinyButton(context, firstButtonX + 16, y, "+X");
        drawTinyButton(context, firstButtonX + 32, y, "-Y");
        drawTinyButton(context, firstButtonX + 48, y, "+Y");
    }

    private void drawTinyButton(DrawContext context, int x, int y, String label) {
        DrawUtils.drawPanel(context, x, y, x + 15, y + 14, 0xAA111827, 0xFF374151);
        context.drawTextWithShadow(textRenderer, label, x + 2, y + 3, 0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int panelX = width / 2 - PANEL_WIDTH / 2;
        int panelY = height / 2 - PANEL_HEIGHT / 2;

        if (handleCategoryClick(panelX, panelY, mouseX, mouseY)) {
            return true;
        }

        if (handleModuleClick(panelX, panelY, mouseX, mouseY, button)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean handleCategoryClick(int panelX, int panelY, double mouseX, double mouseY) {
        int startX = panelX + 8;
        int startY = panelY + 32;

        int i = 0;
        for (Category category : Category.values()) {
            int x1 = startX;
            int y1 = startY + i * 24;
            int x2 = x1 + CATEGORY_WIDTH - 16;
            int y2 = y1 + 20;
            if (isInside(mouseX, mouseY, x1, y1, x2, y2)) {
                selectedCategory = category;
                return true;
            }
            i++;
        }

        return false;
    }

    private boolean handleModuleClick(int panelX, int panelY, double mouseX, double mouseY, int button) {
        int listX = panelX + CATEGORY_WIDTH;
        int listY = panelY + 32;
        int listW = PANEL_WIDTH - CATEGORY_WIDTH - 10;

        List<Module> modules = new ArrayList<>(PvpFpsClient.getModuleManager().getModulesByCategory(selectedCategory));
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            int y1 = listY + i * 22;
            int y2 = y1 + 19;
            int x1 = listX;
            int x2 = listX + listW;

            if (!isInside(mouseX, mouseY, x1, y1, x2, y2)) {
                continue;
            }

            if (module instanceof HudModule hudModule) {
                if (handleHudPositionButtons(hudModule, x2 - 62, y1 + 3, mouseX, mouseY)) {
                    return true;
                }
            }

            ColorSetting colorSetting = getColorSetting(module);
            if (colorSetting != null && !(module instanceof HudModule)) {
                int buttonX = x2 - 48;
                if (isInside(mouseX, mouseY, buttonX, y1 + 3, buttonX + 15, y1 + 17)) {
                    cycleColor(colorSetting);
                    return true;
                }
            }

            if (button == 0) {
                module.toggle();
                return true;
            }
        }

        return false;
    }

    private boolean handleHudPositionButtons(HudModule module, int firstButtonX, int y, double mouseX, double mouseY) {
        IntSetting xSetting = module.getXSetting();
        IntSetting ySetting = module.getYSetting();

        if (isInside(mouseX, mouseY, firstButtonX, y, firstButtonX + 15, y + 14)) {
            xSetting.set(xSetting.get() - 5);
            return true;
        }

        if (isInside(mouseX, mouseY, firstButtonX + 16, y, firstButtonX + 31, y + 14)) {
            xSetting.set(xSetting.get() + 5);
            return true;
        }

        if (isInside(mouseX, mouseY, firstButtonX + 32, y, firstButtonX + 47, y + 14)) {
            ySetting.set(ySetting.get() - 5);
            return true;
        }

        if (isInside(mouseX, mouseY, firstButtonX + 48, y, firstButtonX + 63, y + 14)) {
            ySetting.set(ySetting.get() + 5);
            return true;
        }

        return false;
    }

    private boolean isInside(double mouseX, double mouseY, int x1, int y1, int x2, int y2) {
        return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
    }

    private void cycleColor(ColorSetting setting) {
        int current = setting.get();
        int next = COLOR_CYCLE[0];
        boolean found = false;
        for (int i = 0; i < COLOR_CYCLE.length; i++) {
            if (COLOR_CYCLE[i] == current) {
                next = COLOR_CYCLE[(i + 1) % COLOR_CYCLE.length];
                found = true;
                break;
            }
        }
        setting.set(found ? next : COLOR_CYCLE[0]);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
