package com.yu212.optuna;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yu212.optuna.distributions.Distribution;

import java.util.*;

public class Study {
    private boolean deleted;
    private final String studyName;
    private final Storage storage;
    private final StudyDirection direction;
    private final Map<String, Object> userAttributes;

    Study(String studyName, Storage storage, StudyDirection direction) {
        this.deleted = false;
        this.studyName = studyName;
        this.storage = storage;
        this.direction = direction;
        this.userAttributes = new HashMap<>();
    }

    Study(Storage storage, JsonObject json) {
        this.deleted = false;
        this.studyName = json.get("name").getAsString();
        this.storage = storage;
        this.direction = StudyDirection.valueOf(json.get("direction").getAsJsonArray().get(0).getAsString());
        this.userAttributes = new HashMap<>();
    }

    public String getStudyName() {
        return studyName;
    }

    public StudyDirection getDirection() {
        return direction;
    }

    public Trial ask(Distribution... distributions) throws OptunaException {
        if (deleted) {
            throw new IllegalStateException("Study has been deleted");
        }
        JsonObject searchSpace = Distribution.creteSearchSpaceJson(distributions);
        OptunaCommand command = new OptunaCommand("ask")
                .addArgumnt("study-name", studyName)
                .addArgumnt("storage", storage.getStorageName())
                .addArgumnt("direction", direction)
                .addArgumnt("search-space", searchSpace)
                .addArgumnt("format", "json");
        command.start();
        JsonObject outputJson = command.getOutputAsJsonObject();
        return new Trial(outputJson);
    }

    public void tell(Trial trial, Number value) throws OptunaException {
        if (deleted) {
            throw new IllegalStateException("Study has been deleted");
        }
        OptunaCommand command = new OptunaCommand("tell")
                .addArgumnt("study-name", studyName)
                .addArgumnt("trial-number", trial.getNumber())
                .addArgumnt("storage", storage.getStorageName())
                .addArgumnt("values", value);
        command.start();
    }

    public void setUserAttribute(String key, Object value) throws OptunaException {
        if (deleted) {
            throw new IllegalStateException("Study has been deleted");
        }
        userAttributes.put(key, value);
        OptunaCommand command = new OptunaCommand("study", "set-user-attr")
                .addArgumnt("study-name", studyName)
                .addArgumnt("storage", storage.getStorageName())
                .addArgumnt("key", key)
                .addArgumnt("value", value);
        command.start();
    }

    public List<FrozenTrial> getTrials() throws OptunaException {
        if (deleted) {
            throw new IllegalStateException("Study has been deleted");
        }
        OptunaCommand command = new OptunaCommand("trials")
                .addArgumnt("study-name", studyName)
                .addArgumnt("storage", storage.getStorageName())
                .addArgumnt("format", "json");
        command.start();
        JsonArray outputJson = command.getOutputAsJsonArray();
        List<FrozenTrial> trials = new ArrayList<>();
        for (JsonElement element : outputJson) {
            trials.add(new FrozenTrial(element.getAsJsonObject()));
        }
        return trials;
    }

    public FrozenTrial getBestTrial() throws OptunaException {
        if (deleted) {
            throw new IllegalStateException("Study has been deleted");
        }
        OptunaCommand command = new OptunaCommand("best-trial")
                .addArgumnt("study-name", studyName)
                .addArgumnt("storage", storage.getStorageName())
                .addArgumnt("format", "json");
        command.start();
        JsonObject outputJson = command.getOutputAsJsonObject();
        return new FrozenTrial(outputJson);
    }

    public List<FrozenTrial> getBestTrials() throws OptunaException {
        if (deleted) {
            throw new IllegalStateException("Study has been deleted");
        }
        OptunaCommand command = new OptunaCommand("best-trials")
                .addArgumnt("study-name", studyName)
                .addArgumnt("storage", storage.getStorageName())
                .addArgumnt("format", "json");
        command.start();
        JsonArray outputJson = command.getOutputAsJsonArray();
        List<FrozenTrial> trials = new ArrayList<>();
        for (JsonElement element : outputJson) {
            trials.add(new FrozenTrial(element.getAsJsonObject()));
        }
        return trials;
    }

    public void delete() throws OptunaException {
        if (deleted) {
            throw new IllegalStateException("Study has been deleted");
        }
        OptunaCommand command = new OptunaCommand("delete-study")
                .addArgumnt("study-name", studyName)
                .addArgumnt("storage", storage.getStorageName());
        command.start();
        deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Map<String, Object> getUserAttributes() {
        return Collections.unmodifiableMap(userAttributes);
    }
}
