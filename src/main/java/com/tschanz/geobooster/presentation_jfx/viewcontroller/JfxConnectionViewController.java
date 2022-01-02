package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.presentation.actions.LoadDrAction;
import com.tschanz.geobooster.presentation.actions.SelectDrAction;
import com.tschanz.geobooster.presentation.controller.ConnectionViewController;
import com.tschanz.geobooster.presentation.presenter.ConnectionViewPresenter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;


public class JfxConnectionViewController implements ConnectionViewPresenter, ConnectionViewController {
    public Button loadDrButton;
    @FXML private ChoiceBox<String> connectionChoiceBox;
    private SelectDrAction selectDrAction;
    private LoadDrAction loadDrAction;


    @Override
    public void bindState(ConnectionState connectionState) {
        for (var i = 0; i < connectionState.getConnectionList().size(); i++) {
            connectionChoiceBox.getItems().add(i, connectionState.getConnectionList().get(i));
        }
    }


    @Override
    public void bindActions(SelectDrAction selectDrAction, LoadDrAction loadDrAction) {
        this.selectDrAction = selectDrAction;
        this.loadDrAction = loadDrAction;
    }


    @FXML
    private void onLoadDrButtonClicked(ActionEvent actionEvent) {
        this.loadDrAction.loadDr();
    }


    @FXML
    private void onConnectionSelected(ActionEvent actionEvent) {
        var idx = this.connectionChoiceBox.getSelectionModel().getSelectedIndex();
        this.selectDrAction.selectConnection(idx);
        this.loadDrButton.setDisable(false);
    }
}
