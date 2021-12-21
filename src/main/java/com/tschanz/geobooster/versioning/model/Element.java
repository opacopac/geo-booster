package com.tschanz.geobooster.versioning.model;

public interface Element<T, K extends Version<T>> {
    ElementInfo<T, K> getElementInfo();
}
