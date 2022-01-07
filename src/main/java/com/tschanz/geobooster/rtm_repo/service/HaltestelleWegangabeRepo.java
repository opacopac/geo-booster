package com.tschanz.geobooster.rtm_repo.service;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabe;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;

import java.util.List;


public interface HaltestelleWegangabeRepo extends VersionedObjectRepo<HaltestelleWegangabe, HaltestelleWegangabeVersion> {
    List<HaltestelleWegangabeVersion> searchByExtent(Extent extent);

    HaltestelleVersion getHaltestelleVersion(HaltestelleWegangabeVersion hstWegangabeVersion);
}
