package cn.zxtaotao.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zxtaotao.common.bean.ItemCatData;
import cn.zxtaotao.common.bean.ItemCatResult;
import cn.zxtaotao.common.service.RedisService;
import cn.zxtaotao.manage.pojo.ItemCat;
import cn.zxtaotao.manage.service.base.BaseService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ItemCatService extends BaseService<ItemCat>{
    
    @Autowired
    private RedisService redisService;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    //定义redis缓存的key的规则：项目名_模块名_业务名
    private static final String REDIS_KEY = "TAOTAO_MANAGE_ITEM_CAT_API";
    private static final Integer REDIS_TIME = 60*60*60*24*30*3;

    public ItemCatResult queryAllToTree() {
        
        try {
            //从缓存中命中数据，原则：对于缓存的操作不能影响到业务成功，所以必须try-catch
            String cacheData = this.redisService.get(REDIS_KEY);
            if(StringUtils.isNotEmpty(cacheData)){
                //返回命中的数据
              return  MAPPER.readValue(cacheData, ItemCatResult.class);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
        
        ItemCatResult result = new ItemCatResult();
        // 全部查出，并且在内存中生成树形结构
        List<ItemCat> cats = super.queryAll();
        
        // 转为map存储，key为父节点ID，value为数据集合
        Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
        for (ItemCat itemCat : cats) {
            if(!itemCatMap.containsKey(itemCat.getParentId())){
                itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
            }
            itemCatMap.get(itemCat.getParentId()).add(itemCat);
        }
        
        //封装json数的第一级类目
        List<ItemCat> list = itemCatMap.get(0L);        
        for (ItemCat itemCat : list) {
            ItemCatData itemCatData = new ItemCatData();
            itemCatData.setUrl("/products/"+itemCat.getId()+".html");
            itemCatData.setName("<a href='" + itemCatData.getUrl() + "'>"+itemCat.getName()+"</a>");
            result.getItemCats().add(itemCatData);
            if(!itemCat.getIsParent()){
                continue;
            }
            
            //封装第二级类目对象
            List<ItemCatData> itemCatDatas = new ArrayList<ItemCatData>();
            itemCatData.setItems(itemCatDatas);
            List<ItemCat> list2 = itemCatMap.get(itemCat.getId());
            for (ItemCat itemCat2 : list2) {
                ItemCatData itemCatData2 = new ItemCatData();
                itemCatData2.setUrl("/products/"+itemCat2.getId()+".html");
                itemCatData2.setName("<a href='" + itemCatData2.getUrl() + "'>"+itemCat2.getName()+"</a>");
                itemCatDatas.add(itemCatData2);
                
                if(itemCat2.getIsParent()){
                    //封装三级对象
                    List<ItemCat> list3 = itemCatMap.get(itemCat2.getId());
                    List<String> itemCatDatas2 = new ArrayList<String>();
                    itemCatData2.setItems(itemCatDatas2);
                    for (ItemCat itemCat3 : list3) {
                        itemCatDatas2.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
                    }
                }
            }
            if (result.getItemCats().size() >= 14) {
                break;
            }            
        }
        
        try {
            //将结果集写入到缓存中，原则：对于缓存的操作不能影响到业务成功，所以必须try-catch
            this.redisService.set(REDIS_KEY, MAPPER.writeValueAsString(result), REDIS_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    
   

}
