package com.yu212.optuna;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final String storageName;

    public Storage(Path directory, String studyName) {
        this(directory.resolve(studyName + ".db"));
    }

    public Storage(Path path) {
        this("sqlite:///" + path.toAbsolutePath());
    }

    public Storage(String storageName) {
        this.storageName = storageName;
    }

    public List<Study> getStudies() throws OptunaException {
        OptunaCommand command = new OptunaCommand("studies")
                .addArgumnt("storage", getStorageName())
                .addArgumnt("format", "json");
        command.start();
        JsonArray outputJson = command.getOutputAsJsonArray();
        List<Study> studies = new ArrayList<>();
        for (JsonElement element : outputJson) {
            studies.add(new Study(this, element.getAsJsonObject()));
        }
        return studies;
    }

    public String getStorageName() {
        return storageName;
    }
}
