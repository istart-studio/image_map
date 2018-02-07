package studio.istart.tile.constants;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public class TileBlockConstant {

    /**
     * tile size must be larger the SIZE_PIXEL
     */
    public static final int SIZE_PIXEL = 256;

    public static int toPixelXY(int tileXY) {
        return tileXY * SIZE_PIXEL;
    }
}
