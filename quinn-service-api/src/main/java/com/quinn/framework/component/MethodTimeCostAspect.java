//package com.quinn.framework.component;
//
//import com.quinn.util.base.api.LoggerExtend;
//import com.quinn.util.base.factory.LoggerExtendFactory;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
///**
// * 系统服务组件Aspect切面Bean
// *
// * @author Qunhua.Liao
// * @since 2020-04-20
// */
////@Component
////@Aspect
//public class MethodTimeCostAspect {
//
//    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(MethodTimeCostAspect.class);
//
//    private static final Long MAX_TIME_IN_MS = 2000L;
//
//    @Pointcut("execution(* com..service.impl..*.*(..))")
//    public void timeCostMethod() {
//    }
//
//    @Around("timeCostMethod()")
//    public Object timeCostAround(ProceedingJoinPoint pjp) throws Throwable {
//        long start = System.currentTimeMillis();
//        try {
//            return pjp.proceed();
//        } finally {
//            long cost = System.currentTimeMillis() - start;
//            if (cost > MAX_TIME_IN_MS) {
//                String className = pjp.getTarget().getClass().getSimpleName();
//                String methodName = pjp.getSignature().getName();
//                LOGGER.error("{}.{} cost {} ms", className, methodName, cost);
//            }
//        }
//    }
//
//}