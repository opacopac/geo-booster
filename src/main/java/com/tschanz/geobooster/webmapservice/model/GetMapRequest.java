package com.tschanz.geobooster.webmapservice.model;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.geofeature_wms.model.WmsExtentConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;


@Getter
@RequiredArgsConstructor
public class GetMapRequest {
    private static final String LAYER_HALTESTELLEN = "novap:HALTESTELLEN";
    private static final String LAYER_VERKEHRSKANTEN = "novap:VERKEHRSKANTEN";
    private static final String LAYER_TARIFKANTEN = "novap:TARIFKANTEN";
    private static final String PARAM_VERSION = "version";
    private static final String PARAM_LAYERS = "layers";
    private static final String PARAM_STYLES = "styles";
    private static final String PARAM_FORMAT = "format";
    private static final String PARAM_TRANSPARENT = "transparent";
    private static final String PARAM_VIEWPARAMS = "viewparams";
    private static final String PARAM_WIDTH = "width";
    private static final String PARAM_HEIGHT = "height";
    private static final String PARAM_SRS = "srs";
    private static final String PARAM_BBOX = "bbox";

    private final String version;
    private final String layers;
    private final String styles;
    private final String format;
    private final boolean transparent;
    private final GetMapViewParams viewparams;
    private final int width;
    private final int height;
    private final String srs;
    private final Extent bbox;


    public static GetMapRequest fromParams(Map<String,String> params) {
        return new GetMapRequest(
            params.get(PARAM_VERSION),
            params.get(PARAM_LAYERS),
            params.get(PARAM_STYLES),
            params.get(PARAM_FORMAT),
            Boolean.parseBoolean(params.get(PARAM_TRANSPARENT)),
            GetMapViewParams.parse(params.get(PARAM_VIEWPARAMS)),
            Integer.parseInt(params.get(PARAM_WIDTH)),
            Integer.parseInt(params.get(PARAM_HEIGHT)),
            params.get(PARAM_SRS),
            WmsExtentConverter.fromRest(params.get(PARAM_BBOX), params.get(PARAM_SRS))
        );
    }


    public float getZoomLevel() {
        var minDeg = CoordinateConverter.convertToEpsg4326(this.getBbox().getMinCoordinate()).getLongitude();
        var maxDeg = CoordinateConverter.convertToEpsg4326(this.getBbox().getMaxCoordinate()).getLongitude();

        return (float) (Math.log(360.0 / (maxDeg - minDeg)) / Math.log(2));
    }


    public boolean hasLayerHaltestellen() {
        return this.layers.contains(GetMapRequest.LAYER_HALTESTELLEN);
    }


    public boolean hasLayerVerkehrskanten() {
        return this.layers.contains(GetMapRequest.LAYER_VERKEHRSKANTEN);
    }


    public boolean hasLayerTarifkanten() {
        return this.layers.contains(GetMapRequest.LAYER_TARIFKANTEN);
    }
}
