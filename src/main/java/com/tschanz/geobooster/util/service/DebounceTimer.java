package com.tschanz.geobooster.util.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor
public class DebounceTimer {
    private final int debounceTimeSec;
    private LocalDateTime currentChangeCheck = LocalDateTime.now();
    private LocalDateTime previousChangeCheck = LocalDateTime.now();


    public boolean isInDebounceTime() {
        if (LocalDateTime.now().isBefore(this.currentChangeCheck.plusSeconds(this.debounceTimeSec))) {
            return true;
        } else {
            this.touch();
            return false;
        }
    }


    public void touch() {
        this.previousChangeCheck = this.currentChangeCheck;
        this.currentChangeCheck = LocalDateTime.now();
    }
}
