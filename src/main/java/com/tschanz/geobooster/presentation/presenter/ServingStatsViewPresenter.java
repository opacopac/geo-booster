package com.tschanz.geobooster.presentation.presenter;

import com.tschanz.geobooster.webmapservice.model.WmsStats;
import io.reactivex.Observable;


public interface ServingStatsViewPresenter {
    void bindState(Observable<WmsStats> wmsStats$);
}
