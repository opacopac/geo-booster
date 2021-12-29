package com.tschanz.geobooster.presentation.presenter;

import io.reactivex.Observable;


public interface StatusBarViewPresenter {
    void bindState(Observable<Boolean> isInProgress$, Observable<String> progressText$);
}
