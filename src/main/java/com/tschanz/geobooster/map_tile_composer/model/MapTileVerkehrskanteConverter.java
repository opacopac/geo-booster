package com.tschanz.geobooster.map_tile_composer.model;

import com.tschanz.geobooster.map_style.model.MapStyle;
import com.tschanz.geobooster.map_style.service.StyleHelper;
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
        return new MapTileLine(
            this.verkehrskanteRepo.getStartCoordinate(vkV),
            this.verkehrskanteRepo.getEndCoordinate(vkV),
            StyleHelper.getLineStyle(mapStyles, zoomLevel)
        );
    }
}
