package cn.zxtaotao.manage.controller.api;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.zxtaotao.common.bean.ItemCatResult;
import cn.zxtaotao.manage.pojo.ItemCat;
import cn.zxtaotao.manage.service.ItemCatService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("api/item/cat")
public class ApiItemCatController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemCat.class);
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    @Autowired
    private ItemCatService itemCatService;
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> queryItemCat(@RequestParam(value="callback",required=false)String callback) {

        
        try {
            ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
            if (itemCatResult == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            String result = MAPPER.writeValueAsString(itemCatResult);
            if(StringUtils.isEmpty(callback)){//无需支持跨域，无需给一个回掉函数
                return ResponseEntity.ok(result);
            }else{
                return ResponseEntity.ok(callback+"("+result+")");
            }
            
        } catch (Exception e) {
            LOGGER.error("查询商品类目信息出错：cn.zxtaotao.manage.controller.api.ApiItemCatController.queryItemCat()",
                    e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
