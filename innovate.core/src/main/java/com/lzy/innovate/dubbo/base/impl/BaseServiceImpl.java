package com.lzy.innovate.dubbo.base.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.lzy.innovate.dubbo.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by lzy on 2017/2/25.
 */
public class BaseServiceImpl<E extends IService,T> implements BaseService<T> {

    @Autowired
    private E e;

    @Override
    public boolean insert(T entity) {
        return e.insert(entity);
    }

    @Override
    public boolean insertBatch(List<T> entityList) {
        return e.insertBatch(entityList);
    }

    @Override
    public boolean insertBatch(List<T> entityList, int batchSize) {
        return e.insertBatch(entityList,batchSize);
    }

    @Override
    public boolean insertOrUpdateBatch(List<T> entityList) {
        return e.insertOrUpdateBatch(entityList);
    }

    @Override
    public boolean insertOrUpdateBatch(List<T> entityList, int batchSize) {
        return e.insertOrUpdateBatch(entityList,batchSize);
    }

    @Override
    public boolean deleteById(Serializable id) {
        return e.deleteById(id);
    }

    @Override
    public boolean deleteByMap(Map<String, Object> columnMap) {
        return e.deleteByMap(columnMap);
    }

    @Override
    public boolean delete(Wrapper<T> wrapper) {
        return e.delete(wrapper);
    }

    @Override
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        return e.deleteBatchIds(idList);
    }

    @Override
    public boolean updateById(T entity) {
        return e.updateById(entity);
    }

    @Override
    public boolean update(T entity, Wrapper<T> wrapper) {
        return e.update(entity,wrapper);
    }

    @Override
    public boolean updateBatchById(List<T> entityList) {
        return e.updateBatchById(entityList);
    }

    @Override
    public boolean insertOrUpdate(T entity) {
        return e.insertOrUpdate(entity);
    }

    @Override
    public T selectById(Serializable id) {
        return (T)e.selectById(id);
    }

    @Override
    public List<T> selectBatchIds(List<? extends Serializable> idList) {
        return e.selectBatchIds(idList);
    }

    @Override
    public List<T> selectByMap(Map<String, Object> columnMap) {
        return e.selectByMap(columnMap);
    }

    @Override
    public T selectOne(Wrapper<T> wrapper) {
        return (T)e.selectOne(wrapper);
    }

    @Override
    public Map<String, Object> selectMap(Wrapper<T> wrapper) {
        return e.selectMap(wrapper);
    }

    @Override
    public Object selectObj(Wrapper<T> wrapper) {
        return e.selectObj(wrapper);
    }

    @Override
    public int selectCount(Wrapper<T> wrapper) {
        return e.selectCount(wrapper);
    }

    @Override
    public List<T> selectList(Wrapper<T> wrapper) {
        return e.selectList(wrapper);
    }

    @Override
    public Page<T> selectPage(Page<T> page) {
        return e.selectPage(page);
    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<T> wrapper) {
        return e.selectMaps(wrapper);
    }

    @Override
    public List<Object> selectObjs(Wrapper<T> wrapper) {
        return e.selectObjs(wrapper);
    }

    @Override
    public Page<Map<String, Object>> selectMapsPage(Page page, Wrapper<T> wrapper) {
        return e.selectMapsPage(page,wrapper);
    }

    @Override
    public Page<T> selectPage(Page<T> page, Wrapper<T> wrapper) {
        return e.selectPage(page,wrapper);
    }
}
