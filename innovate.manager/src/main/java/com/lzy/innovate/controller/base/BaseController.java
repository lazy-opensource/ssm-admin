package com.lzy.innovate.controller.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.SSOToken;
import com.baomidou.kisso.common.util.HttpUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.lzy.innovate.controller.common.WebJsonResult;
import com.lzy.innovate.controller.enums.JSONMessageEnum;
import com.lzy.innovate.entity.SysMenu;
import com.lzy.innovate.exception.WebException;
import com.lzy.innovate.utils.Sets;
import com.lzy.innovate.utils.tree.BootstrapTreeView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by lzy on 2017/3/12.
 * 控制器基类
 */
public class BaseController {

    protected Logger logger = LoggerFactory.getLogger("innovate_web_log");

    private String contextPath = null;

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected HttpSession session;
    @Autowired
    protected ServletContext application;

    protected boolean isSuccess = false;

    private String viewName;

    private String viewPath = "";

    public BaseController() {
    }

    protected String contextPath(){
        if (contextPath == null){
            String path = request.getContextPath();
            contextPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        }

        return contextPath;
    }

    public BaseController(String viewName) {
        this.viewName = viewName;
    }

    public BaseController(String viewPath, String viewName) {
        this.viewPath = viewPath;
        this.viewName = viewName;
    }

    /**
     * 构建菜单树
     * @param sysMenus
     * @return
     */
    protected BootstrapTreeView generatorTree(List<SysMenu> sysMenus){
        BootstrapTreeView rootNode = new BootstrapTreeView();
        if (sysMenus == null || sysMenus.size() < 1){
            return rootNode;
        }
        /**
         * 找根节点
         */
        for (SysMenu sysMenu : sysMenus){
            if ("-1".equals(sysMenu.getParentId())){
                rootNode.setText(sysMenu.getName());
                if (!StringUtils.isEmpty(sysMenu.getUrl())){
                    rootNode.setHref(contextPath() + sysMenu.getUrl()  + "/" + sysMenu.getUuid());
                }
                rootNode.setPid(sysMenu.getParentId());
                rootNode.setUuid(sysMenu.getUuid());
                rootNode.setIcon(sysMenu.getIcon());
                rootNode.setBackColor("#4B4B4B");
                rootNode.setColor("white");
                rootNode.getState().setSelectable(false);
                return findChildNode(rootNode, sysMenus);
            }
        }

        return rootNode;
    }

    /**
     * 递归构建菜单树
     * @param parentNode
     * @param sysMenus
     * @return
     */
    private BootstrapTreeView findChildNode(BootstrapTreeView parentNode, List<SysMenu> sysMenus){
        if (parentNode == null || sysMenus == null){
            return parentNode;
        }

        String parentUuid = parentNode.getUuid();
        BootstrapTreeView child = null;
        for (SysMenu m : sysMenus){
            if (parentUuid.equals(m.getParentId())){
                child = new BootstrapTreeView();
                child.setText(m.getName());
                child.setIcon(m.getIcon());
                child.setUuid(m.getUuid());
                child.setPid(m.getParentId());
                if (!StringUtils.isEmpty(m.getUrl())){
                    child.setHref(contextPath() + m.getUrl()  + "/" + m.getUuid());
                }
                /**
                 * 递归， 后续优化，或者缓存，目前实现方式产生笛卡尔乘积
                 */

                parentNode.getNodes().add(child);
                findChildNode(child, sysMenus);
            }
        }
        return parentNode;
    }

    /**
     * 子类可以覆盖该方法额外做些事情
     */
    protected void doSomething(Model model) {

        //doSomething
    }

    /**
     * Object类型list转换为String类型list
     * @param object
     * @return
     */
    protected List<String> objectListConversionStringList(List<Object> object){
        List<String> list = Sets.list();
        if (object != null && object.size() > 0){
            for (Object o : object){
                list.add(o.toString());
            }
        }
        return list;
    }

    /**
     * 当前用户是否合法
     * @return
     */
    protected boolean isLegal(){

        return getCurrentUserId() == null ? false : true;
    }

    /**
     * 获得当前用户ID
     * @return
     */
    protected String getCurrentUserId(){
        if (request == null){
            return null;
        }

        SSOToken token = SSOHelper.getToken(request);

        if (token == null){
            return null;
        }

        return token.getUid();
    }

    /**
     * 获得当前登录用户组
     * @return
     */
    protected String getCurrentGroupId(){
        if (request == null){
            return null;
        }

        try{
            return getSSOToken().getData().split("@")[1];
        }catch (Exception ex){
            return "";
        }
    }

    /**
     * 处理结果返回json
     * @param b
     * @return
     */
    protected WebJsonResult handleResult(boolean b){

        WebJsonResult webResult = new WebJsonResult();
        if (!b){
            webResult.setState(JSONMessageEnum.FAILD.getMessage());
            webResult.setMessage(JSONMessageEnum.OPER_FAILD.getMessage());
        }
        return webResult;
    }

    /**
     * 系统异常时
     * @return
     */
    public WebJsonResult returnException(){
        WebJsonResult webJsonResult = new WebJsonResult();
        webJsonResult.setState(JSONMessageEnum.FAILD.getMessage());
        webJsonResult.setMessage(JSONMessageEnum.SYSTEM_EXCEPTION.getMessage());
        return webJsonResult;
    }

    /**
     * 设置结果数据
     * @param map
     * @param list
     * @return
     */
    public WebJsonResult returnSuccess(Map map, List list){
        WebJsonResult webJsonResult = new WebJsonResult();
        webJsonResult.setList(list);
        webJsonResult.setMap(map);
        webJsonResult.setState(JSONMessageEnum.SUCCESS.getMessage());
        webJsonResult.setMessage(JSONMessageEnum.OPER_SUCCESS.getMessage());
        return webJsonResult;
    }

    /**
     * 参数为空时
     * @return
     */
    public WebJsonResult returnParamEmpty(){
        WebJsonResult webJsonResult = new WebJsonResult();
        webJsonResult.setState(JSONMessageEnum.FAILD.getMessage());
        webJsonResult.setMessage(JSONMessageEnum.PARAM_EMPTY.getMessage());
        return webJsonResult;
    }

    /**
     * 当未关联组时
     * @return
     */
    public WebJsonResult returnNotContactGroup(){
        WebJsonResult webJsonResult = new WebJsonResult();
        webJsonResult.setState(JSONMessageEnum.FAILD.getMessage());
        webJsonResult.setMessage(JSONMessageEnum.NOT_CONTACT_GROUP.getMessage());
        return webJsonResult;
    }

    /**
     * 当当前用户TOKEN为空时
     * @return
     */
    public WebJsonResult returnNotIegal(){
        WebJsonResult webJsonResult = new WebJsonResult();
        webJsonResult.setState(JSONMessageEnum.FAILD.getMessage());
        webJsonResult.setMessage(JSONMessageEnum.NOT_LEGAL.getMessage());
        return webJsonResult;
    }

    /**
     * 返回kisso token
     *
     * @return
     */
    protected SSOToken getSSOToken() {
        SSOToken tk = (SSOToken) SSOHelper.attrToken(this.request);
        if (tk == null) {
            logger.error("current user token is null ");
            throw new WebException("webException is ------> The user does not exist, please relogin.");
        } else {
            return tk;
        }
    }

    /**
     * 判断post请求
     *
     * @return
     */
    protected boolean isPost() {
        return HttpUtil.isPost(this.request);
    }

    /**
     * 判断get请求
     *
     * @return
     */
    protected boolean isGet() {
        return HttpUtil.isGet(this.request);
    }


    protected <T> Page<T> getPage() {
        return this.getPage(10);
    }

    protected <T> Page<T> getPage(int size) {
        int _size = size;
        int _index = 1;
        if (this.request.getParameter("_size") != null) {
            _size = Integer.parseInt(this.request.getParameter("_size"));
        }

        if (this.request.getParameter("_index") != null) {
            _index = Integer.parseInt(this.request.getParameter("_index"));
        }

        return new Page(_index, _size);
    }

    /**
     * 重定向方法
     *
     * @param url 控制器路劲字符串
     * @return
     */
    protected String redirectTo(String url) {
        StringBuffer rto = new StringBuffer("redirect:");
        rto.append(url);
        return rto.toString();
    }

    /**
     * 对象序列化json格式
     *
     * @param object 对象
     * @return
     */
    protected String toJson(Object object) {
        return JSON.toJSONString(object, new SerializerFeature[]{SerializerFeature.BrowserCompatible});
    }

    /**
     * 按指定格式序列化对象
     *
     * @param object 对像
     * @param format 格式
     * @return
     */
    protected String toJson(Object object, String format) {
        return format == null ? this.toJson(object) : JSON.toJSONStringWithDateFormat(object, format, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat});
    }
}
