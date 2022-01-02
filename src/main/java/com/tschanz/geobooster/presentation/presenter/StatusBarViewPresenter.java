package com.tschanz.geobooster.presentation.presenter;

import com.tschanz.geobooster.netz_repo.model.ProgressState;


public interface StatusBarViewPresenter {
    void bindState(ProgressState progressState);
}
