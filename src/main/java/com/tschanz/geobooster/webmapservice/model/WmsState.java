package com.tschanz.geobooster.webmapservice.model;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import lombok.Synchronized;
import org.springframework.stereotype.Component;


@Component
public class WmsState {
    private final BehaviorSubject<Long> pngRequestCount$ = BehaviorSubject.createDefault(0L);
    private final BehaviorSubject<Long> utfGridRequestCount$ = BehaviorSubject.createDefault(0L);
    private final BehaviorSubject<Long> pngResponseTimeMs$ = BehaviorSubject.create();
    private final BehaviorSubject<Long> utfGridResponseTimeMs$ = BehaviorSubject.create();


    public Observable<Long> getPngRequestCount$() {
        return this.pngRequestCount$;
    }


    public Observable<Long> getUtfGridRequestCount$() {
        return this.utfGridRequestCount$;
    }


    public Observable<Long> getPngRequestMs$(int averageN) {
        if (averageN <= 1) {
            return this.pngResponseTimeMs$;
        } else {
            return this.getLastNAverage$(this.pngResponseTimeMs$, averageN);
        }
    }


    public Observable<Long> getUtfGridRequestMs$(int averageN) {
        if (averageN <= 1) {
            return this.utfGridResponseTimeMs$;
        } else {
            return this.getLastNAverage$(this.utfGridResponseTimeMs$, averageN);
        }
    }


    @Synchronized
    public void incPngRequestCount() {
        var currentValue = this.pngRequestCount$.getValue();
        var newValue = currentValue == null ? 1 : currentValue + 1;
        this.pngRequestCount$.onNext(newValue);
    }


    @Synchronized
    public void incUtfGridRequestCount() {
        var currentValue = this.utfGridRequestCount$.getValue();
        var newValue = currentValue == null ? 1 : currentValue + 1;
        this.utfGridRequestCount$.onNext(newValue);
    }


    @Synchronized
    public void nextPngRequestMs(long valueMs) {
        this.pngResponseTimeMs$.onNext(valueMs);
    }


    @Synchronized
    public void nextUtfGridRequestMs(long valueMs) {
        this.utfGridResponseTimeMs$.onNext(valueMs);
    }


    private Observable<Long> getLastNAverage$(Observable<Long> observable, int averageN) {
        return observable
            .buffer(averageN, 1)
            .map(list -> list.stream().reduce(0L, Long::sum) / list.size());
    }
}
