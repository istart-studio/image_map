package studio.istart.tile.constants;

import com.google.common.collect.Range;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public class TileConstant {

    /**
     * tile size must be larger the SIZE_PIXEL
     */
    public static final int SIZE_PIXEL = 256;

    public static int toPixelXY(int tileXY) {
        return tileXY * SIZE_PIXEL;
    }

    public static Range<Integer> tileNumRange(ZLevel zLevel) {
        int maxTileNum = (1 << zLevel.getLevel()) - 1;
        return Range.closed(0, maxTileNum);
    }
}
