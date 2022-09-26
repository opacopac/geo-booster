package com.tschanz.geobooster.versioning.model;

import java.time.LocalDate;


public interface Version extends HasId, HasPflegestatus {
    long getId();
    long getElementId();
    LocalDate getGueltigVon();
    LocalDate getGueltigBis();
    Pflegestatus getPflegestatus();
}
