package org.alexdev.photorenderer.test;

import org.alexdev.photorenderer.PhotoViewer;
import org.alexdev.photorenderer.test.game.Photo;
import org.alexdev.photorenderer.test.util.LoggingConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;

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

        var photo = Photo.getPhoto(36);

        if (photo == null) {
            throw new Exception("Photo not found");
        }

        PhotoViewer photoViewer = new PhotoViewer();
        var palette = photoViewer.readPalette("grayscale.pal");
        photoViewer.readFile(photo.getData(), palette);
    }
}
