package com.lzy.innovate.permission;

import com.alibaba.fastjson.JSON;
import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.SSOToken;
import com.baomidou.kisso.annotation.Action;
import com.baomidou.kisso.common.util.HttpUtil;
import com.lzy.innovate.controller.common.WebJsonResult;
import com.lzy.innovate.controller.enums.JSONMessageEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Created by laizhiyuan on 2017/3/11.
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger("innovate_web_log");

    /*
     * 系统权限授权接口
     */
    private com.lzy.innovate.permission.Permission authorization;

    /*
     * 非法请求提示 URL
     */
    private String illegalUrl;


    /**
     * <p>
     * 用户权限验证
     * </p>
     * <p>
     * 方法拦截 Controller 处理之前进行调用。
     * </p>
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler )
            throws Exception {
        if ( handler instanceof HandlerMethod) {
            SSOToken token = SSOHelper.attrToken(request);
            if ( token == null ) {
                return true;
            }

			/*
			 * 权限验证合法
			 */
            if ( isVerification(request, handler, token) ) {
                return true;
            }

			/*
			 * 无权限访问
			 */
            return unauthorizedAccess(request, response);

        }

        return true;
    }


    /**
     * <p>
     * 判断权限是否合法，支持 1、请求地址 2、注解编码
     * </p>
     * @param request
     * @param handler
     * @param token
     * @return
     */
    protected boolean isVerification( HttpServletRequest request, Object handler, SSOToken token ) {
		/*
		 * URL 权限认证
		 */
        if ( SSOConfig.getInstance().isPermissionUri() ) {
            String uri = request.getRequestURI();
            if ( uri == null || authorization.checkPermission(token, uri) ) {
                return true;
            }
        }
		/*
		 * 注解权限认证,按钮和菜单
		 */
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        MenuPermission pm = method.getAnnotation(MenuPermission.class);
        OpertionPermission op = method.getAnnotation(OpertionPermission.class);

        boolean tag = true;

        /**
         * 处理菜单权限
         */
        if ( pm != null ) {
            if (StringUtils.isEmpty(pm.code())){
                tag = false;
            }else{
                if (pm.action() != Action.Skip && !authorization.checkMenuPermission(token,pm.code())){
                    tag = false;
                }
            }
        }

        /**
         * 处理按钮权限
         */
        if (op != null){
            if (StringUtils.isEmpty(op.code())){
                tag = false;
            }else{
                if (op.action() != Action.Skip && !authorization.checkOperPermission(token, op.code())){
                    tag = false;
                }
            }
        }
		/*
		 * 非法访问
		 */
        return tag;
    }


    /**
     *
     * <p>
     * 无权限访问处理，默认返回 no permission  ，illegalUrl 非空重定向至该地址
     * </p>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected boolean unauthorizedAccess(HttpServletRequest request, HttpServletResponse response ) throws Exception {
        logger.info(" request 403 url: " + request.getRequestURI());
        if ( HttpUtil.isAjax(request) ) {
            WebJsonResult webJsonResult = new WebJsonResult();
            webJsonResult.setState("faild");
            webJsonResult.setMessage(JSONMessageEnum.NOT_PERMISSION.getMessage());
            PrintWriter pw = null;
            try{
                pw = response.getWriter();
                pw.write(JSON.toJSONString(webJsonResult));
            }catch (Exception ex){
                logger.error(ex.toString());
                webJsonResult.setMessage(JSONMessageEnum.SYSTEM_EXCEPTION.getMessage());
                pw.write(JSON.toJSONString(webJsonResult));
            }finally {
                if (pw != null){
                    pw.close();
                }
            }

            return false;
        } else {
			/* 正常 HTTP 请求 */
            if ( illegalUrl == null || "".equals(illegalUrl) ) {
                response.sendError(403, "Forbidden");
            } else {
                response.sendRedirect(illegalUrl);
            }
        }
        return false;
    }


    public com.lzy.innovate.permission.Permission getAuthorization() {
        return authorization;
    }


    public void setAuthorization( com.lzy.innovate.permission.Permission authorization ) {
        this.authorization = authorization;
    }


    public String getIllegalUrl() {
        return illegalUrl;
    }


    public void setIllegalUrl( String illegalUrl ) {
        this.illegalUrl = illegalUrl;
    }

}
