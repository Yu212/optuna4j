package com.yu212.optuna;

public enum TrialState {
    RUNNING,
    WAITING,
    COMPLETE,
    PRUNED,
    FAIL
}
