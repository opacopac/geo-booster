package com.tschanz.geobooster.tarif_repo.service;

import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;


public interface AwbRepo extends VersionedObjectRepo<Awb, AwbVersion> {
}
