package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.presentation.presenter.StatusBarViewPresenter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;


public class JfxStatusBarViewController implements StatusBarViewPresenter {
    @FXML private Label infoLabel;
    @FXML private Label errorLabel;
    @FXML private ProgressBar progressBar;


    @Override
    public void bindState(ProgressState progressState) {
        progressState.getIsInProgress$().subscribe(isInProgress -> {
            Platform.runLater(() -> {
                this.progressBar.setVisible(isInProgress);
            });
        });

        progressState.getProgressText$().subscribe(progressText -> {
            Platform.runLater(() -> {
                this.infoLabel.setText(progressText);
            });
        });
    }
}
