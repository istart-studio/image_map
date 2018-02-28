package studio.istart.tile.component;

import com.google.common.collect.Range;
import studio.istart.tile.model.ZoomLevel;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public class TileComponent {

    /**
     * tile size must be larger the SIZE_PIXEL
     */
    public static final int SIZE_PIXEL = 256;

    public static int toPixelXY(int tileXY) {
        return tileXY * SIZE_PIXEL;
    }

    public static Range<Integer> tileNumRange(ZoomLevel zLevel) {
        int maxTileNum = (1 << zLevel.getLevel()) - 1;
        return Range.closed(0, maxTileNum);
    }
}
