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
         BufferedImage in = ImageIO.read(new FileInputStream("image.png"));

        BufferedImage image = new BufferedImage(
                in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = image.createGraphics();
        g.drawImage(in, 0, 0, null);
        g.dispose();

        List<Color> colorList = new ArrayList<>();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));

                if (colorList.stream().noneMatch(c -> color.getRed() == c.getRed() && color.getGreen() == c.getGreen() && color.getBlue() == c.getBlue())) {
                    colorList.add(color);
                }
            }
        }

        for (Color color : colorList) {
            System.out.println("0xff" + String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()) + ",");
        }

 */

}
