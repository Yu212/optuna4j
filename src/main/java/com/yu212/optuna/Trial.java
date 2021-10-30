package com.yu212.optuna;

import com.google.gson.JsonObject;

public class Trial {
    private final int number;
    private final JsonObject paramsJson;

    Trial(JsonObject json) {
        this.number = json.get("number").getAsInt();
        this.paramsJson = json.getAsJsonObject("params");
    }

    public int getNumber() {
        return number;
    }

    public int getParameterInt(String key) {
        return paramsJson.get(key).getAsInt();
    }

    public long getParameterLong(String key) {
        return paramsJson.get(key).getAsLong();
    }

    public double getParameterDouble(String key) {
        return paramsJson.get(key).getAsDouble();
    }

    public String getParameterString(String key) {
        return paramsJson.get(key).getAsString();
    }
}
