package com.quinn.framework.controller;

import com.quinn.framework.util.RequestUtil;
import com.quinn.util.base.api.LoggerExtend;
import com.quinn.util.base.factory.LoggerExtendFactory;
import com.quinn.util.constant.HttpHeadersConstant;
import com.quinn.util.constant.NumberConstant;
import com.quinn.util.licence.model.ApplicationInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * 框架应用相关
 *
 * @author Qunhua.Liao
 * @since 2020-03-31
 */
@RestController
@RequestMapping("/framework/app/*")
@Api(tags = {"0ZZ090框架：应用管理"})
public class ApplicationController extends AbstractController {

    private static final LoggerExtend LOGGER = LoggerExtendFactory.getLogger(ApplicationController.class);

    @Resource
    private ApplicationInfo applicationInfo;

    @Value("${com.quinn-service.framework.magic-code:2106db2f557e45b588720f21acf6973f}")
    private String magicCode;

    /**
     * 1.限定127.0.0.1 访问(限定本机访问)
     * 2.云模式: zookeeper 修改当前应用信息为half,
     * 3.等待5s  等待处理同步应用状态时差,导致访问请求
     * 4.5s间隔轮询, 访问次数为0时,应用无其他请求,执行优雅停机（访问不统计白名单访问）
     * 5.存在阻塞线程,超时60s后,强制退出
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws Exception
     */
    @PostMapping(value = "/shutdown", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "优雅停机", notes = "只能在应用(127.0.0.1)调用, 调用成功后应用将拒绝新服务直到当前所有服务线程执行完毕后关闭")
    public void shutdown(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        if (isAccessAllowed(httpServletRequest)) {
            LOGGER.info("访问统计次数:[{}]", applicationInfo.getVisitTimes());
            if (applicationInfo != null) {
                applicationInfo.waiting();
            }

            PrintWriter writer = httpServletResponse.getWriter();
            long startTime = System.currentTimeMillis();
            while (true) {
                long nRemaining = applicationInfo.getRemainRequestCount().longValue();

                if (nRemaining == NumberConstant.INT_ONE
                        || System.currentTimeMillis() - startTime > NumberConstant.TIME_MILL_ONE_MINUTE) {
                    writer.println("Application have been shutdown");
                    writer.flush();
                    applicationInfo.stopping();

                    new Thread(new ShutdownTask()).start();
                    break;
                }

                try {
                    TimeUnit.SECONDS.sleep(NumberConstant.INT_FIVE);
                } catch (InterruptedException e) {
                    LOGGER.error("Error occurs when sleep", e);
                }
            }
        } else {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.setContentType(HttpHeadersConstant.CONTENT_TYPE_HTML);
            httpServletResponse.getWriter().write("Shutdown commands can only be accessed from localhost" +
                    " or intranet by passing magic code<br/>");
            httpServletResponse.getWriter().flush();
        }
    }

    /**
     * 是否有权限操作
     *
     * @param request 请求对象
     * @return 是否有权限
     */
    private boolean isAccessAllowed(HttpServletRequest request) {
        return "127.0.0.1".equals(request.getRemoteAddr()) || (RequestUtil.isRequestFromIntranet(request)
                && magicCode.equalsIgnoreCase(request.getParameter("magic-code")));
    }

    /**
     * 停应用线程
     */
    private class ShutdownTask implements Runnable {
        @Override
        public void run() {
            LOGGER.info("Graceful shut down will be done after 3 seconds");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
            }
            LOGGER.info("Application has been shut down");
            System.exit(0);
        }
    }

}
