package com.tschanz.geobooster.graphics.model;

public interface GbImage {
    void drawLine(int x1, int y1, int x2, int y2, GbLineStyle style);

    void drawPoint(int x, int y, GbPointStyle style);

    byte[] getBytes();
}
