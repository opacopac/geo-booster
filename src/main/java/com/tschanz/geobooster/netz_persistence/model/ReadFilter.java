package com.tschanz.geobooster.netz_persistence.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class ReadFilter {
    private final List<Long> idList;
    private final LocalDateTime changedSince;
}
