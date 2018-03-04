package studio.istart.tile.controller;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import studio.istart.framework.service.BaseServiceResult;
import studio.istart.framework.service.ResultTypeEnum;
import studio.istart.tile.model.SnapshotModel;

import java.util.List;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@RestController
@RequestMapping("/snapshot")
@Log4j2
public class SnapshotApi {

    private final static List<SnapshotModel> cache = Lists.newArrayList();

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public BaseServiceResult upload(@RequestBody SnapshotModel snapshotModel) {
        cache.add(snapshotModel);
        log.info("收到快照[base64]：{}", snapshotModel.getImageBase64());
        return BaseServiceResult.builder().build(ResultTypeEnum.SUCCESS);
    }
}
