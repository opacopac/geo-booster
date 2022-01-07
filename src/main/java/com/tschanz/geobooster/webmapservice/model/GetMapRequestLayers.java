package com.tschanz.geobooster.webmapservice.model;

import com.tschanz.geobooster.map_layer.model.MapLayerType;
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


    public static Collection<MapLayerType> parse(String value) {
        var layerStrings = value.split(SEPARATOR);

        return Arrays.stream(layerStrings)
            .map(layerString -> {
                switch (layerString) {
                    case LAYER_HALTESTELLEN: return MapLayerType.HALTESTELLE;
                    case LAYER_VERKEHRSKANTEN: return MapLayerType.VERKEHRSKANTE;
                    case LAYER_TARIFKANTEN: return MapLayerType.TARIFKANTE;
                    case LAYER_UNMAPPED_TARIFKANTEN: return MapLayerType.UNMAPPED_TARIFKANTE;
                    case LAYER_AWB_VK: return MapLayerType.ANWENDUNGSBEREICH_VK;
                    case LAYER_AWB_TK: return MapLayerType.ANWENDUNGSBEREICH_TK;
                    default: return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
