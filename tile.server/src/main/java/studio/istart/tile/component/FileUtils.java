package studio.istart.tile.component;

import java.io.File;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public class FileUtils {
    public static boolean deleteDir(File dir)
    {
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success)
                {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
