package com.pvpfps.client.settings;

import com.google.gson.JsonElement;

public abstract class Setting<T> {
    private final String name;
    private final T defaultValue;
    private T value;

    protected Setting(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public String getName() {
        return name;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public JsonElement toJson() {
        return serialize(get());
    }

    public void fromJson(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return;
        }
        set(deserialize(element));
    }

    protected abstract JsonElement serialize(T value);

    protected abstract T deserialize(JsonElement element);
}
