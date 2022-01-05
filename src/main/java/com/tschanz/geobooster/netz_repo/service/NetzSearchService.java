package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.netz_repo.model.SearchNetzObjectsRequest;
import com.tschanz.geobooster.netz_repo.model.SearchNetzObjectsResponse;


public interface NetzSearchService {
    SearchNetzObjectsResponse searchNetzObjects(SearchNetzObjectsRequest request);
}
