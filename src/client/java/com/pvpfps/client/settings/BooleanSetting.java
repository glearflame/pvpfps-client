package com.pvpfps.client.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, boolean defaultValue) {
        super(name, defaultValue);
    }

    @Override
    protected JsonElement serialize(Boolean value) {
        return new JsonPrimitive(value);
    }

    @Override
    protected Boolean deserialize(JsonElement element) {
        return element.getAsBoolean();
    }
}
