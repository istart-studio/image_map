package studio.istart.tile.storage;

import studio.istart.tile.model.ZoomLevel;

import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public interface BaseTileImageStoreService {

    void save(BufferedImage tileImage, String imageId, ZoomLevel z, int x, int y);

    InputStream load(String imageId, ZoomLevel z, int x, int y);
}
