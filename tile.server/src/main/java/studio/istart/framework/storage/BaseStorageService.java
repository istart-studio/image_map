package studio.istart.framework.storage;

import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * 仓储服务
 *
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@Log4j2
public abstract class BaseStorageService {

    @Value("${upload-path}")
    private String uploadRootPath;

    private static Path rootLocation;

    /**
     * 获取当前存储路径
     *
     * @return 绝对路径
     * @throws FileNotFoundException 路径没有找到
     */
    public Path getRootLocation() throws FileNotFoundException {
        if (rootLocation == null) {
            //获取根目录
            File rootPath = new File(uploadRootPath);
            if (!rootPath.exists()) {
                Preconditions.checkState(rootPath.mkdirs(), "rootPath.mkdirs() is fail");
            }
            log.info("path:" + rootPath.getAbsolutePath());
            //如果上传目录为/static/images/upload/，则可以如下获取：
            File upload = new File(rootPath.getAbsolutePath(), "upload");
            log.info("upload-path:" + upload.getAbsolutePath());
            if (!upload.exists()) {
                Preconditions.checkState(upload.mkdirs(), "upload.mkdirs() is fail");
            }
            log.info("basic upload url:" + upload.getAbsolutePath());
            rootLocation = Paths.get(upload.toURI());
        }
        return rootLocation;
    }

    protected void saveFile(InputStream inputStream, String fileFullPath, StorageActionEnum storageAction) throws IOException {
        File file = new File(fileFullPath);
        if (file.exists()) {
            if (storageAction == StorageActionEnum.OVERWRITE) {
                Preconditions.checkState(file.delete(), "file.delete() is fail");
            } else {
                throw new IOException(fileFullPath + "already exists!");
            }
        }
        Preconditions.checkState(file.createNewFile(), "file.createNewFile() is fail");
        //读取，及写入
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[inputStream.available()];
        int r;
        while ((r = inputStream.read(buffer)) > 0) {
            output.write(buffer, 0, r);
        }
        FileOutputStream fos = new FileOutputStream(fileFullPath);
        output.writeTo(fos);
        output.flush();
        output.close();
        fos.close();
    }

    public abstract Path load(String filename) throws IOException;

    public abstract Optional<StorageObject> loadAsResource(String filename) throws IOException;

    /**
     * 存储
     *
     * @param inputStream 内容
     * @param paths       路径或文件全名，顺序(a/b/c.exe)为a,b,c.exe
     */
    public abstract void store(InputStream inputStream, String... paths) throws IOException;
}
