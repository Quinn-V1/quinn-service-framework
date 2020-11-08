package com.quinn.framework.mapper;

import com.github.pagehelper.Page;
import com.quinn.framework.entity.data.BaseDO;
import com.quinn.framework.entity.data.IdGenerateAbleDO;
import com.quinn.framework.entity.dto.BaseDTO;

/**
 * 基础数据操作接口
 *
 * @author Qunhua.Liao
 * @since 2020-03-28
 */
public interface BaseMapper<DO extends BaseDO, TO extends BaseDTO, VO extends DO> {

    /**
     * 综合计数
     *
     * @param t 实体条件
     * @return 满足条件数量
     */
    int count(TO t);

    /**
     * 综合查询
     *
     * @param t 实体条件
     * @return 满足条件结果
     */
    Page<VO> select(TO t);

    /**
     * 插入单条数据
     *
     * @param t 实体数据
     * @return 插入是否成功
     */
    int insert(DO t);

    /**
     * 删除单条数据
     *
     * @param t 实体数据
     * @return 删除是否成功
     */
    int delete(DO t);

    /**
     * 更新单条数据
     *
     * @param t 实体数据
     * @return 更新是否成功
     */
    int update(DO t);

    /**
     * 更新单条数据（全量）
     *
     * @param t 数据
     * @return 是否成功（记录数）
     */
    int updateAll(DO t);

}
