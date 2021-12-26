package com.tschanz.geobooster.graphics_awt.model;

import com.pngencoder.PngEncoder;
import com.tschanz.geobooster.graphics.model.GbImage;
import com.tschanz.geobooster.graphics.model.GbLineStyle;
import com.tschanz.geobooster.graphics.model.GbPointStyle;
import lombok.Getter;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.image.BufferedImage;


@Getter
public class AwtImage implements GbImage {
    private static final String FORMAT_PNG = "png";
    private static final Color BG_COLOR_NON_TRANSPARENT = Color.WHITE;

    private final BufferedImage image;
    private final Graphics2D graphics;


    public AwtImage (int width, int height, boolean isBgTransparent) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.graphics = this.image.createGraphics();

        // create bg
        if (isBgTransparent) {
            this.graphics.setComposite(AlphaComposite.Clear);
            this.graphics.fillRect(0, 0, width, height);
            this.graphics.setComposite(AlphaComposite.Src);
        } else {
            this.graphics.setColor(BG_COLOR_NON_TRANSPARENT);
            this.graphics.fillRect(0, 0, width, height);
        }
    }


    public void drawLine(int x1, int y1, int x2, int y2, GbLineStyle style) {
        this.graphics.setColor(AwtColorConverter.toAwt(style.getColor()));
        this.graphics.setStroke(AwtStrokeConverter.toAwt(style.getStroke()));
        this.getGraphics().drawLine(x1, y1, x2, y2);
    }


    public void drawPoint(int x, int y, GbPointStyle style) {
        var radius = style.getRadius();
        this.graphics.setColor(AwtColorConverter.toAwt(style.getColor()));
        this.graphics.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        this.graphics.setColor(AwtColorConverter.toAwt(style.getBorderColor()));
        this.graphics.setStroke(AwtStrokeConverter.toAwt(style.getBorderStroke()));
        this.graphics.drawOval(x - radius, y - radius, radius * 2, radius * 2);
    }


    @Override
    @SneakyThrows
    public byte[] getBytes() {
        // ImageIO
        /*var bos = new ByteArrayOutputStream();
        ImageIO.write(this.image, FORMAT_PNG, bos);
        return bos.toByteArray();*/

        // Apache Commons Imaging
        /* var format = ImageFormats.PNG;
        Map<String, Object> params = new HashMap<>();
        //params.put(PngConstants.FILTER_METHOD_ADAPTIVE, );
        return Imaging.writeImageToBytes(image, format, params); */

        // PngEncoder (https://github.com/pngencoder/pngencoder)
        return new PngEncoder()
            .withBufferedImage(this.image)
            .withCompressionLevel(7)
            .toBytes();

    }
}
