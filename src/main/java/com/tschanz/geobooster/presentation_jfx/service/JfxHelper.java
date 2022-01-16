package com.tschanz.geobooster.presentation_jfx.service;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;


public class JfxHelper {
    public static Tooltip createTooltip(String text) {
        var tip = new Tooltip();
        tip.setText(text);
        tip.setShowDelay(new Duration(100));

        return tip;
    }
}
