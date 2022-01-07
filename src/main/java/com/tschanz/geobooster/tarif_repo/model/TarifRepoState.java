package com.tschanz.geobooster.tarif_repo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Getter
@Component
@RequiredArgsConstructor
public class TarifRepoState {
    private final AwbRepoState awbRepoState;
}
