package studio.istart.tile.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Paths;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@Log4j2
@Controller
@RequestMapping("/source")
public class SourceController {

    @RequestMapping(value = "/tiles/{zLevel}/{xy}", method = RequestMethod.GET)
    public void tiles(@PathVariable String zLevel, @PathVariable String xy,
                      HttpServletResponse response) throws IOException {
        String dir = "/Users/dongyan/Downloads/tiles";

        //将图片输出给浏览器
        URI fileUri = Paths.get(dir, zLevel, xy + ".jpg").toUri();
        log.info("img uri:{}", fileUri);
        File file = new File(fileUri);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            inputStream.read(data);
            inputStream.close();

            response.setContentType("image/jpg");

            OutputStream stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
            stream.close();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void image(@RequestParam String imageName, HttpServletResponse response) throws IOException {
        String dir = "/Users/dongyan/Downloads/图片/";

        //将图片输出给浏览器
        URI fileUri = Paths.get(dir, imageName).toUri();
        log.info("img uri:{}", fileUri);
        File file = new File(fileUri);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            inputStream.read(data);
            inputStream.close();

            response.setContentType("image/jpg");

            OutputStream stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
