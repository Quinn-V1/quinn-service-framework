package com.quinn.framework.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.api.PageAdapter;
import com.quinn.framework.component.EntityServiceInterceptorChain;
import com.quinn.framework.component.SpringBeanHolder;
import com.quinn.framework.entity.data.BaseDO;
import com.quinn.framework.entity.data.IdGenerateAbleDO;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.framework.entity.dto.PageDTO;
import com.quinn.framework.exception.DataOperationTransactionException;
import com.quinn.framework.mapper.BaseMapper;
import com.quinn.framework.model.BatchUpdateInfo;
import com.quinn.framework.model.PageInfo;
import com.quinn.framework.model.methodinvorker.*;
import com.quinn.framework.service.BaseEntityService;
import com.quinn.framework.util.SessionUtil;
import com.quinn.framework.util.enums.CommonDataTypeEnum;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.convertor.BaseConverter;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.constant.enums.*;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.quinn.util.constant.enums.CommonMessageEnum.*;

/**
 * 基础实体类业务实现层
 *
 * @author Qunhua.Liao
 * @since 2020-03-27
 */
public abstract class BaseEntityServiceImpl<DO extends BaseDO, TO extends BaseDTO, VO extends DO>
        implements BaseEntityService<DO, TO, VO> {

    {
        Type[] actualTypeArguments = ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments();

        DOClass = (Class<DO>) actualTypeArguments[0];
        TOClass = (Class<TO>) actualTypeArguments[1];
        VOClass = (Class<VO>) actualTypeArguments[2];
        SpringBeanHolder.registerEntityClass(this, this.DOClass, this.TOClass, this.VOClass);
    }

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(BaseEntityServiceImpl.class);

    /**
     * 带数据操作对象构造器
     *
     * @param baseMapper 数据构造对象
     */
    public BaseEntityServiceImpl(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    /**
     * 过滤器链
     */
    private EntityServiceInterceptorChain entityServiceInterceptorChain = new EntityServiceInterceptorChain();

    /**
     * 数据操作对象
     */
    private BaseMapper<DO, TO, VO> baseMapper;

    /**
     * 实体类对象
     */
    protected Class<DO> DOClass;

    /**
     * 实体类对象
     */
    protected Class<TO> TOClass;

    /**
     * 实体类对象
     */
    protected Class<VO> VOClass;

    @Resource
    private PageAdapter pageAdapter;

    @Override
    public BaseResult<VO> insert(VO data) {
        BaseResult result = beforeInsert(data);

        result.setLevel(MessageLevelEnum.TRACE.status);
        entityServiceInterceptorChain.doChain(new BaseInsertMethodInvoker<DO>(result, data) {
            @SneakyThrows
            @Override
            public void invoke() {
                try {
                    TO to = getTOClass().newInstance();
                    boolean b = to.dataKey(data.dataKey());
                    if (b) {
                        to.setAvailableStatus(AvailableStatusEnum.ALL);
                        BaseResult<VO> oldDataRes = get(to);
                        if (oldDataRes.isSuccess()) {
                            VO data = oldDataRes.getData();
                            if (data.isAvailable()) {
                                getResult().ofSuccess(false).ofLevel(MessageLevelEnum.ERROR)
                                        .buildMessage(BUSINESS_KEY_REPEAT.key(), 1, 1)
                                        .addParamI8n(BUSINESS_KEY_REPEAT.paramNames[0],
                                                CommonDataTypeEnum.wrapperKey(VOClass.getSimpleName()))
                                        .addParam(BUSINESS_KEY_REPEAT.paramNames[1], getData().dataKey())
                                ;
                                return;
                            }

                            DO newData = this.getData();
                            newData.setId(data.getId());
                            newData.setDataVersion(data.getDataVersion());
                            newData.setDataStatus(data.getDataStatus());
                            newData.prepareForRecover(newData.getInsertUser(), false);
                            baseMapper.update(newData);
                            getResult().ofSuccess(true).ofLevel(MessageLevelEnum.WARN)
                                    .buildMessage(DATA_OPERATION_MISS_HINT.key(), 1, 2);
                            return;
                        }
                    }

                    int res = baseMapper.insert(this.getData());
                    if (res >= 0) {
                        getResult().ofData(data);
                    } else {
                        getResult().ofSuccess(false).ofLevel(MessageLevelEnum.ERROR)
                                .buildMessage(DATA_OPERATION_MISS_HINT.key(), 1, 2)
                                .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[0], DataOperateTypeEnum.INSERT.key())
                                .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[1],
                                        CommonDataTypeEnum.wrapperKey(VOClass.getSimpleName()))
                                .addParam(DATA_OPERATION_MISS_HINT.paramNames[2], getData().dataKey())
                        ;
                    }
                } catch (RuntimeException e) {
                    LOGGER.errorError("error occurs when insert {0}", e, data.toString());
                    throw e;
                }
            }
        });
        return afterInsert(result);
    }

    @Override
    public BaseResult<VO> delete(VO data) {
        BaseResult<VO> result = getById(data.getId());
        if (!result.isSuccess()) {
            return result;
        }

        VO vo = result.getData();
        BaseResult res = beforeDelete(vo);
        if (!res.isSuccess()) {
            return res;
        }

        if (vo.getDataStatus() != null && vo.getDataStatus() >= DataStatusEnum.SYS_INIT.code) {
            return BaseResult.fail().buildMessage(DATA_OPERATION_NOT_SUPPORT_OF_STATUS.key(), 0, 3)
                    .addParamI8n(DATA_OPERATION_NOT_SUPPORT_OF_STATUS.paramNames[0],
                            DataStatusEnum.generateStatus(vo.getDataStatus()).key())
                    .addParamI8n(DATA_OPERATION_NOT_SUPPORT_OF_STATUS.paramNames[1], getVOClass().getSimpleName())
                    .addParamI8n(DATA_OPERATION_NOT_SUPPORT_OF_STATUS.paramNames[2], DataOperateTypeEnum.DELETE.key())
                    .result();
        }

        result.setLevel(MessageLevelEnum.TRACE.status);
        entityServiceInterceptorChain.doChain(new BaseWriteMethodInvoker<DO>(result, data) {
            @Override
            public void invoke() {
                int res;

                try {
                    if (this.getData().getDbOperateType() == DbOperateTypeEnum.DELETE_HARD) {
                        res = baseMapper.delete(this.getData());
                    } else {
                        res = baseMapper.update(this.getData());
                    }
                } catch (RuntimeException e) {
                    LOGGER.errorError("error occurs when delete {0}", e, data.toString());
                    throw e;
                }

                if (res >= 0) {
                    getResult().ofData(getData());
                } else {
                    getResult().ofSuccess(false).ofLevel(MessageLevelEnum.ERROR)
                            .buildMessage(DATA_OPERATION_MISS_HINT.key(), 1, 2)
                            .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[0], DataOperateTypeEnum.DELETE.key())
                            .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[1],
                                    CommonDataTypeEnum.wrapperKey(VOClass.getSimpleName()))
                            .addParam(DATA_OPERATION_MISS_HINT.paramNames[2], getData().dataKey())
                    ;
                }
            }
        });
        return afterDelete(result);
    }

    @Override
    public BaseResult<VO> update(VO data) {
        BaseResult<VO> result = getById(data.getId());
        if (!result.isSuccess()) {
            return result;
        }

        // 旧数据
        VO vo = result.getData();
        BaseResult res = beforeUpdate(vo, data);
        if (!res.isSuccess()) {
            return res;
        }

        if (vo.getDataStatus() != null && vo.getDataStatus() >= DataStatusEnum.SYS_INIT.code) {
            return BaseResult.fail().buildMessage(DATA_OPERATION_NOT_SUPPORT_OF_STATUS.key(), 0, 3)
                    .addParamI8n(DATA_OPERATION_NOT_SUPPORT_OF_STATUS.paramNames[0],
                            DataStatusEnum.generateStatus(vo.getDataStatus()).key())
                    .addParamI8n(DATA_OPERATION_NOT_SUPPORT_OF_STATUS.paramNames[1], getVOClass().getSimpleName())
                    .addParamI8n(DATA_OPERATION_NOT_SUPPORT_OF_STATUS.paramNames[2], DataOperateTypeEnum.UPDATE.key())
                    .result();
        }

        result.setLevel(MessageLevelEnum.TRACE.status);
        entityServiceInterceptorChain.doChain(new BaseWriteMethodInvoker<DO>(result, data) {
            @Override
            public void invoke() {
                int res;

                try {
                    if (this.getData().getDbOperateType() == DbOperateTypeEnum.UPDATE_ALL) {
                        res = baseMapper.updateAll(this.getData());
                    } else {
                        res = baseMapper.update(this.getData());
                    }
                } catch (RuntimeException e) {
                    LOGGER.errorError("error occurs when update {0}", e, data.toString());
                    throw e;
                }

                if (res <= 0) {
                    getResult().ofSuccess(false).ofLevel(MessageLevelEnum.ERROR)
                            .buildMessage(DATA_OPERATION_MISS_HINT.key(), 1, 2)
                            .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[0], DataOperateTypeEnum.UPDATE.key())
                            .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[1],
                                    CommonDataTypeEnum.wrapperKey(VOClass.getSimpleName()))
                            .addParam(DATA_OPERATION_MISS_HINT.paramNames[2], getData().dataKey())
                    ;
                }
            }
        });

        return afterUpdate(result, vo);
    }

    @Override
    public BaseResult recovery(Long dataId) {
        BaseResult<VO> result = getById(dataId);
        if (!result.isSuccess()) {
            return result;
        }

        VO data = result.getData();
        beforeRecovery(data);

        entityServiceInterceptorChain.doChain(new BaseWriteMethodInvoker<VO>(result, data) {
            @Override
            public void invoke() {
                try {
                    int res = baseMapper.update(this.getData());
                    if (res >= 0) {
                        getResult().ofData(data);
                    } else {
                        getResult().ofSuccess(false).ofLevel(MessageLevelEnum.ERROR)
                                .buildMessage(DATA_OPERATION_MISS_HINT.key(), 1, 2)
                                .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[0], DataOperateTypeEnum.UPDATE.key())
                                .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[1],
                                        CommonDataTypeEnum.wrapperKey(VOClass.getSimpleName()))
                                .addParam(DATA_OPERATION_MISS_HINT.paramNames[2], getData().dataKey())
                        ;
                    }
                } catch (RuntimeException e) {
                    LOGGER.errorError("error occurs when recovery {0}", e, data.toString());
                    throw e;
                }
            }
        });

        return afterRecovery(result);
    }

    @Override
    public BatchResult<VO> insertList(List<VO> list, boolean transaction) {
        BatchResult<VO> result = new BatchResult(list.size());
        for (int i = 0; i < list.size(); i++) {
            BaseResult<VO> res = insert(list.get(i));
            if (!res.isSuccess() && transaction) {
                throw new DataOperationTransactionException(res.getMessage())
                        .getMessageProp()
                        .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[0], DataOperateTypeEnum.INSERT.key())
                        .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[1],
                                CommonDataTypeEnum.wrapperKey(VOClass.getSimpleName()))
                        .addParam(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[2], list.size())
                        .addParam(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[3], i)
                        .ofPrevProp(res.getMessageProp())
                        .exception();
            }
            result.addItem(res);
        }
        return result;
    }

    @Override
    public BatchResult<VO> deleteList(List<VO> list, boolean transaction, boolean hardFlag) {
        BatchResult<VO> result = new BatchResult(list.size());
        for (int i = 0; i < list.size(); i++) {
            BaseResult<VO> res = delete(list.get(i).prepareForDelete(SessionUtil.getUserKey(), hardFlag));
            if (!res.isSuccess() && transaction) {
                throw new DataOperationTransactionException(res.getMessage())
                        .getMessageProp()
                        .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[0], DataOperateTypeEnum.DELETE.key())
                        .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[1],
                                CommonDataTypeEnum.wrapperKey(VOClass.getSimpleName()))
                        .addParam(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[2], list.size())
                        .addParam(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[3], i)
                        .ofPrevProp(res.getMessageProp())
                        .exception();
            }
            result.addItem(res);
        }
        return result;
    }

    @Override
    public BatchResult<VO> updateList(List<VO> list, boolean transaction, boolean allFlag) {
        BatchResult<VO> result = new BatchResult(list.size());
        for (int i = 0; i < list.size(); i++) {
            BaseResult<VO> res = update(list.get(i).prepareForUpdate(SessionUtil.getUserKey(), allFlag));
            if (!res.isSuccess() && transaction) {
                throw new DataOperationTransactionException(res.getMessage())
                        .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[0], DataOperateTypeEnum.UPDATE.key())
                        .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[1],
                                CommonDataTypeEnum.wrapperKey(VOClass.getSimpleName()))
                        .addParam(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[2], list.size())
                        .addParam(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[3], i)
                        .ofPrevProp(res.getMessageProp())
                        .exception();
            }
            result.addItem(res);
        }
        return result;
    }

    @Override
    public BatchResult<VO> updateBatch(BatchUpdateInfo<VO> dataList, boolean transaction, boolean allFlag, boolean hardFlag) {
        BaseResult<List<VO>> dataRes = dataList.flag(allFlag, hardFlag);
        if (!dataRes.wantContinue()) {
            return BatchResult.fromPrev(dataRes);
        }
        return updateBatchExec(dataRes.getData(), transaction);
    }

    /**
     * 批量更新操作
     *
     * @param dataList    数据列表
     * @param transaction 事务标识
     * @return 更新结果
     */
    @Override
    @SneakyThrows
    public BatchResult<VO> saveOrUpdate(List<VO> dataList, boolean transaction) {
        BatchResult batchResult = new BatchResult(dataList.size());

        TO dto = getTOClass().newInstance();
        dto.setAvailableStatus(AvailableStatusEnum.ALL);
        String userKey = SessionUtil.getUserKey();
        String orgKey = SessionUtil.getOrgKey();

        for (VO vo : dataList) {
            dto.dataKey(vo.dataKey());
            BaseResult<VO> oldRes = get(dto);

            if (!oldRes.isSuccess()) {
                if (vo.isAvailable()) {
                    vo.prepareForInsert(userKey, orgKey);
                }
            } else {
                VO old = oldRes.getData();
                vo.setId(old.getId());

                if (vo.isAvailable()) {
                    if (old.isAvailable()) {
                        vo.setDataVersion(old.getDataVersion());
                        vo.prepareForUpdate(userKey, false);
                    } else {
                        vo.setDataVersion(old.getDataVersion());
                        vo.prepareForRecover(userKey, false);
                    }
                } else {
                    if (oldRes.getData().isAvailable()) {
                        vo.setDataVersion(old.getDataVersion());
                        vo.prepareForDelete(userKey, false);
                    }
                }
            }
        }

        return updateBatchExec(dataList, transaction);
    }

    @Override
    @SneakyThrows
    public BaseResult<VO> getById(Long id) {
        TO condition = TOClass.getConstructor().newInstance();
        condition.setId(id);
        condition.setAvailableStatus(AvailableStatusEnum.ALL);
        return get(condition);
    }

    @Override
    public BaseResult<List<VO>> selectByMap(Map condition) {
        return select(new JSONObject(condition).toJavaObject(getTOClass()));
    }

    @Override
    public BaseResult<PageInfo<VO>> pageByMap(Map condition) {
        return page(new JSONObject(condition).toJavaObject(getTOClass()));
    }

    @Override
    public BaseResult<VO> get(TO condition) {
        BaseResult<TO> res = handDataAuthParam(condition);
        if (!res.isSuccess()) {
            return BaseResult.fromPrev(res);
        }

        BaseResult<VO> result = BaseResult.build(true);
        condition.setResultNumExpected(NumberConstant.INT_ONE);
        try {
            pageAdapter.handlePageParam(NumberConstant.INT_ONE, NumberConstant.INT_TWO);
            entityServiceInterceptorChain.doChain(new BaseGetMethodInvoker<TO>(result, condition) {

                @Override
                public void invoke() {
                    Page<VO> select = baseMapper.select(getData());
                    if (CollectionUtils.isEmpty(select)) {
                        String dataKey = getData().dataKey();
                        dataKey = StringUtil.isEmpty(dataKey) ? BaseConverter.staticToString(condition.getId()) : dataKey;
                        getResult().ofSuccess(false).ofLevel(MessageLevelEnum.WARN)
                                .buildMessage(DATA_OPERATION_MISS_HINT.key(), 2, 1)
                                .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[0], DataOperateTypeEnum.QUERY.key())
                                .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[1],
                                        CommonDataTypeEnum.wrapperKey(VOClass.getSimpleName()))
                                .addParam(DATA_OPERATION_MISS_HINT.paramNames[2], dataKey)
                        ;
                    } else if (select.size() > 1) {
                        String dataKey = getData().dataKey();
                        dataKey = StringUtil.isEmpty(dataKey) ? BaseConverter.staticToString(condition.getId()) : dataKey;
                        getResult().ofSuccess(false).ofLevel(MessageLevelEnum.ERROR)
                                .buildMessage(RESULT_NOT_UNIQUE.key(), 2, 1)
                                .addParamI8n(RESULT_NOT_UNIQUE.paramNames[0],
                                        CommonDataTypeEnum.wrapperKey(VOClass.getSimpleName()))
                                .addParam(RESULT_NOT_UNIQUE.paramNames[1], dataKey)
                                .addParam(RESULT_NOT_UNIQUE.paramNames[2], select.size())
                        ;
                    } else {
                        getResult().ofData(select.get(0));
                    }
                }
            });
        } finally {
            pageAdapter.clearPage();
        }
        return result;
    }

    @Override
    public BaseResult<List<VO>> select(TO condition) {
        BaseResult<TO> res = handDataAuthParam(condition);
        if (!res.isSuccess()) {
            return BaseResult.fromPrev(res);
        }

        BaseResult<List<VO>> result = BaseResult.build(true);
        condition.ofEntityClass(DOClass);
        entityServiceInterceptorChain.doChain(new BaseSelectMethodInvoker<TO>(result, condition) {

            @Override
            public void invoke() {
                TO data = getData();
                Boolean pageFlag = data.getPageFlag();
                if (pageFlag != null && pageFlag) {
                    pageAdapter.handlePageParam((PageDTO) condition);
                }

                try {
                    Page<VO> select = baseMapper.select(data);
                    if (CollectionUtils.isEmpty(select)) {
                        getResult().ofSuccess(false).ofLevel(MessageLevelEnum.DEBUG).ofData(Collections.emptyList())
                                .buildMessage(RESULT_NOT_FOUND.key(), 0, 1)
                                .addParamI8n(RESULT_NOT_FOUND.paramNames[0],
                                        CommonDataTypeEnum.wrapperKey(VOClass.getSimpleName()))
                        ;
                    } else {
                        getResult().ofData(select);
                    }
                } finally {
                    if (pageFlag != null && pageFlag) {
                        pageAdapter.clearPage();
                    }
                }
            }
        });
        return result;
    }

    @Override
    public BaseResult<PageInfo<VO>> page(TO condition) {
        BaseResult<TO> res = handDataAuthParam(condition);
        if (!res.isSuccess()) {
            return BaseResult.fromPrev(res);
        }

        BaseResult<PageInfo<VO>> result = BaseResult.build(true);
        condition.ofEntityClass(DOClass);
        entityServiceInterceptorChain.doChain(new BaseReadMethodInvoker<TO>(result, condition) {

            @Override
            public void invoke() {
                BaseResult res = pageAdapter.handlePageParam((PageDTO) condition);
                if (!res.isSuccess()) {
                    result.appendPrev(res);
                    return;
                }

                try {
                    Page<VO> select = baseMapper.select(getData());
                    if (CollectionUtils.isEmpty(select)) {
                        getResult().ofSuccess(false).ofLevel(MessageLevelEnum.DEBUG).ofData(PageInfo.EMPTY)
                                .buildMessage(RESULT_NOT_FOUND.key(), 0, 1)
                                .addParamI8n(RESULT_NOT_FOUND.paramNames[0],
                                        CommonDataTypeEnum.wrapperKey(VOClass.getSimpleName()))
                        ;
                    } else {
                        getResult().ofData(pageAdapter.toPageInf(select));
                    }
                } finally {
                    pageAdapter.clearPage();
                }
            }
        });
        return result;
    }

    @Override
    public BaseEntityService addEntityServiceInterceptor(EntityServiceInterceptor entityServiceInterceptor) {
        entityServiceInterceptorChain.addInterceptor(entityServiceInterceptor);
        return this;
    }

    @Override
    public Class<DO> getDOClass() {
        return DOClass;
    }

    @Override
    public Class<TO> getTOClass() {
        return TOClass;
    }

    @Override
    public Class<VO> getVOClass() {
        return VOClass;
    }

    /**
     * 根据标记更新列表
     *
     * @param list 列表
     * @return 批处理结果
     */
    protected BatchResult updateBatchExec(List<VO> list, boolean transaction) {
        BatchResult result = new BatchResult(list.size());

        for (int i = 0; i < list.size(); i++) {
            VO data = list.get(i);
            DbOperateTypeEnum dbOperateType = data.getDbOperateType();
            if (dbOperateType == null) {
                continue;
            }
            BaseResult res = BaseResult.SUCCESS;
            try {
                switch (dbOperateType) {
                    case INSERT:
                    case RECOVERY_HARD:
                        res = insert(data);
                        break;
                    case DELETE_SOFT:
                    case UPDATE_NON_EMPTY:
                    case UPDATE_ALL:
                    case RECOVERY_SOFT:
                        res = update(data);
                        break;
                    case DELETE_HARD:
                        res = delete(data);
                        break;
                    default:
                        break;
                }
            } catch(RuntimeException e) {
                if (transaction) {
                    throw e;
                }
                result.addItem(BaseResult.fromException(e));
            }

            if (transaction && !res.isSuccess()) {
                throw new DataOperationTransactionException()
                        .addParam(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[0], dbOperateType)
                        .addParam(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[1],
                                CommonDataTypeEnum.wrapperKey(VOClass.getSimpleName()))
                        .addParam(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[2], list.size())
                        .addParam(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[3], i + 1)
                        .ofPrevProp(res.getMessageProp())
                        .exception()
                        ;
            }

            result.addItem(res);
        }
        return result;
    }

    /**
     * 处理分页参数
     *
     * @param pageNum  分页条件
     * @param pageSize 分页条件
     * @return 处理是否成功
     */
    protected BaseResult handlePageParam(int pageNum, int pageSize) {
        return this.pageAdapter.handlePageParam(pageNum, pageSize);
    }

    /**
     * 处理分页
     *
     * @param pageDTO 分页参数
     * @return 处理结果
     */
    protected BaseResult handlePageParam(PageDTO pageDTO) {
        return this.pageAdapter.handlePageParam(pageDTO);
    }

    /**
     * 处理分页
     *
     * @param cond 分页参数
     * @return 处理结果
     */
    protected BaseResult handlePageParam(Map<String, Object> cond) {
        return this.pageAdapter.handlePageParam(cond);
    }

    /**
     * 清除分业参数
     */
    protected void clearPageParam() {
        pageAdapter.clearPage();
    }

    /**
     * 获取分页工具适配器
     *
     * @return 分页工具适配器
     */
    protected PageAdapter getPageAdapter() {
        return pageAdapter;
    }

    /**
     * 将外部分页对象转为内部分页对象
     *
     * @param t   源数据
     * @param <V> 结果泛型
     * @return 分页信息
     */
    protected <V> PageInfo<V> toPageInf(Object t) {
        return this.pageAdapter.toPageInf(t);
    }

    /**
     * 处理授权参数（具体交给子类实现：可能根据用户、角色、组织）
     *
     * @param to 参数
     * @return 是否成功
     */
    protected BaseResult<TO> handDataAuthParam(TO to) {
        return BaseResult.success(to);
    }

}
