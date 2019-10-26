package org.alexdev.photoviewer;

import com.google.common.io.LittleEndianDataInputStream;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Arrays;

public class PhotoViewer {
    public BufferedImage readFile(byte[] photoData, Color[] paletteData) throws Exception {
        int CAST_PROPERTIES_OFFSET = 28;

        var bigEndianStream = new DataInputStream(new ByteArrayInputStream(photoData));
        var littleEndianStream = new LittleEndianDataInputStream(bigEndianStream);

        littleEndianStream.skip(CAST_PROPERTIES_OFFSET);

        int totalWidth = bigEndianStream.readShort() & 0x7FFF;

        int top = bigEndianStream.readShort();
        int left = bigEndianStream.readShort();
        int bottom = bigEndianStream.readShort();
        int right = bigEndianStream.readShort();

        Rectangle rectangle = new Rectangle(left, top, right - left, bottom - top);

        littleEndianStream.read();
        littleEndianStream.skip(7);
        bigEndianStream.readShort();
        bigEndianStream.readShort();
        littleEndianStream.read();

        int bitDepth = littleEndianStream.read();

        if (bitDepth != 8)
            throw new Exception("illegal");

        int palette = bigEndianStream.readInt() - 1; //Make sure that this one equals -3 = Grayscale

        if (palette != -3)
            throw new Exception("illegal");

        littleEndianStream.readInt(); // No idea! Lmao
        littleEndianStream.skip(4); // Reversed, should equal BITD

        int length = littleEndianStream.readInt();
        int position = 0;

        var data = new int[totalWidth * rectangle.height];

        while (littleEndianStream.available() > 0) {
            int marker = littleEndianStream.read();

            if (marker >= 128) {
                int fill = littleEndianStream.read();

                for (int i = 0; i < 257 - marker; i++) {
                    data[position] = fill;
                    position++;
                }

            } else {
                int[] buffer = new int[marker + 1];

                for (int i = 0; i < buffer.length; i++) {
                    data[position] = littleEndianStream.read();
                    position++;
                }
            }
        }

        var image = new BufferedImage(rectangle.width, rectangle.height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < rectangle.height; y++) {
            int[] row = Arrays.copyOfRange(data, y * totalWidth, (y * totalWidth) + totalWidth);

            if (row.length > 0) {
                for (int x = 0; x < rectangle.width; x++) {
                    int index = row[x];
                    var rgb = paletteData[index];

                    int r = rgb.getRed();
                    int g = rgb.getGreen();
                    int b = rgb.getBlue();

                    Color color = new Color(r, g, b);
                    image.setRGB(x, y, color.getRGB());
                }
            }
        }

        littleEndianStream.close();
        bigEndianStream.close();

        return image;
    }

    public Color[] readPalette(String paletteFileName) throws Exception {
        var input = new LittleEndianDataInputStream(new FileInputStream(paletteFileName));
        new String(input.readNBytes(4));

        input.readInt();

        new String(input.readNBytes(4));
        new String(input.readNBytes(4));

        input.readInt();
        input.readShort();

        Color[] colors = new Color[input.readShort()];

        for (int i = 0; i < colors.length; i++) {
            int r = input.read();
            int g = input.read();
            int b = input.read();
            colors[i] = new Color(r, g, b);
            input.readByte();
        }

        return colors;
    }
}
