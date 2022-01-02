package com.tschanz.geobooster.state_netz;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Getter
@Component
@RequiredArgsConstructor
public class NetzState {
    private final BetreiberState betreiberState;
    private final VerwaltungState verwaltungState;
    private final HaltestelleState haltestelleState;
    private final VerkehrskanteState verkehrskanteState;
    private final TarifkanteState tarifkanteState;
}
