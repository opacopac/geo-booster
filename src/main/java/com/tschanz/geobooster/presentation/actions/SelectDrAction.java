package com.tschanz.geobooster.presentation.actions;

import com.tschanz.geobooster.netz_persistence_json.service.GbDrJsonRepo;
import com.tschanz.geobooster.presentation.service.ConnectionPropertySelector;
import com.tschanz.geobooster.presentation.service.QuickStartDrSelector;
import com.tschanz.geobooster.presentation.state.GbState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SelectDrAction {
    private final GbState gbState;
    private final GbDrJsonRepo gbDrJsonRepo;
    private final ConnectionPropertySelector connectionPropertySelector;
    private final QuickStartDrSelector quickStartDrSelector;


    public void selectConnection(int index) {
        this.connectionPropertySelector.selectConnection(index);
        this.quickStartDrSelector.selectQuickStartDr(index);

        var hasQuickStartDr = this.gbDrJsonRepo.hasQuickStartDr();
        this.gbState.getHasQuickStartDr$().onNext(hasQuickStartDr);
    }
}
