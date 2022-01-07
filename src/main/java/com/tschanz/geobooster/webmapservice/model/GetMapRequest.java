package com.tschanz.geobooster.webmapservice.model;

import com.tschanz.geobooster.geofeature.model.Extent;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_layer.model.MapLayerType;
import com.tschanz.geobooster.map_style.model.MapStyle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;


@Getter
@RequiredArgsConstructor
public class GetMapRequest {
    public static final String REQ_GETMAP = "GetMap";
    public static final String REQ_FORMAT_PNG = "image/png8";
    public static final String REQ_FORMAT_UTFGRID = "application/json;type=utfgrid";
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
    private final Collection<MapLayerType> mapLayerTypes;
    private final Collection<MapStyle> mapStyles;
    private final String format;
    private final boolean transparent;
    private final GetMapRequestViewParams viewparams;
    private final int width;
    private final int height;
    private final String srs;
    private final Extent bbox;


    public static GetMapRequest fromParams(Map<String,String> params) {
        return new GetMapRequest(
            params.get(PARAM_VERSION),
            GetMapRequestLayers.parse(params.get(PARAM_LAYERS)),
            GetMapRequestStyles.parse(params.get(PARAM_STYLES)),
            params.get(PARAM_FORMAT),
            Boolean.parseBoolean(params.get(PARAM_TRANSPARENT)),
            GetMapRequestViewParams.parse(params.get(PARAM_VIEWPARAMS)),
            Integer.parseInt(params.get(PARAM_WIDTH)),
            Integer.parseInt(params.get(PARAM_HEIGHT)),
            params.get(PARAM_SRS),
            GetMapRequestExtentConverter.fromRest(params.get(PARAM_BBOX), params.get(PARAM_SRS))
        );
    }


    public float getZoomLevel() {
        var minDeg = CoordinateConverter.convertToEpsg4326(this.getBbox().getMinCoordinate()).getLongitude();
        var maxDeg = CoordinateConverter.convertToEpsg4326(this.getBbox().getMaxCoordinate()).getLongitude();

        return (float) (Math.log(360.0 / (maxDeg - minDeg)) / Math.log(2));
    }
}
