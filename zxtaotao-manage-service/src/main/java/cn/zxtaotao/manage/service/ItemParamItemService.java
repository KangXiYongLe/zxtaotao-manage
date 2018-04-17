package cn.zxtaotao.manage.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;

import cn.zxtaotao.manage.mapper.ItemParamItemMapper;
import cn.zxtaotao.manage.pojo.ItemParamItem;
import cn.zxtaotao.manage.service.base.BaseService;

@Service
public class ItemParamItemService extends BaseService<ItemParamItem> {
    
    @Autowired
    ItemParamItemMapper itemParamItemMapper;

    public Integer updateItemParamItem(Long itemId, String itemParams) {
        //要更新的数据
        ItemParamItem paramItem = new ItemParamItem();
        paramItem.setParamData(itemParams);
        paramItem.setUpdated(new Date());
        
        //更新条件
        Example example = new Example(ItemParamItem.class);
        example.createCriteria().andEqualTo("itemId", itemId);        

        return this.itemParamItemMapper.updateByExampleSelective(paramItem, example);
    }

}
