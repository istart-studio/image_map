package studio.istart.framework.storage;

/**
 * 仓储文件没有找到
 *
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public class StorageFileNotFoundException extends StorageException{

    private static final long serialVersionUID = 1L;

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}