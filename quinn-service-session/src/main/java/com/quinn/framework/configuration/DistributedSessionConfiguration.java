package com.quinn.framework.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.util.StringUtils;

/**
 * 分布式会话配置
 *
 * @author Qunhua.Liao
 * @since 2020-05-21
 */
@EnableRedisHttpSession
public class DistributedSessionConfiguration {

    /**
     * 会话存储的redis默认命名空间
     */
    private static final String SESSION_REDIS_NAMESPACE = "quinn-session:";

    @Value("${server.session.timeout:1800}")
    private int sessionTimeout;

    static {
        String namespace = System.getProperty("com.quinn-service.session.redis.namespace", SESSION_REDIS_NAMESPACE);
        if (StringUtils.hasText(namespace)) {
            System.setProperty("spring.session.redis.namespace", namespace);
        }
    }

    /**
     * 会话仓库配置（bean本身无意义：重点在config方法）
     *
     * @param redisOperationsSessionRepository 会话仓库
     * @return 会话仓库配置类
     */
    @Bean
    public SessionRepositoryConfigurer sessionRepositoryConfigurer(
            RedisOperationsSessionRepository redisOperationsSessionRepository) {
        return new SessionRepositoryConfigurer(redisOperationsSessionRepository, sessionTimeout).config();
    }

    /**
     * 会话仓库配置类
     *
     * @author Qunhua.Liao
     * @since 2020-05-21
     */
    public static class SessionRepositoryConfigurer {

        /**
         * 会话仓库
         */
        private RedisOperationsSessionRepository redisOperationsSessionRepository;

        /**
         * 超时时间
         */
        private int sessionTimeout;

        public SessionRepositoryConfigurer(RedisOperationsSessionRepository redisOperationsSessionRepository,
                                           int sessionTimeout) {
            this.redisOperationsSessionRepository = redisOperationsSessionRepository;
            this.sessionTimeout = sessionTimeout;
        }

        SessionRepositoryConfigurer config() {
            this.redisOperationsSessionRepository.setDefaultMaxInactiveInterval(this.sessionTimeout);
            return this;
        }
    }
}
