package com.pvpfps.client.modules.combat;

import com.pvpfps.client.PvpFpsClient;
import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.DoubleSetting;
import com.pvpfps.client.utils.OptionUtils;
import net.minecraft.client.MinecraftClient;

public class ZoomModule extends Module {
    private final DoubleSetting zoomFov = addSetting(new DoubleSetting("zoomFov", 30.0D, 5.0D, 90.0D, 1.0D));

    private Object previousFovValue;
    private boolean zooming;

    public ZoomModule() {
        super("Zoom", "Zoom estilo OptiFine (segure a tecla de zoom).", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (PvpFpsClient.ZOOM_KEY == null) {
            return;
        }

        boolean pressed = PvpFpsClient.ZOOM_KEY.isPressed();
        if (pressed && !zooming) {
            Object current = OptionUtils.getOptionValue(client.options, "getFov");
            if (current != null) {
                previousFovValue = current;
                Object adjusted = convertNumber(current, zoomFov.get());
                OptionUtils.setOptionValue(client.options, "getFov", adjusted);
                zooming = true;
            }
            return;
        }

        if (!pressed && zooming) {
            restoreFov(client);
        }
    }

    @Override
    public void onDisable() {
        restoreFov(MinecraftClient.getInstance());
    }

    private void restoreFov(MinecraftClient client) {
        if (zooming && previousFovValue != null) {
            OptionUtils.setOptionValue(client.options, "getFov", previousFovValue);
        }

        zooming = false;
        previousFovValue = null;
    }

    private Object convertNumber(Object current, double targetValue) {
        if (current instanceof Integer) {
            return (int) Math.round(targetValue);
        }

        if (current instanceof Float) {
            return (float) targetValue;
        }

        if (current instanceof Long) {
            return (long) targetValue;
        }

        return targetValue;
    }
}
