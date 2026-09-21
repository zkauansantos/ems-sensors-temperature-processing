package com.zkauansantos.sensors.temperature.processing.common;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;
import java.util.UUID;

public class IdGenerator {
    private static final TimeBasedEpochRandomGenerator timeBasedEpochRandomGenerator =
            Generators.timeBasedEpochRandomGenerator();

    private IdGenerator() {
    }

    public static UUID generateTimeBaseUUID() {
        return timeBasedEpochRandomGenerator.generate();
    }
}