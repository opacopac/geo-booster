package com.tschanz.geobooster.webmapservice.controller;

import com.tschanz.geobooster.GbDataSourceProperties;
import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.webmapservice.model.GetMapRequest;
import com.tschanz.geobooster.webmapservice.service.WmsPngService;
import com.tschanz.geobooster.webmapservice.service.WmsUtfGridService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
@CrossOrigin
@RequiredArgsConstructor
public class WmsController {
    private static final String REQ_SERVICE_WMS = "WMS";
    private static final String REQ_GETMAP = "GetMap";
    private static final String REQ_FORMAT_PNG = "image/png8";
    private static final String REQ_FORMAT_UTFGRID = "application/json;type=utfgrid";
    private static final String RESP_UTFGRID_CONTENT_TYPE = "application/json;type=utfgrid";
    private static final String RESP_CONTENT_DISPO_KEY = "Content-Disposition";
    private static final String RESP_CONTENT_DISPO_VALUE = "inline; filename=";
    private static final Logger logger = LogManager.getLogger(WmsController.class);

    private final GbDataSourceProperties gbProperties;
    private final WmsUtfGridService wmsUtfGridService;
    private final WmsPngService wmsPngService;


    @GetMapping(
        value = "/geo/wms",
        params = {"service=" + REQ_SERVICE_WMS, "request=" + REQ_GETMAP, "format=" + REQ_FORMAT_PNG},
        produces = MediaType.IMAGE_PNG_VALUE
    )
    @ResponseBody
    public byte[] wmsGetMapPngHandler(@RequestParam Map<String,String> allParams, HttpServletResponse response) {
        var mapRequest = GetMapRequest.fromParams(allParams);

        logger.info(String.format("PNG request for bbox %s,%s %s,%s",
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMinCoordinate()).getLatitude(),
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMinCoordinate()).getLongitude(),
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMaxCoordinate()).getLatitude(),
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMaxCoordinate()).getLongitude()
        ));

        var fileName = this.getFilename(mapRequest) + ".png";
        response.setHeader(RESP_CONTENT_DISPO_KEY, RESP_CONTENT_DISPO_VALUE + fileName);

        var pngResponse = this.wmsPngService.getResponse(mapRequest);

        return pngResponse.getImgBytes();
    }


    @GetMapping(
        value = "/geo/wms",
        params = {"service=" + REQ_SERVICE_WMS, "request=" + REQ_GETMAP, "format=" + REQ_FORMAT_UTFGRID},
        produces = RESP_UTFGRID_CONTENT_TYPE
    )
    @CrossOrigin(allowedHeaders = "X-NewRelic-App-Data")
    @ResponseBody
    public String wmsGetMapUtfGridHandler(@RequestParam Map<String,String> allParams, HttpServletResponse response) {
        var mapRequest = GetMapRequest.fromParams(allParams);

        logger.info(String.format("UTF grid request for bbox %s,%s %s,%s",
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMinCoordinate()).getLatitude(),
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMinCoordinate()).getLongitude(),
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMaxCoordinate()).getLatitude(),
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMaxCoordinate()).getLongitude()
        ));

        var fileName = this.getFilename(mapRequest);
        response.setHeader(RESP_CONTENT_DISPO_KEY, RESP_CONTENT_DISPO_VALUE + fileName);

        var utfGridResponse = this.wmsUtfGridService.getResponse(mapRequest);

        return utfGridResponse.getText();
    }


    // TODO: temp for performance measurements
    @GetMapping(value = "/nix")
    @ResponseBody
    public String nixHandler() {
        logger.info("NIX request");

        return "NIX";
    }


    private String getFilename(GetMapRequest getMapRequest) {
        if (getMapRequest.hasLayerHaltestellen()) {
            return "novap-HALTESTELLEN";
        }

        if (getMapRequest.hasLayerVerkehrskanten()) {
            return "novap-VERKEHRSKANTEN";
        }

        if (getMapRequest.hasLayerTarifkanten()) {
            return "novap-TARIFKANTEN";
        }

        return "unknown";
    }
}
