package com.quinn.framework.component;

import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 默认语言解析器
 *
 * @author Qunhua.Liao
 * @since 2020-06-01
 */
public class DefaultQuinnLocalResolver extends CookieLocaleResolver {

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        super.setLocale(request, response, locale);
    }

}
