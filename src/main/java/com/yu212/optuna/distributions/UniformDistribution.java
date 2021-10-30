package com.yu212.optuna.distributions;

import com.google.gson.JsonObject;

public class UniformDistribution extends Distribution {
    private final double low;
    private final double high;

    public UniformDistribution(String key, double low, double high) {
        super(key, "UniformDistribution");
        if (low > high) {
            throw new IllegalArgumentException("low > high");
        }
        this.low = low;
        this.high = high;
    }

    public boolean isSingle() {
        return low == high;
    }

    @Override
    public JsonObject getAttributesJson() {
        JsonObject attributesJson = new JsonObject();
        attributesJson.addProperty("low", low);
        attributesJson.addProperty("high", high);
        return attributesJson;
    }
}
