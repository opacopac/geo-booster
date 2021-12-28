package com.tschanz.geobooster.versioning.model;

import java.time.LocalDate;


public interface Version {
    long getId();
    long getElementId();
    LocalDate getGueltigVon();
    LocalDate getGueltigBis();

}
