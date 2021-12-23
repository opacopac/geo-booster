package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;

import java.util.Map;


public interface BetreiberRepo {
    Map<Long, Betreiber> getElementMap();

    Map<Long, BetreiberVersion> getVersionMap();
}
