package com.pvpfps.client.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class ColorSetting extends Setting<Integer> {
    public ColorSetting(String name, int defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected JsonElement serialize(Integer value) {
        return new JsonPrimitive(value);
    }

    @Override
    protected Integer deserialize(JsonElement element) {
        return element.getAsInt();
    }
}
