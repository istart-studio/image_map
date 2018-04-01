package studio.istart.tile.storage;

import lombok.extern.log4j.Log4j2;
import studio.istart.framework.storage.AliyunOSS;
import studio.istart.framework.storage.StorageObject;
import studio.istart.tile.component.ImageLoader;
import studio.istart.tile.model.ZoomLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@Log4j2
public class ImageStore extends AliyunOSS implements BaseTileImageStoreService {

    private String baseDir;

    public ImageStore() {

    }

    @Deprecated
    public ImageStore(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    protected String getBucketName() {
        return "medical2018";
    }

    @Override
    public void save(BufferedImage tileImage, String imageId, ZoomLevel z, int x, int y) {
        log.info("save :{},{},{},{}", imageId, z.getLevel(), x, y);
        ossSave(tileImage, imageId, z, x, y);
    }

    private String fileName(String imageId, ZoomLevel z, int x, int y) {
        return imageId + "_" + z.getLevel() + "_" + x + "_" + y + ".jpg";
    }

    @Override
    public InputStream load(String imageId, ZoomLevel z, int x, int y) {

        Optional<StorageObject> storageObjectOptional = Optional.empty();
        String fileName = fileName(imageId, z, x, y);
        System.out.println("load :" + fileName);
        try {
            storageObjectOptional = loadAsResource(fileName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        if (storageObjectOptional.isPresent()) {
            return storageObjectOptional.get().getContent();
        } else {
            throw new IllegalStateException("can't found the file by " + imageId);
        }
    }

    public void ossSave(BufferedImage tileImage, String imageId, ZoomLevel z, int x, int y) {
        String fileName = fileName(imageId, z, x, y);
        System.out.println("save :" + fileName);
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(tileImage, "JPG", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            store(is, fileName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Deprecated
    public void localSave(BufferedImage tileImage, String imageId, ZoomLevel z, int x, int y) {
        String tileTemplate = "{x}_{y}.jpg";
        String tileName = tileTemplate.replace("{x}", String.valueOf(x))
                .replace("{y}", String.valueOf(y));
        File dir = ImageLoader.tilesDir(baseDir, imageId, z);
        try {
            ImageIO.write(tileImage, "JPG", Paths.get(dir.getPath(), tileName).toFile());
        } catch (IOException e) {
            log.error(e);
        }
    }
}
