package com.tschanz.geobooster.presentation.actions;

import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SelectDrAction {
    private final ConnectionState connectionState;


    public void selectConnection(int index) {
        this.connectionState.selectConnection(index);
    }
}
