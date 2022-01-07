package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.presentation.actions.GbActions;
import com.tschanz.geobooster.presentation.controller.MainViewController;
import com.tschanz.geobooster.presentation.model.GbState;
import com.tschanz.geobooster.presentation.presenter.MainViewPresenter;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


public class JfxMainViewController implements MainViewController, MainViewPresenter {
    private static final String GB_IMAGE = "/geobooster_sm.png";

    @FXML private ImageView gbImageView;
    @FXML private AnchorPane connectionView;
    @FXML private AnchorPane cachingStatsView;
    @FXML private AnchorPane servingStatsView;
    @FXML private AnchorPane statusBarView;
    @FXML private JfxConnectionViewController connectionViewController;
    @FXML private JfxCachingStatsViewController cachingStatsViewController;
    @FXML private JfxServingStatsViewController servingStatsViewController;
    @FXML private JfxStatusBarViewController statusBarViewController;


    @FXML
    public void initialize() {
        var img = new Image(GB_IMAGE);
        this.gbImageView.setImage(img);
    }


    @Override
    public void bindActions(GbActions gbActions) {
        this.connectionViewController.bindActions(
            gbActions.getConnectionActions()
        );
    }


    @Override
    public void bindState(GbState gbState) {
        this.connectionViewController.bindState(gbState.getConnectionState());
        this.cachingStatsViewController.bindState(
            gbState.getNetzRepoState(),
            gbState.getRtmRepoState(),
            gbState.getTarifRepoState()
        );
        this.servingStatsViewController.bindState(gbState.getWmsState());
        this.statusBarViewController.bindState(gbState.getProgressState());
    }
}
