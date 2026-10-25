package com.pvpfps.client.modules.combat;

import com.pvpfps.client.PvpFpsClient;
import com.pvpfps.client.modules.Category;
import com.pvpfps.client.modules.Module;
import com.pvpfps.client.settings.BooleanSetting;
import com.pvpfps.client.settings.DoubleSetting;
import com.pvpfps.client.utils.OptionUtils;
import net.minecraft.client.MinecraftClient;

public class ZoomModule extends Module {
    private final DoubleSetting zoomFov = addSetting(new DoubleSetting("zoomFov", 30.0D, 5.0D, 90.0D, 1.0D));
    private final BooleanSetting smoothZoom = addSetting(new BooleanSetting("smoothZoom", false));
    private final DoubleSetting smoothSpeed = addSetting(new DoubleSetting("smoothSpeed", 0.2D, 0.05D, 1.0D, 0.05D));

    private Object previousFovValue;
    private boolean zooming;
    private double currentFov;

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
            Object current = getOptionValueSafe(client.options, "getFov");
            if (current != null) {
                previousFovValue = current;
                currentFov = toDouble(current);
                Object adjusted = convertNumber(current, zoomFov.get());
                if (setOptionValueSafe(client.options, "getFov", adjusted)) {
                    zooming = true;
                }
            }
            return;
        }

        if (!pressed && zooming) {
            restoreFov(client);
        }

        if (smoothZoom.get() && zooming) {
            applySmoothFov(client);
        }
    }

    @Override
    public void onDisable() {
        restoreFov(MinecraftClient.getInstance());
    }

    private void restoreFov(MinecraftClient client) {
        if (zooming && previousFovValue != null) {
            setOptionValueSafe(client.options, "getFov", previousFovValue);
        }

        zooming = false;
        previousFovValue = null;
        currentFov = 0.0D;
    }

    private void applySmoothFov(MinecraftClient client) {
        if (previousFovValue == null) {
            return;
        }

        double target = zoomFov.get();
        double diff = target - currentFov;
        if (Math.abs(diff) < 0.5D) {
            currentFov = target;
        } else {
            currentFov += diff * smoothSpeed.get();
        }

        Object converted = convertNumberFromDouble(currentFov);
        setOptionValueSafe(client.options, "getFov", converted);
    }

    private Object getOptionValueSafe(Object options, String getterName) {
        try {
            return OptionUtils.getOptionValue((net.minecraft.client.option.GameOptions) options, getterName);
        } catch (Exception ex) {
            System.err.println("[ZoomModule] Falha ao ler opcao " + getterName + ": " + ex.getMessage());
            return null;
        }
    }

    private boolean setOptionValueSafe(Object options, String getterName, Object value) {
        try {
            return OptionUtils.setOptionValue((net.minecraft.client.option.GameOptions) options, getterName, value);
        } catch (Exception ex) {
            System.err.println("[ZoomModule] Falha ao definir opcao " + getterName + ": " + ex.getMessage());
            return false;
        }
    }

    private static double toDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        }
        if (value instanceof Float) {
            return ((Float) value).doubleValue();
        }
        if (value instanceof Long) {
            return ((Long) value).doubleValue();
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        return 0.0D;
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

    private Object convertNumberFromDouble(double value) {
        if (previousFovValue instanceof Integer) {
            return (int) Math.round(value);
        }
        if (previousFovValue instanceof Float) {
            return (float) value;
        }
        if (previousFovValue instanceof Long) {
            return (long) value;
        }
        return value;
    }
}
