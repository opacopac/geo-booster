package com.tschanz.geobooster.presentation_web.model;


import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import lombok.Getter;


@Getter
public class ConnectionData {
    private final String connection;
    private final String trackChanges;


    public ConnectionData(ConnectionState connectionState) {
        this.connection = connectionState.hasSelectedConnection()
            ? connectionState.getSelectedConnectionProperties().getTitle()
            : "(none)";

        this.trackChanges = connectionState.isTrackChanges()
            ? "yes"
            : "no";
    }
}
