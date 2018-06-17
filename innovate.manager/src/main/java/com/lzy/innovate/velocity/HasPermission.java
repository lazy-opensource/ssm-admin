package com.lzy.innovate.velocity;

import com.lzy.innovate.dubbo.system.ISysOperServiceSoa;
import com.lzy.innovate.utils.SpringUtils;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;

/**
 * 模板权限控制标签
 * Created by lzy on 2017/3/12.
 */
public class HasPermission extends Directive {

    private static Logger logger = LoggerFactory.getLogger("innovate_web_log");
    private static ISysOperServiceSoa iSysOperServiceSoa = (ISysOperServiceSoa) SpringUtils.getBean("iSysOperServiceSoa");

    @Override
    public String getName() {
        return "hasPermission";
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public boolean render(InternalContextAdapter internalContextAdapter, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {

        String code = (String) node.jjtGetChild(0).value(internalContextAdapter);
        if (org.springframework.util.StringUtils.isEmpty(code)){
            return false;
        }

        return false;
    }
}
