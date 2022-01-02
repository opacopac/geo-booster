package com.tschanz.geobooster.state_versioning;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;


public class VersionedObjectState {
    private final BehaviorSubject<Boolean> isLoading$ = BehaviorSubject.createDefault(false);
    private final BehaviorSubject<Integer> loadedElementCount$ = BehaviorSubject.createDefault(0);
    private final BehaviorSubject<Integer> loadedVersionCount$ = BehaviorSubject.createDefault(0);


    public Observable<Boolean> getIsLoading$() {
        return this.isLoading$;
    }


    public void updateIsLoading(boolean isLoading) {
        this.isLoading$.onNext(isLoading);
    }


    public Observable<Integer> getLoadedElementCount$() {
        return this.loadedElementCount$;
    }


    public void updateLoadedElementCount(int count) {
        this.loadedElementCount$.onNext(count);
    }


    public Observable<Integer> getLoadedVersionCount$() {
        return this.loadedVersionCount$;
    }


    public void updateLoadedVersionCount(int count) {
        this.loadedVersionCount$.onNext(count);
    }
}
