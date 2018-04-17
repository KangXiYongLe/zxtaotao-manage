package cn.zxtaotao.manage.controller;

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

import cn.zxtaotao.common.bean.EasyUIResult;
import cn.zxtaotao.manage.pojo.ItemParam;
import cn.zxtaotao.manage.service.ItemParamService;

@Controller
@RequestMapping("item/param")
public class ItemParamController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemParam.class);
    
    @Autowired
    private ItemParamService itemParamService;
    
    @RequestMapping(value = "{itemCatId}", method = RequestMethod.GET)
    public ResponseEntity<ItemParam> queryItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId) {

        try {
            ItemParam itemParam = new ItemParam();
            itemParam.setItemCatId(itemCatId);
            ItemParam param = this.itemParamService.queryOne(itemParam);
            if (null == param) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(param);
        } catch (Exception e) {
            LOGGER.error(
                    "查询规格参数模板出错：cn.zxtaotao.manage.controller.ItemParamController.queryItemParamByItemCatId(itemCatId = {})",
                    itemCatId, e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    @RequestMapping(value = "{itemCatId}", method = RequestMethod.POST)
    public ResponseEntity<Void> saveItemParam(@PathVariable("itemCatId") Long itemCatId,
            @RequestParam("paramData") String paramData) {

        try {
            ItemParam record = new ItemParam();
            record.setItemCatId(itemCatId);
            record.setParamData(paramData);
            this.itemParamService.save(record);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            LOGGER.error(
                    "保存规格参数模板失败：cn.zxtaotao.manage.controller.ItemParamController.saveItemParam(itemCatId={}, paramData={})",
                    itemCatId, paramData, e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemParam(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows) {
        try {
            EasyUIResult easyUIResult = this.itemParamService.queryItemParam(page, rows);
            if (null == easyUIResult) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            LOGGER.error(
                    "查询规格参数出错：cn.zxtaotao.manage.controller.ItemParamController.queryItemParam(page={}, rows={})",
                    page, rows, e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    

}
