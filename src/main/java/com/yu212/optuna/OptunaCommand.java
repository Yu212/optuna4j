package com.yu212.optuna;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptunaCommand {
    private final List<String> commands = new ArrayList<>();
    private Process process;
    private String output;

    OptunaCommand(String... type) {
        commands.add("optuna");
        commands.addAll(Arrays.asList(type));
    }

    OptunaCommand addArgumnt(String key, Object value) {
        commands.add("--" + key);
        if (value != null) {
            String valueString = String.valueOf(value).replace("\"", "\\\"");
            if (valueString.contains(" ")) {
                valueString = "\"" + valueString + "\"";
            }
            commands.add(valueString);
        }
        return this;
    }

    void start() throws OptunaException {
        ProcessBuilder builder = new ProcessBuilder(commands);
        try {
            process = builder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new OptunaException(e);
        }
    }

    String getOutput() throws OptunaException {
        if (output == null) {
            try {
                output = new String(process.getInputStream().readAllBytes());
            } catch (IOException e) {
                throw new OptunaException(e);
            }
        }
        return output;
    }

    JsonObject getOutputAsJsonObject() throws OptunaException {
        return JsonParser.parseString(getOutput()).getAsJsonObject();
    }

    JsonArray getOutputAsJsonArray() throws OptunaException {
        return JsonParser.parseString(getOutput()).getAsJsonArray();
    }
}
