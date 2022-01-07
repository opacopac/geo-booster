package com.tschanz.geobooster.tarif_repo.service;

import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;

import java.time.LocalDate;
import java.util.Collection;


public interface AwbRepo extends VersionedObjectRepo<Awb, AwbVersion> {
    Collection<TarifkanteVersion> getRgaTarifkanten(AwbVersion version, LocalDate date);
}
