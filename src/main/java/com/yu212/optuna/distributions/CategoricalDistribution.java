package com.yu212.optuna.distributions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Arrays;

public class CategoricalDistribution extends Distribution {
    private final Object[] choices;

    public CategoricalDistribution(String key, String... choices) {
        super(key, "CategoricalDistribution");
        if (choices.length == 0) {
            throw new IllegalArgumentException("choices do not contain any elements");
        }
        this.choices = choices;
    }

    public CategoricalDistribution(String key, int... choices) {
        super(key, "CategoricalDistribution");
        if (choices.length == 0) {
            throw new IllegalArgumentException("choices do not contain any elements");
        }
        Integer[] wrapper = new Integer[choices.length];
        Arrays.setAll(wrapper, i -> choices[i]);
        this.choices = wrapper;
    }

    public CategoricalDistribution(String key, double... choices) {
        super(key, "CategoricalDistribution");
        if (choices.length == 0) {
            throw new IllegalArgumentException("choices do not contain any elements");
        }
        Double[] wrapper = new Double[choices.length];
        Arrays.setAll(wrapper, i -> choices[i]);
        this.choices = wrapper;
    }

    public boolean isSingle() {
        return choices.length == 1;
    }

    @Override
    public JsonObject getAttributesJson() {
        JsonObject attributesJson = new JsonObject();
        JsonArray choicesJson = new JsonArray();
        for (Object choice : choices) {
            choicesJson.add(String.valueOf(choice));
        }
        attributesJson.add("choices", choicesJson);
        return attributesJson;
    }
}
