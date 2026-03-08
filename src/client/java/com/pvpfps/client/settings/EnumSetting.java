package com.pvpfps.client.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class EnumSetting<E extends Enum<E>> extends Setting<E> {
    private final Class<E> enumClass;

    public EnumSetting(String name, E defaultValue, Class<E> enumClass) {
        super(name, defaultValue);
        this.enumClass = enumClass;
    }

    @Override
    protected JsonElement serialize(E value) {
        return new JsonPrimitive(value.name());
    }

    @Override
    protected E deserialize(JsonElement element) {
        try {
            return Enum.valueOf(enumClass, element.getAsString());
        } catch (IllegalArgumentException ex) {
            return getDefaultValue();
        }
    }
}
