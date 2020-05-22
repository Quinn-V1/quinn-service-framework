package com.quinn.framework.model;

import javax.servlet.Filter;
import java.util.List;

/**
 * Shiro 过滤器 增强条目
 *
 * @author Qunhua.Liao
 * @since 2020-05-22
 */
public class QuinnFilterItem {

    /**
     * 主键
     */
    private String key;

    /**
     * 过滤器名称
     */
    private String filterName;

    /**
     * 路径样式
     */
    private List<String> urlPattens;

    /**
     * 过滤器
     */
    private Filter filter;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getUrlPattens() {
        return urlPattens;
    }

    public void setUrlPattens(List<String> urlPattens) {
        this.urlPattens = urlPattens;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
