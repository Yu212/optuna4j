package com.yu212.optuna;

import com.yu212.optuna.distributions.CategoricalDistribution;
import com.yu212.optuna.distributions.Distribution;
import com.yu212.optuna.distributions.UniformDistribution;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class OptunaTest {
    private static Path STORAGE_PATH;

    @BeforeAll
    public static void deleteDatabases() throws IOException {
        STORAGE_PATH = Files.createTempDirectory("test");
        STORAGE_PATH.toFile().deleteOnExit();
    }

    @Test
    public void testCreateStudy() throws OptunaException {
        Storage storage = new Storage(STORAGE_PATH.resolve("testCreateStudy.db"));
        Study study = Optuna.createStudy(null, storage, false, StudyDirection.MINIMIZE);
        Assertions.assertTrue(study.getStudyName().matches("no-name-[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
    }

    @Test
    public void testCreateStudyWithStudyName() throws OptunaException {
        Storage storage = new Storage(STORAGE_PATH.resolve("testCreateStudyWithStudyName.db"));
        String studyName = "test_study";
        Study study = Optuna.createStudy(studyName, storage, false, StudyDirection.MINIMIZE);
        Assertions.assertEquals(study.getStudyName(), studyName);
    }

    @Test
    public void testDeleteStudy() throws OptunaException {
        Storage storage = new Storage(STORAGE_PATH.resolve("testDeleteStudy.db"));
        Study study = Optuna.createStudy("test_study", storage, false, StudyDirection.MINIMIZE);
        Assertions.assertTrue(storage.getStudies().stream()
                .map(Study::getStudyName)
                .anyMatch(study.getStudyName()::equals));
        study.delete();
        Assertions.assertFalse(storage.getStudies().stream()
                .map(Study::getStudyName)
                .anyMatch(study.getStudyName()::equals));
    }

    @Test
    public void testStudies() throws OptunaException {
        Storage storage = new Storage(STORAGE_PATH.resolve("testStudies.db"));
        Study study1 = Optuna.createStudy("test_study_1", storage, false, StudyDirection.MINIMIZE);
        Study study2 = Optuna.createStudy("test_study_2", storage, false, StudyDirection.MINIMIZE);
        List<Study> studies = storage.getStudies();
        Assertions.assertEquals(studies.size(), 2);
        Assertions.assertEquals(studies.get(0).getStudyName(), study1.getStudyName());
        Assertions.assertEquals(studies.get(1).getStudyName(), study2.getStudyName());
    }

    @Test
    public void testTrials() throws OptunaException {
        Storage storage = new Storage(STORAGE_PATH.resolve("testTrials.db"));
        Study study = Optuna.createStudy("test_study", storage, false, StudyDirection.MINIMIZE);
        int nTrials = 10;
        for (int i = 0; i < nTrials; i++) {
            study.tell(study.ask(), i);
        }
        List<FrozenTrial> trials = study.getTrials();
        Assertions.assertEquals(trials.size(), nTrials);
        for (int i = 0; i < nTrials; i++) {
            FrozenTrial trial = trials.get(i);
            Assertions.assertEquals(trial.getNumber(), i);
            Assertions.assertEquals(trial.getValue().intValue(), i);
        }
    }

    @Test
    public void testBestTrial() throws OptunaException {
        Storage storage = new Storage(STORAGE_PATH.resolve("testBestTrial.db"));
        Study study = Optuna.createStudy("test_study", storage, false, StudyDirection.MINIMIZE);
        int nTrials = 10;
        for (int i = 0; i < nTrials; i++) {
            study.tell(study.ask(), i);
        }
        FrozenTrial bestTrial = study.getBestTrial();
        Assertions.assertEquals(bestTrial.getNumber(), 0);
        Assertions.assertEquals(bestTrial.getValue().intValue(), 0);
    }

    @Test
    public void testBestTrials() throws OptunaException {
        Storage storage = new Storage(STORAGE_PATH.resolve("testBestTrials.db"));
        Study study = Optuna.createStudy("test_study", storage, false, StudyDirection.MINIMIZE);
        int nTrials = 10;
        for (int i = 0; i < nTrials; i++) {
            study.tell(study.ask(), i / 2);
        }
        List<FrozenTrial> trials = study.getBestTrials();
        Assertions.assertEquals(trials.size(), 2);
        for (int i = 0; i < 2; i++) {
            FrozenTrial trial = trials.get(i);
            Assertions.assertEquals(trial.getNumber(), i);
            Assertions.assertEquals(trial.getValue().intValue(), 0);
        }
    }

    @Test
    public void testCreateStudyWithSkipIfExists() throws OptunaException {
        Storage storage = new Storage(STORAGE_PATH.resolve("testCreateStudyWithSkipIfExists.db"));
        String studyName = "test_study";
        Study study1 = Optuna.createStudy(studyName, storage, false, StudyDirection.MINIMIZE);
        Assertions.assertEquals(study1.getStudyName(), studyName);
        Study study2 = Optuna.createStudy(studyName, storage, true, StudyDirection.MINIMIZE);
        Assertions.assertEquals(study2.getStudyName(), studyName);
        List<Study> studies = storage.getStudies();
        Assertions.assertEquals(studies.size(), 1);
        Assertions.assertEquals(studies.get(0).getStudyName(), studyName);
    }

    @Test
    public void testAsk() throws OptunaException {
        Storage storage = new Storage(STORAGE_PATH.resolve("testAsk.db"));
        Study study = Optuna.createStudy("test_study", storage, false, StudyDirection.MINIMIZE);
        Distribution[] distributions = {
                new UniformDistribution("x", 0, 1),
                new CategoricalDistribution("y", "foo")};
        Trial trial = study.ask(distributions);
        Assertions.assertEquals(trial.getNumber(), 0);
        double x = trial.getParameterDouble("x");
        String y = trial.getParameterString("y");
        Assertions.assertTrue(0 <= x && x <= 1);
        Assertions.assertEquals(y, "foo");
    }

    @Test
    public void testAskEmptySearchSpace() throws OptunaException {
        Storage storage = new Storage(STORAGE_PATH.resolve("testAskEmptySearchSpace.db"));
        Study study = Optuna.createStudy("test_study", storage, false, StudyDirection.MINIMIZE);
        Trial trial = study.ask();
        Assertions.assertEquals(trial.getNumber(), 0);
    }

    @Test
    public void testTell() throws OptunaException {
        Storage storage = new Storage(STORAGE_PATH.resolve("testTell.db"));
        Study study = Optuna.createStudy("test_study", storage, false, StudyDirection.MINIMIZE);
        Trial trial = study.ask();
        study.tell(trial, 1.2);
        List<FrozenTrial> trials = study.getTrials();
        Assertions.assertEquals(trials.size(), 1);
        Assertions.assertEquals(trials.get(0).getNumber(), 0);
        Assertions.assertEquals(trials.get(0).getState(), TrialState.COMPLETE);
        Assertions.assertEquals(trials.get(0).getValue().doubleValue(), 1.2);
    }
}
