package com.tschanz.geobooster.utfgrid.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class UtfGridImg {
    private static final String EMPTY_SYMBOL = " ";
    private final int width;
    private final int height;
    private final char[][] bitMap;


    public UtfGridImg(int width, int height) {
        this.width = width;
        this.height = height;
        this.bitMap = new char[width][height];

        var emptyLine = EMPTY_SYMBOL.repeat(width);
        for (var y = 0; y < height; y++) {
            this.bitMap[y] = emptyLine.toCharArray();
        }
    }


    public void drawPoint(int x, int y, float width, char symbol) {
        var xmin = x - width / 2.0f;
        var xmax = x + width / 2.0f;
        var ymin = y - width / 2.0f;
        var ymax = y + width / 2.0f;

        for (var i = xmin; i < xmax; i++) {
            for (var j = ymin; j < ymax; j++) {
                this.setSymbol(i, j, symbol);
            }
        }
    }


    public void drawLine(int x0, int y0, int x1, int y1, float width, char symbol) {
        // bresham algorithm (https://de.wikipedia.org/wiki/Bresenham-Algorithmus)
        var dx = Math.abs(x1 - x0);
        var sx = x0 < x1 ? 1 : -1;
        var dy = -Math.abs(y1 - y0);
        var sy = y0 < y1 ? 1 : -1;

        var err = dx + dy;
        int e2; /* error value e_xy */

        while (true) {
            this.drawLineStep(x0, y0, width, symbol);

            if (x0 == x1 && y0 == y1) break;
            e2 = 2 * err;

            if (e2 > dy) {
                err += dy;
                x0 += sx;
            } /* e_xy + e_x > 0 */

            if (e2 < dx) {
                err += dx;
                y0 += sy;
            } /* e_xy + e_y < 0 */
        }
    }


    private void drawLineStep(int x, int y, float width, char symbol) {
        var xmin = x - width / 2.0f;
        var xmax = x + width / 2.0f;
        var ymin = y - width / 2.0f;
        var ymax = y + width / 2.0f;

        for (var i = xmin; i < xmax; i++) {
            this.setSymbol(i, ymin, symbol);
        }

        for (var j = ymin; j < ymax; j++) {
            this.setSymbol(xmin, j, symbol);
        }
    }


    private void setSymbol(float x, float y, char symbol) {
        var xGrid = Math.round(x);
        var yGrid = this.height - Math.round(y) - 2;
        if (xGrid >= 0 && yGrid >= 0 && xGrid < this.width && yGrid < this.height) {
            this.bitMap[yGrid][xGrid] = symbol;
        }
    }
}
