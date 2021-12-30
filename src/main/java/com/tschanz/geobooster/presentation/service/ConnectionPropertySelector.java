package com.tschanz.geobooster.presentation.service;

import java.util.List;


public interface ConnectionPropertySelector {
    public List<String> getConnectionList();

    void selectConnection(int index);
}
