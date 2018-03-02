package studio.istart.tile.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import studio.istart.framework.service.BaseServiceResult;
import studio.istart.framework.service.ResultTypeEnum;
import studio.istart.tile.model.AreaModel;

import java.util.List;
import java.util.Optional;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@RestController
@RequestMapping("/area")
@Log4j2
public class AreaApi {

    private static List<AreaModel> cache = Lists.newArrayList();

    @RequestMapping(method = RequestMethod.GET)
    public BaseServiceResult query() {
        return BaseServiceResult.builder().build(ResultTypeEnum.SUCCESS, cache);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseServiceResult save(@RequestBody AreaModel areaModel) {
        if (Strings.isNullOrEmpty(areaModel.getAreaId())) {
            return BaseServiceResult.builder().build(ResultTypeEnum.PARAM_IS_ILLEGALITY, null, "area id can't be null or empty");
        }
        cache.add(areaModel);
        log.info("add area by {}", areaModel.getAreaId());

        return BaseServiceResult.builder().build(ResultTypeEnum.SUCCESS);
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public BaseServiceResult del(@RequestParam String areaId) {

        Optional<AreaModel> areaModelOptional = cache.stream().filter(area -> area.getAreaId().equals(areaId)).findFirst();
        if (areaModelOptional.isPresent()) {
            AreaModel areaModel = areaModelOptional.get();
            cache.remove(areaModel);
            log.info("remove area by {}", areaId);
        } else {
            log.info("can't area by {} it will throw Exception", areaId);
        }
        return BaseServiceResult.builder().build(ResultTypeEnum.SUCCESS);
    }
}
