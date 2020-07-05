package com.quinn.framework.component;

import com.quinn.framework.api.AuthInfo;
import com.quinn.framework.api.CredentialsSubMatcher;
import com.quinn.framework.api.TokenInfo;
import com.quinn.util.base.Md5EncryptUtil;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.convertor.BaseConverter;

/**
 * Md5加密比较器
 *
 * @author Qunhua.Liao
 * @since 2020-05-29
 */
public class Md5CredentialsSubMatcher implements CredentialsSubMatcher {

    @Override
    public String name() {
        return "md5CredentialsSubMatcher";
    }

    @Override
    public boolean doCredentialsMatch(TokenInfo tokenInfo, AuthInfo authInfo) {
        String credentialsOfAuth = BaseConverter.staticToString(authInfo.getCredentials());
        String credentialsOfToken = BaseConverter.staticToString(tokenInfo.getCredentials());
        String principal = BaseConverter.staticToString(authInfo.getPrincipal());
        if (StringUtil.isEmpty(credentialsOfAuth)) {
            return true;
        }

        if (StringUtil.isEmpty(credentialsOfToken) || StringUtil.isEmpty(principal)) {
            return false;
        }

        return credentialsOfAuth.equals(Md5EncryptUtil.encryptHMAC(credentialsOfToken, principal));
    }

    public static void main(String[] args) {
        System.out.println(Md5EncryptUtil.encryptHMAC("123456", "zcx"));
    }
}
