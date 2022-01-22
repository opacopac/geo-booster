package com.tschanz.geobooster.utfgrid_composer.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_style.model.KanteBlackStyle;
import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import com.tschanz.geobooster.utfgrid.model.UtfGridLineItem;
import com.tschanz.geobooster.util.model.Tuple2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;


@Component
@RequiredArgsConstructor
public class UtfGridVerkehrskanteConverter {
    private final VerkehrskanteRepo vkRepo;


    public UtfGridLineItem toUtfGrid(VerkehrskanteVersion vkVersion, float zoomLevel, Collection<MapStyle> mapStyles) {
        var hst1E = this.vkRepo.getStartHaltestelle(vkVersion);
        var hst2E = this.vkRepo.getEndHaltestelle(vkVersion);
        var hst1V = this.vkRepo.getStartHaltestelleVersion(vkVersion);
        var hst2V = this.vkRepo.getEndHaltestelleVersion(vkVersion);
        var latLon1 = CoordinateConverter.convertToEpsg4326(hst1V.getCoordinate());
        var latLon2 = CoordinateConverter.convertToEpsg4326(hst2V.getCoordinate());

        return new UtfGridLineItem(
            hst1V.getCoordinate(),
            hst2V.getCoordinate(),
            KanteBlackStyle.WIDTH.getWidth(zoomLevel),
            Arrays.asList(
                new Tuple2<>("id", "VERKEHRSKANTEN." + vkVersion.getId()),
                new Tuple2<>("ID", vkVersion.getId()),
                new Tuple2<>("HS1_ID", hst1V.getId()),
                new Tuple2<>("HS1_UIC_CODE", hst1E.getUicCode()),
                new Tuple2<>("HS1_NAME", hst1V.getName()),
                new Tuple2<>("HS1_LAT", latLon1.getLatitude()),
                new Tuple2<>("HS1_LNG", latLon1.getLongitude()),
                new Tuple2<>("HS2_ID", hst2V.getId()),
                new Tuple2<>("HS2_UIC_CODE", hst2E.getUicCode()),
                new Tuple2<>("HS2_NAME", hst2V.getName()),
                new Tuple2<>("HS2_LAT", latLon2.getLatitude()),
                new Tuple2<>("HS2_LNG", latLon2.getLongitude()),
                new Tuple2<>("GUELTIG_VON", vkVersion.getGueltigVon()),
                new Tuple2<>("GUELTIG_BIS", vkVersion.getGueltigBis()),
                new Tuple2<>("TERMINIERT_PER", vkVersion.getTerminiertPer())
            )
        );
    }
}
