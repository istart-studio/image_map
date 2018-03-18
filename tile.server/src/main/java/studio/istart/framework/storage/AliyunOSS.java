package studio.istart.framework.storage;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.google.common.base.Function;
import lombok.extern.log4j.Log4j2;
import studio.istart.framework.tool.JsonHelper;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * 阿里云OSS
 *
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@Log4j2
public abstract class AliyunOSS extends BaseStorageService {

    protected String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    protected String accessKeyId = "LTAI3UVWyYNM3mnz";
    protected String accessKeySecret = "crghf3xHPsAvSx27pJeYcEPdQBKsAR";
    private String bucketName = "**********";

    protected abstract String getBucketName();

    @Override
    @Deprecated
    public Path getRootLocation() throws FileNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public Path load(String filename) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<StorageObject> loadAsResource(String filename) throws IOException {

        Object storageObject = executor(client -> {
            /*
             * Upload an object to your bucket from an input stream
             */
            log.info("Uploading a new object to OSS from an input stream\n");
            OSSObject object = client.getObject(new GetObjectRequest(getBucketName(), filename));
            if (object != null) {
                // copy inputStream
                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                try {
                    while ((len = object.getObjectContent().read(buffer)) > -1) {
                        byteOutputStream.write(buffer, 0, len);
                    }
                    byteOutputStream.flush();
                    return new StorageObject(filename, new ByteArrayInputStream(byteOutputStream.toByteArray()));
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
            return null;
        });

        return Optional.ofNullable((StorageObject) storageObject);
    }

    @Override
    public void store(InputStream inputStream, String... paths) throws IOException {
        String key = Paths.get("", paths).toString();
        executor(client -> {
            /*
             * Upload an object to your bucket from an input stream
             */
            log.info("Uploading a new object to OSS from an input stream\n");
            client.putObject(getBucketName(), key, inputStream);
            return null;
        });
    }

    private Object executor(Function<OSSClient, Object> function) throws IOException {
        /*
         * Constructs a client instance with your account for accessing OSS
         */
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        Object result = null;
        try {
            result = function.apply(client);
        } catch (OSSException oe) {
            log.error(oe.getMessage(),oe);
            log.info("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.info("Error Message: " + oe.getErrorCode());
            log.info("Error Code:       " + oe.getErrorCode());
            log.info("Request ID:      " + oe.getRequestId());
            log.info("Host ID:           " + oe.getHostId());
            if (!oe.getErrorCode().equals("NoSuchKey")) {
                throw new IOException(JsonHelper.serialize(oe));
            }
        } catch (ClientException ce) {
            log.error(ce.getMessage(),ce);
            log.info("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.info("Error Message: " + ce.getMessage());
            throw new IOException(JsonHelper.serialize(ce));
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            client.shutdown();
        }
        return result;
    }

}
