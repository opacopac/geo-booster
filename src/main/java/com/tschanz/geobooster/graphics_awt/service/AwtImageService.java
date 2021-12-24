package com.tschanz.geobooster.graphics_awt.service;

import com.tschanz.geobooster.graphics.service.ImageService;
import com.tschanz.geobooster.graphics_awt.model.AwtImage;
import org.springframework.stereotype.Service;


@Service
public class AwtImageService implements ImageService {
    public AwtImage createImage(int width, int height, boolean isBgTransparent) {
        return new AwtImage(width, height, isBgTransparent);
    }
}
