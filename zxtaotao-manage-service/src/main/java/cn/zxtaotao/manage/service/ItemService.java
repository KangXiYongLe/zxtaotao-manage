package cn.zxtaotao.manage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.zxtaotao.common.bean.EasyUIResult;
import cn.zxtaotao.common.service.ApiService;
import cn.zxtaotao.manage.pojo.Item;
import cn.zxtaotao.manage.pojo.ItemDesc;
import cn.zxtaotao.manage.pojo.ItemParamItem;
import cn.zxtaotao.manage.service.base.BaseService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class ItemService extends BaseService<Item> {
    
    @Autowired
    private ItemDescService itemDescService;
    
    @Autowired
    private ItemParamItemService itemParamItemService;
    
    @Autowired
    private Mapper<Item> mapper;
    
    @Autowired
    private ApiService apiService;
    
    @Value("${TAOTAO_WEB_URL}")
    private String TAOTAO_WEB_URL;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    public Boolean saveItem(Item item,String desc, String itemParams){
        //保存商品信息
        item.setStatus(1);
        item.setId(null);
        Integer i = super.saveSelective(item);
        
        //保存商品描述信息
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());
        Integer i2 = this.itemDescService.saveSelective(itemDesc);
        
        //保存商品规格参数信息
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        Integer i3 = this.itemParamItemService.saveSelective(itemParamItem); 
        
        //发送消息通知其它系统
        sendMsg(item.getId(), "insert");
        
        return i==1&&i2==1&&i3==1;
    }

    public EasyUIResult queryItemList(Integer page, Integer rows) {
        
        PageHelper.startPage(page, rows);
        
        Example example = new Example(Item.class);
        example.setOrderByClause("updated DESC");
        List<Item> list = this.mapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<Item>(list);
        
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

    public Boolean delePic(Long id) {
        Item item = this.queryById(id);
        item.setImage(null);
        Integer integer = this.update(item);
        return integer == 1;
    }

    public Boolean updateItem(Item item, String desc, String itemParams) {
        //设置不能更新的字段为null
        item.setStatus(null);
        //更新商品信息
        Integer i1 = super.updateSelective(item);
        
        //更新商品描述信息
        ItemDesc record = new ItemDesc();
        record.setItemDesc(desc);
        record.setItemId(item.getId());
        Integer i2 = this.itemDescService.updateSelective(record);
        
        //更新商品规格参数信息
        Integer i3 = this.itemParamItemService.updateItemParamItem(item.getId(),itemParams); 
        
        //发送消息通知其它系统
        sendMsg(item.getId(), "update");
        
        return i1.intValue()==1 && i2.intValue()==1 && i3.intValue()==1;
    }
    
    //发送消息到RabbitMQ消息队列（其实是发送到RabbitMQ的交换机，交换机绑定队列）
    private void sendMsg(Long itemId,String type){
        try {
            //用Map组装消息内容，包括itemId,type,date
            Map<String, Object> msg = new HashMap<String, Object>();
            msg.put("itemId", itemId);
            msg.put("type", type);
            msg.put("date", System.currentTimeMillis());
            this.rabbitTemplate.convertAndSend("item."+type, OBJECT_MAPPER.writeValueAsString(msg));
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

}
