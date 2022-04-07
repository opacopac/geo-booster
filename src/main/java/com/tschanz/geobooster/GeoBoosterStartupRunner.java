package com.tschanz.geobooster;

import com.tschanz.geobooster.presentation.actions.GbActions;
import com.tschanz.geobooster.presentation.model.GbState;
import com.tschanz.geobooster.presentation_jfx.JfxGeoBoosterApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GeoBoosterStartupRunner implements ApplicationRunner {
    private final AppProperties appProperties;
    private final GbActions gbActions;
    private final GbState gbState;


    @Override
    public void run(ApplicationArguments args) {
        if (this.appProperties.isUseServerMode()) {
            var connectionId = this.appProperties.getServerModeConnectionId();
            var connActions = this.gbActions.getConnectionActions();
            connActions.setTrackChanges(true);
            connActions.selectConnection(connectionId);
            connActions.loadDr();
        } else {
            JfxGeoBoosterApplication.main2(this.gbState, this.gbActions);
            System.exit(0);
        }
    }
}
