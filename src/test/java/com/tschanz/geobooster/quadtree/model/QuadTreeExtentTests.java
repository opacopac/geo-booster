package com.tschanz.geobooster.quadtree.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class QuadTreeExtentTests {
    @Test
    void test_isCoordinateInside() {
        var extent = new QuadTreeExtent(new QuadTreeCoordinate(0, 0), new QuadTreeCoordinate(10, 10));
        var point1 = new QuadTreeCoordinate(5, 5);
        var point2 = new QuadTreeCoordinate(11, 5);
        var point3 = new QuadTreeCoordinate(5, -1);
        var point4 = new QuadTreeCoordinate(-1, -1);

        var result1 = extent.isCoordinateInside(point1);
        var result2 = extent.isCoordinateInside(point2);
        var result3 = extent.isCoordinateInside(point3);
        var result4 = extent.isCoordinateInside(point4);

        assertTrue(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertFalse(result4);
    }


    @Test
    void test_isIntersecting() {
        var extent = new QuadTreeExtent(new QuadTreeCoordinate(0, 0), new QuadTreeCoordinate(10, 10));
        var extent1 = new QuadTreeExtent(new QuadTreeCoordinate(5, 5), new QuadTreeCoordinate(15, 15));
        var extent2 = new QuadTreeExtent(new QuadTreeCoordinate(5, 5), new QuadTreeCoordinate(6, 6));
        var extent3 = new QuadTreeExtent(new QuadTreeCoordinate(5, 11), new QuadTreeCoordinate(10, 15));
        var extent4 = new QuadTreeExtent(new QuadTreeCoordinate(-5, -5), new QuadTreeCoordinate(5, 5));
        var extent5 = new QuadTreeExtent(new QuadTreeCoordinate(-5, 5), new QuadTreeCoordinate(-2, 7));

        var result1 = extent.isIntersecting(extent1);
        var result2 = extent.isIntersecting(extent2);
        var result3 = extent.isIntersecting(extent3);
        var result4 = extent.isIntersecting(extent4);
        var result5 = extent.isIntersecting(extent5);

        assertTrue(result1);
        assertTrue(result2);
        assertFalse(result3);
        assertTrue(result4);
        assertFalse(result5);
    }
}
