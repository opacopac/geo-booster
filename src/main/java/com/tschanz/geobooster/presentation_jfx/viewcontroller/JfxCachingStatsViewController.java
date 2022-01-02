package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.presentation.presenter.CachingStatsViewPresenter;
import com.tschanz.geobooster.state_netz.NetzState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;


public class JfxCachingStatsViewController implements CachingStatsViewPresenter {
    @FXML private Label betrCountLabel;
    @FXML private Label verwCountLabel;
    @FXML private Label hstCountLabel;
    @FXML private Label vkCountLabel;
    @FXML private Label tkCountLabel;
    @FXML private Label betrVersionCountLabel;
    @FXML private Label verwVersionCountLabel;
    @FXML private Label hstVersionCountLabel;
    @FXML private Label vkVersionCountLabel;
    @FXML private Label tkVersionCountLabel;
    @FXML private ProgressIndicator betrProgressIndicator;
    @FXML private ProgressIndicator verwProgressIndicator;
    @FXML private ProgressIndicator hstProgressIndicator;
    @FXML private ProgressIndicator vkProgressIndicator;
    @FXML private ProgressIndicator tkProgressIndicator;


    @Override
    public void bindState(NetzState netzState) {
        netzState.getBetreiberState().getLoadedElementCount$().subscribe(elementCount -> {
            Platform.runLater(() -> betrCountLabel.setText(elementCount.toString()));
        });
        netzState.getBetreiberState().getLoadedVersionCount$().subscribe(versionCount -> {
            Platform.runLater(() -> betrVersionCountLabel.setText(versionCount.toString()));
        });
        netzState.getBetreiberState().getIsLoading$().subscribe(isLoading -> {
            Platform.runLater(() -> betrProgressIndicator.setVisible(isLoading));
        });

        netzState.getVerwaltungState().getLoadedElementCount$().subscribe(elementCount -> {
            Platform.runLater(() -> verwCountLabel.setText(elementCount.toString()));
        });
        netzState.getVerwaltungState().getLoadedVersionCount$().subscribe(versionCount -> {
            Platform.runLater(() -> verwVersionCountLabel.setText(versionCount.toString()));
        });
        netzState.getVerwaltungState().getIsLoading$().subscribe(isLoading -> {
            Platform.runLater(() -> verwProgressIndicator.setVisible(isLoading));
        });

        netzState.getHaltestelleState().getLoadedElementCount$().subscribe(elementCount -> {
            Platform.runLater(() -> hstCountLabel.setText(elementCount.toString()));
        });
        netzState.getHaltestelleState().getLoadedVersionCount$().subscribe(versionCount -> {
            Platform.runLater(() -> hstVersionCountLabel.setText(versionCount.toString()));
        });
        netzState.getHaltestelleState().getIsLoading$().subscribe(isLoading -> {
            Platform.runLater(() -> hstProgressIndicator.setVisible(isLoading));
        });

        netzState.getVerkehrskanteState().getLoadedElementCount$().subscribe(elementCount -> {
            Platform.runLater(() -> vkCountLabel.setText(elementCount.toString()));
        });
        netzState.getVerkehrskanteState().getLoadedVersionCount$().subscribe(versionCount -> {
            Platform.runLater(() -> vkVersionCountLabel.setText(versionCount.toString()));
        });
        netzState.getVerkehrskanteState().getIsLoading$().subscribe(isLoading -> {
            Platform.runLater(() -> vkProgressIndicator.setVisible(isLoading));
        });

        netzState.getTarifkanteState().getLoadedElementCount$().subscribe(elementCount -> {
            Platform.runLater(() -> tkCountLabel.setText(elementCount.toString()));
        });
        netzState.getTarifkanteState().getLoadedVersionCount$().subscribe(versionCount -> {
            Platform.runLater(() -> tkVersionCountLabel.setText(versionCount.toString()));
        });
        netzState.getTarifkanteState().getIsLoading$().subscribe(isLoading -> {
            Platform.runLater(() -> tkProgressIndicator.setVisible(isLoading));
        });
    }
}
