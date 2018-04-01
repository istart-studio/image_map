package studio.istart.tile.component;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import lombok.extern.log4j.Log4j2;
import studio.istart.tile.model.ImageProps;
import studio.istart.tile.model.ZoomLevel;
import studio.istart.tile.storage.BaseTileImageStoreService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * todo:create images in all z-level
 * zoomed
 * filled
 * saved
 * todo:cut out
 *
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@Log4j2
public class ImageLoader {

    private BufferedImage srcImage = null;
    private ImageProps imageProps = null;
    private ZoomLevel zLevel = null;
    private String name;

    private ImageLoader() {
    }

    private void setImage(BufferedImage image) {
        this.srcImage = image;
        this.imageProps = new ImageProps(this.srcImage);
        log.info("image props :{}", imageProps);
    }

    public static ImageLoader builder(File sourceFile) throws IOException {
        ImageLoader imageLoader = new ImageLoader();
        imageLoader.setImage(ImageIO.read(sourceFile));
        imageLoader.setZLevel();
        imageLoader.setName(sourceFile);
        return imageLoader;
    }

    public ImageLoader setName(File file) {
        this.name = file.getName().substring(0, file.getName().indexOf("."));
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ImageLoader setZLevel(ZoomLevel zLevel) {
        this.zLevel = zLevel;
        return this;
    }

    public int getMaxZoomLevel() {
        return this.zLevel.getLevel();
    }

    private ImageLoader setZLevel() {
        // belong which z-level
        Optional<ZoomLevel> currentZLevel = ZoomLevelComponent.matchZLevel(imageProps.getMaxLength());
        log.info("currentZLevel:{}", currentZLevel);
        Preconditions.checkState(currentZLevel.isPresent(),
                "can't found the z-level by:" + imageProps.getMaxLength());
        Preconditions.checkState(currentZLevel.isPresent() &&
                        currentZLevel.get().getLengthRange().upperEndpoint() < Integer.MAX_VALUE,
                "not support the image length > Int.MAX_VALUE" + imageProps.getMaxLength());
        this.zLevel = currentZLevel.get();
        return this;
    }

    @Deprecated
    public ImageLoader zoom() {
        // throw "out of memory" exception if this.zLevel > 5
        if (this.zLevel.getLevel() < 6) {
            this.setImage(zoom(this.zLevel, this.imageProps, this.srcImage));
        }
        return this;
    }

    public ImageLoader toFile(File outFile) throws IOException {

        return this;
    }

    public static double caleZoomRate(ZoomLevel z, ImageProps imageProps) {
        long targetLength = z.getLengthRange().upperEndpoint();
        return (double) targetLength / imageProps.getMaxLength();
    }

    public static BufferedImage zoom(final ZoomLevel z, final ImageProps imageProps,
                                     final Image originalImage) {
        long targetLength = z.getLengthRange().upperEndpoint();
        double zoomRate = caleZoomRate(z, imageProps);
        //new image
        int zoomWidth = (int) (imageProps.getWidth() * zoomRate);
        int zoomHeight = (int) (imageProps.getHeight() * zoomRate);
        Image zoomImage = originalImage.getScaledInstance(zoomWidth,
                zoomHeight, Image.SCALE_AREA_AVERAGING);

        BufferedImage outputImage = new BufferedImage(zoomImage.getWidth(null),
                zoomImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics outputImageGraphics = outputImage.getGraphics();
        outputImageGraphics.drawImage(zoomImage, 0, 0, null);
        outputImageGraphics.dispose();
        return outputImage;
    }

    @Deprecated
    public ImageLoader fill() throws IOException {
        this.setImage(fill(this.zLevel, this.imageProps, this.srcImage));
        return this;
    }

    @Deprecated
    public BufferedImage fill(final ZoomLevel z, final ImageProps imageProps, final BufferedImage srcImage)
            throws IOException {
        //
        int destLength = z.getLengthRange().upperEndpoint().intValue();
        int destWidth = destLength;
        int destHeight = destLength;
        // out image
        BufferedImage outImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
        //draw the new image
        Graphics2D outImageGraphics = outImage.createGraphics();
        //background color
        //https://stackoverflow.com/questions/14097386/how-to-make-drawn-images-transparent-in-java
        int originalWidth = imageProps.getWidth();
        int originalHeight = imageProps.getHeight();
        // 设置图片居中显示
        int topRightPointX = (destWidth - originalWidth) / 2;
        int topRightPointY = (destHeight - originalHeight) / 2;
        outImageGraphics.drawImage(srcImage, topRightPointX, topRightPointY,
                null);
        return outImage;
    }

    public void cut(ZoomLevel minZoomLevel, BaseTileImageStoreService storeService) throws Exception {
        cut(this.zLevel, this.name, storeService);
        log.info("max zoom level {} cut done!", this.zLevel.getLevel());
        int minZLevel = minZoomLevel.getLevel();
        for (int zLevel = this.zLevel.getLevel() - 1; zLevel >= minZLevel; zLevel--) {
            cut(new ZoomLevel(zLevel), new ZoomLevel(zLevel + 1), this.name, storeService);
            log.info("zoom level {} cut done!", zLevel);
        }
    }


    /**
     * cut current zoom level tiles
     *
     * @param zLevel
     * @return
     * @throws IOException
     */
    private void cut(ZoomLevel zLevel, String imageId, BaseTileImageStoreService storageService) throws Exception {
        Range<Integer> tileXRange = TileComponent.tileNumRange(zLevel);
        Range<Integer> tileYRange = TileComponent.tileNumRange(zLevel);
        for (int x = tileXRange.lowerEndpoint(); tileXRange.contains(x); x++) {
            for (int y = tileYRange.lowerEndpoint(); tileYRange.contains(y); y++) {
                BufferedImage tileImage = cutCurrentLevel(x, y, TileComponent.SIZE_PIXEL);
                storageService.save(tileImage, imageId, zLevel, x, y);
            }
        }
    }

    private static byte[] getBytes(BufferedImage tileImage) {
        return ((DataBufferByte) tileImage.getData().getDataBuffer()).getData();
    }

    private BufferedImage cutCurrentLevel(int x, int y, int tileSize) throws Exception {
        //outside of raster
        int pixelX = TileComponent.toPixelXY(x);
        int pixelY = TileComponent.toPixelXY(y);
        int cutWidthSize = tileLength(pixelX, this.imageProps.getWidth(), tileSize);
        int cutHeightSize = tileLength(pixelY, this.imageProps.getHeight(), tileSize);

        //not enough size
        BufferedImage tileImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_RGB);
        if (cutWidthSize > 0 && cutHeightSize > 0) {
            BufferedImage tileContent = this.srcImage.getSubimage(pixelX, pixelY, cutWidthSize, cutHeightSize);
            fillImage(tileImage, tileContent, 0, 0);
            log.info("cut:{},{}", x, y);
        }
        return tileImage;
    }

    private static BufferedImage fillImage(BufferedImage baseBufferedImage,
                                           BufferedImage coverBufferedImage,
                                           int x, int y) throws Exception {

        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = baseBufferedImage.createGraphics();
        // 绘制
        g2d.drawImage(coverBufferedImage, x, y, null);
        g2d.dispose();// 释放图形上下文使用的系统资源
        return baseBufferedImage;
    }

    private static int tileLength(int xOrY, int widthOrHeight, int tileSize) {
        int size = widthOrHeight - xOrY;
        return size > tileSize
                ? tileSize : size;
    }

    public static File tilesDir(String baseDir, String imageId, ZoomLevel zoomLevel) {
        File dir = Paths.get(baseDir, imageId, String.valueOf(zoomLevel.getLevel())).toFile();
        if (!dir.exists()) {
            Preconditions.checkState(dir.mkdirs());
        }
        return dir;
    }

    /**
     * cut level
     *
     * @param zLevel          difference zoom level
     * @param parentZoomLevel parent zoom level
     * @param imageId         image id as sub dir name
     * @throws Exception
     */
    public static void cut(final ZoomLevel zLevel, final ZoomLevel parentZoomLevel,
                           String imageId, BaseTileImageStoreService storeService) throws Exception {
        //z总瓦片数
        Range<Integer> tileXRange = TileComponent.tileNumRange(zLevel);
        Range<Integer> tileYRange = TileComponent.tileNumRange(zLevel);
        //瓦片模版
        for (int x = tileXRange.lowerEndpoint(); tileXRange.contains(x); x++) {
            for (int y = tileYRange.lowerEndpoint(); tileYRange.contains(y); y++) {
                BufferedImage tileImage = new BufferedImage(TileComponent.SIZE_PIXEL, TileComponent.SIZE_PIXEL, BufferedImage.TYPE_INT_RGB);
                // scale ： 256 * 2 => 256
                BufferedImage parentTileImage = margeParentTiles(imageId, parentZoomLevel, x, y, storeService);
                Graphics2D g2d = tileImage.createGraphics();
                g2d.drawImage(parentTileImage, 0, 0, TileComponent.SIZE_PIXEL, TileComponent.SIZE_PIXEL, null);
                g2d.dispose();
                storeService.save(tileImage, imageId, zLevel, x, y);

            }
        }
    }

    public static BufferedImage margeParentTiles(String imageId, ZoomLevel zLevel, int x, int y,
                                                 BaseTileImageStoreService storeService) throws IOException {
        // get 4 parent tiles
        Range<Integer> xRange = parentTilesRange(x);
        Range<Integer> yRange = parentTilesRange(y);
        //draw 512(256*2) * 512(256*2)
        BufferedImage margeImage = new BufferedImage(TileComponent.SIZE_PIXEL * 2,
                TileComponent.SIZE_PIXEL * 2, BufferedImage.TYPE_INT_RGB);
        for (int parentX = xRange.lowerEndpoint(), xIndex = 0;
             parentX < xRange.upperEndpoint() + 1; parentX++, xIndex++) {
            for (int parentY = yRange.lowerEndpoint(), yIndex = 0;
                 parentY < yRange.upperEndpoint() + 1; parentY++, yIndex++) {
//                String tileName = parentX + "_" + parentY + ".jpg";
//                log.info("parent tileName:" + tileName);
//                File tileFile = Paths.get(parentTilesDir, tileName).toFile();
                BufferedImage tileImage = ImageIO.read(storeService.load(imageId, zLevel, parentX, parentY));
                Graphics margeImageGraphics = margeImage.getGraphics();
                log.info("xIndex:" + xIndex);
                log.info("yIndex:" + yIndex);
                int pointX = xIndex * TileComponent.SIZE_PIXEL;
                int pointY = yIndex * TileComponent.SIZE_PIXEL;
                log.info("pointX:" + pointX);
                log.info("pointY:" + pointY);
                margeImageGraphics.drawImage(tileImage, pointX, pointY, null);
                margeImageGraphics.dispose();
            }
        }
        boolean debug = false;
        if (debug) {
            ImageIO.write(margeImage, "JPG", new File("/Users/dongyan/Downloads/dest/test.jpg"));
        }

        return margeImage;
    }

    public static Range<Integer> parentTilesRange(int xOrY) {
        return Range.closed(xOrY * 2, xOrY * 2 + 1);
    }


}

