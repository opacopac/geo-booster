package com.tschanz.geobooster.actions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Getter
@Service
@RequiredArgsConstructor
public class GbActions {
    private final SelectDrAction selectDrAction;
    private final LoadDrAction loadDrAction;
}
