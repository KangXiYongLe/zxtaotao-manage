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

import cn.zxtaotao.manage.pojo.ItemDesc;
import cn.zxtaotao.manage.service.ItemDescService;

@Controller
@RequestMapping("api/item/desc")
public class ApiItemDescController {
    
    private static final Logger LOGGER =LoggerFactory.getLogger(ItemDesc.class);
    
    @Autowired
    private ItemDescService itemDescService;
    
    @RequestMapping(method=RequestMethod.GET,value="{ItemId}")
    public ResponseEntity<ItemDesc> queryItemDescByItemId(@PathVariable("ItemId") Long ItemId){
        
        try {
            ItemDesc itemDesc = this.itemDescService.queryById(ItemId);//把商品的id看作是商品描述的id,1对1
            if(null == itemDesc){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemDesc);
        } catch (Exception e) {
            LOGGER.error("查询商品描述出错：cn.zxtaotao.manage.controller.ItemDescController.queryItemDescByItemId(ItemId={})", ItemId,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
