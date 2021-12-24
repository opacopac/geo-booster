package com.tschanz.geobooster.maptile.service;

import com.tschanz.geobooster.graphics.model.GbImage;
import com.tschanz.geobooster.graphics.service.ImageService;
import com.tschanz.geobooster.maptile.model.MapTile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MapTileServiceImpl implements MapTileService {
    private final ImageService imageService;


    public GbImage renderTile(MapTile tile) {
        var bboxMinN = tile.getMinCoordinate().getN();
        var bboxMinE = tile.getMinCoordinate().getE();
        var bboxHeight = tile.getMaxCoordinate().getN() - tile.getMinCoordinate().getN();
        var bboxWidth = tile.getMaxCoordinate().getE() - tile.getMinCoordinate().getE();
        var image = imageService.createImage(tile.getWidth(), tile.getHeight(), tile.isBgTransparent());

        tile.getLines().forEach(line -> {
            var x1 = (int) ((line.getStartCoordinate().getE() - bboxMinE) / bboxWidth * tile.getWidth());
            var y1 = (int) (tile.getHeight() - (line.getStartCoordinate().getN() - bboxMinN) / bboxHeight * tile.getHeight());
            var x2 = (int) ((line.getEndCoordinate().getE() - bboxMinE) / bboxWidth * tile.getWidth());
            var y2 = (int) (tile.getHeight() - (line.getEndCoordinate().getN() - bboxMinN) / bboxHeight * tile.getHeight());
            image.drawLine(x1, y1, x2, y2, line.getStyle());
        });

        tile.getPoints().forEach(point -> {
            var x = (int) ((point.getCoordinate().getE() - bboxMinE) / bboxWidth * tile.getWidth());
            var y = (int) (tile.getHeight() - (point.getCoordinate().getN() - bboxMinN) / bboxHeight * tile.getHeight());
            image.drawPoint(x, y, point.getStyle());
        });

        return image;
    }
}
