package com.quinn.framework.service.impl;

import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.api.PageAdapter;
import com.quinn.framework.entity.data.BaseDO;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.framework.entity.dto.PageDTO;
import com.quinn.framework.mapper.BaseMapper;
import com.quinn.framework.model.BatchUpdateInfo;
import com.quinn.framework.model.PageInfo;
import com.quinn.framework.service.BaseEntityService;
import com.quinn.util.base.exception.MethodNotFoundException;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;

import java.util.List;
import java.util.Map;

/**
 * 服务基类
 *
 * @author Qunhua.Liao
 * @since 2020-11-08
 */
public abstract class BaseEntityServiceImpl<DO extends BaseDO, TO extends BaseDTO, VO extends DO>
        implements BaseEntityService<DO, TO, VO> {

    public BaseEntityServiceImpl(BaseMapper<DO, TO, VO> baseMapper) {
    }

    @Override
    public BaseResult<VO> insert(VO data) {
        return null;
    }

    @Override
    public BaseResult<VO> delete(VO data) {
        return null;
    }

    @Override
    public BaseResult<VO> update(VO data) {
        return null;
    }

    @Override
    public BaseResult<VO> recovery(Long dataId) {
        return null;
    }

    @Override
    public BaseResult<VO> getById(Long id) {
        return null;
    }

    @Override
    public BaseResult<VO> get(TO condition) {
        return null;
    }

    @Override
    public BaseResult<List<VO>> selectByMap(Map condition) {
        return null;
    }

    @Override
    public BaseResult<PageInfo<VO>> pageByMap(Map condition) {
        return null;
    }

    @Override
    public BaseResult<List<VO>> select(TO condition) {
        return null;
    }

    @Override
    public BaseResult<PageInfo<VO>> page(TO condition) {
        return null;
    }

    @Override
    public BatchResult<VO> updateBatch(BatchUpdateInfo<VO> dataList, boolean transaction, boolean allFlag, boolean hardFlag) {
        return null;
    }

    @Override
    public BatchResult<VO> saveOrUpdate(List<VO> dataList, boolean transaction) {
        return null;
    }

    @Override
    public Class<DO> getDOClass() {
        return null;
    }

    @Override
    public Class<TO> getTOClass() {
        return null;
    }

    @Override
    public Class<VO> getVOClass() {
        return null;
    }

    @Override
    public BaseEntityService addEntityServiceInterceptor(EntityServiceInterceptor entityServiceInterceptor) {
        return null;
    }
    protected BatchResult updateBatchExec(List<VO> list, boolean transaction) {
        throw new MethodNotFoundException();
    }

    protected BaseResult<TO> handDataAuthParam(TO to) {
        throw new MethodNotFoundException();
    }

    /**
     * 处理分页参数
     *
     * @param pageNum  分页条件
     * @param pageSize 分页条件
     * @return 处理是否成功
     */
    protected BaseResult handlePageParam(int pageNum, int pageSize) {
        throw new MethodNotFoundException();
    }

    /**
     * 处理分页
     *
     * @param pageDTO 分页参数
     * @return 处理结果
     */
    protected BaseResult handlePageParam(PageDTO pageDTO) {
        throw new MethodNotFoundException();
    }

    /**
     * 处理分页
     *
     * @param cond 分页参数
     * @return 处理结果
     */
    protected BaseResult handlePageParam(Map<String, Object> cond) {
        throw new MethodNotFoundException();
    }

    /**
     * 清除分业参数
     */
    protected void clearPageParam() {
        throw new MethodNotFoundException();
    }

    /**
     * 获取分页工具适配器
     *
     * @return 分页工具适配器
     */
    protected PageAdapter getPageAdapter() {
        throw new MethodNotFoundException();
    }

    /**
     * 将外部分页对象转为内部分页对象
     *
     * @param t   源数据
     * @param <V> 结果泛型
     * @return 分页信息
     */
    protected <V> PageInfo<V> toPageInf(Object t) {
        throw new MethodNotFoundException();
    }
}
