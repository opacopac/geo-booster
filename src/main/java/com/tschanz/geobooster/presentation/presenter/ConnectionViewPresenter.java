package com.tschanz.geobooster.presentation.presenter;

import com.tschanz.geobooster.persistence_sql.model.ConnectionState;


public interface ConnectionViewPresenter {
    void bindState(ConnectionState connectionState);
}
