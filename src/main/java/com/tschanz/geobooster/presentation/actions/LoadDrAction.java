package com.tschanz.geobooster.presentation.actions;

import com.tschanz.geobooster.netz.service.GbDrRepo;
import com.tschanz.geobooster.netz_persistence_sql.service.GbDrSqlRepo;
import com.tschanz.geobooster.presentation.state.GbState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LoadDrAction {
    private final GbDrSqlRepo gbDrSqlRepo;
    private final GbDrRepo gbDrRepo;
    private final GbState gbState;


    public void loadDr() {
        new Thread(() -> {
            try {
                this.gbState.setProgressText("loading dr...");
                var dr = this.gbDrSqlRepo.loadDr();
                this.gbState.setProgressText("loading dr done");

                this.gbState.setProgressText("init repos...");
                this.gbDrRepo.init(dr);
                this.gbState.setProgressText("init repos done");

                this.gbState.getGbDr$().onNext(dr);
                this.gbState.setProgressText("loading dr done", false);
            } catch (Exception e) {
                this.gbState.setErrorText(String.format("error loading dr: %s", e.getMessage()));
            }
        }).start();
    }
}
