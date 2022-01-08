package com.tschanz.geobooster.webmapservice.model;

import com.tschanz.geobooster.map_style.model.MapStyle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class GetMapRequestStyles {
    private static final String SEPARATOR = ",";
    private static final String STYLE_HALTESTELLEN = "Haltestellen";
    private static final String STYLE_KANTEN_BLACK = "kanten_black";
    private static final String STYLE_KANTEN_BLACK_BOLD = "kanten_black_bold";
    private static final String STYLE_KANTEN_BLACK_DASHED ="kanten_black_dashed";
    private static final String STYLE_KANTEN_BLUE = "kanten_blue";
    private static final String STYLE_KANTEN_BLUE_BOLD_DASHED = "kanten_blue_bold_dashed";
    private static final String STYLE_WA_HALTESTELLEN = "WegangabeHaltestellen";


    public static Collection<MapStyle> parse(String value) {
        var styleStrings = value.split(SEPARATOR);

        return Arrays.stream(styleStrings)
            .map(styleString -> {
                switch (styleString) {
                    case STYLE_HALTESTELLEN: return MapStyle.HALTESTELLEN;
                    case STYLE_KANTEN_BLACK: return MapStyle.KANTEN_BLACK;
                    case STYLE_KANTEN_BLACK_DASHED: return MapStyle.KANTEN_BLACK_DASHED;
                    case STYLE_KANTEN_BLACK_BOLD: return MapStyle.KANTEN_BLACK_BOLD;
                    case STYLE_KANTEN_BLUE: return MapStyle.KANTEN_BLUE;
                    case STYLE_KANTEN_BLUE_BOLD_DASHED: return MapStyle.KANTEN_BLUE_BOLD_DASHED;
                    case STYLE_WA_HALTESTELLEN: return MapStyle.WEGANGABE_HALTESTELLEN;
                    default: return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
