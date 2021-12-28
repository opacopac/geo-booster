package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.versioning.service.VersionedObjectRepo;


public interface BetreiberRepo extends VersionedObjectRepo<Betreiber, BetreiberVersion> {
}
