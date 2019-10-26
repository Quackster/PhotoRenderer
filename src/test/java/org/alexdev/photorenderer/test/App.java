package org.alexdev.photorenderer.test;

import org.alexdev.photorenderer.palettes.GreyscalePalette;
import org.alexdev.photorenderer.RenderOption;
import org.alexdev.photorenderer.PhotoRenderer;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class App {
    public static void main(String[] args) throws Exception {
        // Feel free to read your own PAL files by using PaletteUtils.readPalette("palette/greyscale.pal")
        // and adding it to the constructor instead
        PhotoRenderer photoViewer = new PhotoRenderer(GreyscalePalette.getPalette(), RenderOption.SEPIA);

        var photoData = Files.readAllBytes(Path.of("photo.bin"));
        var src = photoViewer.createImage(photoData);

        ImageIO.write(src, "PNG", new File("output_sepia.png"));
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
