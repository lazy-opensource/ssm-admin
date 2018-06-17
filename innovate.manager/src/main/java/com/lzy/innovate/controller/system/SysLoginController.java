package com.lzy.innovate.controller.system;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.SSOToken;
import com.baomidou.kisso.annotation.Action;
import com.baomidou.kisso.annotation.Login;
import com.baomidou.kisso.common.encrypt.SaltEncoder;
import com.baomidou.kisso.common.util.RandomUtil;
import com.baomidou.kisso.web.waf.request.WafRequestWrapper;
import com.lzy.innovate.controller.base.BaseController;
import com.lzy.innovate.controller.common.MyCaptcha;
import com.lzy.innovate.controller.enums.SysUserStatusEnum;
import com.lzy.innovate.dubbo.system.ISysUserServiceSoa;
import com.lzy.innovate.entity.SysUser;
import com.lzy.innovate.utils.date.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Created by lzy on 2017/3/7.
 */
@Controller
@RequestMapping("/index")
public class SysLoginController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger("innovate_web_log");

    @Autowired
    private ISysUserServiceSoa iSysUserServiceSoa;

    /**
     * 去登录
     * @param model
     * @return
     */
    @Login(action = Action.Skip)
    @RequestMapping(value = "/toLogin")
    public String toLogin(Model model){

        if (isPost()) {
            String errorMsg = "用户名或密码错误";
            /**
             * 过滤 XSS SQL 注入
             */
            WafRequestWrapper wr = new WafRequestWrapper(request);
            String ctoken = wr.getParameter("ctoken");
            String captcha = wr.getParameter("captcha");
            if (StringUtils.isNotBlank(ctoken)
                    && StringUtils.isNotBlank(captcha)
                    && MyCaptcha.getInstance().verification(wr, ctoken, captcha)) {
                String username = wr.getParameter("username");
                String password = wr.getParameter("password");
                /**
                 * <p>
                 * 用户存在且激活，签名合法，登录成功
                 * <br>
                 * 进入后台
                 * </p>
                 */
                SysUser user = iSysUserServiceSoa.selectByNameAndPwd(username , SaltEncoder.md5SaltEncode(username,password));
                if (user != null) {
                    if (!SysUserStatusEnum.ACTIVATE.getStatus().equals(user.getStatus())){
                        errorMsg = "账号未激活!";
                    }else{
                        //设置IP，和app
                        SSOToken st = new SSOToken(request);
                        st.setData(username + "@" + user.getGroupId());
                        st.setUid(user.getUuid());
                        // 记住密码，设置 cookie 时长 1 周 = 604800 秒
                        String rememberMe = wr.getParameter("rememberMe");
                        if ("on".equals(rememberMe)) {
                            request.setAttribute(SSOConfig.SSO_COOKIE_MAXAGE, 604800);
                        }

                        SSOHelper.setSSOCookie(request, response, st, true);
                        logger.info(DateUtil.getCurrentTime() + ":" + user.getLoginName() + "login system !!!");
                        return redirectTo("/index/toIndex");
                    }
                }
            } else {
                errorMsg = "验证码错误";
            }
            model.addAttribute("errorMsg", errorMsg);
        }
        model.addAttribute(MyCaptcha.CAPTCHA_TOKEN, RandomUtil.get32UUID());
        return "/login";
    }

    /**
     * 登出
     * @return
     */
    @Login(action = Action.Skip)
    @RequestMapping(value = "/logout" , method = RequestMethod.GET)
    public String logout(){
        SSOHelper.clearLogin(request , response);

        return redirectTo("/index/toLogin");
    }

    /**
     * 没有权限菜单
     * @return
     */
    @RequestMapping(value = "/noPer", method = {RequestMethod.GET})
    public String noPer(){
        return "common/noPer";
    }

    /**
     * 主页
     * @return
     */
    @Login(action = Action.Skip)
    @RequestMapping(value = "/toIndex" , method = RequestMethod.GET)
    public String toIndex(){

        return "/index";
    }

    /**
     * 生成图片
     */
    @ResponseBody
    @Login(action = Action.Skip)
    @RequestMapping("/image")
    public void image(String ctoken) {
        try {
            MyCaptcha.getInstance().generate(request, response.getOutputStream(), ctoken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
