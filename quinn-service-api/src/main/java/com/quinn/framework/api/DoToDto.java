package com.quinn.framework.api;

import com.quinn.framework.entity.data.IdGenerateAbleDO;
import com.quinn.framework.entity.dto.PageDTO;

/**
 * DO转DTO
 *
 * @author Qunhua.Liao
 * @since 2020-04-07
 */
public interface DoToDto<S extends IdGenerateAbleDO, T extends PageDTO<S>> {

    /**
     * DO转DTO
     *
     * @param s 原对象
     * @return DTO对象
     */
    T doToDto(S s);

}
