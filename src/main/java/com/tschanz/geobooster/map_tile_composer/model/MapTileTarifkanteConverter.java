package com.tschanz.geobooster.map_tile_composer.model;

import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.map_style.service.StyleHelper;
import com.tschanz.geobooster.map_tile.model.MapTileLine;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_repo.service.TarifkanteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
@RequiredArgsConstructor
public class MapTileTarifkanteConverter {
    private final TarifkanteRepo tarifkanteRepo;


    public MapTileLine toMapTile(TarifkanteVersion tkV, float zoomLevel, Collection<MapStyle> mapStyles) {
        return new MapTileLine(
            this.tarifkanteRepo.getStartCoordinate(tkV),
            this.tarifkanteRepo.getEndCoordinate(tkV),
            StyleHelper.getLineStyle(mapStyles, zoomLevel)
        );
    }
}
