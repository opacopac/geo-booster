package com.tschanz.geobooster.zone_repo.service;

import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;
import com.tschanz.geobooster.zonen.model.Zonenplan;
import com.tschanz.geobooster.zonen.model.ZonenplanVersion;


public interface ZonenplanRepo extends VersionedObjectRepo<Zonenplan, ZonenplanVersion> {
}
