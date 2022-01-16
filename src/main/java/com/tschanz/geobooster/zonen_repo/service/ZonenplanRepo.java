package com.tschanz.geobooster.zonen_repo.service;

import com.tschanz.geobooster.geofeature.model.Epsg3857Coordinate;
import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;
import com.tschanz.geobooster.zonen.model.Zonenplan;
import com.tschanz.geobooster.zonen.model.ZonenplanVersion;

import java.time.LocalDate;
import java.util.Collection;


public interface ZonenplanRepo extends VersionedObjectRepo<Zonenplan, ZonenplanVersion> {
    Collection<VerkehrskanteVersion> searchZpVerkehrskanten(ZonenplanVersion zpVersion, LocalDate date, Extent<Epsg3857Coordinate> bbox);
}
