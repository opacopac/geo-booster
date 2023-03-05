package com.tschanz.geobooster.presentation.actions;

import com.tschanz.geobooster.netz_repo.service.*;
import com.tschanz.geobooster.presentation.model.GbState;
import com.tschanz.geobooster.rtm_repo.service.HaltestelleWegangabeRepo;
import com.tschanz.geobooster.rtm_repo.service.RgAuspraegungRepo;
import com.tschanz.geobooster.rtm_repo.service.RgKorridorRepo;
import com.tschanz.geobooster.tarif_repo.service.AwbRepo;
import com.tschanz.geobooster.util.service.ExceptionHelper;
import com.tschanz.geobooster.zonen_repo.service.ZoneRepo;
import com.tschanz.geobooster.zonen_repo.service.ZonenplanRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
public class ConnectionActions {
    private static final Logger logger = LogManager.getLogger(ConnectionActions.class);

    private final BetreiberRepo betreiberRepo;
    private final VerwaltungRepo verwaltungRepo;
    private final HaltestelleRepo haltestelleRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;
    private final TarifkanteRepo tarifkanteRepo;
    private final HaltestelleWegangabeRepo hstWegangabeRepo;
    private final RgKorridorRepo rgKorridorRepo;
    private final RgAuspraegungRepo rgAuspraegungRepo;
    private final ZoneRepo zoneRepo;
    private final ZonenplanRepo zonenplanRepo;
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
            this.gbState.getProgressState().updateIsInProgress(true);

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                CompletableFuture.runAsync(betreiberRepo::loadAll),
                CompletableFuture.runAsync(verwaltungRepo::loadAll),
                CompletableFuture.runAsync(haltestelleRepo::loadAll),
                CompletableFuture.runAsync(verkehrskanteRepo::loadAll),
                CompletableFuture.runAsync(tarifkanteRepo::loadAll),
                CompletableFuture.runAsync(hstWegangabeRepo::loadAll),
                CompletableFuture.runAsync(rgKorridorRepo::loadAll),
                CompletableFuture.runAsync(rgAuspraegungRepo::loadAll),
                CompletableFuture.runAsync(zoneRepo::loadAll),
                CompletableFuture.runAsync(zonenplanRepo::loadAll),
                CompletableFuture.runAsync(awbRepo::loadAll)
            );

            allFutures.thenRun(() -> {
                this.gbState.getProgressState().updateProgressText("loading dr done");
                this.gbState.getProgressState().updateIsInProgress(false);
            }).exceptionally(e -> {
                logger.error(e);
                this.gbState.getProgressState().updateProgressText(
                    String.format("ERROR loading dr: %s", ExceptionHelper.getErrorText(e, "\n")),
                    true
                );
                this.gbState.getProgressState().updateIsInProgress(false);

                return null;
            });
        }).start();
    }
}
