package com.tschanz.geobooster.map_tile_composer.model;

import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.map_style.service.StyleHelper;
import com.tschanz.geobooster.map_tile.model.MapTileLine;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;


@Component
@RequiredArgsConstructor
public class MapTileVerkehrskanteConverter {
    private final HaltestelleRepo hstRepo;
    private final VerkehrskanteRepo verkehrskanteRepo;


    public MapTileLine toMapTile(VerkehrskanteVersion vkV, float zoomLevel, LocalDate date, Collection<MapStyle> mapStyles) {
        var hst1E = this.verkehrskanteRepo.getStartHaltestelle(vkV);
        var hst2E = this.verkehrskanteRepo.getEndHaltestelle(vkV);
        var hst1V = this.hstRepo.getElementVersionAtDate(hst1E.getId(), date);
        var hst2V = this.hstRepo.getElementVersionAtDate(hst2E.getId(), date);

        return new MapTileLine(
            hst1V.getCoordinate(),
            hst2V.getCoordinate(),
            StyleHelper.getLineStyle(mapStyles, zoomLevel)
        );
    }
}
