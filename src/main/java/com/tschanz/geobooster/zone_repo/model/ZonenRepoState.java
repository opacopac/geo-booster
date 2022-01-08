package com.tschanz.geobooster.zone_repo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Getter
@Component
@RequiredArgsConstructor
public class ZonenRepoState {
    private final ZonenplanRepoState zonenplanRepoState;
}
