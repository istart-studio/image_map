package studio.istart.tile.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import studio.istart.framework.service.BaseServiceResult;
import studio.istart.framework.service.ResultTypeEnum;
import studio.istart.tile.component.ImageLoader;
import studio.istart.tile.model.ZoomLevel;
import studio.istart.tile.service.ConfigService;
import studio.istart.tile.storage.ImageStore;

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
@RestController
@RequestMapping("/source")
public class SourceApi {

    /**
     * 处理图片，（切图）
     *
     * @param localFilePath 文件的本地地址
     * @param baseDir       要切图的根目录
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/tiles/handleImage", method = RequestMethod.GET)
    public BaseServiceResult handleImage(@RequestParam String localFilePath,
                                         @RequestParam String baseDir) throws Exception {
        File baseDirFile = new File(baseDir);
        if (!baseDirFile.isDirectory()) {
            return BaseServiceResult.builder().build(ResultTypeEnum.PARAM_IS_ILLEGALITY, "baseDir 不是目录");
        }
        if (!baseDirFile.exists()) {
            return BaseServiceResult.builder().build(ResultTypeEnum.PARAM_IS_ILLEGALITY, "baseDir 不存在");
        }

        ConfigService.baseDir = baseDir;

        File sourceFile = new File(localFilePath);
        if (!sourceFile.exists()) {
            return BaseServiceResult.builder()
                    .build(ResultTypeEnum.FILE_IS_NOT_FOUND, "", "图片不存在");
        }
        ImageLoader imageLoader = ImageLoader.builder(sourceFile);
        new Thread(() -> {
            try {
                imageLoader.cut(new ZoomLevel(1), new ImageStore(ConfigService.baseDir));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }).start();

        return BaseServiceResult.builder()
                .build(ResultTypeEnum.SUCCESS,
                        new ImageInfo(imageLoader.getName(), imageLoader.getMaxZoomLevel()),
                        "图片正在处理，请查看服务器日志");
    }

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
        if (ConfigService.baseDir == null) {
            throw new IllegalStateException("ConfigService.baseDir is BLANK!");
        }
        //将图片输出给浏览器
        URI fileUri = Paths.get(ConfigService.baseDir, imageId, zLevel, xy + ".jpg").toUri();
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
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ImageInfo {
    private String name;
    private int maxZoomLevel;
}
