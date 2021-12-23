package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;

import java.util.Map;


public interface VerwaltungRepo {
    Map<Long, Verwaltung> getElementMap();

    Map<Long, VerwaltungVersion> getVersionMap();
}
