package com.tschanz.geobooster.presentation.actions;

import com.tschanz.geobooster.netz.model.GbDr;
import com.tschanz.geobooster.netz.service.GbDrRepo;
import com.tschanz.geobooster.netz_persistence_json.service.GbDrJsonRepo;
import com.tschanz.geobooster.netz_persistence_sql.service.GbDrSqlRepo;
import com.tschanz.geobooster.presentation.state.GbState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GbActions {
    private final GbDrSqlRepo gbDrSqlRepo;
    private final GbDrJsonRepo gbDrJsonRepo;
    private final GbDrRepo gbDrRepo;
    private final GbState gbState;


    public void loadDr() {
        this.gbState.setProgressText("loading dr...");
        GbDr dr;
        if (this.gbDrJsonRepo.hasQuickStartDr()) {
            this.gbState.setProgressText("loading json data...");
            dr = this.gbDrJsonRepo.readDr();
            this.gbState.setProgressText("loading json data done");
        } else {
            this.gbState.setProgressText("loading sql data...");
            dr = this.gbDrSqlRepo.loadDr();
            this.gbState.setProgressText("loading sql data done");

            this.gbState.setProgressText("saving quick start dr...");
            this.gbDrJsonRepo.save(dr);
            this.gbState.setProgressText("saving quick start dr done");
        }

        this.gbState.setProgressText("init repos...");
        this.gbDrRepo.init(dr);
        this.gbState.setProgressText("init repos done");

        this.gbState.getGbDr$().onNext(dr);
        this.gbState.setProgressText("loading dr done", false);
    }
}
