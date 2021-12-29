package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.presentation.actions.GbActions;
import com.tschanz.geobooster.presentation.controller.MainViewController;
import com.tschanz.geobooster.presentation.presenter.MainViewPresenter;
import com.tschanz.geobooster.presentation.state.GbState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;


public class JfxMainViewController implements MainViewController, MainViewPresenter {
    @FXML private AnchorPane cachingStatsView;
    @FXML private AnchorPane servingStatsView;
    @FXML private AnchorPane statusBarView;
    @FXML private JfxCachingStatsViewController cachingStatsViewController;
    @FXML private JfxServingStatsViewController servingStatsViewController;
    @FXML private JfxStatusBarViewController statusBarViewController;
    private GbActions gbActions;


    @FXML
    private void onLoadDrButtonClicked(ActionEvent actionEvent) {
        new Thread(() -> {
            this.gbActions.loadDr();
        }).start();
    }


    @Override
    public void bindAction(GbActions gbActions) {
        this.gbActions = gbActions;
    }


    @Override
    public void bindState(GbState gbState) {
        this.cachingStatsViewController.bindState(gbState.getGbDr$());
        this.servingStatsViewController.bindState(gbState.getWmsStats$());
        this.statusBarViewController.bindState(gbState.getIsInProgress$(), gbState.getProgressText$());
    }
}
