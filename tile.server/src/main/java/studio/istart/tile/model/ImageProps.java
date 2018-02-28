package studio.istart.tile.model;

import lombok.Data;
import lombok.ToString;
import studio.istart.tile.component.ZoomLevelComponent;

import java.awt.image.BufferedImage;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@Data
@ToString
public class ImageProps {

    private int width;
    private int height;
    private int maxLength;
    private int zLevel;

    public ImageProps(BufferedImage image) {
        this.width = image.getTileWidth();
        this.height = image.getTileHeight();
        this.maxLength = width > height ? width : height;
        this.zLevel = ZoomLevelComponent.matchZLevel(this.maxLength).get().getLevel();
    }
}
