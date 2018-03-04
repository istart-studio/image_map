package studio.istart.tile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnapshotModel {
    private String imageName;
    private String imageType;
    private String imageBase64;
}
