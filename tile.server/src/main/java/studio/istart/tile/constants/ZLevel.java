package studio.istart.tile.constants;

import com.google.common.collect.Range;
import lombok.Data;
import lombok.NoArgsConstructor;

import static studio.istart.tile.constants.ZLevelConstant.MAX_LEVEL_NUM;
import static studio.istart.tile.constants.ZLevelConstant.MIN_LEVEL_NUM;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@Data
@NoArgsConstructor
public class ZLevel {
    private int level;
    /**
     * map width = map height = 256 * 2lg(level) pixels
     */
    private Range<Long> lengthRange;

    public ZLevel(int level) {
        this.level = level;
        int lower;
        if (MIN_LEVEL_NUM <= level && level <= MAX_LEVEL_NUM) {
            lower = TileConstant.SIZE_PIXEL << (level - 1);
        } else {
            throw new IllegalArgumentException("the level " + level + " not in range");
        }
        this.lengthRange = Range.openClosed((long) lower,
                (long) TileConstant.SIZE_PIXEL << (long) level);
    }
}