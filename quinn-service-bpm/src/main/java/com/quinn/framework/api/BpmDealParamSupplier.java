package com.quinn.framework.api;

import com.quinn.framework.model.deal.AbstractBpmDealParam;

/**
 * Bpm 处理参数提供者
 *
 * @author Qunhua.Liao
 * @since 2020-07-02
 */
public interface BpmDealParamSupplier<T extends AbstractBpmDealParam> {

    /**
     * 生成参数
     *
     * @return 参数对象
     */
    T supply();

    /**
     * 处理类型
     *
     * @return 处理类型
     */
    String[] getDealTypes();

}
