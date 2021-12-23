package com.tschanz.geobooster.utfgrid.service;

import com.tschanz.geobooster.utfgrid.model.UtfGrid;
import com.tschanz.geobooster.utfgrid.model.UtfGridItem;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UtfGridServiceImpl implements UtfGridService {
    private static final Logger logger = LogManager.getLogger(UtfGridServiceImpl.class);


    @Override
    public String render(UtfGrid utfGrid) {
        return "{\n"
            + "\"grid\": [\n" + this.renderGridPart(utfGrid) + "],\n"
            + "\"keys\": [\n" + this.renderKeysPart(utfGrid) + "],\n"
            + "\"data\": {\n" + this.renderDataPart(utfGrid) + "}\n"
            + "}";
    }


    private String renderGridPart(UtfGrid utfGrid) {
        StringBuilder sb = new StringBuilder();
        // TODO: tmp
        for (var i = 0; i < 256; i++) {
            sb.append("\"                                                                                                                                                                                                                                                                \"");
            if (i < 255) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }

        return sb.toString();
    }


    private String renderKeysPart(UtfGrid utfGrid) {
        return "  \"\",\n" + utfGrid.getNumberedItems().stream()
            .map(item -> "  \"" + item.getKey() + "\"")
            .collect(Collectors.joining(",\n")) + "\n";
    }


    private String renderDataPart(UtfGrid utfGrid) {
        return utfGrid.getNumberedItems().stream()
            .map(entry -> "  \""
                + entry.getKey()
                + "\" : {"
                + this.renderDataItem(entry.getValue())
                + "}"
            )
            .collect(Collectors.joining(",\n"));
    }


    private String renderDataItem(UtfGridItem item) {
        return item.getDataFields().stream()
            .map(field -> "\"" + field.getKey() + "\":" + field.getValue().toString())
            .collect(Collectors.joining(","));
    }
}
