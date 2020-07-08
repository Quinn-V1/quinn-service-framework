package com.quinn.framework.model.deal;

import com.quinn.framework.api.BpmDealParamSupplier;
import com.quinn.util.base.exception.DataStyleNotMatchException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * BPM 处理参数工厂类
 *
 * @author Qunhua.Liao
 * @since 2020-07-02
 */
public class BpmDealParamFactory {

    private static Map<String, BpmDealParamSupplier> supplierMap = new HashMap<>();

    static {
        ServiceLoader<BpmDealParamSupplier> dealParamSuppliers = ServiceLoader.load(BpmDealParamSupplier.class);
        Iterator<BpmDealParamSupplier> dealParamSupplierIterator = dealParamSuppliers.iterator();
        while (dealParamSupplierIterator.hasNext()) {
            BpmDealParamSupplier supplier = dealParamSupplierIterator.next();
            for (String dealType : supplier.getDealTypes()) {
                supplierMap.put(dealType, supplier);
            }
        }
    }

    /**
     * 创建具体处理参数
     *
     * @param complexDealParam 复杂参数
     * @param <T>              泛型
     * @return 具体参数
     */
    public static <T extends AbstractBpmDealParam> T create(ComplexDealParam complexDealParam) {
        String dealType = complexDealParam.getDealType();
        BpmDealParamSupplier supplier = supplierMap.get(dealType);
        if (supplier == null) {
            throw new DataStyleNotMatchException();
        }

        AbstractBpmDealParam param = supplier.supply();
        param.initWithParam(complexDealParam);
        return (T) param;
    }

}
