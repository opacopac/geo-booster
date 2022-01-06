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
    private static final String LAYER_AWB_VK = "novap:ANWENDUNGSBEREICHE";
    private static final String LAYER_AWB_TK = "novap:ANWENDUNGSBEREICHE_TARIF";


    public static Collection<MapLayer> parse(String value) {
        var layerStrings = value.split(SEPARATOR);

        return Arrays.stream(layerStrings)
            .map(layerString -> {
                switch (layerString) {
                    case LAYER_HALTESTELLEN: return MapLayer.HALTESTELLE;
                    case LAYER_VERKEHRSKANTEN: return MapLayer.VERKEHRSKANTE;
                    case LAYER_TARIFKANTEN: return MapLayer.TARIFKANTE;
                    case LAYER_UNMAPPED_TARIFKANTEN: return MapLayer.UNMAPPED_TARIFKANTE;
                    case LAYER_AWB_VK: return MapLayer.ANWENDUNGSBEREICH_VK;
                    case LAYER_AWB_TK: return MapLayer.ANWENDUNGSBEREICH_TK;
                    default: return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
