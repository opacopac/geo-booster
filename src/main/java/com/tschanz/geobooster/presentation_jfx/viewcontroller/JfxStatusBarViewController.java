package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.presentation.presenter.StatusBarViewPresenter;
import com.tschanz.geobooster.presentation_jfx.service.JfxHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;


public class JfxStatusBarViewController implements StatusBarViewPresenter {
    @FXML private Label progressText;
    @FXML private ProgressBar progressBar;


    @Override
    public void bindState(ProgressState progressState) {
        progressState.getIsInProgress$().subscribe(isInProgress -> {
            Platform.runLater(() -> {
                this.progressBar.setVisible(isInProgress);
                this.progressBar.setManaged(isInProgress);
            });
        });

        progressState.getProgressText$().subscribe(progressText -> {
            Platform.runLater(() -> {
                this.progressText.setText(progressText);
                var tooltip = JfxHelper.createTooltip(progressText);
                this.progressText.setTooltip(tooltip);
            });
        });

        progressState.isTextError$().subscribe(isError -> {
            Platform.runLater(() -> {
                var color = isError ? Color.RED : Color.BLACK;
                this.progressText.setTextFill(color);
            });
        });
    }
}
