package com.tschanz.geobooster.presentation.presenter;

import com.tschanz.geobooster.state_netz.NetzState;


public interface CachingStatsViewPresenter {
    void bindState(NetzState netzState);
}
