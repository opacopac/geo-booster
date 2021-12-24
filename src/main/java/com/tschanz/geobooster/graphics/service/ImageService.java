package com.tschanz.geobooster.graphics.service;

import com.tschanz.geobooster.graphics.model.GbImage;


public interface ImageService {
    GbImage createImage(int width, int height, boolean isBgTransparent);
}
