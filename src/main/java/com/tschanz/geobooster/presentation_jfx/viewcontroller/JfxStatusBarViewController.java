package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.presentation.presenter.StatusBarViewPresenter;
import io.reactivex.Observable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;


public class JfxStatusBarViewController implements StatusBarViewPresenter {
    @FXML private Label infoLabel;
    @FXML private Label errorLabel;
    @FXML private ProgressBar progressBar;


    @Override
    public void bindState(Observable<Boolean> isInProgress$, Observable<String> progressText$) {
        isInProgress$.subscribe(isInProgress -> {
            Platform.runLater(() -> {
                this.progressBar.setVisible(isInProgress);
            });
        });

        progressText$.subscribe(progressText -> {
            Platform.runLater(() -> {
                this.infoLabel.setText(progressText);
            });
        });
    }
}
