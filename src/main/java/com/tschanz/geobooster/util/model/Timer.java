package com.tschanz.geobooster.util.model;

import java.time.Instant;


public class Timer {
    private long lastCheckSec;


    public Timer() {
        this.lastCheckSec = Instant.now().getEpochSecond();
    }


    public boolean checkSecElapsed(int count) {
        var elapsedSec = Instant.now().getEpochSecond() - this.lastCheckSec;
        if (elapsedSec >= count) {
            this.lastCheckSec = Instant.now().getEpochSecond();
            return true;
        } else {
            return false;
        }
    }
}
