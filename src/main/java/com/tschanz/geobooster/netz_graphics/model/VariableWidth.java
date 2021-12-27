package com.tschanz.geobooster.netz_graphics.model;


public class VariableWidth {
    private final float lowerZoom;
    private final float upperZoom;
    private final float lowerZoomWidth;
    private final float upperZoomWidth;
    private final float widthStep;


    public VariableWidth(
        float lowerZoom,
        float upperZoom,
        float lowerZoomWidth,
        float upperZoomWidth
    ) {
        this.lowerZoom = lowerZoom;
        this.upperZoom = upperZoom;
        this.lowerZoomWidth = lowerZoomWidth;
        this.upperZoomWidth = upperZoomWidth;
        this.widthStep = (upperZoom - lowerZoom) / (upperZoomWidth - lowerZoomWidth);
    }


    public float getWidth(float zoomLevel) {
        if (zoomLevel <= this.lowerZoom) {
            return this.lowerZoomWidth;
        } else if (zoomLevel >= this.upperZoom) {
            return this.upperZoomWidth;
        } else {
            return (zoomLevel - this.lowerZoom) * this.widthStep;
        }
    }
}
