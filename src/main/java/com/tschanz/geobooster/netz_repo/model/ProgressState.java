package com.tschanz.geobooster.netz_repo.model;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import org.springframework.stereotype.Component;


@Component
public class ProgressState {
    private final BehaviorSubject<Boolean> isInProgress$ = BehaviorSubject.createDefault(false);
    private final BehaviorSubject<String> progressText$ = BehaviorSubject.createDefault("");
    private final BehaviorSubject<Boolean> isTextError$ = BehaviorSubject.createDefault(false);


    public Observable<Boolean> getIsInProgress$() {
        return this.isInProgress$;
    }


    public void updateIsInProgress(boolean isInProgress) {
        this.isInProgress$.onNext(isInProgress);
    }


    public Observable<String> getProgressText$() {
        return this.progressText$;
    }


    public Observable<Boolean> isTextError$() {
        return this.isTextError$;
    }


    public void updateProgressText(String progressText) {
        this.updateProgressText(progressText, false);
    }


    public void updateProgressText(String progressText, boolean isError) {
        this.progressText$.onNext(progressText);
        this.isTextError$.onNext(isError);
    }
}
