package com.tschanz.geobooster.map_tile_composer.model;

import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.map_style.service.StyleHelper;
import com.tschanz.geobooster.map_tile.model.MapTileLine;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_repo.service.HaltestelleRepo;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;


@Component
@RequiredArgsConstructor
public class MapTileTarifkanteConverter {
    private final HaltestelleRepo hstRepo;
    private final TarifkanteRepo tarifkanteRepo;


    public MapTileLine toMapTile(TarifkanteVersion tkV, float zoomLevel, LocalDate date, Collection<MapStyle> mapStyles) {
        var hst1E = this.tarifkanteRepo.getStartHaltestelle(tkV);
        var hst2E = this.tarifkanteRepo.getEndHaltestelle(tkV);
        var hst1V = this.hstRepo.getElementVersionAtDate(hst1E.getId(), date);
        var hst2V = this.hstRepo.getElementVersionAtDate(hst2E.getId(), date);

        return new MapTileLine(
            hst1V.getCoordinate(),
            hst2V.getCoordinate(),
            StyleHelper.getLineStyle(mapStyles, zoomLevel)
        );
    }
}
