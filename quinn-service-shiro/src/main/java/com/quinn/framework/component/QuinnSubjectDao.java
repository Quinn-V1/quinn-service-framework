package com.quinn.framework.component;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.model.AuthInfoFactory;
import com.quinn.framework.util.SessionUtil;
import com.quinn.util.base.convertor.BaseConverter;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.subject.Subject;

/**
 * Subject创建对象
 *
 * @author Qunhua.Liao
 * @since 2020-06-01
 */
public class QuinnSubjectDao extends DefaultSubjectDAO {

    @Override
    public Subject save(Subject subject) {
        Object principal = subject.getPrincipal();
        if (principal != null) {
            SessionUtil.setAuthInfo(principal);
            AuthInfo authInfo = AuthInfoFactory.generate(principal);
            SessionUtil.setValue(SessionUtil.SESSION_KEY_USER_ID, authInfo.attr(AuthInfo.ATTR_NAME_ID));
            SessionUtil.setValue(SessionUtil.SESSION_KEY_USER_KEY, BaseConverter.staticToString(authInfo.getPrincipal()));
            SessionUtil.setValue(SessionUtil.SESSION_KEY_ORG_KEY, authInfo.getCurrentTenantCode());
        }
        return super.save(subject);
    }
}
