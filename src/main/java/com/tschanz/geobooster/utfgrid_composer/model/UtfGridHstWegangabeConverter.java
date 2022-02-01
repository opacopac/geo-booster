package com.tschanz.geobooster.utfgrid_composer.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_style.model.HaltestelleStyle;
import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.rtm_repo.service.HaltestelleWegangabeRepo;
import com.tschanz.geobooster.utfgrid.model.UtfGridPointItem;
import com.tschanz.geobooster.util.model.Tuple2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;


@Component
@RequiredArgsConstructor
public class UtfGridHstWegangabeConverter {
    private final HaltestelleRepo hstRepo;
    private final HaltestelleWegangabeRepo hstWaRepo;


    public UtfGridPointItem toUtfGrid(HaltestelleWegangabeVersion hstWegangabeVersion, float zoomLevel, Collection<MapStyle> mapStyles) {
        var hstVersion = this.hstWaRepo.getHaltestelleVersion(hstWegangabeVersion);
        var hstElement = this.hstRepo.getElement(hstVersion.getElementId());
        var latLon = CoordinateConverter.convertToEpsg4326(hstVersion.getCoordinate());

        return new UtfGridPointItem(
            hstVersion.getCoordinate(),
            HaltestelleStyle.WIDTH.getWidth(zoomLevel),
            Arrays.asList(
                new Tuple2<>("id", "WHALTESTELLEN." + hstVersion.getId()),
                new Tuple2<>("ID", hstVersion.getId()),
                new Tuple2<>("UIC_CODE", hstElement.getUicCode()),
                new Tuple2<>("NAME", hstVersion.getName()),
                new Tuple2<>("LAT", (float) latLon.getLatitude()),
                new Tuple2<>("LNG", (float) latLon.getLongitude())
            )
        );
    }
}
