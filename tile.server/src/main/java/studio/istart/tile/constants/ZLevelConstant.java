package studio.istart.tile.constants;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@Log4j2
public class ZLevelConstant {

    public static final int MIN_LEVEL_NUM = 1;
    public static final int MAX_LEVEL_NUM = 23;
    public static ArrayList<ZLevel> zLevels;

    static {
        zLevels = Lists.newArrayListWithCapacity(MAX_LEVEL_NUM + 1);
        for (int levelNum = MIN_LEVEL_NUM;
             levelNum < MAX_LEVEL_NUM + 1;
             levelNum++) {
            zLevels.add(new ZLevel(levelNum));
        }
    }

    /**
     * @param size image largest size (width or height)
     * @return
     */
    public static Optional<ZLevel> matchZLevel(int size) {
        Preconditions.checkArgument(size > TileBlockConstant.SIZE_PIXEL,
                "the size " + size + " must be larger " + TileBlockConstant.SIZE_PIXEL + " pixel");
        return zLevels.stream()
                .filter(zLevel -> zLevel.getLengthRange().contains((long) size))
                .findFirst();
    }

    public static List<ZLevel> range(int z) {
        Preconditions.checkArgument(MIN_LEVEL_NUM <= z && z <= MAX_LEVEL_NUM);
        return zLevels.stream()
                .filter(zLevel -> zLevel.getLevel() <= z)
                .collect(Collectors.toList());
    }
}
