package com.lydbook.audiobook.services;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    public BufferedImage createSeriesCover(List<Path> pathList) {
        List<BufferedImage> bufferedImages = new ArrayList<>();
        for (Path s : pathList) {
            try {
                bufferedImages.add(ImageIO.read(s.toFile()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int columns = (int) Math.ceil(Math.sqrt(bufferedImages.size()));
        int imageDimensions = (int) Math.ceil(500.0 / columns);
        BufferedImage mergedImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D mergedGraphics = mergedImage.createGraphics();

        for (int i = 0; i < bufferedImages.size(); i++) {
            int x = (i % columns) * imageDimensions;
            int y = (int) Math.floor(i / (double) columns) * imageDimensions;

            mergedGraphics.drawImage(resizeImage(bufferedImages.get(i), imageDimensions),
                    x, y, null);

        }
        mergedGraphics.dispose();
        return mergedImage;
    }


    public static BufferedImage resizeImage(BufferedImage originalImage, int scale) {
        return Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, scale, scale, Scalr.OP_ANTIALIAS);
    }

}
