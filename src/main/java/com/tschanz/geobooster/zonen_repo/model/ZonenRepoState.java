package com.tschanz.geobooster.zonen_repo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Getter
@Component
@RequiredArgsConstructor
public class ZonenRepoState {
    private final ZoneRepoState zoneRepoState;
    private final ZonenplanRepoState zonenplanRepoState;
}
