package com.quinn.framework.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.quinn.framework.api.EntityServiceInterceptor;
import com.quinn.framework.api.PageAdapter;
import com.quinn.framework.component.EntityServiceInterceptorChain;
import com.quinn.framework.component.SpringBeanHolder;
import com.quinn.framework.entity.data.BaseDO;
import com.quinn.framework.entity.dto.BaseDTO;
import com.quinn.framework.entity.dto.PageDTO;
import com.quinn.framework.exception.DataOperationTransactionException;
import com.quinn.framework.mapper.BaseMapper;
import com.quinn.framework.model.PageInfo;
import com.quinn.framework.model.methodinvorker.*;
import com.quinn.framework.service.BaseEntityService;
import com.quinn.framework.util.SessionUtil;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.base.model.BatchResult;
import com.quinn.util.constant.enums.DataOperateTypeEnum;
import com.quinn.util.constant.enums.MessageLevelEnum;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static com.quinn.util.constant.enums.ExceptionEnum.*;

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

    @Autowired
    private PageAdapter pageAdapter;

    @Override
    public BaseResult<VO> insert(VO data) {
        data.prepareForInsert(SessionUtil.getUserKey(), SessionUtil.getOrgKey());
        BaseResult result = beforeInsert(data);
        entityServiceInterceptorChain.doChain(new BaseInsertMethodInvoker<DO>(result, data) {
            @Override
            public void invoke() {
                int res = baseMapper.insert(this.getData());
                if (res >= 0) {
                    getResult().ofData(getById(this.getData().getId()).getData());
                } else {
                    getResult().ofSuccess(false).ofLevel(MessageLevelEnum.ERROR)
                            .buildMessage(DATA_OPERATION_MISS_HINT.name(), 1, 2)
                            .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[0], DataOperateTypeEnum.INSERT.name())
                            .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[1], getDOClass().getSimpleName())
                            .addParam(DATA_OPERATION_MISS_HINT.paramNames[2], getData().dataKey())
                    ;
                }
            }
        });
        return afterInsert(result);
    }

    @Override
    public BaseResult<VO> delete(VO data) {
        BaseResult<VO> result = getByDO(data);
        if (!result.isSuccess()) {
            return result;
        }

        VO vo = result.getData();
        vo.prepareForDelete(SessionUtil.getUserKey());

        BaseResult res = beforeDelete(vo);
        if (!res.isSuccess()) {
            return res;
        }

        entityServiceInterceptorChain.doChain(new BaseWriteMethodInvoker<DO>(result, data) {
            @Override
            public void invoke() {
                int res = baseMapper.delete(this.getData());
                if (res >= 0) {
                    getResult().ofData(getData());
                } else {
                    getResult().ofSuccess(false).ofLevel(MessageLevelEnum.ERROR)
                            .buildMessage(DATA_OPERATION_MISS_HINT.name(), 1, 2)
                            .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[0], DataOperateTypeEnum.DELETE.name())
                            .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[1], getDOClass().getSimpleName())
                            .addParam(DATA_OPERATION_MISS_HINT.paramNames[2], getData().dataKey())
                    ;
                }
            }
        });
        return afterDelete(result);
    }

    @Override
    public BaseResult<VO> update(VO data) {
        BaseResult<VO> result = getByDO(data);
        if (!result.isSuccess()) {
            return result;
        }

        data.prepareForUpdate(SessionUtil.getUserKey());

        VO vo = result.getData();
        BaseResult res = beforeUpdate(vo, data);
        if (!res.isSuccess()) {
            return res;
        }

        entityServiceInterceptorChain.doChain(new BaseWriteMethodInvoker<DO>(result, data) {
            @Override
            public void invoke() {
                int res = baseMapper.update(this.getData());
                if (res >= 0) {
                    getResult().ofData(vo);
                } else {
                    getResult().ofSuccess(false).ofLevel(MessageLevelEnum.ERROR)
                            .buildMessage(DATA_OPERATION_MISS_HINT.name(), 1, 2)
                            .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[0], DataOperateTypeEnum.UPDATE.name())
                            .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[1], getDOClass().getSimpleName())
                            .addParam(DATA_OPERATION_MISS_HINT.paramNames[2], getData().dataKey())
                    ;
                }
            }
        });
        return afterUpdate(result, data);
    }

    @Override
    public BaseResult recovery(Long dataId) {
        BaseResult<VO> result = getById(dataId);
        if (!result.isSuccess()) {
            return result;
        }

        VO data = result.getData();
        beforeRecovery(data);

        data.prepareForRecover(SessionUtil.getUserKey());
        entityServiceInterceptorChain.doChain(new BaseWriteMethodInvoker<VO>(result, data) {
            @Override
            public void invoke() {
                int res = baseMapper.update(this.getData());
                if (res >= 0) {
                    getResult().ofData(data);
                } else {
                    getResult().ofSuccess(false).ofLevel(MessageLevelEnum.ERROR)
                            .buildMessage(DATA_OPERATION_MISS_HINT.name(), 1, 2)
                            .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[0], DataOperateTypeEnum.UPDATE.name())
                            .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[1], getDOClass().getSimpleName())
                            .addParam(DATA_OPERATION_MISS_HINT.paramNames[2], getData().dataKey())
                    ;
                }
            }
        });

        return afterRecovery(result);
    }

    @Override
    @SneakyThrows
    public BaseResult<VO> getById(Long id) {
        TO condition = TOClass.getConstructor().newInstance();
        condition.ofEntityClass(DOClass);
        condition.setId(id);
        condition.setDataVersionFrom(null);
        return get(condition);
    }

    @Override
    public BaseResult<VO> get(TO condition) {
        BaseResult<VO> result = new BaseResult();
        condition.ofEntityClass(DOClass);
        condition.setResultNumExpected(1);
        entityServiceInterceptorChain.doChain(new BaseGetMethodInvoker<TO>(result, condition) {

            @Override
            public void invoke() {
                Page<VO> select = baseMapper.select(getData());
                if (CollectionUtils.isEmpty(select)) {
                    getResult().ofSuccess(false).ofLevel(MessageLevelEnum.WARN)
                            .buildMessage(DATA_OPERATION_MISS_HINT.name(), 2, 1)
                            .addParam(DATA_OPERATION_MISS_HINT.paramNames[0], DataOperateTypeEnum.QUERY.name())
                            .addParamI8n(DATA_OPERATION_MISS_HINT.paramNames[1], TOClass.getSimpleName())
                            .addParam(DATA_OPERATION_MISS_HINT.paramNames[2], getData().dataKey())
                    ;
                } else if (select.size() > 1) {
                    getResult().ofSuccess(false).ofLevel(MessageLevelEnum.ERROR)
                            .buildMessage(RESULT_NOT_UNIQUE.name(), 2, 1)
                            .addParamI8n(RESULT_NOT_UNIQUE.paramNames[0], TOClass.getSimpleName())
                            .addParam(RESULT_NOT_UNIQUE.paramNames[1], getData().dataKey())
                            .addParam(RESULT_NOT_UNIQUE.paramNames[2], select.size())
                    ;
                } else {
                    getResult().ofData(select.get(0));
                }
            }
        });
        return result;
    }

    @Override
    public BaseResult<List<VO>> selectByMap(Map condition) {
        return select(new JSONObject(condition).toJavaObject(getTOClass()));
    }

    @Override
    public BaseResult<List<VO>> select(TO condition) {
        BaseResult<List<VO>> result = new BaseResult();
        condition.ofEntityClass(DOClass);
        entityServiceInterceptorChain.doChain(new BaseSelectMethodInvoker<TO>(result, condition) {

            @Override
            public void invoke() {
                Page<VO> select = baseMapper.select(getData());
                if (CollectionUtils.isEmpty(select)) {
                    getResult().ofSuccess(false).ofLevel(MessageLevelEnum.WARN)
                            .buildMessage(RESULT_NOT_FOUND.name(), 0, 1)
                            .addParamI8n(RESULT_NOT_FOUND.paramNames[0], DOClass.getSimpleName())
                    ;
                } else {
                    getResult().ofData(select);
                }
            }
        });
        return result;
    }

    @Override
    public BaseResult<PageInfo<VO>> page(TO condition) {
        BaseResult<PageInfo<VO>> result = new BaseResult();
        condition.ofEntityClass(DOClass);
        entityServiceInterceptorChain.doChain(new BaseReadMethodInvoker<TO>(result, condition) {

            @Override
            public void invoke() {
                BaseResult res = pageAdapter.handlePageParam((PageDTO) condition);
                if (!res.isSuccess()) {
                    result.appendPrev(res);
                    return;
                }

                Page<VO> select = baseMapper.select(getData());
                if (CollectionUtils.isEmpty(select)) {
                    getResult().ofSuccess(false).ofLevel(MessageLevelEnum.WARN)
                            .buildMessage(RESULT_NOT_FOUND.name(), 0, 1)
                            .addParamI8n(RESULT_NOT_FOUND.paramNames[0], TOClass.getName())
                    ;
                } else {
                    getResult().ofData(pageAdapter.toPageInf(select));
                }
            }
        });
        return result;
    }

    @Override
    public BatchResult<VO> insertList(List<VO> list, boolean transaction) {
        BatchResult<VO> result = new BatchResult(list.size());
        for (int i = 0; i < list.size(); i++) {
            BaseResult<VO> res = insert(list.get(i));
            if (!res.isSuccess() && transaction) {
                throw new DataOperationTransactionException(res.getMessage())
                        .getMessageProp()
                        .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[0], DataOperateTypeEnum.INSERT.name())
                        .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[1], TOClass.getName())
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
    public BatchResult<VO> deleteList(List<VO> list, boolean transaction) {
        BatchResult<VO> result = new BatchResult(list.size());
        for (int i = 0; i < list.size(); i++) {
            BaseResult<VO> res = delete(list.get(i));
            if (!res.isSuccess() && transaction) {
                throw new DataOperationTransactionException(res.getMessage())
                        .getMessageProp()
                        .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[0], DataOperateTypeEnum.DELETE.name())
                        .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[1], TOClass.getName())
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
    public BatchResult<VO> updateList(List<VO> list, boolean transaction) {
        BatchResult<VO> result = new BatchResult(list.size());
        for (int i = 0; i < list.size(); i++) {
            BaseResult<VO> res = update(list.get(i));
            if (!res.isSuccess() && transaction) {
                throw new DataOperationTransactionException(res.getMessage())
                        .getMessageProp()
                        .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[0], DataOperateTypeEnum.UPDATE.name())
                        .addParamI8n(DATA_OPERATION_TRANSACTION_TERMINATED.paramNames[1], TOClass.getName())
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
     * 处理分页
     *
     * @param pageDTO 分页参数
     * @return 处理结果
     */
    protected BaseResult handlePageParam(PageDTO pageDTO) {
        return pageAdapter.handlePageParam(pageDTO);
    }

    /**
     * 处理分页
     *
     * @param cond 分页参数
     * @return 处理结果
     */
    protected BaseResult handlePageParam(Map<String, Object> cond) {
        return pageAdapter.handlePageParam(cond);
    }

    /**
     * 将外部分页对象转为内部分页对象
     *
     * @param t   源数据
     * @param <V> 结果泛型
     * @return 分页信息
     */
    protected <V> PageInfo<V> toPageInf(Object t) {
        return pageAdapter.toPageInf(t);
    }

    /**
     * 根据DO获取VO
     *
     * @param data 数据
     * @return VO
     */
    protected BaseResult<VO> getByDO(DO data) {
        if (data.getId() != null) {
            return getById(data.getId());
        } else {
            BaseResult<List<VO>> res = selectByMap((JSONObject) JSON.toJSON(data));
            if (!res.isSuccess()) {
                return BaseResult.fromPrev(res);
            }

            if (res.getData().size() > 0) {
                return BaseResult.fail("多条数据");
            }
            return BaseResult.success(res.getData().get(0));
        }
    }
}
