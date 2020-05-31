package com.quinn.framework.component;

import com.quinn.framework.api.JobInstance;
import com.quinn.framework.model.AbstractBusinessJob;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.base.model.BaseResult;

import java.util.Map;

/**
 * 测试任务
 *
 * @author Qunhua.Liao
 * @since 2020-05-31
 */
public class TestBusinessJob extends AbstractBusinessJob {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(TestBusinessJob.class);

    @Override
    public BaseResult doExecute(JobInstance instance, Map<String, Object> param) {
        LOGGER.info("TestBusinessJob.doExecute of {}", instance.getJobTemplate());
        return BaseResult.SUCCESS;
    }

}
