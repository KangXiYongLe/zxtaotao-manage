package cn.zxtaotao.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.zxtaotao.common.bean.EasyUIResult;
import cn.zxtaotao.manage.mapper.ContentMapper;
import cn.zxtaotao.manage.pojo.Content;
import cn.zxtaotao.manage.service.base.BaseService;

@Service
public class ContentService extends BaseService<Content> {

    @Autowired
    private ContentMapper contentMapper;
    
    public EasyUIResult queryListByCategoryId(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<Content> list = this.contentMapper.queryListByCategoryId(categoryId);
        PageInfo<Content> pageInfo = new PageInfo<Content>(list);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

}
