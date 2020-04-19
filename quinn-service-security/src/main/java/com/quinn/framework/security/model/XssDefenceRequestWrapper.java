package com.quinn.framework.security.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 跨站脚本攻击防御过滤器
 *
 * @author Qunhua.Liao
 * @since 2020-04-13
 */
public class XssDefenceRequestWrapper extends HttpServletRequestWrapper {

    private final static Whitelist WHITELIST = Whitelist.relaxed();

    private final static Document.OutputSettings OUTPUT_SETTINGS = new Document.OutputSettings().prettyPrint(false);

    static {
        WHITELIST.addTags("embed", "object", "param", "span", "div", "img");
        WHITELIST.addAttributes(":all", "style", "class", "id", "name");
        WHITELIST.addAttributes("object", "width", "height", "classid", "codebase");
        WHITELIST.addAttributes("param", "name", "value");
        WHITELIST.addAttributes("embed", "src", "quality", "width", "height", "allowFullScreen",
                "allowScriptAccess", "flashvars", "name", "type", "pluginspage");
    }

    public XssDefenceRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    private String cleanXSS(String value) {
        if (null != value) {
            value = ESAPI.encoder().canonicalize(value);
            value = value.replaceAll("\0", "");
            value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
            value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
            value = value.replaceAll("'", "& #39;");
            value = value.replaceAll("eval\\((.*)\\)", "");
            value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
            value = value.replaceAll("script", "");
            value = Jsoup.clean(value, "", WHITELIST, OUTPUT_SETTINGS);
        }
        return value;
    }
}
