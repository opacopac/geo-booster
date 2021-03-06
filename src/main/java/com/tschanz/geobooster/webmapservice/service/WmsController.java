package com.tschanz.geobooster.webmapservice.service;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_layer.model.MapLayerType;
import com.tschanz.geobooster.map_tile_composer.service.MapTileComposer;
import com.tschanz.geobooster.netz_repo.model.ProgressState;
import com.tschanz.geobooster.utfgrid_composer.service.UtfGridComposer;
import com.tschanz.geobooster.util.service.ExceptionHelper;
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
@CrossOrigin()
@RequiredArgsConstructor
public class WmsController {
    private static final String REQ_SERVICE_WMS = "WMS";
    private static final String RESP_UTFGRID_CONTENT_TYPE = "application/json;type=utfgrid";
    private static final String RESP_CONTENT_DISPO_KEY = "Content-Disposition";
    private static final String RESP_CONTENT_DISPO_VALUE = "inline; filename=";
    private static final String RESP_CACHE_CONTROL_KEY = "Cache-Control";
    private static final String RESP_CACHE_CONTROL_VALUE = "no-cache, no-store, max-age=0, must-revalidate";
    private static final String RESP_PRAGMA_KEY = "Pragma";
    private static final String RESP_PRAGMA_VALUE = "no-cache";
    private static final String RESP_EXPIRES_KEY = "Expires";
    private static final String RESP_EXPIRES_VALUE = "0";
    private static final Logger logger = LogManager.getLogger(WmsController.class);

    private final UtfGridComposer utfGridComposer;
    private final MapTileComposer mapTileComposer;
    private final WmsState wmsState;
    private final ProgressState progressState;


    @GetMapping(
        value = "/geo/wms",
        params = {"service=" + REQ_SERVICE_WMS, "request=" + GetMapRequest.REQ_GETMAP, "format=" + GetMapRequest.REQ_FORMAT_PNG},
        produces = MediaType.IMAGE_PNG_VALUE
    )
    @ResponseBody
    public byte[] wmsGetMapPngHandler(@RequestParam Map<String,String> allParams, HttpServletResponse response) {
        try {
            var startMs = Instant.now().toEpochMilli();
            var mapRequest = GetMapRequest.fromParams(allParams);
            this.logMapRequest("PNG", mapRequest);

            var mapTileRequest = NetzMapTileRequestConverter.fromMapRequest(mapRequest);
            var mapTileResponse = this.mapTileComposer.getResponse(mapTileRequest);

            var msElapsed = Instant.now().toEpochMilli() - startMs;
            this.wmsState.incPngRequestCount();
            this.wmsState.nextPngRequestMs(msElapsed);

            this.setHeaders(response, this.getFilename(mapRequest) + ".png");

            return mapTileResponse.getImgBytes();
        } catch (Exception e) {
            this.logException(e);
            throw e;
        }
    }


    @GetMapping(
        value = "/geo/wms",
        params = {"service=" + REQ_SERVICE_WMS, "request=" + GetMapRequest.REQ_GETMAP, "format=" + GetMapRequest.REQ_FORMAT_UTFGRID},
        produces = RESP_UTFGRID_CONTENT_TYPE
    )
    @ResponseBody
    public String wmsGetMapUtfGridHandler(@RequestParam Map<String,String> allParams, HttpServletResponse response) {
        try {
            var startMs = Instant.now().toEpochMilli();
            var mapRequest = GetMapRequest.fromParams(allParams);
            this.logMapRequest("UTF grid", mapRequest);

            var utfGridRequest = NetzUtfGridRequestConverter.fromMapRequest(mapRequest);
            var utfGridResponse = this.utfGridComposer.getResponse(utfGridRequest);

            var msElapsed = Instant.now().toEpochMilli() - startMs;
            this.wmsState.incUtfGridRequestCount();
            this.wmsState.nextUtfGridRequestMs(msElapsed);

            this.setHeaders(response, this.getFilename(mapRequest));

            return utfGridResponse.getText();
        } catch (Exception e) {
            this.logException(e);
            throw e;
        }
    }


    private String getFilename(GetMapRequest getMapRequest) {
        if (getMapRequest.getMapLayerTypes().contains(MapLayerType.HALTESTELLE)) {
            return "novap-HALTESTELLEN";
        }

        if (getMapRequest.getMapLayerTypes().contains(MapLayerType.VERKEHRSKANTE)) {
            return "novap-VERKEHRSKANTEN";
        }

        if (getMapRequest.getMapLayerTypes().contains(MapLayerType.TARIFKANTE)) {
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


    private void setHeaders(HttpServletResponse response, String filename) {
        response.setHeader(RESP_CONTENT_DISPO_KEY, RESP_CONTENT_DISPO_VALUE + filename);
        response.setHeader(RESP_CACHE_CONTROL_KEY, RESP_CACHE_CONTROL_VALUE);
        response.setHeader(RESP_PRAGMA_KEY, RESP_PRAGMA_VALUE);
        response.setHeader(RESP_EXPIRES_KEY, RESP_EXPIRES_VALUE);
    }


    private void logException(Exception e) {
        logger.error(e);
        this.progressState.updateProgressText(
                String.format("ERROR loading dr: %s", ExceptionHelper.getErrorText(e, "\n")),
                true
        );
        this.progressState.updateIsInProgress(false);
    }
}
