package com.quinn.framework.configuration;

import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.framework.component.*;
import com.quinn.framework.model.DefaultPermission;
import com.quinn.framework.model.QuinnSessionFactory;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Shiro自动配置类
 *
 * @author Quinn.Liao<br>
 * @since 2020-05-21
 */
@Configuration
public class QuinnAutoConfiguration {

    @Value("${server.servlet.session.timeout:1800}")
    private int sessionTimeOut;

    @Value("${com.quinn-service.session.cookie-name:SESSIONID}")
    private String sessionIdCookieName;

    @Value("${com.quinn-service.session.principal-field-name:principal}")
    private String principalIdFieldName;

    @Value("${com.quinn-service.session.error-max-limit:5}")
    private int maxRetryLimit;

    @Value("${com.quinn-service.session.authentication-cache-name:authenticationCache}")
    private String authenticationCache;

    @Value("${com.quinn-service.session.authorization-cache-name:authorizationCache}")
    private String authorizationCache;

    @Resource
    @Qualifier("sessionCacheAllService")
    private CacheAllService sessionCacheAllService;

    @Bean
    public CredentialsMatcher credentialsMatcher() {
        return new QuinnCredentialsMatcher();
    }

    @Bean
    public AuthorizingRealm realm(CredentialsMatcher credentialsMatcher) {
        QuinnAuthorizingRealm shiroRealm = new QuinnAuthorizingRealm();
        shiroRealm.setCachingEnabled(true);

        // 启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        // 启用授权缓存，即缓存AuthorizationInfo信息，默认false
        shiroRealm.setAuthenticationCachingEnabled(true);
        shiroRealm.setAuthorizationCachingEnabled(true);

        // 缓存 AuthenticationInfo 信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
        // 缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
        shiroRealm.setAuthenticationCacheName(authenticationCache);
        shiroRealm.setAuthorizationCacheName(authorizationCache);

        // 配置自定义密码比较器
        shiroRealm.setCredentialsMatcher(credentialsMatcher);
        return shiroRealm;
    }

    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie(sessionIdCookieName);
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    @Bean("sessionFactory")
    public SessionFactory sessionFactory() {
        QuinnSessionFactory sessionFactory = new QuinnSessionFactory();
        return sessionFactory;
    }

    @Bean
    public SessionDAO sessionDAO() {
        QuinnSessionDAO sessionDAO = new QuinnSessionDAO(sessionCacheAllService);
        sessionDAO.setExpire(sessionTimeOut);
        return sessionDAO;
    }

    @Bean("cacheManager")
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        QuinnCacheManager quinnCacheManager = new QuinnCacheManager(sessionCacheAllService);
        quinnCacheManager.setPrincipalIdFieldName(principalIdFieldName);
        quinnCacheManager.setExpire(sessionTimeOut);
        return quinnCacheManager;
    }

    @Bean("sessionManager")
    public SessionManager sessionManager(
            org.apache.shiro.cache.CacheManager cacheManager,
            SessionDAO sessionDAO,
            SimpleCookie sessionIdCookie,
            SessionFactory sessionFactory
    ) {
        QuinnSessionManager sessionManager = new QuinnSessionManager();
        Collection<SessionListener> listeners = new ArrayList<>();

        sessionManager.setSessionListeners(listeners);
        sessionManager.setCacheManager(cacheManager);

        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionFactory(sessionFactory);
        sessionManager.setGlobalSessionTimeout(sessionTimeOut * 1000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);

        sessionManager.setSessionValidationInterval(60 * 1000);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    @Bean
    public SubjectDAO subjectDAO(
            CacheManager cacheManager
    ) {
        QuinnSubjectDao subjectDao = new QuinnSubjectDao();
        Cache<String, DefaultPermission> authorCache = cacheManager.getCache(authorizationCache);
        Cache<String, DefaultPermission> authCache = cacheManager.getCache(authenticationCache);
        subjectDao.setAuthorizationCache(authorCache);
        subjectDao.setAuthenticationCache(authCache);
        return subjectDao;
    }

    /**
     * 配置核心安全事务管理器
     */
    @Bean(name = "securityManager")
    public SecurityManager securityManager(
            Realm realm,
            SubjectDAO subjectDAO,
            CacheManager cacheManager,
            SessionManager sessionManager
    ) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setCacheManager(cacheManager);
        securityManager.setSessionManager(sessionManager);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    @Bean
    @ConditionalOnMissingBean(ShiroFilterFactoryBean.class)
    public ShiroFilterFactoryBean shiroFilter(QuinnFilterConfig quinnFilterConfig) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        quinnFilterConfig.config(shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }

    @Bean
    public FormAuthenticationFilter formAuthenticationFilter() {
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        formAuthenticationFilter.setRememberMeParam("rememberMe");
        return formAuthenticationFilter;
    }

    @Bean
    public MethodInvokingFactoryBean getMethodInvokingFactoryBean(SecurityManager securityManager) {
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(new Object[]{securityManager});
        return factoryBean;
    }

    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new QuinnSessionGenerator(sessionIdCookieName);
    }

}
