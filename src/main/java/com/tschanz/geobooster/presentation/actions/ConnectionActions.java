package com.tschanz.geobooster.presentation.actions;

import com.tschanz.geobooster.netz_repo.service.*;
import com.tschanz.geobooster.presentation.model.GbState;
import com.tschanz.geobooster.tarif_repo.service.AwbRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ConnectionActions {
    private static final Logger logger = LogManager.getLogger(ConnectionActions.class);

    private final BetreiberRepo betreiberRepo;
    private final VerwaltungRepo verwaltungRepo;
    private final HaltestelleRepo haltestelleRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;
    private final AwbRepo awbRepo;
    private final GbState gbState;


    public void selectConnection(int index) {
        this.gbState.getConnectionState().selectConnection(index);
    }


    public void setTrackChanges(boolean trackChanges) {
        this.gbState.getConnectionState().setTrackChanges(trackChanges);
    }


    public void loadDr() {
        new Thread(() -> {
            try {
                this.gbState.getProgressState().updateIsInProgress(true);
                this.betreiberRepo.loadAll();
                this.verwaltungRepo.loadAll();
                this.haltestelleRepo.loadAll();
                this.verkehrskanteRepo.loadAll();
                this.tarifkanteRepo.loadAll();
                this.awbRepo.loadAll();
                this.gbState.getProgressState().updateProgressText("loading dr done");
                this.gbState.getProgressState().updateIsInProgress(false);
            } catch (Exception e) {
                logger.error(e);
                this.gbState.getProgressState().updateProgressText(String.format("ERROR loading dr: %s", e.getMessage()));
                this.gbState.getProgressState().updateIsInProgress(false);
            }
        }).start();
    }
}
