package com.tschanz.geobooster.presentation_jfx.viewcontroller;

import com.tschanz.geobooster.netz.model.GbDr;
import com.tschanz.geobooster.presentation.presenter.CachingStatsViewPresenter;
import io.reactivex.Observable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class JfxCachingStatsViewController implements CachingStatsViewPresenter {
    @FXML private Label betrCountLabel;
    @FXML private Label verwCountLabel;
    @FXML private Label hstCountLabel;
    @FXML private Label vkCountLabel;
    @FXML private Label tkCountLabel;


    @Override
    public void bindState(Observable<GbDr> gbDr$) {
        gbDr$.subscribe(gbDr -> {
            Platform.runLater(() -> {
                if (gbDr != null) {
                    var betrText = gbDr.getBetreiber().size() + " / " + gbDr.getBetreiberVersions().size();
                    betrCountLabel.setText(betrText);
                    var verwText = gbDr.getVerwaltungen().size() + " / " + gbDr.getVerwaltungVersions().size();
                    verwCountLabel.setText(verwText);
                    var hstText = gbDr.getHaltestellen().size() + " / " + gbDr.getHaltestelleVersions().size();
                    hstCountLabel.setText(hstText);
                    var vkText = gbDr.getVerkehrskanten().size() + " / " + gbDr.getVerkehrskanteVersions().size();
                    vkCountLabel.setText(vkText);
                    var tkText = gbDr.getTarifkanten().size() + " / " + gbDr.getTarifkanteVersions().size();
                    tkCountLabel.setText(tkText);
                }
            });
        });
    }
}
