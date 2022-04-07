package com.tschanz.geobooster.presentation_web.model;


import com.tschanz.geobooster.webmapservice.model.WmsState;
import io.reactivex.Observable;
import lombok.Getter;

import java.util.function.Function;


@Getter
public class ServingStatsData {
    private String pngRequests;
    private String utfGridRequests;


    public ServingStatsData(WmsState wmsState) {
        this.apply(
            wmsState.getPngRequestCount$(),
            wmsState.getPngRequestMs$(1),
            val -> this.pngRequests = val
        );

        this.apply(
            wmsState.getUtfGridRequestCount$(),
            wmsState.getUtfGridRequestMs$(1),
            val -> this.utfGridRequests = val
        );
    }


    private void apply(
        Observable<Long> req$,
        Observable<Long> lastMs$,
        Function<String, String> fn
    ) {
        Observable.combineLatest(
            req$,
            lastMs$,
            (req, lastMs) -> req + " / " + lastMs + "ms "
        ).take(1).subscribe(fn::apply);
    }
}
