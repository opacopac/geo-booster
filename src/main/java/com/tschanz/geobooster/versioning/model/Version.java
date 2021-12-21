package com.tschanz.geobooster.versioning.model;


public interface Version<T> {
    VersionInfo<T> getVersionInfo();
}

