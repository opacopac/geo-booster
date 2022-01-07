package com.tschanz.geobooster.map_tile_composer.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.map_style.model.TarifkanteStyle;
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
            CoordinateConverter.convertToEpsg3857(this.tarifkanteRepo.getStartCoordinate(tkV)),
            CoordinateConverter.convertToEpsg3857(this.tarifkanteRepo.getEndCoordinate(tkV)),
            TarifkanteStyle.getStyle(zoomLevel)
        );
    }
}
