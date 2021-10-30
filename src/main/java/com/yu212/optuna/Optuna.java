package com.yu212.optuna;

import java.nio.file.Path;

public class Optuna {
    public static Study createStudy(String studyName, Path directory, boolean skipIfExists, StudyDirection direction) throws OptunaException {
        return createStudy(studyName, new Storage(directory, studyName), skipIfExists, direction);
    }

    public static Study createStudy(String studyName, Storage storage, boolean skipIfExists, StudyDirection direction) throws OptunaException {
        OptunaCommand command = new OptunaCommand("create-study")
                .addArgumnt("direction", direction)
                .addArgumnt("storage", storage.getStorageName());
        if (studyName != null) {
            command.addArgumnt("study-name", studyName);
        }
        if (skipIfExists) {
            command.addArgumnt("skip-if-exists", null);
        }
        command.start();
        studyName = command.getOutput().strip();
        return new Study(studyName, storage, direction);
    }
}
