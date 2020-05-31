package com.quinn.framework.model;

import com.quinn.framework.api.JobInfoSupplier;
import com.quinn.framework.api.JobInstance;
import com.quinn.framework.api.JobTemplate;
import com.quinn.framework.util.JobInfoUtil;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.licence.model.ApplicationInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 定时任务信息生成工厂
 *
 * @author Qunhua.Liao
 * @since 2020-05-30
 */
public class JobInfoFactory {

    private static final Map<Class, JobInfoSupplier> JOB_INFO_SUPPLIER_MAP = new HashMap<>();

    static {
        ServiceLoader<JobInfoSupplier> authInfoSuppliers = ServiceLoader.load(JobInfoSupplier.class);
        Iterator<JobInfoSupplier> authInfoSupplierIterator = authInfoSuppliers.iterator();
        while (authInfoSupplierIterator.hasNext()) {
            JobInfoSupplier next = authInfoSupplierIterator.next();
            JOB_INFO_SUPPLIER_MAP.put(next.getDivClass(), next);
        }
    }

    /**
     * 生成任务实例
     *
     * @param object 任务模板
     * @return 任务实例
     */
    public static JobInstance createJobInstance(Object object) {
        JobInfoSupplier jobInfoSupplier = JOB_INFO_SUPPLIER_MAP.get(object.getClass());
        if (jobInfoSupplier == null) {
            if (object instanceof JobTemplate) {
                JobTemplate jobTemplate = (JobTemplate) object;
                DefaultJobInstance jobInstance = new DefaultJobInstance();
                JobInfoUtil.fillEmptyInstWithTemp(jobInstance, jobTemplate);
                return jobInstance;
            } else {
                // FIXME
                throw new BaseBusinessException();
            }
        }

        JobInstance jobInstance = jobInfoSupplier.createJobInstance(object);
        jobInstance.setApplicationKey(ApplicationInfo.getAppKey());
        return jobInstance;
    }

}
