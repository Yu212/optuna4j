package com.yu212.optuna;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class FrozenTrial {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final int number;
    private final Number value;
    private final LocalDateTime dateTimeStart;
    private final LocalDateTime dateTimeComplete;
    private final TrialState state;

    FrozenTrial(JsonObject json) {
        this.number = json.get("number").getAsInt();
        this.state = TrialState.valueOf(json.get("state").getAsString());
        this.value = getAndMap(json, "value", JsonElement::getAsNumber);
        this.dateTimeStart = getAndMap(json, "datetime_start", element -> LocalDateTime.parse(element.getAsString(), FORMATTER));
        this.dateTimeComplete = getAndMap(json, "datetime_complete", element -> LocalDateTime.parse(element.getAsString(), FORMATTER));
    }

    private <T> T getAndMap(JsonObject json, String key, Function<JsonElement, T> mapper) {
        JsonElement element = json.get(key);
        return element.isJsonNull() ? null : mapper.apply(element);
    }

    public int getNumber() {
        return number;
    }

    public Number getValue() {
        return value;
    }

    public LocalDateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public LocalDateTime getDateTimeComplete() {
        return dateTimeComplete;
    }

    public TrialState getState() {
        return state;
    }
}
