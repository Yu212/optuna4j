package com.yu212.optuna.distributions;

import com.google.gson.JsonObject;

public class IntUniformDistribution extends Distribution {
    private final int low;
    private final int high;
    private final int step;

    public IntUniformDistribution(String key, int low, int high) {
        this(key, low, high, 1);
    }

    public IntUniformDistribution(String key, int low, int high, int step) {
        super(key, "IntUniformDistribution");
        if (low > high) {
            throw new IllegalArgumentException("low > high");
        }
        if (step <= 0) {
            throw new IllegalArgumentException("step <= 0");
        }
        this.low = low;
        this.high = high;
        this.step = step;
    }

    public boolean isSingle() {
        return low == high;
    }

    @Override
    public JsonObject getAttributesJson() {
        JsonObject attributesJson = new JsonObject();
        attributesJson.addProperty("low", low);
        attributesJson.addProperty("high", high);
        attributesJson.addProperty("step", step);
        return attributesJson;
    }
}
