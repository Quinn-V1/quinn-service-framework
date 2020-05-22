package com.quinn.framework.configuration;

import com.quinn.framework.api.cache.CacheAllService;
import com.quinn.framework.component.*;
import com.quinn.framework.filter.PathMatchPermissionFilter;
import com.quinn.framework.filter.QuinnIntranetFilter;
import com.quinn.framework.model.QuinnFilterConfig;
import com.quinn.framework.model.QuinnSessionFactory;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.Resource;
import javax.servlet.Filter;
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

    @Value("${server.session.timeout:1800}")
    private int sessionTimeOut;

    @Value("${com.quinn-service.session.cookie-name:JSESSIONID}")
    private String sessionIdCookieName;

    @Value("${com.quinn-service.session.principal-field-name:userKey}")
    private String principalIdFieldName;

    @Value("${com.quinn-service.session.error-max-limit:5}")
    private int maxRetryLimit;

    @Resource
    private CacheAllService cacheAllService;

    @Bean
    public CredentialsMatcher credentialsMatcher() {
        return new MultiCredentialsMatcher();
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
        shiroRealm.setAuthenticationCacheName("authenticationCache");
        shiroRealm.setAuthorizationCacheName("authorizationCache");

        // 配置自定义密码比较器
        shiroRealm.setCredentialsMatcher(credentialsMatcher);
        return shiroRealm;
    }

    @Bean
    public SimpleCookie rememberMeCookie() {
        // 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // setCookie的httpOnly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：
        // setCookie()的第七个参数
        // 设为true后，只能通过http访问，javascript无法访问
        // 防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager(SimpleCookie rememberMeCookie) {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie);
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie() {
        // 这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie(sessionIdCookieName);
        // setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        // setcookie()的第七个参数
        // 设为true后，只能通过http访问，javascript无法访问
        // 防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        // maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    @Bean
    public SessionDAO sessionDAO() {
        QuinnSessionDAO shiroSessionDAO = new QuinnSessionDAO(cacheAllService);
        shiroSessionDAO.setExpire(sessionTimeOut);
        return shiroSessionDAO;
    }

    @Bean("sessionFactory")
    public SessionFactory sessionFactory() {
        QuinnSessionFactory sessionFactory = new QuinnSessionFactory();
        return sessionFactory;
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

    @Bean("cacheManager")
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        QuinnCacheManager quinnCacheManager = new QuinnCacheManager(cacheAllService);
        quinnCacheManager.setPrincipalIdFieldName(principalIdFieldName);
        quinnCacheManager.setExpire(sessionTimeOut);
        return quinnCacheManager;
    }

    /**
     * 配置核心安全事务管理器
     */
    @Bean(name = "securityManager")
    public SecurityManager securityManager(
            Realm realm,
            CacheManager cacheManager,
            SessionManager sessionManager,
            RememberMeManager rememberMeManager
    ) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setRememberMeManager(rememberMeManager);
        securityManager.setCacheManager(cacheManager);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean
    public Filter pathMatchPermissionFilter() {
        return new PathMatchPermissionFilter();
    }

    @Bean
    public Filter intranetFilter() {
        return new QuinnIntranetFilter();
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
