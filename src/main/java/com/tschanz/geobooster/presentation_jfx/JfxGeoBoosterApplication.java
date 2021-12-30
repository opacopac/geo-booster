package com.tschanz.geobooster.presentation_jfx;


import com.tschanz.geobooster.presentation.actions.GbActions;
import com.tschanz.geobooster.presentation.state.GbState;
import com.tschanz.geobooster.presentation_jfx.viewcontroller.JfxMainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.SneakyThrows;


public class JfxGeoBoosterApplication extends Application {
    private static final String MAIN_VIEW_FXML = "/jfx_views/JfxMainView.fxml";
    private static final String WINDOW_TITLE = "GeoBooster";
    private static final String ICON_FILE = "/gbicon.png";
    private static GbState gbState;
    private static GbActions gbActions;


    public static void main2(GbState gbState, GbActions gbActions) {
        JfxGeoBoosterApplication.gbState = gbState;
        JfxGeoBoosterApplication.gbActions = gbActions;
        launch();
    }


    //@Override
    @SneakyThrows
    public void start(Stage mainStage) {
        var mainViewUrl = JfxMainViewController.class.getResource(MAIN_VIEW_FXML);
        var fxmlLoader = new FXMLLoader(mainViewUrl);
        Parent root = fxmlLoader.load();
        JfxMainViewController jfxMainViewController = fxmlLoader.getController();
        jfxMainViewController.bindState(gbState);
        jfxMainViewController.bindAction(gbActions);
        var icon = new Image(ICON_FILE);
        mainStage.getIcons().add(icon);
        mainStage.setTitle(WINDOW_TITLE);
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }
}
