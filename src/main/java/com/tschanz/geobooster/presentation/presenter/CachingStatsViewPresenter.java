package com.tschanz.geobooster.presentation.presenter;

import com.tschanz.geobooster.netz_repo.model.NetzRepoState;


public interface CachingStatsViewPresenter {
    void bindState(NetzRepoState netzRepoState);
}
