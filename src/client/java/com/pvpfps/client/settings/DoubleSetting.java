package com.pvpfps.client.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class DoubleSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double step;

    public DoubleSetting(String name, double defaultValue, double min, double max, double step) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
        this.step = Math.max(step, 0.0001D);
    }

    @Override
    public void set(Double value) {
        double clamped = Math.max(min, Math.min(max, value));
        double snapped = min + Math.round((clamped - min) / step) * step;
        super.set(snapped);
    }

    @Override
    protected JsonElement serialize(Double value) {
        return new JsonPrimitive(value);
    }

    @Override
    protected Double deserialize(JsonElement element) {
        return element.getAsDouble();
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }
}
