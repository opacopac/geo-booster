package com.tschanz.geobooster.presentation.actions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Getter
@Service
@RequiredArgsConstructor
public class GbActions {
    private final ConnectionActions connectionActions;
}
