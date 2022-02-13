package com.tschanz.geobooster.util.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;


public class DeltaLogger {
    private static final Logger logger = LogManager.getLogger(DeltaLogger.class);
    private static long previousLogTimeMs = 0;
    private static long startLogTimeMs = 0;


    public static void log(String message) {
        if (startLogTimeMs == 0) {
            startLogTimeMs = Instant.now().toEpochMilli();
        }
        if (previousLogTimeMs == 0) {
            previousLogTimeMs = Instant.now().toEpochMilli();
        }

        var msTotal = Instant.now().toEpochMilli() - startLogTimeMs;
        var msDelta = Instant.now().toEpochMilli() - previousLogTimeMs;
        var text = msTotal + "ms (+" + msDelta + "ms): " + message;

        logger.info(text);

        previousLogTimeMs = Instant.now().toEpochMilli();
    }
}
