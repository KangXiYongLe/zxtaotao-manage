package cn.zxtaotao.manage.mapper;

import java.util.List;

import cn.zxtaotao.manage.pojo.Content;

import com.github.abel533.mapper.Mapper;

public interface ContentMapper extends Mapper<Content> {

    public List<Content> queryListByCategoryId(Long categoryId);
}
