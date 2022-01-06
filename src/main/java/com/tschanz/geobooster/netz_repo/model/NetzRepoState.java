package com.tschanz.geobooster.netz_repo.model;

import com.tschanz.geobooster.tarif_repo.model.AwbRepoState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Getter
@Component
@RequiredArgsConstructor
public class NetzRepoState {
    private final BetreiberRepoState betreiberRepoState;
    private final VerwaltungRepoState verwaltungRepoState;
    private final HaltestelleRepoState haltestelleRepoState;
    private final VerkehrskanteRepoState verkehrskanteRepoState;
    private final TarifkanteRepoState tarifkanteRepoState;
    private final AwbRepoState awbRepoState;
}
