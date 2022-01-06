package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.netz_repo.model.NetzRepoState;
import com.tschanz.geobooster.presentation.presenter.CachingStatsViewPresenter;
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
    @FXML private Label awbCountLabel;
    @FXML private Label betrVersionCountLabel;
    @FXML private Label verwVersionCountLabel;
    @FXML private Label hstVersionCountLabel;
    @FXML private Label vkVersionCountLabel;
    @FXML private Label tkVersionCountLabel;
    @FXML private Label awbVersionCountLabel;
    @FXML private ProgressIndicator betrProgressIndicator;
    @FXML private ProgressIndicator verwProgressIndicator;
    @FXML private ProgressIndicator hstProgressIndicator;
    @FXML private ProgressIndicator vkProgressIndicator;
    @FXML private ProgressIndicator tkProgressIndicator;
    @FXML private ProgressIndicator awbProgressIndicator;


    @Override
    public void bindState(NetzRepoState netzRepoState) {
        // betreiber
        netzRepoState.getBetreiberRepoState().getLoadedElementCount$().subscribe(elementCount -> {
            Platform.runLater(() -> betrCountLabel.setText(elementCount.toString()));
        });
        netzRepoState.getBetreiberRepoState().getLoadedVersionCount$().subscribe(versionCount -> {
            Platform.runLater(() -> betrVersionCountLabel.setText(versionCount.toString()));
        });
        netzRepoState.getBetreiberRepoState().getIsLoading$().subscribe(isLoading -> {
            Platform.runLater(() -> betrProgressIndicator.setVisible(isLoading));
        });

        // verwaltung
        netzRepoState.getVerwaltungRepoState().getLoadedElementCount$().subscribe(elementCount -> {
            Platform.runLater(() -> verwCountLabel.setText(elementCount.toString()));
        });
        netzRepoState.getVerwaltungRepoState().getLoadedVersionCount$().subscribe(versionCount -> {
            Platform.runLater(() -> verwVersionCountLabel.setText(versionCount.toString()));
        });
        netzRepoState.getVerwaltungRepoState().getIsLoading$().subscribe(isLoading -> {
            Platform.runLater(() -> verwProgressIndicator.setVisible(isLoading));
        });

        // hst
        netzRepoState.getHaltestelleRepoState().getLoadedElementCount$().subscribe(elementCount -> {
            Platform.runLater(() -> hstCountLabel.setText(elementCount.toString()));
        });
        netzRepoState.getHaltestelleRepoState().getLoadedVersionCount$().subscribe(versionCount -> {
            Platform.runLater(() -> hstVersionCountLabel.setText(versionCount.toString()));
        });
        netzRepoState.getHaltestelleRepoState().getIsLoading$().subscribe(isLoading -> {
            Platform.runLater(() -> hstProgressIndicator.setVisible(isLoading));
        });

        // vk
        netzRepoState.getVerkehrskanteRepoState().getLoadedElementCount$().subscribe(elementCount -> {
            Platform.runLater(() -> vkCountLabel.setText(elementCount.toString()));
        });
        netzRepoState.getVerkehrskanteRepoState().getLoadedVersionCount$().subscribe(versionCount -> {
            Platform.runLater(() -> vkVersionCountLabel.setText(versionCount.toString()));
        });
        netzRepoState.getVerkehrskanteRepoState().getIsLoading$().subscribe(isLoading -> {
            Platform.runLater(() -> vkProgressIndicator.setVisible(isLoading));
        });

        // tk
        netzRepoState.getTarifkanteRepoState().getLoadedElementCount$().subscribe(elementCount -> {
            Platform.runLater(() -> tkCountLabel.setText(elementCount.toString()));
        });
        netzRepoState.getTarifkanteRepoState().getLoadedVersionCount$().subscribe(versionCount -> {
            Platform.runLater(() -> tkVersionCountLabel.setText(versionCount.toString()));
        });
        netzRepoState.getTarifkanteRepoState().getIsLoading$().subscribe(isLoading -> {
            Platform.runLater(() -> tkProgressIndicator.setVisible(isLoading));
        });

        // awb
        netzRepoState.getAwbRepoState().getLoadedElementCount$().subscribe(elementCount -> {
            Platform.runLater(() -> awbCountLabel.setText(elementCount.toString()));
        });
        netzRepoState.getAwbRepoState().getLoadedVersionCount$().subscribe(versionCount -> {
            Platform.runLater(() -> awbVersionCountLabel.setText(versionCount.toString()));
        });
        netzRepoState.getAwbRepoState().getIsLoading$().subscribe(isLoading -> {
            Platform.runLater(() -> awbProgressIndicator.setVisible(isLoading));
        });
    }
}
