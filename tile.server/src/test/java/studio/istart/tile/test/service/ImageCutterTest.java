package studio.istart.tile.test.service;

import org.junit.Test;
import studio.istart.tile.component.ImageCutter;
import studio.istart.tile.component.ImageLoader;
import studio.istart.tile.constants.ZLevel;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public class ImageCutterTest {
    @Test
    public void cut() throws Exception {
        //cut 6
//        ImageLoader
//                .builder(Paths.get("/Users/dongyan/Downloads/original/4_6827_13793.jpg").toFile())
//                .cut("/Users/dongyan/Downloads/tiles");
        System.out.println("level 6 is done!");
        //cut 5
        ImageCutter.cutLevel(new ZLevel(5),
                "/Users/dongyan/Downloads/tiles/6",
                "/Users/dongyan/Downloads/tiles");
    }
}
