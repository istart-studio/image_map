package studio.istart.tile.test.service;

import org.junit.Test;
import studio.istart.tile.service.Cutter;
import studio.istart.tile.service.OnCompliteListener;
import studio.istart.tile.service.OnProgressUpdateListener;
import studio.istart.tile.service.PointVO;


/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public class Main {

    /**
     * @author DongYan
     * @version 1.0.0
     * @since 1.8
     */
    public static class Config {
        public static String imgPath = "/Users/dongyan/Downloads/图片/thumbnail4.jpg";// 待裁剪的图片
        public static String saveDirPath = "/Users/dongyan/Downloads/tile/";// 裁剪后的保存路径
        public static String name = "tiles";
        public static int tileSize = 256;// 瓦片的大小
        // top left point
        public static int pointTopLeftX = 0;// 左上角图片X坐标
        public static int pointTopLeftY = 0;// 左上角图片Y坐标
        //坐标拾取器：http://api.map.baidu.com/lbsapi/getpoint/，如核桃冲：105.178774,26.824964
        public static double pointTopLeftLat = 84;// 左上角纬度//
        public static double pointTopLeftLon = -180;// 左上角经度
        //bottom right point
        public static int pointBottomRightX = 6827;// 右下角图片X坐标
        public static int pointBottomRightY = 13793;// 右下角图片Y坐标
        public static double pointBottomRightLat = -84;// 右下角纬度
        public static double pointBottomRightLon = 180;// 右下角经度
    }

    @Test
    public void main() {
        Cutter cutter = new Cutter(new OnProgressUpdateListener() {
            @Override
            public void onProgressUpdate(int value) {
                System.out.println("progress: " + value);
            }
        }, new OnCompliteListener() {
            @Override
            public void onComplite() {
                System.out.println("finish");
            }
        });
        PointVO pointTopLeft = new PointVO();
        pointTopLeft.setX(Config.pointTopLeftX);
        pointTopLeft.setY(Config.pointTopLeftY);
        pointTopLeft.setLat(Config.pointTopLeftLat);
        pointTopLeft.setLon(Config.pointTopLeftLon);
        PointVO pointBottomRight = new PointVO();
        pointBottomRight.setX(Config.pointBottomRightX);
        pointBottomRight.setY(Config.pointBottomRightY);
        pointBottomRight.setLat(Config.pointBottomRightLat);
        pointBottomRight.setLon(Config.pointBottomRightLon);
        cutter.startCuttingAndroid(Config.imgPath, Config.saveDirPath,
            Config.name, Config.tileSize, pointTopLeft, pointBottomRight);
    }
}
