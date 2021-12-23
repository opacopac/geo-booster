package com.tschanz.geobooster.graphics.service;

import com.tschanz.geobooster.graphics.model.GbImage;
import com.tschanz.geobooster.graphics.model.GbLineStyle;
import com.tschanz.geobooster.graphics.model.GbPointStyle;

import java.io.ByteArrayOutputStream;


public interface ImageService {
    GbImage createImage(int width, int height, boolean isBgTransparent);

    void drawLine(GbImage image, int x1, int y1, int x2, int y2, GbLineStyle style);

    void drawPoint(GbImage image, int x, int y, GbPointStyle style);

    ByteArrayOutputStream getPngByteStream(GbImage image);
}
