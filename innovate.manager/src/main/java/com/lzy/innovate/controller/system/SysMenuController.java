package com.lzy.innovate.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lzy.innovate.controller.base.BaseController;
import com.lzy.innovate.controller.common.Pagin;
import com.lzy.innovate.controller.common.ParseFrontParamtHelper;
import com.lzy.innovate.controller.common.WebJsonResult;
import com.lzy.innovate.dubbo.system.*;
import com.lzy.innovate.entity.*;
import com.lzy.innovate.permission.MenuPermission;
import com.lzy.innovate.permission.OpertionPermission;
import com.lzy.innovate.service.ISysGroupMenuService;
import com.lzy.innovate.utils.Sets;
import com.lzy.innovate.utils.date.DateUtil;
import com.lzy.innovate.utils.tree.BootstrapTreeView;
import com.lzy.innovate.utils.uuid.UUIDGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by lzy on 2017/3/7.
 * 菜单控制器
 */
@Controller
@RequestMapping("/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private ISysMenuServiceSoa iSysMenuServiceSoa;

    @Autowired
    private ISysGroupMenuService iSysGroupMenuService;

    @Autowired
    private ISysRoleMenuServiceSoa iSysRoleMenuServiceSoa;

    @Autowired
    private ISysOperServiceSoa iSysOperServiceSoa;

    @Autowired
    private ISysGroupOperServiceSoa iSysGroupOperServiceSoa;

    @Autowired
    private ISysRoleOperServiceSoa iSysRoleOperServiceSoa;

    /**
     * 去菜单列表，权限控制
     * @return
     */
    @MenuPermission(code = "menu_sys:menu:3")
    @RequestMapping(value = "/toList/{uuid}" , method = RequestMethod.GET)
    public String toList(@PathVariable("uuid")String uuid, Model model){

        model.addAttribute("menuId" , uuid);
        return "system/menu";
    }

    /**
     * 添加菜单，权限控制，按钮级别
     * @param sysMenu
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:menu:add:4")
    @RequestMapping(value = "/add" , method = {RequestMethod.POST})
    public WebJsonResult add(SysMenu sysMenu){
        return edit(sysMenu);
    }

    /**
     * 编辑
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/edit" , method = {RequestMethod.POST})
    public WebJsonResult edit(SysMenu sysMenu){
        String currentGroupId = getCurrentGroupId();
        if (!isLegal() || currentGroupId == null){
            return returnNotIegal();
        }

        String uuid = sysMenu.getUuid();
        /**
         * add
         */
        if (StringUtils.isEmpty(uuid)){
            if (StringUtils.isEmpty(sysMenu.getStatus())){
                /**
                 * 默认启用
                 */
                sysMenu.setStatus("1");
            }
            try{
                String newUuid = UUIDGenerate.generateUuidByTime();
                sysMenu.setUuid(newUuid);
                //默认根级
                sysMenu.setParentId("0");
                sysMenu.setCreateTime(DateUtil.getCurrentTime());
                sysMenu.setUpdateTime(sysMenu.getCreateTime());
                isSuccess = iSysMenuServiceSoa.insert(sysMenu);

                /**
                 * 所有新增菜单都关联到超级管理员角色和组以及当前用户组
                 */
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setUuid(UUIDGenerate.generateUuidByTime());
                sysRoleMenu.setRoleUuid("1");
                sysRoleMenu.setMenuUuid(newUuid);
                isSuccess = iSysRoleMenuServiceSoa.insert(sysRoleMenu);

                SysGroupMenu sysGroupMenu = null;
                List<SysGroupMenu> sysGroupMenus = Sets.list();
                sysGroupMenu = new SysGroupMenu();
                sysGroupMenu.setUuid(UUIDGenerate.generateUuidByTime());
                sysGroupMenu.setGroupUuid(currentGroupId);
                sysGroupMenu.setMenuUuid(newUuid);
                sysGroupMenus.add(sysGroupMenu);
                if ("1".equals(getCurrentGroupId())){
                    /**
                     * 防止数据重复
                     */
                    sysGroupMenu = new SysGroupMenu();
                    sysGroupMenu.setUuid(UUIDGenerate.generateUuidByTime());
                    sysGroupMenu.setMenuUuid(newUuid);
                    sysGroupMenu.setGroupUuid("1");
                }
                isSuccess = iSysGroupMenuService.insertBatch(sysGroupMenus);
                return handleResult(true);
            }catch (Exception ex){
                logger.error(ex.toString());
                return returnException();
            }
        }else{
            /**
             * eidt
             */
            isSuccess = iSysMenuServiceSoa.updateById(sysMenu);
            return handleResult(isSuccess);
        }
    }

    /**
     * 去编辑
     * @return
     */
    @OpertionPermission(code = "oper_sys:menu:edit:4")
    @ResponseBody
    @RequestMapping(value = "/toEdit/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult toEdit(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }

        try{
            SysMenu sysMenu = iSysMenuServiceSoa.selectById(uuid);
            WebJsonResult webJsonResult = new WebJsonResult();
            webJsonResult.setObject(sysMenu);
            return webJsonResult;
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }

    }

    /**
     * 删除菜单，可批量，权限控制，按钮级别
     * @return
     */
    @OpertionPermission(code = "oper_sys:menu:del:4")
    @ResponseBody
    @RequestMapping(value = "/del/{ids}", method = {RequestMethod.POST})
    public WebJsonResult del(@PathVariable("ids")String ids){
        if (StringUtils.isEmpty(ids)){
            return returnParamEmpty();
        }
        /**
         * 删除菜单。菜单下子菜单。超级管理员角色关联，组关联,操作关联等所有数据
         */
        try{
            List<String> idsList = Arrays.asList(ids.split(","));
            iSysMenuServiceSoa.delete(new EntityWrapper<SysMenu>().in("uuid", idsList));
            iSysGroupMenuService.delete(new EntityWrapper<SysGroupMenu>().in("menu_uuid", idsList));
            iSysRoleMenuServiceSoa.delete(new EntityWrapper<SysRoleMenu>().in("menu_uuid", idsList));
            List<Object> operIds = iSysOperServiceSoa.selectObjs(new EntityWrapper<SysOper>().in("menuId", idsList));
            List<String> operIdsStr = objectListConversionStringList(operIds);
            if (operIdsStr != null && operIdsStr.size() > 0){
                /**
                 * 执行删除操作逻辑，删除操作，组操作，角色操作关联数据
                 */
                iSysGroupOperServiceSoa.delete(new EntityWrapper<SysGroupOper>().in("oper_uuid", operIdsStr));
                iSysRoleOperServiceSoa.delete(new EntityWrapper<SysRoleOper>().in("oper_uuid", operIdsStr));
                iSysOperServiceSoa.deleteBatchIds(operIdsStr);
            }
            return handleResult(true);
        }catch (Exception ex){
            logger.error(ex.toString());
            return handleResult(false);
        }
    }

    /**
     * 关联父菜单，权限控制，按钮级别
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:menu:contactParentMenu:4")
    @RequestMapping(value = "/contactParentMenu/{ids}/{pids}", method = {RequestMethod.POST})
    public WebJsonResult contactParentMenu(@PathVariable("ids")String ids, @PathVariable("pids")String pids){
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(pids);
        sysMenu.setUpdateTime(DateUtil.getCurrentTime());
        try{
            iSysMenuServiceSoa.update(sysMenu, new EntityWrapper<SysMenu>().in("uuid", Arrays.asList(ids.split(","))));
            return handleResult(true);
        }catch (Exception ex){
            logger.error(ex.toString());
            return handleResult(false);
        }

    }

    /**
     * 菜单树列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findMenuList" , method = RequestMethod.POST)
    public WebJsonResult findMenuList(){
        WebJsonResult webJsonResult = new WebJsonResult();
        if (!isLegal()){
            return returnNotIegal();
        }

        String isMenu = request.getParameter("isMenu");
        List<SysMenu> sysMenus = iSysMenuServiceSoa.findMenusByUserId(getCurrentUserId());
        if (!StringUtils.isEmpty(isMenu)){
            Map<String, Object> map = Sets.map();
            for (SysMenu m : sysMenus){
                map.put(m.getUuid(), m.getName());
            }
            webJsonResult.setMap(map);
        }
        BootstrapTreeView tree = generatorTree(sysMenus);
        List<Object> list = Sets.list();
        list.add(tree);
        webJsonResult.setList(list);
        return webJsonResult;
    }

    /**
     * 条件查找
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findMenuListByCondition" , method = {RequestMethod.POST})
    public WebJsonResult findMenuListByCondition(){
        if (!isLegal()){
            return returnNotIegal();
        }
        Pagin<SysMenu> page = new Pagin();
        page.setAll(true);
        page.setSort(false);
        ParseFrontParamtHelper helper = new ParseFrontParamtHelper(page);
        helper.handleSearchParam(request);

        Map<String, Object> params = Sets.map();
        params.put("page" , page);
        params.put("userId", getCurrentUserId());
        List<SysMenu> sysMenus = iSysMenuServiceSoa.findMenusByCondition(params);

        WebJsonResult webJsonResult = new WebJsonResult();
        webJsonResult.setObject(sysMenus);
        return webJsonResult;

    }

    /**
     * 获得导航数据，不做权限控制
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findOneMenusByUserId" , produces = "application/json" , method = RequestMethod.POST)
    public Map<String,Object> findOneMenusByUserId(){

        String userId = getSSOToken().getUid();
        String temp = getSSOToken().getData();
        String username = temp.split("@")[0];
        List<SysMenu> data = iSysMenuServiceSoa.findOneMenusByUserId(userId);

        Map<String,Object> result = Sets.map();
        result.put("username" , username);
        result.put("rows" , data);
        return result;
    }

    /**
     * 获取父菜单下的子菜单，权限控制
     * @return
     */
    @ResponseBody
    @MenuPermission(code = "menu_sys:menu:3")
    @RequestMapping(value = "/findChildMenusByParentId" , method = {RequestMethod.POST})
    public Map<String, Object> findChildMenusByParentId(){

        String userId = getSSOToken().getUid();
        String temp = getSSOToken().getData();
        String username = temp.split("@")[0];
        List<SysMenu> data = iSysMenuServiceSoa.findChildMenusByParentId(userId,request.getParameter("parentId"));

        Map<String,Object> result = Sets.map();
        result.put("rows" , data);
        result.put("parentName" , request.getParameter("parentName"));
        return result;
    }

    /**
     * 加载导航条控制器，不做权限控制
     * @return
     */
    @RequestMapping(value = "/loadNavigation" ,method = RequestMethod.GET)
    public String findNavigation(){

        return "/common/nav";
    }


}
