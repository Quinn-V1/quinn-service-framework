package com.quinn.framework.model;

import com.quinn.framework.api.BusinessJob;
import com.quinn.framework.api.JobInfoSupplier;
import com.quinn.framework.api.JobInstance;
import com.quinn.framework.api.JobTemplate;
import com.quinn.framework.util.JobInfoUtil;
import com.quinn.util.base.CollectionUtil;
import com.quinn.util.base.exception.BaseBusinessException;
import com.quinn.util.licence.model.ApplicationInfo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定时任务信息生成工厂
 *
 * @author Qunhua.Liao
 * @since 2020-05-30
 */
public class JobInfoFactory {

    private static final Map<String, BusinessJob> BUSINESS_JOB_MAP = new ConcurrentHashMap<>();

    /**
     * 任务实例生成器
     */
    private static final Map<Class, JobInfoSupplier> JOB_INFO_SUPPLIER_MAP = new HashMap<>();

    /**
     * 模板过渡器
     */
    private static ThreadLocal<JobInstance> instanceThreadLocal = new ThreadLocal<>();

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
        JobInstance jobInstance = instanceThreadLocal.get();

        if (jobInstance == null) {
            JobInfoSupplier jobInfoSupplier = JOB_INFO_SUPPLIER_MAP.get(object.getClass());
            if (jobInfoSupplier == null) {
                if (object instanceof JobTemplate) {
                    JobTemplate jobTemplate = (JobTemplate) object;
                    jobInstance = new DefaultJobInstance();
                    JobInfoUtil.fillEmptyInstWithTemp(jobInstance, jobTemplate);
                } else {
                    // FIXME
                    throw new BaseBusinessException();
                }
            } else {
                jobInstance = jobInfoSupplier.createJobInstance(object);
            }
        } else {
            instanceThreadLocal.remove();
        }

        jobInstance.setApplicationKey(ApplicationInfo.getAppKey());
        return jobInstance;
    }

    /**
     * 暂存任务实例
     *
     * @param jobInstance 任务实例
     */
    public static void setInstanceLocalThread(JobInstance jobInstance) {
        instanceThreadLocal.set(jobInstance);
    }

    /**
     * 添加所有任务MAP
     *
     * @param businessJobMap 任务MAP
     */
    public static void addAll(Map<String, BusinessJob> businessJobMap) {
        if (businessJobMap != null) {
            BUSINESS_JOB_MAP.putAll(businessJobMap);
        }
    }

    /**
     * 获取实现类
     *
     * @param jobImplement 任务实现类Bean名
     * @return 任务实现类
     */
    public static BusinessJob getImplement(String jobImplement) {
        return BUSINESS_JOB_MAP.get(jobImplement);
    }

    /**
     * 获取支持胡任务列表
     *
     * @return 任务名称
     */
    public static List<String> supportJobs(String keyWords, Integer pageSize, Integer pageNum) {
        Set<String> strings = BUSINESS_JOB_MAP.keySet();
        List<String> names = new ArrayList<>(strings);
        Collections.sort(names);
        return CollectionUtil.page(names, keyWords, pageSize, pageNum);
    }

}
