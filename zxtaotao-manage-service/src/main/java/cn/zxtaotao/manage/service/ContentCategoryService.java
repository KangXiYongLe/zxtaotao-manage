package cn.zxtaotao.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.zxtaotao.manage.pojo.ContentCategory;
import cn.zxtaotao.manage.service.base.BaseService;

@Service
public class ContentCategoryService extends BaseService<ContentCategory> {

    public Boolean saveContentCategory(ContentCategory contentCategory) {
        contentCategory.setId(null);
        contentCategory.setIsParent(false);
        contentCategory.setSortOrder(1);
        contentCategory.setStatus(1);
        Integer save = super.save(contentCategory);
        
        ContentCategory category = super.queryById(contentCategory.getParentId());
        Integer update = 0;
        if(!category.getIsParent()){
            category.setIsParent(true);
            update = super.update(category);
        }
        
        return save == 1 && update==1;
    }

    public void deleteAll(ContentCategory contentCategory) {
        List<Object> ids = new ArrayList<Object>();
        ids.add(contentCategory.getId());  
        
        //递归查找该节点下的所有的子节点的id，并放入ids集合
        this.findAllSubNode(ids,contentCategory.getId());
        
        //通过id集合，批量删除节点
        super.deleteByIds(ids, ContentCategory.class, "id");
        
        //判断该节点的父节点下面是否还有子节点，如果没有了，将父节点的isParent改成false
        ContentCategory category = new ContentCategory();
        category.setParentId(contentCategory.getParentId());
        List<ContentCategory> list = super.queryListByWhere(category);
        if(null==list||list.isEmpty()){
            ContentCategory parent = new ContentCategory();
            parent.setId(contentCategory.getParentId());
            parent.setIsParent(false);
            super.updateSelective(parent);
        }
        
    }

    private void findAllSubNode(List<Object> ids, Long parentId) {
        
        //通过父节点id，查询到所有的直接子节点
        ContentCategory record = new ContentCategory();
        record.setParentId(parentId);
        List<ContentCategory> list = super.queryListByWhere(record);
        //遍历子节点集合，取出id的值放入ids集合，并且判断该子节点下面是否还有子节点，如果有，继续这一操作
        for (ContentCategory contentCategory : list) {
            ids.add(contentCategory.getId());
            if(contentCategory.getIsParent()){
                findAllSubNode(ids, contentCategory.getId());
            }
        }
        
    }

}
