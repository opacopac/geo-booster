package com.tschanz.geobooster.versioning.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


@Getter
@RequiredArgsConstructor
public class VersionInfo<T> {
    private final long id;
    private final T element;
    private final LocalDate gueltigVon;
    private final LocalDate gueltigBis;
}
