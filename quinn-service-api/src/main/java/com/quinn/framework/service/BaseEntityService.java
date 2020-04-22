package com.quinn.framework.service;

import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.entity.data.BaseDO;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.framework.model.BatchUpdateInfo;
import com.quinn.framework.model.PageInfo;
import com.quinn.util.base.exception.UnSupportedMethodException;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;

import java.util.List;
import java.util.Map;

/**
 * 基础实体业务实现接口
 *
 * @author Qunhua.Liao
 * @since 2020-03-27
 */
public interface BaseEntityService<DO extends BaseDO, TO extends BaseDTO, VO extends DO> {

    /**
     * 新增之前操作
     *
     * @param data 业务数据
     * @return 前置操作是否成功
     */
    default BaseResult<VO> beforeInsert(VO data) {
        return BaseResult.success(data);
    }

    /**
     * 插入单条数据
     *
     * @param data 实体数据
     * @return 插入是否成功
     */
    BaseResult<VO> insert(VO data);

    /**
     * 新增之后操作
     *
     * @param result 业务数据
     * @return 后置操作是否成功
     */
    default BaseResult<VO> afterInsert(BaseResult<VO> result) {
        return result;
    }

    /**
     * 删除之前操作
     *
     * @param data 业务数据
     * @return 前置操作是否成功
     */
    default BaseResult<VO> beforeDelete(VO data) {
        return BaseResult.success(data);
    }

    /**
     * 删除单条数据
     *
     * @param data 实体数据
     * @return 删除是否成功
     */
    BaseResult<VO> delete(VO data);

    /**
     * 删除之后操作
     *
     * @param result 业务数据
     * @return 前置操作是否成功
     */
    default BaseResult<VO> afterDelete(BaseResult<VO> result) {
        return result;
    }

    /**
     * 更新之前操作
     *
     * @param oldData 业务数据（旧）
     * @param newData 业务数据(新)
     * @return 后置操作是否成功
     */
    default BaseResult<VO> beforeUpdate(VO oldData, VO newData) {
        return BaseResult.success(oldData);
    }

    /**
     * 更新单条数据
     *
     * @param data 实体数据
     * @return 更新是否成功
     */
    BaseResult<VO> update(VO data);

    /**
     * 更新之后操作
     *
     * @param result  新业务数据
     * @param oldData 旧业务数据
     * @return 后置操作是否成功
     */
    default BaseResult<VO> afterUpdate(BaseResult<VO> result, VO oldData) {
        return result;
    }

    /**
     * 更新之前操作
     *
     * @param data 业务数据
     * @return 后置操作是否成功
     */
    default BaseResult<VO> beforeRecovery(VO data) {
        return BaseResult.success(data);
    }

    /**
     * 恢复数据
     *
     * @param dataId 数据ID
     * @return 恢复结果
     */
    BaseResult<VO> recovery(Long dataId);

    /**
     * 更新之后操作
     *
     * @param result 业务数据
     * @return 后置操作是否成功
     */
    default BaseResult<VO> afterRecovery(BaseResult<VO> result) {
        return result;
    }

    /**
     * 查找一条记录
     *
     * @param id 系统主键
     * @return 数据
     */
    BaseResult<VO> getById(Long id);

    /**
     * 查找一条记录
     *
     * @param condition 综合条件
     * @return
     */
    BaseResult<VO> get(TO condition);

    /**
     * 综合查询：不分页
     *
     * @param condition 综合条件
     * @return
     */
    BaseResult<List<VO>> selectByMap(Map condition);

    /**
     * 综合查询：不分页
     *
     * @param condition 综合条件
     * @return
     */
    BaseResult<PageInfo<VO>> pageByMap(Map condition);

    /**
     * 综合查询：不分页
     *
     * @param condition 综合条件
     * @return
     */
    BaseResult<List<VO>> select(TO condition);

    /**
     * 综合查询：分页
     *
     * @param condition 综合条件
     * @return
     */
    BaseResult<PageInfo<VO>> page(TO condition);

    /**
     * 插入列表数据
     *
     * @param list        实体列表
     * @param transaction 是否事务管理
     * @return 插入是否成功
     */
    default BatchResult<VO> insertList(List<VO> list, boolean transaction) {
        throw new UnSupportedMethodException();
    }

    /**
     * 删除列表数据
     *
     * @param list        实体列表
     * @param transaction 是否事务管理
     * @param hardFlag 是否硬删除
     * @return 删除是否成功
     */
    default BatchResult<VO> deleteList(List<VO> list, boolean transaction, boolean hardFlag) {
        throw new UnSupportedMethodException();
    }

    /**
     * 更新列表数据
     *
     * @param list        实体列表
     * @param transaction 是否事务管理
     * @param allFlag 是否全量更新
     * @return 更新是否成功
     */
    default BatchResult<VO> updateList(List<VO> list, boolean transaction, boolean allFlag) {
        throw new UnSupportedMethodException();
    }

    /**
     * 批量更新操作
     *
     * @param dataList    数据列表
     * @param transaction 事务标识
     * @param allFlag     全量更新标识
     * @param hardFlag    硬删除标识
     * @return 更新结果
     */
    BatchResult<VO> updateBatch(BatchUpdateInfo<VO> dataList, boolean transaction, boolean allFlag, boolean hardFlag);

    /**
     * 获取服务实体的数据类对象
     *
     * @return 服务实体类数据对象
     */
    Class<DO> getDOClass();

    /**
     * 获取服务实体的数据传输类对象
     *
     * @return 服务实体数据传输类对象
     */
    Class<TO> getTOClass();

    /**
     * 获取服务实体的展示类对象
     *
     * @return 服务实体展示类对象
     */
    Class<VO> getVOClass();

    /**
     * 添加过业务拦截器
     *
     * @param entityServiceInterceptor 业务拦截器
     * @return 业务操作对象本身, 方便链式操作
     */
    BaseEntityService addEntityServiceInterceptor(EntityServiceInterceptor entityServiceInterceptor);
}
