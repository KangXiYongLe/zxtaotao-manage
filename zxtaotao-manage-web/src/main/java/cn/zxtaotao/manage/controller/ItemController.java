package cn.zxtaotao.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.zxtaotao.common.bean.EasyUIResult;
import cn.zxtaotao.manage.pojo.Item;
import cn.zxtaotao.manage.service.ItemService;

@Controller
@RequestMapping("item")
public class ItemController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
    
    @Autowired
    private ItemService itemService;
    
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,
            @RequestParam("itemParams") String itemParams) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("新增商品cn.zxtaotao.manage.controller.ItemController.saveItem(Item ={}, String={})",
                    item, desc);
        }
        if (item == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            Boolean saveBoolean = itemService.saveItem(item, desc,itemParams);
            if (!saveBoolean) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(
                            "新增商品失败cn.zxtaotao.manage.controller.ItemController.saveItem(Item ={}, String={})",
                            item, desc);
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            LOGGER.error("新增商品出错，item = {}", item, e);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    /**
     * 查询商品列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows) {

        try {
            EasyUIResult easyUIResult = this.itemService.queryItemList(page, rows);
            if (null == easyUIResult) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            LOGGER.error(
                    "查询商品失败：cn.zxtaotao.manage.controller.ItemController.queryItemList(page={}, rows={})",
                    page, rows, e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc,
            @RequestParam("itemParams") String itemParams) {

        try {
            Boolean b = this.itemService.updateItem(item, desc,itemParams);
            if (b) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            LOGGER.error(
                    "更新商品出错：cn.zxtaotao.manage.controller.ItemController.updateItem(item = {}, desc = {})",
                    item, desc, e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
