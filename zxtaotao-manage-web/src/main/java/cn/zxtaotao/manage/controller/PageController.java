package cn.zxtaotao.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 通用的跳转页面
 * @author zengkang
 *
 */
@Controller
@RequestMapping("page")
public class PageController {

    /**
     * 通用跳转到WEB-INF目录下页面的方法
     * @param pageName
     * @return
     */
    @RequestMapping(value="{pageName}",method=RequestMethod.GET)
    public String toPage(@PathVariable("pageName")String pageName){
        return pageName;
    }
}
