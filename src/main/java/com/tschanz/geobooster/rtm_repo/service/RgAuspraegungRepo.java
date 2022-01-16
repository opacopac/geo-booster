package com.tschanz.geobooster.rtm_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.rtm.model.RgAuspraegung;
import com.tschanz.geobooster.rtm.model.RgAuspraegungVersion;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;

import java.time.LocalDate;
import java.util.Collection;


public interface RgAuspraegungRepo extends VersionedObjectRepo<RgAuspraegung, RgAuspraegungVersion> {
    Collection<TarifkanteVersion> searchRgaTarifkanten(RgAuspraegungVersion rgaVersion, LocalDate date, Extent<Epsg3857Coordinate> bbox);
}
