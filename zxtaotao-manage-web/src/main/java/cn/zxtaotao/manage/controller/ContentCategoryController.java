package cn.zxtaotao.manage.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.zxtaotao.manage.pojo.ContentCategory;
import cn.zxtaotao.manage.service.ContentCategoryService;

@Controller
@RequestMapping("content/category")
public class ContentCategoryController {
    
    private Logger LOGGER = LoggerFactory.getLogger(ContentCategory.class);
    
    @Autowired
    private ContentCategoryService contentCategoryService;
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> queryContentCategoryList(
            @RequestParam(value = "id", defaultValue = "0") Long parentId) {

        try {
            ContentCategory category = new ContentCategory();
            category.setParentId(parentId);
            List<ContentCategory> list = this.contentCategoryService.queryListByWhere(category);
            if (null == list || list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            LOGGER.error(
                    "查询内容类目失败：cn.zxtaotao.manage.controller.ContentCategoryController.queryContentCategoryList(parentId={})",
                    parentId, e);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory) {

        try {
            Boolean b = this.contentCategoryService.saveContentCategory(contentCategory);
            if (b) {
                return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
            }
        } catch (Exception e) {
            LOGGER.error(
                    "添加内容类目失败：cn.zxtaotao.manage.controller.ContentCategoryController.saveContentCategory(contentCategory={})",
                    contentCategory, e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> rename(@RequestParam("id") Long id, @RequestParam("name") String name) {
        try {
            ContentCategory category = new ContentCategory();
            category.setId(id);
            category.setName(name);
            this.contentCategoryService.updateSelective(category);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOGGER.error(
                    "重命名内容类目失败：cn.zxtaotao.manage.controller.ContentCategoryController.rename(id={}, name={})",
                    id, name, e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    /**
     * 删除节点，包括它所有的子节点都删除
     * 
     * @param contentCategory
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(ContentCategory contentCategory) {
        try {
            this.contentCategoryService.deleteAll(contentCategory);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOGGER.error(
                    "节点删除失败：cn.zxtaotao.manage.controller.ContentCategoryController.delete(contentCategory={})",
                    contentCategory, e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
