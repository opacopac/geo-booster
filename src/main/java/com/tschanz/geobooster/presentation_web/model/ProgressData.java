package com.tschanz.geobooster.presentation_web.model;


import com.tschanz.geobooster.netz_repo.model.ProgressState;
import lombok.Getter;


@Getter
public class ProgressData {
    private boolean isInProgress;
    private String progressText;


    public ProgressData(ProgressState progressState) {
        progressState.getIsInProgress$().take(1).subscribe(value -> this.isInProgress = value);
        progressState.getProgressText$().take(1).subscribe(value -> this.progressText = value);
    }
}
