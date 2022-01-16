package com.tschanz.geobooster.presentation.presenter;

import com.tschanz.geobooster.netz_repo.model.NetzRepoState;
import com.tschanz.geobooster.rtm_repo.model.RtmRepoState;
import com.tschanz.geobooster.tarif_repo.model.TarifRepoState;
import com.tschanz.geobooster.zonen_repo.model.ZonenRepoState;


public interface CachingStatsViewPresenter {
    void bindState(NetzRepoState netzRepoState, RtmRepoState rtmRepoState, ZonenRepoState zonenRepoState, TarifRepoState tarifRepoState);
}
