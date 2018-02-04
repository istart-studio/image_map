package studio.istart.tile.controller;

import com.sun.imageio.plugins.common.ImageUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@Log4j2
@Controller
@RequestMapping("/source")
public class SourceController {

    @RequestMapping(value = "/{zLevel}/{xy}", method = RequestMethod.GET)
    public void request(@PathVariable String zLevel, @PathVariable String xy,
                        HttpServletResponse response) throws IOException {
        String dir = "/Users/dongyan/Downloads/tile/tiles/tiles_files/";

        //将图片输出给浏览器
//        BufferedImage image = (BufferedImage) objs[1];
//        response.setContentType("image/png");
//        OutputStream os = response.getOutputStream();
//        ImageIO.write(image, "png", os);
    }
}
