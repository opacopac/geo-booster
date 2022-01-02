package com.tschanz.geobooster.state;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import org.springframework.stereotype.Component;


@Component
public class ProgressState {
    private final BehaviorSubject<Boolean> isInProgress$ = BehaviorSubject.createDefault(false);
    private final BehaviorSubject<String> progressText$ = BehaviorSubject.createDefault("");


    public Observable<Boolean> getIsInProgress$() {
        return this.isInProgress$;
    }


    public void updateIsInProgress(boolean isInProgress) {
        this.isInProgress$.onNext(isInProgress);
    }


    public Observable<String> getProgressText$() {
        return this.progressText$;
    }


    public void updateProgressText(String progressText) {
        this.progressText$.onNext(progressText);
    }
}
