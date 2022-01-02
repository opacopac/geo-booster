package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;


public interface VerwaltungRepo extends VersionedObjectRepo<Verwaltung, VerwaltungVersion> {
}
