package com.quinn.framework.component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.quinn.framework.api.PageAdapter;
import com.quinn.framework.entity.dto.PageDTO;
import com.quinn.framework.model.PageInfo;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.enums.ExceptionEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 分页处理适配器（PageHelper）
 *
 * @author Qunhua.Liao
 * @since 2020-03-28
 */
@Component
@ConditionalOnMissingBean(name = "pageAdapter")
public class PageHelperPageAdapter implements PageAdapter<Page> {

    @Override
    public BaseResult handlePageParam(PageDTO condition) {
        return staticHandlePageParam(condition);
    }

    @Override
    public BaseResult handlePageParam(Map<String, Object> condition) {
        return staticHandlePageParam(condition);
    }

    @Override
    public <V> PageInfo<V> toPageInf(Page page) {
        return staticToPageInf(page);
    }

    public static BaseResult staticHandlePageParam(PageDTO condition) {
        if (condition.getPageNum() == null || condition.getPageSize() == null
                || condition.getPageSize().intValue() == 0) {
            return BaseResult.build(false)
                    .buildMessage(ExceptionEnum.PARAM_PAGE_NOT_PROVIDED.name(), 1, 0)
                    .addParam("paramName", "pageNum or pageSize")
                    .result()
                    ;
        }

        PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        return BaseResult.SUCCESS;
    }

    public static BaseResult staticHandlePageParam(Map<String, Object> condition) {
        Integer pageNum = BaseConverter.staticConvert(condition.get("pageNum"), Integer.class);
        Integer pageSize = BaseConverter.staticConvert(condition.get("pageSize"), Integer.class);

        if (pageNum == null || pageSize == null || pageSize.intValue() == 0) {
            return BaseResult.build(false)
                    .buildMessage(ExceptionEnum.PARAM_PAGE_NOT_PROVIDED.name(), 1, 0)
                    .addParam("paramName", "pageNum or pageSize")
                    .result()
                    ;
        }

        PageHelper.startPage(pageNum, pageSize);
        return BaseResult.SUCCESS;
    }

    public static <V> PageInfo<V> staticToPageInf(Page page) {
        PageInfo<V> result = new PageInfo<>();
        result.setDataList(page);
        result.setPageNum(page.getPageNum());
        result.setPageSize(page.getPageSize());
        result.setTotal(page.getTotal());
        return result;
    }

}
