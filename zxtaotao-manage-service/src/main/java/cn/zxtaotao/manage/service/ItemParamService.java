package cn.zxtaotao.manage.service;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

import cn.zxtaotao.common.bean.EasyUIResult;
import cn.zxtaotao.manage.pojo.ItemParam;
import cn.zxtaotao.manage.service.base.BaseService;

@Service
public class ItemParamService extends BaseService<ItemParam> {

    public EasyUIResult queryItemParam(Integer page, Integer rows) {
        
        PageInfo<ItemParam> pageInfo = super.queryPageListByWhere(page, rows, null);
        
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

}
