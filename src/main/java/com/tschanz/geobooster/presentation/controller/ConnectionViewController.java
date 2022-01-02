package com.tschanz.geobooster.presentation.controller;

import com.tschanz.geobooster.presentation.actions.LoadDrAction;
import com.tschanz.geobooster.presentation.actions.SelectDrAction;


public interface ConnectionViewController {
    void bindActions(SelectDrAction selectDrAction, LoadDrAction loadDrAction);
}
