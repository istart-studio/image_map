package studio.istart.tile.test.service;

import org.junit.Test;
import studio.istart.tile.component.ImageLoader;
import studio.istart.tile.constants.ZLevel;

import java.io.File;
import java.io.IOException;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public class ImageLoaderTest {

    @Test
    public void zoom() throws IOException {
        ImageLoader
                .builder(new File("/Users/dongyan/Downloads/original/4_6827_13793.jpg"))
                .setZLevel(new ZLevel(1))
                .zoom()
                .toFile(new File("/Users/dongyan/Downloads/dest/zoom.jpg"));
    }

    @Test
    public void fill() throws IOException {
        ImageLoader
                .builder(new File("/Users/dongyan/Downloads/original/4_6827_13793.jpg"))
                .setZLevel(new ZLevel(1))
                .zoom()
                .toFile(new File("/Users/dongyan/Downloads/dest/zoom.jpg"))
                .fill()
                .toFile(new File("/Users/dongyan/Downloads/dest/fill.jpg"));
    }

    @Test
    public void cut() throws Exception {
        for (int z = 1; z < 7; z++) {
            ImageLoader
                    .builder(new File("/Users/dongyan/Downloads/original/4_6827_13793.jpg"))
                    .setZLevel(new ZLevel(z))
                    .zoom()
//                .toFile(new File("/Users/dongyan/Downloads/dest/zoom.jpg"))
//                .fill()
//                .toFile(new File("/Users/dongyan/Downloads/dest/fill.jpg"))
                    .cut("/Users/dongyan/Downloads/tiles");
        }
    }

    @Test
    public void cutAll() throws IOException {
        ImageLoader.cutAll(
                ImageLoader
                        .builder(new File("/Users/dongyan/Downloads/original/4_6827_13793.jpg")),
                "/Users/dongyan/Downloads/tiles");
    }
}
