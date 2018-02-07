package studio.istart.tile.component;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import studio.istart.tile.constants.ImageProps;
import studio.istart.tile.constants.TileConstant;
import studio.istart.tile.constants.ZLevel;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public class ImageCutter {

    public static void cutLevel(final ZLevel zLevel,
                                String parentTilesDir, String outDir) throws Exception {
        //上一级
        ZLevel parentZLevel = new ZLevel(zLevel.getLevel() + 1);
        //z总瓦片数
        Range<Integer> tileXRange = TileConstant.tileNumRange(zLevel);
        Range<Integer> tileYRange = TileConstant.tileNumRange(zLevel);
        //存放路径
        File dir = Paths.get(outDir, String.valueOf(zLevel.getLevel())).toFile();
        if (!dir.exists()) {
            Preconditions.checkState(dir.mkdirs());
        }
        //瓦片模版
        String tileTemplate = "{x}_{y}.jpg";
        for (int x = tileXRange.lowerEndpoint(); tileXRange.contains(x); x++) {
            for (int y = tileYRange.lowerEndpoint(); tileYRange.contains(y); y++) {
                String tileName = tileTemplate.replace("{x}", String.valueOf(x)).replace("{y}", String.valueOf(y));
                System.out.println("sub tileName:" + tileName);
                BufferedImage tileImage = new BufferedImage(TileConstant.SIZE_PIXEL, TileConstant.SIZE_PIXEL, BufferedImage.TYPE_INT_RGB);
                // scale ： 256 * 2 => 256
                BufferedImage parentTileImage = parentTiles(x, y, parentTilesDir);
                // 创建Graphics2D对象，用在底图对象上绘图
                Graphics2D g2d = tileImage.createGraphics();
                // 绘制
                g2d.drawImage(parentTileImage, 0, 0, TileConstant.SIZE_PIXEL, TileConstant.SIZE_PIXEL, null);
                g2d.dispose();// 释放图形上下文使用的系统资源

                ImageIO.write(tileImage, "JPG",
                        Paths.get(dir.getPath(), tileName).toFile());
            }
        }
    }

    public static BufferedImage parentTiles(int x, int y, String parentDirPath) throws IOException {
        //parent tiles file dir


        // get 4 parent tiles
        Range<Integer> xRange = parentTilesRange(x);
        Range<Integer> yRange = parentTilesRange(y);
        //draw 512(256*2) * 512(256*2)
        BufferedImage margeImage = new BufferedImage(TileConstant.SIZE_PIXEL * 2,
                TileConstant.SIZE_PIXEL * 2, BufferedImage.TYPE_INT_RGB);
        boolean debug = false;
        for (int parentX = xRange.lowerEndpoint(), xIndex = 0;
             parentX < xRange.upperEndpoint() + 1; parentX++, xIndex++) {
            for (int parentY = yRange.lowerEndpoint(), yIndex = 0;
                 parentY < yRange.upperEndpoint() + 1; parentY++, yIndex++) {
                String tileName = parentX + "_" + parentY + ".jpg";
                System.out.println("parent tileName:" + tileName);
                File tileFile = Paths.get(parentDirPath, tileName).toFile();
                BufferedImage tileImage = ImageIO.read(tileFile);
                Graphics margeImageGraphics = margeImage.getGraphics();
                System.out.println("xIndex:" + xIndex);
                System.out.println("yIndex:" + yIndex);
                int pointX = xIndex * TileConstant.SIZE_PIXEL;
                int pointY = yIndex * TileConstant.SIZE_PIXEL;
                System.out.println("pointX:" + pointX);
                System.out.println("pointY:" + pointY);
                margeImageGraphics.drawImage(tileImage, pointX, pointY, null);
                margeImageGraphics.dispose();
            }
        }
        if (debug) {
            ImageIO.write(margeImage, "JPG", new File("/Users/dongyan/Downloads/dest/test.jpg"));
        }

        return margeImage;
    }

    public static Range<Integer> parentTilesRange(int xOrY) {
        return Range.closed(xOrY * 2, xOrY * 2 + 1);
    }

}
