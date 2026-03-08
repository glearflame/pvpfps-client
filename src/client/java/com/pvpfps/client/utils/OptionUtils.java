package com.pvpfps.client.utils;

import net.minecraft.client.option.GameOptions;

import java.lang.reflect.Method;

public final class OptionUtils {
    private OptionUtils() {
    }

    public static Object getOptionObject(GameOptions options, String getterName) {
        try {
            Method getter = options.getClass().getMethod(getterName);
            return getter.invoke(options);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    public static Object getOptionValue(GameOptions options, String getterName) {
        Object option = getOptionObject(options, getterName);
        if (option == null) {
            return null;
        }

        try {
            Method getValue = option.getClass().getMethod("getValue");
            return getValue.invoke(option);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    public static boolean setOptionValue(GameOptions options, String getterName, Object value) {
        Object option = getOptionObject(options, getterName);
        if (option == null) {
            return false;
        }

        try {
            Method setValue = option.getClass().getMethod("setValue", Object.class);
            setValue.invoke(option, value);
            return true;
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static boolean setEnumByName(GameOptions options, String getterName, String enumName) {
        Object current = getOptionValue(options, getterName);
        if (current == null || !current.getClass().isEnum()) {
            return false;
        }

        try {
            Enum enumValue = Enum.valueOf((Class<? extends Enum>) current.getClass(), enumName);
            return setOptionValue(options, getterName, enumValue);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
