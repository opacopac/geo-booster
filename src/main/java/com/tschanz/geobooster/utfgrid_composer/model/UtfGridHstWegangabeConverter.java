package com.tschanz.geobooster.utfgrid_composer.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_style.model.HaltestelleStyle;
import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.rtm_repo.service.HaltestelleWegangabeRepo;
import com.tschanz.geobooster.utfgrid.model.UtfGridPointItem;
import com.tschanz.geobooster.util.model.KeyValue;
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

        return new UtfGridPointItem(
            CoordinateConverter.convertToEpsg3857(hstVersion.getCoordinate()),
            HaltestelleStyle.WIDTH.getWidth(zoomLevel),
            Arrays.asList(
                new KeyValue<>("id", "WHALTESTELLEN." + hstVersion.getId()),
                new KeyValue<>("ID", hstVersion.getId()),
                new KeyValue<>("UIC_CODE", hstElement.getUicCode()),
                new KeyValue<>("NAME", hstVersion.getName()),
                new KeyValue<>("LAT", hstVersion.getCoordinate().getLatitude()),
                new KeyValue<>("LNG", hstVersion.getCoordinate().getLongitude())
            )
        );
    }
}
