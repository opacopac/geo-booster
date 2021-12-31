package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.presentation.actions.LoadDrAction;
import com.tschanz.geobooster.presentation.actions.SelectDrAction;
import com.tschanz.geobooster.presentation.controller.ConnectionViewController;
import com.tschanz.geobooster.presentation.presenter.ConnectionViewPresenter;
import io.reactivex.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

import java.util.List;


public class JfxConnectionViewController implements ConnectionViewPresenter, ConnectionViewController {
    public Button loadDrButton;
    @FXML private CheckBox quickStartCheckBox;
    @FXML private ChoiceBox<String> connectionChoiceBox;
    private SelectDrAction selectDrAction;
    private LoadDrAction loadDrAction;


    @Override
    public void bindState(Observable<List<String>> connectionList$, Observable<Boolean> hasQuickStartDr$) {
        connectionList$.subscribe(connectionList -> {
            if (connectionList != null) {
                connectionChoiceBox.getItems().clear();
                for (var i = 0; i < connectionList.size(); i++) {
                    connectionChoiceBox.getItems().add(i, connectionList.get(i));
                }
            }
        });

        hasQuickStartDr$.subscribe(hasQuickStartDr -> {
            if (!hasQuickStartDr) {
                this.quickStartCheckBox.setSelected(false);
                this.quickStartCheckBox.setDisable(true);
            } else {
                this.quickStartCheckBox.setSelected(true);
                this.quickStartCheckBox.setDisable(false);
            }
        });
    }


    @Override
    public void bindActions(SelectDrAction selectDrAction, LoadDrAction loadDrAction) {
        this.selectDrAction = selectDrAction;
        this.loadDrAction = loadDrAction;
    }


    @FXML
    private void onLoadDrButtonClicked(ActionEvent actionEvent) {
        var useQuickStartDr = this.quickStartCheckBox.isSelected();
        this.loadDrAction.loadDr(useQuickStartDr);
    }


    @FXML
    private void onConnectionSelected(ActionEvent actionEvent) {
        var idx = this.connectionChoiceBox.getSelectionModel().getSelectedIndex();
        this.selectDrAction.selectConnection(idx);
        this.loadDrButton.setDisable(false);
    }
}
