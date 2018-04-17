package cn.zxtaotao.manage.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.zxtaotao.manage.pojo.ItemCat;
import cn.zxtaotao.manage.service.ItemCatService;

@Controller
@RequestMapping("item/cat")
public class ItemCatController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemCat.class);

    @Autowired
    private ItemCatService itemCatService;
    
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<ItemCat>> queryItemCat(@RequestParam(value="id",defaultValue="0") Long pid){
        try {
            ItemCat itemCat = new ItemCat();
            itemCat.setParentId(pid);
            List<ItemCat> itemCats = itemCatService.queryListByWhere(itemCat);
            if(null == itemCats||itemCats.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemCats);
        } catch (Exception e) {
            LOGGER.error("查询商品类目出错cn.zxtaotao.manage.controller.ItemCatController.queryItemCat(pid={})",pid,e );
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    @RequestMapping(method=RequestMethod.GET,value="{id}")
    public ResponseEntity<ItemCat> queryItemCatById(@PathVariable Long id){
        try {
            ItemCat itemCat = this.itemCatService.queryById(id);
            if(null == itemCat){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemCat);
        } catch (Exception e) {
            
            LOGGER.error("查询商品类目粗错cn.zxtaotao.manage.controller.ItemCatController.queryItemCatById(id={})", id,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
}
