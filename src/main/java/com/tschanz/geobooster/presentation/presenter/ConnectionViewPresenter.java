package com.tschanz.geobooster.presentation.presenter;

import io.reactivex.Observable;

import java.util.List;


public interface ConnectionViewPresenter {
    void bindState(Observable<List<String>> connectionList$);
}
