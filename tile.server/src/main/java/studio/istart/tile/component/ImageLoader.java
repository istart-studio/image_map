package studio.istart.tile.component;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import lombok.extern.log4j.Log4j2;
import studio.istart.tile.constants.ImageProps;
import studio.istart.tile.constants.TileBlockConstant;
import studio.istart.tile.constants.ZLevel;
import studio.istart.tile.constants.ZLevelConstant;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
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
    private ZLevel zLevel = null;

    private ImageLoader() {
    }

    private void setImage(BufferedImage image) {
        this.srcImage = image;
        this.imageProps = new ImageProps(this.srcImage);
        log.info("image props :{}", imageProps);
    }

    public static ImageLoader builder(File file) throws IOException {
        ImageLoader imageLoader = new ImageLoader();
        imageLoader.setImage(ImageIO.read(file));
        imageLoader.setZLevel();
        return imageLoader;
    }

    public ImageLoader setZLevel(ZLevel zLevel) {
        this.zLevel = zLevel;
        return this;
    }

    private ImageLoader setZLevel() {
        // belong which z-level
        Optional<ZLevel> currentZLevel = ZLevelConstant.matchZLevel(imageProps.getMaxLength());
        log.info("currentZLevel:{}", currentZLevel);
        Preconditions.checkState(currentZLevel.isPresent(),
                "can't found the z-level by:" + imageProps.getMaxLength());
        Preconditions.checkState(currentZLevel.isPresent() &&
                        currentZLevel.get().getLengthRange().upperEndpoint() < Integer.MAX_VALUE,
                "not support the image length > Int.MAX_VALUE" + imageProps.getMaxLength());
        this.zLevel = currentZLevel.get();
        return this;
    }

    public ImageLoader zoom() {
        //out of memory , this.zLevel > 5
        if (this.zLevel.getLevel() != this.imageProps.getZLevel() && this.zLevel.getLevel() < 6) {
            this.setImage(zoom(this.zLevel, this.imageProps, this.srcImage));
        }
        return this;
    }

    public ImageLoader toFile(File outFile) throws IOException {

        return this;
    }

    public static void toFile(BufferedImage srcImage, File outFile) throws IOException {
        ImageIO.write(srcImage, "JPG", outFile);
    }

    public static double caleZoomRate(ZLevel z, ImageProps imageProps) {
        long targetLength = z.getLengthRange().upperEndpoint();
        return (double) targetLength / imageProps.getMaxLength();
    }

    public static BufferedImage zoom(final ZLevel z, final ImageProps imageProps,
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
    public BufferedImage fill(final ZLevel z, final ImageProps imageProps, final BufferedImage srcImage)
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

    public ZLevel cut(String outDirPath) throws Exception {
        return cut(this.zLevel, outDirPath);
    }

    /**
     * todo: when difference z-level ,scale the tile
     *
     * @param zLevel
     * @param outDirPath
     * @return
     * @throws IOException
     */
    private ZLevel cut(ZLevel zLevel, String outDirPath) throws Exception {
        int maxTileNum = (1 << zLevel.getLevel()) - 1;
        int maxX = maxTileNum, maxY = maxTileNum;
        Range<Integer> tileXRange = Range.closed(0, maxX);
        Range<Integer> tileYRange = Range.closed(0, maxY);

        String tileTemplate = "{x}_{y}.jpg";
        for (int x = tileXRange.lowerEndpoint(); tileXRange.contains(x); x++) {
            for (int y = tileYRange.lowerEndpoint(); tileYRange.contains(y); y++) {
                String tileName = tileTemplate.replace("{x}", String.valueOf(x))
                        .replace("{y}", String.valueOf(y));
                //outside of raster
                int pixelX = TileBlockConstant.toPixelXY(x);
                int pixelY = TileBlockConstant.toPixelXY(y);
                int cutWidthSize = tileLength(pixelX, this.imageProps.getWidth());
                int cutHeightSize = tileLength(pixelY, this.imageProps.getHeight());
                if (y == 54) {
                    System.out.println("debug:" + x + "," + y);
                }

                //todo:-31
                BufferedImage tileImage = new BufferedImage(TileBlockConstant.SIZE_PIXEL,
                        TileBlockConstant.SIZE_PIXEL, BufferedImage.TYPE_INT_RGB);
                if (cutWidthSize > 0 && cutHeightSize > 0) {
                    BufferedImage tileContent = this.srcImage.getSubimage(pixelX, pixelY,
                            cutWidthSize, cutHeightSize);
                    coverImage(tileImage, tileContent, 0, 0, tileImage.getTileWidth(), tileImage.getTileHeight());
                    System.out.println("cut:" + x + "," + y);
                }
                File dir = Paths.get(outDirPath, String.valueOf(zLevel.getLevel())).toFile();
                if (!dir.exists()) {
                    Preconditions.checkState(dir.mkdirs());
                }
                ImageIO.write(tileImage, "JPG",
                        Paths.get(dir.getPath(), tileName).toFile());
            }
        }
        return this.zLevel;
    }

    public static BufferedImage coverImage(BufferedImage baseBufferedImage,
                                           BufferedImage coverBufferedImage,
                                           int x, int y, int width, int height) throws Exception {

        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = baseBufferedImage.createGraphics();

        // 绘制
        g2d.drawImage(coverBufferedImage, x, y, null);
        g2d.dispose();// 释放图形上下文使用的系统资源

        return baseBufferedImage;
    }

    private int tileLength(int xOrY, int widthOrHeight) {
        int size = widthOrHeight - xOrY;
        return size > TileBlockConstant.SIZE_PIXEL
                ? TileBlockConstant.SIZE_PIXEL : size;
    }

    public static void cutAll(ImageLoader imageLoader, String outDirPath) throws IOException {

        ZLevelConstant.range(imageLoader.zLevel.getLevel()).forEach(zLevel -> {
            try {
                imageLoader
                        .setZLevel(zLevel)
                        .zoom()
                        .toFile(new File("/Users/dongyan/Downloads/dest/zoom_" + zLevel.getLevel() + ".jpg"))
//                        .fill()
                        .toFile(new File("/Users/dongyan/Downloads/dest/fill_" + zLevel.getLevel() + ".jpg"))
                        .cut(outDirPath);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

