package com.tschanz.geobooster.presentation.actions;

import com.tschanz.geobooster.presentation.service.ConnectionPropertySelector;
import com.tschanz.geobooster.presentation.state.GbState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SelectDrAction {
    private final GbState gbState;
    private final ConnectionPropertySelector connectionPropertySelector;


    public void selectConnection(int index) {
        this.connectionPropertySelector.selectConnection(index);
    }
}
