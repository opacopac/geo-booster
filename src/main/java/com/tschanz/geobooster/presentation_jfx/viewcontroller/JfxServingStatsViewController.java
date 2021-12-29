package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.presentation.presenter.ServingStatsViewPresenter;
import com.tschanz.geobooster.webmapservice.model.WmsStats;
import io.reactivex.Observable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class JfxServingStatsViewController implements ServingStatsViewPresenter {
    @FXML private Label pngRequestsLabel;
    @FXML private  Label utfGridRequestsLabel;

    @Override
    public void bindState(Observable<WmsStats> wmsStats$) {
        wmsStats$.subscribe(wmsStats -> {
            Platform.runLater(() -> {
                if (wmsStats != null) {
                    var pngRequests = String.valueOf(wmsStats.getPngRequests());
                    this.pngRequestsLabel.setText(pngRequests);
                    var utfGridRequests = String.valueOf(wmsStats.getUtfGridRequests());
                    this.utfGridRequestsLabel.setText(utfGridRequests);
                }
            });
        });
    }
}
