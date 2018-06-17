package com.lzy.innovate.controller.common;

import com.baomidou.mybatisplus.activerecord.Model;
import com.lzy.innovate.utils.Sets;

import java.util.List;
import java.util.Map;

/**
 * Created by laizhiyuan on 2017/3/13.
 */
public class Pagin<E extends Model> {

    private Map<String , Object> conditions = Sets.map();
    private Map<String , String> rule = Sets.map();

    private int total;
    private int size = 10;
    private int pages;
    private int current = 0;
    private String sortType = "desc";
    private String sortField = "createTime";
    private List<E> rows = Sets.list();
    private boolean isAll = false;
    private boolean isSort = true;

    public boolean isSort() {
        return isSort;
    }

    public void setSort(boolean sort) {
        isSort = sort;
    }

    public Map<String, String> getRule() {
        return rule;
    }

    public void setRule(Map<String, String> rule) {
        this.rule = rule;
    }

    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
    }

    public Map<String, Object> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, Object> conditions) {
        this.conditions = conditions;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public List<E> getRows() {
        return rows;
    }

    public void setRows(List<E> rows) {
        this.rows = rows;
    }
}
