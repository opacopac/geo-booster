package com.tschanz.geobooster.search.service;

import com.tschanz.geobooster.search.model.SearchNetzObjectsRequest;
import com.tschanz.geobooster.search.model.SearchNetzObjectsResponse;


public interface NetzSearchService {
    SearchNetzObjectsResponse searchNetzObjects(SearchNetzObjectsRequest request);
}
