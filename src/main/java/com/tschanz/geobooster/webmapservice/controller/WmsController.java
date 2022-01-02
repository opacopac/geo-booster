package com.tschanz.geobooster.webmapservice.controller;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.netz_maptile.service.NetzMapTileService;
import com.tschanz.geobooster.netz_utfgrid.service.NetzUtfGridService;
import com.tschanz.geobooster.webmapservice.model.GetMapRequest;
import com.tschanz.geobooster.webmapservice.model.NetzMapTileRequestConverter;
import com.tschanz.geobooster.webmapservice.model.NetzUtfGridRequestConverter;
import com.tschanz.geobooster.webmapservice.model.WmsState;
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
import java.time.Instant;
import java.util.Map;


@Controller
@CrossOrigin
@RequiredArgsConstructor
public class WmsController {
    private static final String REQ_SERVICE_WMS = "WMS";
    private static final String RESP_UTFGRID_CONTENT_TYPE = "application/json;type=utfgrid";
    private static final String RESP_CONTENT_DISPO_KEY = "Content-Disposition";
    private static final String RESP_CONTENT_DISPO_VALUE = "inline; filename=";
    private static final Logger logger = LogManager.getLogger(WmsController.class);

    private final NetzUtfGridService netzUtfGridService;
    private final NetzMapTileService netzMapTileService;
    private final WmsState wmsState;


    @GetMapping(
        value = "/geo/wms",
        params = {"service=" + REQ_SERVICE_WMS, "request=" + GetMapRequest.REQ_GETMAP, "format=" + GetMapRequest.REQ_FORMAT_PNG},
        produces = MediaType.IMAGE_PNG_VALUE
    )
    @ResponseBody
    public byte[] wmsGetMapPngHandler(@RequestParam Map<String,String> allParams, HttpServletResponse response) {
        var startMs = Instant.now().toEpochMilli();
        var mapRequest = GetMapRequest.fromParams(allParams);
        this.logMapRequest("PNG", mapRequest);

        var fileName = this.getFilename(mapRequest) + ".png";
        response.setHeader(RESP_CONTENT_DISPO_KEY, RESP_CONTENT_DISPO_VALUE + fileName);

        var mapTileRequest = NetzMapTileRequestConverter.fromMapRequest(mapRequest);
        var mapTileResponse = this.netzMapTileService.getResponse(mapTileRequest);

        var msElapsed = Instant.now().toEpochMilli() - startMs;
        this.wmsState.incPngRequestCount();
        this.wmsState.nextPngRequestMs(msElapsed);

        return mapTileResponse.getImgBytes();
    }


    @GetMapping(
        value = "/geo/wms",
        params = {"service=" + REQ_SERVICE_WMS, "request=" + GetMapRequest.REQ_GETMAP, "format=" + GetMapRequest.REQ_FORMAT_UTFGRID},
        produces = RESP_UTFGRID_CONTENT_TYPE
    )
    @ResponseBody
    public String wmsGetMapUtfGridHandler(@RequestParam Map<String,String> allParams, HttpServletResponse response) {
        var startMs = Instant.now().toEpochMilli();
        var mapRequest = GetMapRequest.fromParams(allParams);
        this.logMapRequest("UTF grid", mapRequest);

        var fileName = this.getFilename(mapRequest);
        response.setHeader(RESP_CONTENT_DISPO_KEY, RESP_CONTENT_DISPO_VALUE + fileName);

        var utfGridRequest = NetzUtfGridRequestConverter.fromMapRequest(mapRequest);
        var utfGridResponse = this.netzUtfGridService.getResponse(utfGridRequest);

        var msElapsed = Instant.now().toEpochMilli() - startMs;
        this.wmsState.incUtfGridRequestCount();
        this.wmsState.nextUtfGridRequestMs(msElapsed);

        return utfGridResponse.getText();
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


    private void logMapRequest(String requestType, GetMapRequest mapRequest) {
        logger.info(String.format("%s request for bbox %s,%s %s,%s",
            requestType,
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMinCoordinate()).getLatitude(),
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMinCoordinate()).getLongitude(),
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMaxCoordinate()).getLatitude(),
            CoordinateConverter.convertToEpsg4326(mapRequest.getBbox().getMaxCoordinate()).getLongitude()
        ));
    }
}
