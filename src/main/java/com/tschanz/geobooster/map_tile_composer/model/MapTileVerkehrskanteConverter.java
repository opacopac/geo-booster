package com.tschanz.geobooster.map_tile_composer.model;

import com.tschanz.geobooster.geofeature.service.CoordinateConverter;
import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.map_style.model.VerkehrskanteBlackStyle;
import com.tschanz.geobooster.map_style.model.VerkehrskanteBlueStyle;
import com.tschanz.geobooster.map_tile.model.MapTileLine;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz_repo.service.VerkehrskanteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
@RequiredArgsConstructor
public class MapTileVerkehrskanteConverter {
    private final VerkehrskanteRepo verkehrskanteRepo;


    public MapTileLine toMapTile(VerkehrskanteVersion vkV, float zoomLevel, Collection<MapStyle> mapStyles) {
        var style = mapStyles.contains(MapStyle.KANTEN_BLUE)
            ? VerkehrskanteBlueStyle.getStyle(zoomLevel)
            : VerkehrskanteBlackStyle.getStyle(zoomLevel);

        return new MapTileLine(
            CoordinateConverter.convertToEpsg3857(this.verkehrskanteRepo.getStartCoordinate(vkV)),
            CoordinateConverter.convertToEpsg3857(this.verkehrskanteRepo.getEndCoordinate(vkV)),
            style
        );
    }
}
