package com.pvpfps.client.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class IntSetting extends Setting<Integer> {
    private final int min;
    private final int max;
    private final int step;

    public IntSetting(String name, int defaultValue, int min, int max, int step) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
        this.step = Math.max(step, 1);
    }

    @Override
    public void set(Integer value) {
        int clamped = Math.max(min, Math.min(max, value));
        int snapped = min + ((clamped - min) / step) * step;
        super.set(snapped);
    }

    @Override
    protected JsonElement serialize(Integer value) {
        return new JsonPrimitive(value);
    }

    @Override
    protected Integer deserialize(JsonElement element) {
        return element.getAsInt();
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getStep() {
        return step;
    }
}
