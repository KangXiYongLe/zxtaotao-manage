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

import cn.zxtaotao.manage.pojo.ItemParamItem;
import cn.zxtaotao.manage.service.ItemParamItemService;

@Controller
@RequestMapping("api/item/param/item")
public class ApiItemParamItemController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemParamItem.class);
    
    @Autowired
    private ItemParamItemService itemParamItemService;
    
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ResponseEntity<ItemParamItem> queryItemParamByItemId(@PathVariable("itemId") Long itemId) {

        try {
            ItemParamItem record = new ItemParamItem();
            record.setItemId(itemId);
            ItemParamItem itemParamItem = this.itemParamItemService.queryOne(record);
            if (itemParamItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemParamItem);
        } catch (Exception e) {
            LOGGER.error(
                    "查询商品规格信息出错：cn.zxtaotao.manage.controller.ItemParamItemController.queryItemParamByItemId(itemId = {})",
                    itemId, e);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
