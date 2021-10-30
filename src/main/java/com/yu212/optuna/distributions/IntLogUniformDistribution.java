package com.yu212.optuna.distributions;

import com.google.gson.JsonObject;

public class IntLogUniformDistribution extends Distribution {
    private final int low;
    private final int high;

    public IntLogUniformDistribution(String key, int low, int high) {
        super(key, "IntLogUniformDistribution");
        if (low > high) {
            throw new IllegalArgumentException("low > high");
        }
        if (low < 1) {
            throw new IllegalArgumentException("low < 1");
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
