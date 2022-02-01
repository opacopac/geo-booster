package com.tschanz.geobooster.utfgrid_composer.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_style.model.KanteBlueBoldDashedStyle;
import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import com.tschanz.geobooster.utfgrid.model.UtfGridLineItem;
import com.tschanz.geobooster.util.model.Tuple2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;


@Component
@RequiredArgsConstructor
public class UtfGridTarifkanteConverter {
    private final TarifkanteRepo tkRepo;


    public UtfGridLineItem toUtfGrid(TarifkanteVersion tkVersion, float zoomLevel, Collection<MapStyle> mapStyles, boolean showUnmapped) {
        var hst1E = this.tkRepo.getStartHaltestelle(tkVersion);
        var hst2E = this.tkRepo.getEndHaltestelle(tkVersion);
        var hst1V = this.tkRepo.getStartHaltestelleVersion(tkVersion);
        var hst2V = this.tkRepo.getEndHaltestelleVersion(tkVersion);
        var idPrefix = showUnmapped ? "UNMAPPED_TARIFKANTEN." : "TARIFKANTEN.";
        var latLon1 = CoordinateConverter.convertToEpsg4326(hst1V.getCoordinate());
        var latLon2 = CoordinateConverter.convertToEpsg4326(hst2V.getCoordinate());

        return new UtfGridLineItem(
            hst1V.getCoordinate(),
            hst2V.getCoordinate(),
            KanteBlueBoldDashedStyle.WIDTH.getWidth(zoomLevel),
            Arrays.asList(
                new Tuple2<>("id", idPrefix + tkVersion.getId()),
                new Tuple2<>("ID", tkVersion.getId()),
                new Tuple2<>("HS1_ID", hst1V.getId()),
                new Tuple2<>("HS1_UIC_CODE", hst1E.getUicCode()),
                new Tuple2<>("HS1_NAME", hst1V.getName()),
                new Tuple2<>("HS1_LAT", (float) latLon1.getLatitude()),
                new Tuple2<>("HS1_LNG", (float) latLon1.getLongitude()),
                new Tuple2<>("HS2_ID", hst2V.getId()),
                new Tuple2<>("HS2_UIC_CODE", hst2E.getUicCode()),
                new Tuple2<>("HS2_NAME", hst2V.getName()),
                new Tuple2<>("HS2_LAT", (float) latLon2.getLatitude()),
                new Tuple2<>("HS2_LNG", (float) latLon2.getLongitude()),
                new Tuple2<>("GUELTIG_VON", tkVersion.getGueltigVon()),
                new Tuple2<>("GUELTIG_BIS", tkVersion.getGueltigBis()),
                new Tuple2<>("TERMINIERT_PER", tkVersion.getTerminiertPer())
            )
        );
    }
}
