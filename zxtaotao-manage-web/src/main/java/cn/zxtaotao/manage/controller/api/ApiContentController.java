package cn.zxtaotao.manage.controller.api;

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
import cn.zxtaotao.manage.pojo.Content;
import cn.zxtaotao.manage.service.ContentService;

@Controller
@RequestMapping("api/content")
public class ApiContentController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Content.class);
    
    @Autowired
    private ContentService contentService;
    
    /**
     * 根据内容分类id查询内容列表
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryListByCategoryId(@RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows) {
        try {
            EasyUIResult result = this.contentService.queryListByCategoryId(categoryId, page, rows);
            if (result == null || result.getRows().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            LOGGER.error(
                    "查询内容列表失败：cn.zxtaotao.manage.controller.ContentController.queryListByCategoryId(categoryId={}, page={}, rows={})",
                    categoryId, page, rows, e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

    }

}
