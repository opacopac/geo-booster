package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.presentation.actions.GbActions;
import com.tschanz.geobooster.presentation.controller.MainViewController;
import com.tschanz.geobooster.presentation.presenter.MainViewPresenter;
import com.tschanz.geobooster.presentation.state.GbState;
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
            gbActions.getSelectDrAction(),
            gbActions.getLoadDrAction()
        );
    }


    @Override
    public void bindState(GbState gbState) {
        this.connectionViewController.bindState(gbState.getConnectionList$(), gbState.getHasQuickStartDr$());
        this.cachingStatsViewController.bindState(gbState.getGbDr$());
        this.servingStatsViewController.bindState(gbState.getWmsStats$());
        this.statusBarViewController.bindState(gbState.getIsInProgress$(), gbState.getProgressText$());
    }
}
