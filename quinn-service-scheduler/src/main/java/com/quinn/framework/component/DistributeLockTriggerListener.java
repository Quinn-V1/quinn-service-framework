package com.quinn.framework.component;

import com.quinn.framework.api.SortTriggerListener;
import com.quinn.framework.api.TriggerExecuteInfo;
import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.util.base.api.MethodInvokerTwoParam;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.constant.OrderedConstant;
import com.quinn.util.constant.enums.SyncTypeEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 分布式锁触发监听器
 *
 * @author Qunhua.Liao
 * @since 2020-05-30
 */
@Component("distributeLockTriggerListener")
public class DistributeLockTriggerListener implements SortTriggerListener {

    @Resource
    @Qualifier("cacheLockService")
    private CacheAllService cacheLockService;

    @Override
    public MethodInvokerTwoParam<String, String, Boolean> effectStrategy() {
        return (keyName, keyGroup) -> SyncTypeEnum.SYNC.name().equals(keyGroup);
    }

    @Override
    public int getOrder() {
        return OrderedConstant.LOWER_V1;
    }

    @Override
    public String getName() {
        return "distributeLockTriggerListener";
    }

    @Override
    public void triggerFired(TriggerExecuteInfo executionInfo) {

    }

    @Override
    public boolean vetoJobExecution(TriggerExecuteInfo executionInfo) {
        return cacheLockService.lock(executionInfo.getJobKey());
    }

    @Override
    public void triggerMisfired(TriggerExecuteInfo executionInfo) {

    }

    @Override
    public void triggerComplete(TriggerExecuteInfo executionInfo, BaseBusinessException exception) {

    }
}
