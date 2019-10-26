package org.alexdev.photorenderer.test;

import org.alexdev.photorenderer.GreyscalePalette;
import org.alexdev.photorenderer.PhotoRenderOption;
import org.alexdev.photorenderer.PhotoRenderer;
import org.alexdev.photorenderer.test.game.Photo;
import org.alexdev.photorenderer.test.util.LoggingConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;

public class App {
    private static Logger log;

    public static void main(String[] args) throws Exception {
        LoggingConfiguration.checkLoggingConfig();

        Storage storage = new Storage("localhost", 3306, "root", "123", "dev");

        if (!storage.connect()) {
            return;
        }

        log = LoggerFactory.getLogger(App.class);
        log.info("Loading photo");

        var photo = Photo.getPhoto(49);

        if (photo == null) {
            throw new Exception("Photo not found");
        }

        PhotoRenderer photoViewer = new PhotoRenderer();

        var palette = GreyscalePalette.getPalette();
        var src = photoViewer.createImage(photo.getData(), palette, PhotoRenderOption.SEPIA);

        ImageIO.write(src, "PNG", new File("image2.png"));
    }
/*
    public static BufferedImage toSepia(BufferedImage img, int sepiaIntensity) {
        BufferedImage sepia = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        // Play around with this.  20 works well and was recommended
        //   by another developer. 0 produces black/white image
        int sepiaDepth = 20;

        int w = img.getWidth();
        int h = img.getHeight();

        WritableRaster raster = sepia.getRaster();

        // We need 3 integers (for R,G,B color values) per pixel.
        int[] pixels = new int[w * h * 3];
        img.getRaster().getPixels(0, 0, w, h, pixels);

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {

                int rgb = img.getRGB(x, y);
                Color color = new Color(rgb, true);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int gry = (int) ((r + g + b) / 3.5);

                r = g = b = gry;
                r = r + (sepiaDepth * 3);
                g = g + sepiaDepth;

                if (r > 255) {
                    r = 255;
                }
                if (g > 255) {
                    g = 255;
                }
                if (b > 255) {
                    b = 255;
                }

                // Darken blue color to increase sepia effect
                b -= sepiaIntensity;

                // normalize if out of bounds
                if (b < 0) {
                    b = 0;
                }
                if (b > 255) {
                    b = 255;
                }

                color = new Color(r, g, b, color.getAlpha());
                sepia.setRGB(x, y, color.getRGB());

            }
        }

        return sepia;
    }*/

}
