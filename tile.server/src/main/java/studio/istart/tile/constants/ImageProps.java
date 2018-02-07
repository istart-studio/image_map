package studio.istart.tile.constants;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
        this.zLevel = ZLevelConstant.matchZLevel(this.maxLength).get().getLevel();
    }
}
