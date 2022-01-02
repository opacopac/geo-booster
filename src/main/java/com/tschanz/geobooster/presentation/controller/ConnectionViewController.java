package com.tschanz.geobooster.presentation.controller;

import com.tschanz.geobooster.actions.LoadDrAction;
import com.tschanz.geobooster.actions.SelectDrAction;


public interface ConnectionViewController {
    void bindActions(SelectDrAction selectDrAction, LoadDrAction loadDrAction);
}
