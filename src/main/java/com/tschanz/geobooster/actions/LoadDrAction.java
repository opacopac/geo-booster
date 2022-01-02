package com.tschanz.geobooster.actions;

import com.tschanz.geobooster.netz.service.*;
import com.tschanz.geobooster.state.GbState;
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
                this.gbState.getProgressState().updateIsInProgress(true);
                this.betreiberRepo.loadAll();
                this.verwaltungRepo.loadAll();
                this.haltestelleRepo.loadAll();
                this.verkehrskanteRepo.loadAll();
                this.tarifkanteRepo.loadAll();
                this.gbState.getProgressState().updateProgressText("loading dr done");
                this.gbState.getProgressState().updateIsInProgress(false);
            } catch (Exception e) {
                this.gbState.getProgressState().updateProgressText(String.format("ERROR loading dr: %s", e.getMessage()));
                this.gbState.getProgressState().updateIsInProgress(false);
            }
        }).start();
    }
}
