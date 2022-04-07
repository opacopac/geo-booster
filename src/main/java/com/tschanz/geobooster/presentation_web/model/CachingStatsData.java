package com.tschanz.geobooster.presentation_web.model;


import com.tschanz.geobooster.netz_repo.model.*;
import com.tschanz.geobooster.rtm_repo.model.HaltestelleWegangabeRepoState;
import com.tschanz.geobooster.rtm_repo.model.RgAuspraegungRepoState;
import com.tschanz.geobooster.rtm_repo.model.RgKorridorRepoState;
import com.tschanz.geobooster.tarif_repo.model.AwbRepoState;
import com.tschanz.geobooster.zonen_repo.model.ZoneRepoState;
import com.tschanz.geobooster.zonen_repo.model.ZonenplanRepoState;
import io.reactivex.Observable;
import lombok.Getter;

import java.util.function.Function;


@Getter
public class CachingStatsData {
    private String betreiberEV;
    private String verwaltungEV;
    private String haltestelleEV;
    private String verkehrskanteEV;
    private String tarifkanteEV;
    private String hstWaEV;
    private String rgKorrEV;
    private String rgAuspraegungEV;
    private String zoneEV;
    private String zonenplanEV;
    private String awbEV;


    public CachingStatsData(
        BetreiberRepoState betreiberState,
        VerwaltungRepoState verwaltungState,
        HaltestelleRepoState haltestelleState,
        VerkehrskanteRepoState vkState,
        TarifkanteRepoState tkState,
        HaltestelleWegangabeRepoState hstWaState,
        RgKorridorRepoState rgKorrState,
        RgAuspraegungRepoState rgAuspraegungState,
        ZoneRepoState zoneState,
        ZonenplanRepoState zonenplanState,
        AwbRepoState awbState
    ) {
        this.apply(betreiberState.getLoadedElementCount$(), betreiberState.getLoadedVersionCount$(), val -> this.betreiberEV = val);
        this.apply(verwaltungState.getLoadedElementCount$(), verwaltungState.getLoadedVersionCount$(), val -> this.verwaltungEV = val);
        this.apply(haltestelleState.getLoadedElementCount$(), haltestelleState.getLoadedVersionCount$(), val -> this.haltestelleEV = val);
        this.apply(vkState.getLoadedElementCount$(), vkState.getLoadedVersionCount$(), val -> this.verkehrskanteEV = val);
        this.apply(tkState.getLoadedElementCount$(), tkState.getLoadedVersionCount$(), val -> this.tarifkanteEV = val);
        this.apply(hstWaState.getLoadedElementCount$(), hstWaState.getLoadedVersionCount$(), val -> this.hstWaEV = val);
        this.apply(rgKorrState.getLoadedElementCount$(), rgKorrState.getLoadedVersionCount$(), val -> this.rgKorrEV = val);
        this.apply(rgAuspraegungState.getLoadedElementCount$(), rgAuspraegungState.getLoadedVersionCount$(), val -> this.rgAuspraegungEV = val);
        this.apply(zoneState.getLoadedElementCount$(), zoneState.getLoadedVersionCount$(), val -> this.zoneEV = val);
        this.apply(zonenplanState.getLoadedElementCount$(), zonenplanState.getLoadedVersionCount$(), val -> this.zonenplanEV = val);
        this.apply(awbState.getLoadedElementCount$(), awbState.getLoadedVersionCount$(), val -> this.awbEV = val);
    }


    private void apply(
        Observable<Integer> numE$,
        Observable<Integer> numV$,
        Function<String, String> fn
    ) {
        Observable.combineLatest(
            numE$,
            numV$,
            (numE, numV) -> numE + " / " + numV
        ).take(1).subscribe(fn::apply);
    }
}
