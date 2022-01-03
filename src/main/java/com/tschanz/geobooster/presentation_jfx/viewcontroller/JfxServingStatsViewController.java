package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.presentation.presenter.ServingStatsViewPresenter;
import com.tschanz.geobooster.webmapservice.model.WmsState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class JfxServingStatsViewController implements ServingStatsViewPresenter {
    @FXML private Label pngRequestCountLabel;
    @FXML private Label utfGridRequestCountLabel;
    @FXML private Label pngResponseTimeMs1;
    @FXML private Label utfGridResponseTimeMs1;
    @FXML private Label pngResponseTimeMs2;
    @FXML private Label utfGridResponseTimeMs2;
    @FXML private Label pngResponseTimeMs3;
    @FXML private Label utfGridResponseTimeMs3;

    @Override
    public void bindState(WmsState wmsState) {
        wmsState.getPngRequestCount$().subscribe(requestCount -> {
            Platform.runLater(() -> {
                this.pngRequestCountLabel.setText(requestCount.toString());
            });
        });
        wmsState.getUtfGridRequestCount$().subscribe(requestCount -> {
            Platform.runLater(() -> {
                this.utfGridRequestCountLabel.setText(requestCount.toString());
            });
        });

        wmsState.getPngRequestMs$(1).subscribe(timeMs -> {
            Platform.runLater(() -> {
                this.pngResponseTimeMs1.setText(timeMs + "ms");
            });
        });
        wmsState.getUtfGridRequestMs$(1).subscribe(timeMs -> {
            Platform.runLater(() -> {
                this.utfGridResponseTimeMs1.setText(timeMs + "ms");
            });
        });

        wmsState.getPngRequestMs$(10).subscribe(timeMs -> {
            Platform.runLater(() -> {
                this.pngResponseTimeMs2.setText(timeMs + "ms");
            });
        });
        wmsState.getUtfGridRequestMs$(10).subscribe(timeMs -> {
            Platform.runLater(() -> {
                this.utfGridResponseTimeMs2.setText(timeMs + "ms");
            });
        });

        wmsState.getPngRequestMs$(50).subscribe(timeMs -> {
            Platform.runLater(() -> {
                this.pngResponseTimeMs3.setText(timeMs + "ms");
            });
        });
        wmsState.getUtfGridRequestMs$(50).subscribe(timeMs -> {
            Platform.runLater(() -> {
                this.utfGridResponseTimeMs3.setText(timeMs + "ms");
            });
        });
    }
}
