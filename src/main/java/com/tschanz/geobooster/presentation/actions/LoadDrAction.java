package com.tschanz.geobooster.presentation.actions;

import com.tschanz.geobooster.netz.model.GbDr;
import com.tschanz.geobooster.netz.service.*;
import com.tschanz.geobooster.presentation.state.GbState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LoadDrAction {
    private final BetreiberPersistenceRepo betreiberPersistenceRepo;
    private final VerwaltungPersistenceRepo verwaltungPersistenceRepo;
    private final HaltestellenPersistenceRepo haltestellenPersistenceRepo;
    private final VerkehrskantePersistenceRepo verkehrskantePersistenceRepo;
    private final TarifkantePersistenceRepo tarifkantePersistenceRepo;
    private final BetreiberRepo betreiberRepo;
    private final VerwaltungRepo verwaltungRepo;
    private final HaltestelleRepo haltestelleRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;

    private final GbState gbState;


    public void loadDr() {
        new Thread(() -> {
            try {
                var dr = GbDr.createEmpty();
                this.loadBetreiber(dr);
                this.loadVerwaltung(dr);
                this.loadHaltestelle(dr);
                this.loadVerkehrskante(dr);
                this.loadTarifkante(dr);

                this.gbState.setProgressText("loading dr done", false);
            } catch (Exception e) {
                this.gbState.setErrorText(String.format("error loading dr: %s", e.getMessage()));
            }
        }).start();
    }


    private void loadBetreiber(GbDr dr) {
        this.gbState.setProgressText("loading betreiber...");
        var elements = this.betreiberPersistenceRepo.readAllElements();
        this.gbState.setProgressText("loading betreiber versions...");
        var versions = this.betreiberPersistenceRepo.readAllVersions();
        this.gbState.setProgressText("init betreiber repo...");
        this.betreiberRepo.init(elements, versions);
        dr.setBetreiber(elements);
        dr.setBetreiberVersions(versions);
        this.gbState.getGbDr$().onNext(dr);
    }


    private void loadVerwaltung(GbDr dr) {
        this.gbState.setProgressText("loading verwaltungen...");
        var elements = this.verwaltungPersistenceRepo.readAllElements();
        this.gbState.setProgressText("loading verwaltung versions...");
        var versions = this.verwaltungPersistenceRepo.readAllVersions();
        this.gbState.setProgressText("init verwaltung repo...");
        this.verwaltungRepo.init(elements, versions);
        dr.setVerwaltungen(elements);
        dr.setVerwaltungVersions(versions);
        this.gbState.getGbDr$().onNext(dr);
    }


    private void loadHaltestelle(GbDr dr) {
        this.gbState.setProgressText("loading hst...");
        var elements = this.haltestellenPersistenceRepo.readAllElements();
        this.gbState.setProgressText("loading hst versions...");
        var versions = this.haltestellenPersistenceRepo.readAllVersions();
        this.gbState.setProgressText("init hst repo...");
        this.haltestelleRepo.init(elements, versions);
        dr.setHaltestellen(elements);
        dr.setHaltestelleVersions(versions);
        this.gbState.getGbDr$().onNext(dr);
    }


    private void loadVerkehrskante(GbDr dr) {
        this.gbState.setProgressText("loading vks...");
        var elements = this.verkehrskantePersistenceRepo.readAllElements();
        this.gbState.setProgressText("loading vk versions...");
        var versions = this.verkehrskantePersistenceRepo.readAllVersions();
        this.gbState.setProgressText("init vk repo...");
        this.verkehrskanteRepo.init(elements, versions);
        dr.setVerkehrskanten(elements);
        dr.setVerkehrskanteVersions(versions);
        this.gbState.getGbDr$().onNext(dr);
    }


    private void loadTarifkante(GbDr dr) {
        this.gbState.setProgressText("loading tks...");
        var elements = this.tarifkantePersistenceRepo.readAllElements();
        this.gbState.setProgressText("loading tk versions...");
        var versions = this.tarifkantePersistenceRepo.readAllVersions();
        this.gbState.setProgressText("init tk repo...");
        this.tarifkanteRepo.init(elements, versions);
        dr.setTarifkanten(elements);
        dr.setTarifkanteVersions(versions);
        this.gbState.getGbDr$().onNext(dr);
    }
}
