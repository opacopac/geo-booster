package com.tschanz.geobooster.graphics.service;

import com.tschanz.geobooster.graphics.model.GbImage;
import com.tschanz.geobooster.graphics.model.GbLineStyle;
import com.tschanz.geobooster.graphics.model.GbPointStyle;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;


@Service
public class ImageServiceImpl implements ImageService {
    private static final String FORMAT_PNG = "png";
    private static final Color BG_COLOR_NON_TRANSPARENT = Color.WHITE;


    public GbImage createImage(int width, int height, boolean isBgTransparent) {
        var img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var graphics = img.createGraphics();

        // create bg
        if (isBgTransparent) {
            graphics.setComposite(AlphaComposite.Clear);
        } else {
            graphics.setColor(BG_COLOR_NON_TRANSPARENT);
        }
        graphics.fillRect(0, 0, width, height);

        return new GbImage(img);
    }


    public void drawLine(GbImage image, int x1, int y1, int x2, int y2, GbLineStyle style) {
        var graphics = (Graphics2D) image.getImage().getGraphics();
        graphics.setColor(style.getColor());
        graphics.setStroke(style.getStroke());
        graphics.drawLine(x1, y1, x2, y2);
    }


    public void drawPoint(GbImage image, int x, int y, GbPointStyle style) {
        var radius = style.getRadius();
        var graphics = (Graphics2D) image.getImage().getGraphics();
        graphics.setColor(style.getColor());
        graphics.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        graphics.setColor(style.getBorderColor());
        graphics.setStroke(style.getStroke());
        graphics.drawOval(x - radius, y - radius, radius * 2, radius * 2);
    }


    @SneakyThrows
    public ByteArrayOutputStream getPngByteStream(GbImage image) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image.getImage(), FORMAT_PNG, bos);

        return bos;
    }
}
