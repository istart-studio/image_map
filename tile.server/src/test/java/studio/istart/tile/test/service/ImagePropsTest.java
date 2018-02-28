package studio.istart.tile.test.service;

import org.junit.Test;
import studio.istart.tile.model.ImageProps;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public class ImagePropsTest {
    @Test
    public ImageProps props() throws IOException {
        BufferedImage bufferedImage = ImageIO.read(
                new File("/Users/dongyan/Downloads/original/4_6827_13793.jpg"));
        ImageProps imageProps = new ImageProps(bufferedImage);
        System.out.println(imageProps);
        return imageProps;
    }
}
