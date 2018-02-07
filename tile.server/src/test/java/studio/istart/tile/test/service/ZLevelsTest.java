package studio.istart.tile.test.service;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import studio.istart.tile.constants.ZLevel;
import studio.istart.tile.constants.ZLevelConstant;

import java.io.IOException;
import java.util.Optional;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@Log4j2
public class ZLevelsTest {

    @Test
    public void zLevels() {
        System.out.println("ZLevels :{}" + ZLevelConstant.zLevels);
    }

    @Test
    public void match() throws IOException {
        ImagePropsTest imagePropsTest = new ImagePropsTest();
        Optional<ZLevel> currentZLevel = ZLevelConstant.matchZLevel(imagePropsTest.props().getMaxLength());
        System.out.println("currentZLevel:" + currentZLevel);
    }
}