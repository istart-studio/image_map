package studio.istart.tile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaModel {
    /**
     * 前端定义的id
     */
    private String areaId;

    /**
     * 区域坐标
     */
    private Double[][][] coordinates;

    /**
     * 标记内容
     */
    private String content;

    
}
