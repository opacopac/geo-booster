package com.tschanz.geobooster.webmapservice.controller;

import com.tschanz.geobooster.webmapservice.model.GetMapRequest;
import com.tschanz.geobooster.webmapservice.service.WmsPngService;
import com.tschanz.geobooster.webmapservice.service.WmsUtf8GridService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


@Controller
@RequiredArgsConstructor
public class WmsController {
    private final static String SERVICE_WMS = "WMS";
    private final static String REQ_GETMAP = "GetMap";
    private final static String FORMAT_PNG = "image/png8";
    private final static String FORMAT_UTFGRID = "application/json;type=utfgrid";

    private final WmsUtf8GridService wmsUtf8GridService;
    private final WmsPngService wmsPngService;


    @GetMapping(
        value = "/geo/wms",
        params = {"service=" + SERVICE_WMS, "request=" + REQ_GETMAP, "format=" + FORMAT_PNG},
        produces = MediaType.IMAGE_PNG_VALUE
    )
    @ResponseBody
    public byte[] wmsGetMapPngHandler(@RequestParam Map<String,String> allParams) {
        var mapRequest = GetMapRequest.fromParams(allParams);
        var pngResponse = this.wmsPngService.getResponse(mapRequest);

        return pngResponse.getImgByteStream().toByteArray();
    }


    @GetMapping(
        value = "/geo/wms",
        params = {"service=" + SERVICE_WMS, "request=" + REQ_GETMAP, "format=" + FORMAT_UTFGRID}
    )
    @ResponseBody
    public String wmsGetMapUtf8GridHandler(@RequestParam Map<String,String> allParams) {
        var mapRequest = GetMapRequest.fromParams(allParams);
        var utf8GridResponse = this.wmsUtf8GridService.getResponse(mapRequest);

        return "MEEP_UTF8_GRID";
    }
}
