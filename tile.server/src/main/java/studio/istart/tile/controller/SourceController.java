package studio.istart.tile.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import studio.istart.framework.service.BaseServiceResult;
import studio.istart.framework.service.ResultTypeEnum;
import studio.istart.tile.component.ImageLoader;
import studio.istart.tile.constants.DirConstants;
import studio.istart.tile.model.ZoomLevel;

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

    /**
     * 获取瓦片
     *
     * @param imageId  图片名称（无后缀）且不包含特殊符号
     * @param zLevel
     * @param xy
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/tiles/{imageId}/{zLevel}/{xy}", method = RequestMethod.GET)
    public void tiles(@PathVariable String imageId,
                      @PathVariable String zLevel, @PathVariable String xy,
                      HttpServletResponse response) throws IOException {
        //将图片输出给浏览器
        URI fileUri = Paths.get(DirConstants.TILE, imageId, zLevel, xy + ".jpg").toUri();
        log.info("img uri:{}", fileUri);
        File file = new File(fileUri);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            inputStream.read(data);
            inputStream.close();

            response.setContentType("image/jpg");
            response.setHeader("Access-Control-Allow-Origin", "*");

            OutputStream stream = response.getOutputStream();
            stream.write(data);
            stream.flush();
            stream.close();
        }
    }

    @RequestMapping(value = "/tiles/cut/{localFilePath}", method = RequestMethod.GET)
    public BaseServiceResult cut(@PathVariable String localFilePath) throws Exception {
        File sourceFile = new File(localFilePath);
        if (!sourceFile.exists()) {
            return BaseServiceResult.builder()
                    .build(ResultTypeEnum.FILE_IS_NOT_FOUND, "", "图片不存在");
        }
        new Thread(() -> {
            try {
                ImageLoader.builder(sourceFile).cut(new ZoomLevel(0), DirConstants.TILE);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }).start();
        return BaseServiceResult.builder()
                .build(ResultTypeEnum.SUCCESS, "", "图片正在处理，请查看服务器日志");
    }
}
