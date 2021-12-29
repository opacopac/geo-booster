package com.tschanz.geobooster.presentation.presenter;

import com.tschanz.geobooster.netz.model.GbDr;
import io.reactivex.Observable;


public interface CachingStatsViewPresenter {
    void bindState(Observable<GbDr> gbDr$);
}
