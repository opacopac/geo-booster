package com.tschanz.geobooster.presentation_web.controller;

import com.tschanz.geobooster.presentation.actions.GbActions;
import com.tschanz.geobooster.presentation.model.GbState;
import com.tschanz.geobooster.presentation_web.model.CachingStatsData;
import com.tschanz.geobooster.presentation_web.model.ConnectionData;
import com.tschanz.geobooster.presentation_web.model.ProgressData;
import com.tschanz.geobooster.presentation_web.model.ServingStatsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class WebController {
    private final GbState gbState;
    private final GbActions gbActions;


    @GetMapping("/geo/web")
    public String showStatsPage(Model model) {
        var connectionData = new ConnectionData(this.gbState.getConnectionState());
        var cachingStatsData = new CachingStatsData(
                this.gbState.getNetzRepoState().getBetreiberRepoState(),
                this.gbState.getNetzRepoState().getVerwaltungRepoState(),
                this.gbState.getNetzRepoState().getHaltestelleRepoState(),
                this.gbState.getNetzRepoState().getVerkehrskanteRepoState(),
                this.gbState.getNetzRepoState().getTarifkanteRepoState(),
                this.gbState.getRtmRepoState().getHstWegangabeRepoState(),
                this.gbState.getRtmRepoState().getRgKorridorRepoState(),
                this.gbState.getRtmRepoState().getRgAuspraegungRepoState(),
                this.gbState.getZonenRepoState().getZoneRepoState(),
                this.gbState.getZonenRepoState().getZonenplanRepoState(),
                this.gbState.getTarifRepoState().getAwbRepoState()
        );
        var servingStatsData = new ServingStatsData(this.gbState.getWmsState());
        var progressData = new ProgressData(this.gbState.getProgressState());

        model.addAttribute("connectionData", connectionData);
        model.addAttribute("cachingStatsData", cachingStatsData);
        model.addAttribute("servingStatsData", servingStatsData);
        model.addAttribute("progressData", progressData);

        return "stats.html";
    }


    @GetMapping("/geo/reload")
    public String showReloadDrPage(Model model) {
        this.gbActions.getConnectionActions().loadDr();

        return "reload_dr.html";
    }
}
