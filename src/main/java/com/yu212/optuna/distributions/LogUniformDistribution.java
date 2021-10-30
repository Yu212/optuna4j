package com.yu212.optuna.distributions;

import com.google.gson.JsonObject;

public class LogUniformDistribution extends Distribution {
    private final double low;
    private final double high;

    public LogUniformDistribution(String key, double low, double high) {
        super(key, "LogUniformDistribution");
        if (low > high) {
            throw new IllegalArgumentException("low > high");
        }
        if (low <= 0) {
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
