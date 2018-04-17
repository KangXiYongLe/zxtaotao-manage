package cn.zxtaotao.manage.service.base;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.zxtaotao.manage.pojo.BasePojo;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public abstract class BaseService<T extends BasePojo> {
    
    @Autowired
    private Mapper<T> mapper;
    
    /**
     * 根据id查询数据
     * 
     * @param id
     * @return
     */
    public T queryById(Long id){
       return this.mapper.selectByPrimaryKey(id);
    }
    
    /**
     * 查询所有数据
     * 
     * @return
     */
    public List<T> queryAll(){
        return this.mapper.select(null);
    }
    
    /**
     * 根据条件查询一条数据，如果有多条数据会抛出异常
     * 
     * @param record
     * @return
     */
    public T queryOne(T record){
        return this.mapper.selectOne(record);
    }
    
    /**
     * 根据条件查询数据列表
     * 
     * @param record
     * @return
     */
    public List<T> queryListByWhere(T record){
        return this.mapper.select(record);
    }
    /**
     * 分页查询
     * 
     * @param pageNum
     * @param pageSize
     * @param record
     * @return
     */
    public PageInfo<T> queryPageListByWhere(Integer pageNum,Integer pageSize,T record){
        //设置分页条件
        PageHelper.startPage(pageNum, pageSize);
        List<T> list = this.queryListByWhere(record);
        return new PageInfo<T>(list);
    }
    
    /**
     * 新增数据，返回成功的条数
     * 
     * @param record
     * @return
     */
    public Integer save(T record){
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return this.mapper.insert(record);
    }
    
    /**
     * 新增数据，只插入record中属性不等于null的对应字段，返回成功的条数
     * 
     * @param record
     * @return
     */
    public Integer saveSelective(T record){
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return this.mapper.insertSelective(record);
    }
    
    /**
     * 修改数据，返回成功的条数
     * 
     * @param record
     * @return
     */
    public Integer update(T record){
        record.setUpdated(new Date());
        return this.mapper.updateByPrimaryKey(record);
    }
    
    /**
     * 修改数据，使用不为null的字段，返回成功的条数
     * 
     * @param record
     * @return
     */
    public Integer updateSelective(T record){
        record.setCreated(null);
        record.setUpdated(new Date());
        return this.mapper.updateByPrimaryKeySelective(record);
    }
    
    /**
     * 根据id删除数据
     * 
     * @param id
     * @return
     */
    public Integer deleteById(Long id){
        return this.mapper.deleteByPrimaryKey(id);
    }
    
    /**
     * 批量删除
     * @param clazz
     * @param property
     * @param values
     * @return
     */
    public Integer deleteByIds(List<Object> ids,Class<T> entityClass,String property){
        Example example = new Example(entityClass);
        example.createCriteria().andIn(property, ids);
        return this.mapper.deleteByExample(example);
    }
    
    /**
     * 根据条件做删除
     * 
     * @param record
     * @return
     */
    public Integer deleteByWhere(T record){
        return this.mapper.delete(record);
    }
    

}
