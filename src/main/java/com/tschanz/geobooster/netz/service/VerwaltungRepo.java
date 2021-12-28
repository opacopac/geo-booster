package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.versioning.service.VersionedObjectRepo;


public interface VerwaltungRepo extends VersionedObjectRepo<Verwaltung, VerwaltungVersion> {
}
