package studio.istart.framework.storage;

import java.io.IOException;

/**
 * 仓储异常
 *
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
class StorageException extends IOException {

    private static final long serialVersionUID = 1L;

    StorageException(String message) {
        super(message);
    }

    StorageException(String message, Throwable cause) {
        super(message, cause);
    }

}
