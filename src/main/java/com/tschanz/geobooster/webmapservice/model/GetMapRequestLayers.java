package com.tschanz.geobooster.webmapservice.model;

import com.tschanz.geobooster.map_layer.model.MapLayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class GetMapRequestLayers {
    private static final String SEPARATOR = ",";
    private static final String LAYER_HALTESTELLEN = "novap:HALTESTELLEN";
    private static final String LAYER_VERKEHRSKANTEN = "novap:VERKEHRSKANTEN";
    private static final String LAYER_TARIFKANTEN = "novap:TARIFKANTEN";
    private static final String LAYER_UNMAPPED_TARIFKANTEN = "novap:UNMAPPED_TARIFKANTEN";


    public static Collection<MapLayer> parse(String value) {
        var layerStrings = value.split(SEPARATOR);

        return Arrays.stream(layerStrings)
            .map(layerString -> {
                switch (layerString) {
                    case LAYER_HALTESTELLEN: return MapLayer.Haltestelle;
                    case LAYER_VERKEHRSKANTEN: return MapLayer.Verkehrskante;
                    case LAYER_TARIFKANTEN: return MapLayer.Tarifkante;
                    case LAYER_UNMAPPED_TARIFKANTEN: return MapLayer.UnmappedTarifkante;
                    default: return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
