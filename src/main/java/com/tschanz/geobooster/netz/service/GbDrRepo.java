package com.tschanz.geobooster.netz.service;

import com.tschanz.geobooster.netz.model.GbDr;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GbDrRepo {
    private final BetreiberRepo betreiberRepo;
    private final VerwaltungRepo verwaltungRepo;
    private final HaltestelleRepo haltestelleRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;


    public void init(GbDr dr) {
        this.betreiberRepo.init(dr.getBetreiber(), dr.getBetreiberVersions());
        this.verwaltungRepo.init(dr.getVerwaltungen(), dr.getVerwaltungVersions());
        this.haltestelleRepo.init(dr.getHaltestellen(), dr.getHaltestelleVersions());
        this.verkehrskanteRepo.init(dr.getVerkehrskanten(), dr.getVerkehrskanteVersions());
        this.tarifkanteRepo.init(dr.getTarifkanten(), dr.getTarifkanteVersions());
    }
}
