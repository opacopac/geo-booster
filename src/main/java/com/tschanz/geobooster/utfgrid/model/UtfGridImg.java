package com.tschanz.geobooster.utfgrid.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class UtfGridImg {
    private static final String EMPTY_SYMBOL = " ";
    private final int width;
    private final int height;
    private final char[][] bitMap; // [height - y - 1, x]


    public UtfGridImg(int width, int height) {
        this.width = width;
        this.height = height;
        this.bitMap = new char[width][height];

        var emptyLine = EMPTY_SYMBOL.repeat(width);
        for (var y = 0; y < height; y++) {
            this.bitMap[y] = emptyLine.toCharArray();
        }
    }


    public void drawPoint(int x, int y, char symbol) {
        if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
            this.bitMap[this.height - y - 1][x] = symbol;
        }
    }


    public void drawLine(int x0, int y0, int x1, int y1, char symbol) {
        // bresham algorithm (https://de.wikipedia.org/wiki/Bresenham-Algorithmus)
        var dx = Math.abs(x1 - x0);
        var sx = x0 < x1 ? 1 : -1;
        var dy = -Math.abs(y1 - y0);
        var sy = y0 < y1 ? 1 : -1;

        var err = dx + dy;
        int e2; /* error value e_xy */

        while (true) {
            if (x0 >= 0 && y0 >= 0 && x0 < this.width && y0 < this.height) {
                this.bitMap[this.height - y0 - 1][x0] = symbol;
            }

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
}
