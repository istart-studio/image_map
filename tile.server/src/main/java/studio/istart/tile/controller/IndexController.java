package studio.istart.tile.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
@Log4j2
@Controller
public class IndexController {

    @GetMapping("/index")
    public String index(){
        return "index";
    }
}
