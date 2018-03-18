package studio.istart.framework.storage;

import java.io.InputStream;

/**
 * 仓储对象
 *
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public class StorageObject {

    private String name;
    private InputStream content;

    public StorageObject() {
    }

    public StorageObject(String name, InputStream content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }
}
