package cn.zxtaotao.manage.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.zxtaotao.manage.pojo.Item;
import cn.zxtaotao.manage.service.ItemService;

@Controller
@RequestMapping("api/item")
public class ApiItemController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiItemController.class);
    
    @Autowired
    private ItemService itemService;   
    
    /**
     * 对外接口服务，根据商品的id查询商品的基本数据
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,value="{itemId}")
    public ResponseEntity<Item> queryItemById(@PathVariable("itemId")Long itemId) {

        try {
            Item item = this.itemService.queryById(itemId);
            if (null == item) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            LOGGER.error(
                    "查询商品失败：cn.zxtaotao.manage.controller.ItemController.queryItemList(itemId={})",
                    itemId,  e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
