package com.quinn.framework.configuration;

import com.quinn.framework.api.ApplicationSerializer;
import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.framework.component.ApplicationRedisSerializer;
import com.quinn.framework.service.RedisAllServiceImpl;
import com.quinn.util.base.constant.ConfigConstant;
import com.quinn.util.base.StringUtil;
import com.quinn.util.constant.StringConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis缓存配置类
 *
 * @author Qunhua.Liao
 * @since 2020-04-02
 */
@Configuration
public class RedisConfiguration {

    @Value("${com.quinn-service.redis.host:127.0.0.1}")
    private String host;

    @Value("${com.quinn-service.redis.port:6379}")
    private int port;

    @Value("${com.quinn-service.redis.password:}")
    private String password;

    @Value("${com.quinn-service.redis.database:0}")
    private int database;

    @Value("${com.quinn-service.redis.maxActive:8}")
    private int maxActive;

    @Value("${com.quinn-service.redis.maxIdle:8}")
    private int maxIdle;

    @Value("${com.quinn-service.redis.maxWait:-1}")
    private long maxWait;

    @Value("${com.quinn-service.redis.minIdle:0}")
    private int minIdle;

    @Value("${com.quinn-service.redis.timeout:2000}")
    private int timeout;

    @Value("${com.quinn-service.redis.cache.name:}")
    private String cacheName;

    @Value("${com.quinn-service.redis.keys.namespace:}")
    private String keysNamespace;

    @Value("${com.quinn-service.redis.keys.normalize:true}")
    private boolean keysNormalize;

    @Value("${com.quinn-service.redis.hosts:}")
    private String hosts;

    @Value("${com.quinn-service.redis.hosts.type:sentinel}")
    private String hostsType;

    @Value("${com.quinn-service.redis.sentinel.master:masterName}")
    private String sentinelMaster;

    @Value("${com.quinn-service.redis.cluster.maxRedirects:3}")
    private int maxRedirects;

    @Value("${com.quinn-service.redis.metrics.report.interval:300}")
    private int reportInterval;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxWaitMillis(maxWait);

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        jpcb.poolConfig(jedisPoolConfig);
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();

        RedisConnectionFactory connectionFactory = null;

        if (StringUtil.isNotEmpty(hosts)) {
            List<RedisNode> hostAndPorts = parseRedisNodes(hosts);
            if (CollectionUtils.isEmpty(hostAndPorts)) {
                throw new IllegalArgumentException("Invalid property configuration [com.quinn-service.redis.hosts]");
            }
            if (ConfigConstant.REDIS_DEPLOY_TYPE_CLUSTER.equalsIgnoreCase(hostsType)) {
                RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();
                sentinelConfiguration.setSentinels(hostAndPorts);
                sentinelConfiguration.setMaster(sentinelMaster);
                sentinelConfiguration.setDatabase(database);
                sentinelConfiguration.setPassword(RedisPassword.of(password));

                connectionFactory = new JedisConnectionFactory(sentinelConfiguration, jedisClientConfiguration);
            } else if (ConfigConstant.REDIS_DEPLOY_TYPE_CLUSTER.equalsIgnoreCase(hostsType)) {
                RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
                clusterConfiguration.setClusterNodes(hostAndPorts);
                clusterConfiguration.setPassword(RedisPassword.of(password));
                clusterConfiguration.setMaxRedirects(maxRedirects);

                connectionFactory = new JedisConnectionFactory(clusterConfiguration, jedisClientConfiguration);
            }
        } else if (StringUtil.isNotEmpty(host)) {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
            redisStandaloneConfiguration.setDatabase(database);
            redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

            connectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
        }

        return connectionFactory;
    }

    @Bean("redisTemplate")
    public RedisTemplate redisTemplate() {
        RedisConnectionFactory connectionFactory = redisConnectionFactory();
        RedisTemplate redisTemplate = new RedisTemplate();
        RedisSerializer stringSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean("applicationSerializer")
    public ApplicationSerializer applicationSerializer() {
        return new ApplicationRedisSerializer();
    }

    @Bean(name = {"cacheAllService", "cacheCommonService", "cacheLockService", "cacheCounterService"})
    public CacheAllService cacheAllService(
            RedisTemplate redisTemplate
    ) {
        RedisAllServiceImpl redisBaseService =
                new RedisAllServiceImpl(redisTemplate, applicationSerializer(), cacheName, keysNamespace);
        return redisBaseService;
    }

    /**
     * 解析缓存数据库路径
     *
     * @param hosts 数据库路径
     * @return Redis节点信息列表
     */
    public static List<RedisNode> parseRedisNodes(String hosts) {
        ArrayList<RedisNode> ret = new ArrayList<>();
        String[] parts = hosts.split(StringConstant.CHAR_SEMICOLON);

        for (String sHost : parts) {
            if (!StringUtil.isNotEmpty(sHost)) {
                continue;
            }

            String[] hostPart = sHost.split(StringConstant.CHAR_COLON);
            String hostName = hostPart[0];
            if (!StringUtil.isNotEmpty(hostName)) {
                continue;
            }

            int iPort = hostPart.length > 1 ? Integer.parseInt(hostPart[1]) : 6379;
            ret.add(new RedisNode(hostName, iPort));
        }
        return ret;
    }

}