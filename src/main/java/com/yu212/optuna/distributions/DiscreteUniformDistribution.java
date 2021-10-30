package com.yu212.optuna.distributions;

import com.google.gson.JsonObject;

public class DiscreteUniformDistribution extends Distribution {
    private final double low;
    private final double high;
    private final double q;

    public DiscreteUniformDistribution(String key, double low, double high, double q) {
        super(key, "DiscreteUniformDistribution");
        if (low > high) {
            throw new IllegalArgumentException("low > high");
        }
        this.low = low;
        this.high = high;
        this.q = q;
    }

    public boolean isSingle() {
        return low == high;
    }

    @Override
    public JsonObject getAttributesJson() {
        JsonObject attributesJson = new JsonObject();
        attributesJson.addProperty("low", low);
        attributesJson.addProperty("high", high);
        attributesJson.addProperty("q", q);
        return attributesJson;
    }
}
