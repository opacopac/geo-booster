package com.tschanz.geobooster.presentation.state;

import com.tschanz.geobooster.netz.model.GbDr;
import com.tschanz.geobooster.webmapservice.model.WmsStats;
import io.reactivex.subjects.BehaviorSubject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Component
@RequiredArgsConstructor
public class GbState {
    private final BehaviorSubject<Boolean> isInProgress$ = BehaviorSubject.create();
    private final BehaviorSubject<String> progressText$ = BehaviorSubject.create();
    private final BehaviorSubject<List<String>> connectionList$ = BehaviorSubject.create();
    private final BehaviorSubject<GbDr> gbDr$ = BehaviorSubject.create();
    private final BehaviorSubject<WmsStats> wmsStats$ = BehaviorSubject.create();


    public void setProgressText(String text) {
        this.setProgressText(text, true);
    }


    public void setErrorText(String text) {
        // TODO: red
        this.setProgressText(text, false);
    }


    public void setProgressText(String text, boolean isInProgress) {
        this.progressText$.onNext(text);
        this.isInProgress$.onNext(isInProgress);
    }


    public void incPngRequestCount() {
        var oldWmsStats = this.getWmsStatsValue();
        var newWmsStats = new WmsStats(oldWmsStats.getPngRequests() + 1, oldWmsStats.getUtfGridRequests());
        this.wmsStats$.onNext(newWmsStats);
    }


    public void incUtfGridRequestCount() {
        var oldWmsStats = this.getWmsStatsValue();
        var newWmsStats = new WmsStats(oldWmsStats.getPngRequests(), oldWmsStats.getUtfGridRequests() + 1);
        this.wmsStats$.onNext(newWmsStats);
    }


    private WmsStats getWmsStatsValue() {
        var wmsStats = wmsStats$.getValue();

        return wmsStats != null ? wmsStats : new WmsStats(0, 0);
    }
}
