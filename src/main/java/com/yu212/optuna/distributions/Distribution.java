package com.yu212.optuna.distributions;

import com.google.gson.JsonObject;

public abstract class Distribution {
    private final String key;
    private final String distributionName;

    protected Distribution(String key, String distributionName) {
        this.key = key;
        this.distributionName = distributionName;
    }

    public abstract JsonObject getAttributesJson();

    public static JsonObject creteSearchSpaceJson(Distribution... distributions) {
        JsonObject searchSpace = new JsonObject();
        for (Distribution distribution : distributions) {
            JsonObject value = new JsonObject();
            value.addProperty("name", distribution.distributionName);
            value.add("attributes", distribution.getAttributesJson());
            searchSpace.add(distribution.key, value);
        }
        return searchSpace;
    }
}
