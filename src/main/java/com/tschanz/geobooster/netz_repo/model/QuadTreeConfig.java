package com.tschanz.geobooster.netz_repo.model;


public class QuadTreeConfig {
    // minLat = 43.0062910000
    // maxLat = 53.7988520000
    // minLon = 1x 0.6202740000, 2x 2.0794830000, rest: 4.8248030000
    // maxLon = 14.5659700000
    public static final double MIN_COORD_X = 556597.45 - 1;
    public static final double MIN_COORD_Y = 5654278.34 - 1;
    public static final double MAX_COORD_X = 1246778.30 + 1;
    public static final double MAX_COORD_Y = 6108322.79 + 1;
    public static final int MAX_TREE_DEPTH = 6;
}
