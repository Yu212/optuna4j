package com.yu212.optuna;

public enum StudyDirection {
    MINIMIZE, MAXIMIZE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
