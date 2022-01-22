package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.presentation.actions.ConnectionActions;
import com.tschanz.geobooster.presentation.controller.ConnectionViewController;
import com.tschanz.geobooster.presentation.presenter.ConnectionViewPresenter;
import com.tschanz.geobooster.presentation_jfx.service.JfxHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;


public class JfxConnectionViewController implements ConnectionViewPresenter, ConnectionViewController {
    @FXML private Button loadDrButton;
    @FXML private ChoiceBox<String> connectionChoiceBox;
    @FXML private CheckBox trackChangesCheckBox;
    private ConnectionActions connectionActions;


    @Override
    public void bindState(ConnectionState connectionState) {
        for (var i = 0; i < connectionState.getConnectionList().size(); i++) {
            connectionChoiceBox.getItems().add(i, connectionState.getConnectionList().get(i));
        }
        this.trackChangesCheckBox.setSelected(connectionState.isTrackChanges());

        var tooltip = JfxHelper.createTooltip("tracking changes in TK, AWB, ZP/Zone, RG/Korridor, HST WA");
        this.trackChangesCheckBox.setTooltip(tooltip);
    }


    @Override
    public void bindActions(ConnectionActions connectionActions) {
        this.connectionActions = connectionActions;
    }


    @FXML
    private void onLoadDrButtonClicked(ActionEvent actionEvent) {
        this.connectionActions.loadDr();
    }


    @FXML
    private void onConnectionSelected(ActionEvent actionEvent) {
        var idx = this.connectionChoiceBox.getSelectionModel().getSelectedIndex();
        this.connectionActions.selectConnection(idx);
        this.loadDrButton.setDisable(false);
    }


    @FXML
    private void onCheckForChangesClicked(ActionEvent actionEvent) {
        this.connectionActions.setTrackChanges(this.trackChangesCheckBox.isSelected());
    }
}
