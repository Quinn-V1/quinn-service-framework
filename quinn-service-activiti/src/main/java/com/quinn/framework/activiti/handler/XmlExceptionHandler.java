package com.quinn.framework.activiti.handler;

import com.quinn.framework.model.DefaultErrorHandler;
import com.quinn.util.base.model.BaseResult;
import org.activiti.bpmn.exceptions.XMLException;
import org.springframework.stereotype.Component;

/**
 * XML文件解析错误（BPM）
 *
 * @author Qunhua.Liao
 * @since 2020-06-19
 */
@Component("bpmXmlExceptionHandler")
public class XmlExceptionHandler extends DefaultErrorHandler<XMLException> {

    @Override
    public void generateMessage(XMLException e, BaseResult result) {
        result.setMessage("设计文件格式不正确：" + e.getCause().getLocalizedMessage());
    }

    @Override
    public Class<?> getDivClass() {
        return XMLException.class;
    }
}
