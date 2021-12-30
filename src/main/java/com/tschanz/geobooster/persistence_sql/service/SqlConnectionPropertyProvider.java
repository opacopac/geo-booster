package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.SqlConnectionProperties;


public interface SqlConnectionPropertyProvider {
    SqlConnectionProperties getConnectionProperties();
}
