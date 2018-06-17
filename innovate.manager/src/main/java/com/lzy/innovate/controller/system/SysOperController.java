package com.lzy.innovate.controller.system;

import com.baomidou.kisso.SSOHelper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lzy.innovate.controller.base.BaseController;
import com.lzy.innovate.controller.common.Pagin;
import com.lzy.innovate.controller.common.ParseFrontParamtHelper;
import com.lzy.innovate.controller.common.WebJsonResult;
import com.lzy.innovate.dubbo.system.*;
import com.lzy.innovate.entity.*;
import com.lzy.innovate.permission.OpertionPermission;
import com.lzy.innovate.utils.Sets;
import com.lzy.innovate.utils.date.DateUtil;
import com.lzy.innovate.utils.uuid.UUIDGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by lzy on 2017/3/15.
 */
@Controller
@RequestMapping("/oper")
public class SysOperController extends BaseController {

    @Autowired
    private ISysOperServiceSoa iSysOperServiceSoa;

    @Autowired
    private ISysGroupOperServiceSoa iSysGroupOperServiceSoa;

    @Autowired
    private ISysRoleOperServiceSoa iSysRoleOperServiceSoa;

    @Autowired
    private ISysRoleServiceSoa iSysRoleServiceSoa;

    @Autowired
    private ISysGroupServiceSoa iSysGroupServiceSoa;

    @Autowired
    private ISysMenuServiceSoa iSysMenuServiceSoa;

    /**
     * 去列表
     * @param uuid
     * @return
     */
    @RequestMapping(value = "toList/{uuid}" , method = {RequestMethod.GET})
    public String toList(Model model, @PathVariable("uuid")String uuid){

        model.addAttribute("menuId" , uuid);
        return "system/oper";
    }

    /**
     * 去编辑，权限控制，按钮级别
     * @param uuid
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:oper:edit:5")
    @RequestMapping(value = "/toEdit/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult toEdit(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }
        try{
            SysOper sysOper = iSysOperServiceSoa.selectById(uuid);
            WebJsonResult webJsonResult = new WebJsonResult();
            webJsonResult.setObject(sysOper);
            return webJsonResult;
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }


    }

    /**
     * 添加操作
     * @param sysOper
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:oper:add:5")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public WebJsonResult toAdd(SysOper sysOper){
        return edit(sysOper);
    }

    @ResponseBody
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    public WebJsonResult edit(SysOper sysOper){
        if (sysOper == null){
            return returnParamEmpty();
        }
        if (!isLegal() || StringUtils.isEmpty(getCurrentGroupId())){
            return returnNotIegal();
        }

        String uuid = sysOper.getUuid();
        if (StringUtils.isEmpty(uuid)){
            /**
             * add
             */
            try{
                uuid = UUIDGenerate.generateUuidByTime();
                sysOper.setUuid(uuid);
                sysOper.setCreateTime(DateUtil.getCurrentTime());
                sysOper.setUpdateTime(sysOper.getCreateTime());
                if (StringUtils.isEmpty(sysOper.getStatus())){
                    sysOper.setStatus("1"); //默认启用
                }

                /**
                 * 添加操作，同时往超级管理员角色/组，以及当前用户组添加
                 */
                List<SysGroupOper> sysGroupOpers = Sets.list();
                SysGroupOper sysGroupOper = null;
                sysGroupOper = new SysGroupOper();
                sysGroupOper.setUuid(UUIDGenerate.generateUuidByTime());
                String currentGroupId = getCurrentGroupId();
                sysGroupOper.setGroupUuid(getCurrentGroupId());
                sysGroupOper.setOperUuid(uuid);
                sysGroupOpers.add(sysGroupOper);
                if (!"1".equals(currentGroupId)){
                    sysGroupOper = new SysGroupOper();
                    sysGroupOper.setUuid(UUIDGenerate.generateUuidByTime());
                    sysGroupOper.setOperUuid(uuid);
                    sysGroupOper.setGroupUuid("1");
                    sysGroupOpers.add(sysGroupOper);
                }
                iSysGroupOperServiceSoa.insertBatch(sysGroupOpers);

                SysRoleOper sysRoleOper = new SysRoleOper();
                sysRoleOper.setUuid(UUIDGenerate.generateUuidByTime());
                sysRoleOper.setOperUuid(uuid);
                sysRoleOper.setRoleUuid("1");
                iSysRoleOperServiceSoa.insert(sysRoleOper);


                iSysOperServiceSoa.insert(sysOper);
                return handleResult(true);
            }catch (Exception ex){
                logger.error(ex.toString());
                return returnException();
            }
        }else{
            /**
             * edit
             */
            sysOper.setUpdateTime(DateUtil.getCurrentTime());
            isSuccess = iSysOperServiceSoa.updateById(sysOper);
            return handleResult(isSuccess);
        }
    }

    /**
     * 删除角色，按钮级别权限控制
     * @param ids
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:oper:del:5")
    @RequestMapping(value = "/del/{ids}" , method = {RequestMethod.POST})
    public WebJsonResult del(@PathVariable("ids")String ids){

        if (StringUtils.isEmpty(ids)){
            return returnParamEmpty();
        }

        List<String> idsList = Arrays.asList(ids.split(","));
        /**
         * 删除操作，同时删除所有角色，所有组关联
         */
        try{
            iSysRoleOperServiceSoa.delete(new EntityWrapper<SysRoleOper>().in("oper_uuid", idsList));
            iSysGroupOperServiceSoa.delete(new EntityWrapper<SysGroupOper>().in("oper_uuid", idsList));
            iSysOperServiceSoa.delete(new EntityWrapper<SysOper>().in("uuid", idsList));
            return handleResult(true);
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }

    }

    /**
     * 去关联父菜单，权限控制，按钮级别
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:oper:toContactMenu:5")
    @RequestMapping(value = "/toContactMenu/{ids}", method = {RequestMethod.POST})
    public WebJsonResult toContactMenu(@PathVariable("ids")String ids){
        if (StringUtils.isEmpty(ids)){
            return returnParamEmpty();
        }
        SysOper sysOper = iSysOperServiceSoa.selectById(ids);
        WebJsonResult webJsonResult = new WebJsonResult();
        webJsonResult.setObject(sysOper);
        return webJsonResult;
    }

    /**
     * 关联父菜单
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/contactMenu/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult contactMenu(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }
        String menuId = request.getParameter("menuId");
        SysOper sysOper = new SysOper();
        sysOper.setUuid(uuid);
        sysOper.setMenuId(menuId);
        sysOper.setUpdateTime(DateUtil.getCurrentTime());
        isSuccess = iSysOperServiceSoa.updateById(sysOper);
        return handleResult(isSuccess);
    }

    /**
     * 按钮详情
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/detail/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult detail(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }

        SysOper sysOper = iSysOperServiceSoa.selectById(uuid);
        SysMenu sysMenu = iSysMenuServiceSoa.selectById(sysOper.getMenuId());
        List<Object> sysRoleIds = iSysRoleOperServiceSoa.selectObjs(new EntityWrapper<SysRoleOper>().eq("oper_uuid",uuid).setSqlSelect(" role_uuid"));
        List<Object> sysGroupIds = iSysGroupOperServiceSoa.selectObjs(new EntityWrapper<SysGroupOper>().setSqlSelect(" group_uuid").eq("oper_uuid",uuid));
        List<String> temp1 = Sets.list();
        List<String> temp2 = Sets.list();
        for (Object o : sysRoleIds){
            temp1.add(o.toString());
        }
        for (Object o : sysGroupIds){
            temp2.add(o.toString());
        }
        List<SysRole> sysRoles = iSysRoleServiceSoa.selectBatchIds(temp1);
        List<SysGroup> sysGroups = iSysGroupServiceSoa.selectBatchIds(temp2);
        Map<String, Object> map = Sets.map();
        map.put("roles",sysRoles);
        map.put("groups", sysGroups);
        map.put("menu", sysMenu);
        WebJsonResult webJsonResult = new WebJsonResult();
        webJsonResult.setMap(map);
        return webJsonResult;
    }

    /**
     * 操作列表检索，可分页，可排序
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findOperListByPagin" , produces = "application/json", method = {RequestMethod.POST})
    public Map<String, Object> findOperListByPagin(){
        Map<String, Object> params = Sets.map();
        String userId = getCurrentUserId();

        if (StringUtils.isEmpty(userId)) {
            return params;
        }

        Pagin<SysOper> page = new Pagin();
        ParseFrontParamtHelper helper = new ParseFrontParamtHelper(page);
        helper.handleSearchParam(request);
        helper.handlePaginAndSort(request);
        params.put("userId" , userId);
        params.put("page" , page);

        List<SysOper> sysOpers = iSysOperServiceSoa.findOpersByPagin(params);
        page.setAll(true);
        page.setSort(false);
        params.put("page", page);
        int count = iSysOperServiceSoa.findOperCount(params);
        page.setTotal(count);
        page.setRows(sysOpers);

        params = helper.handleResult(page);
        return params;
    }

    /**
     * 通过菜单ID，查出当前用户的某个菜单下的所有操作
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findOpersByMenuId", produces = "application/json", method = {RequestMethod.POST})
    public List<SysOper> findOpersByMenuId(HttpServletRequest request){

        List<SysOper> sysOpers = Sets.list();
        String userId = SSOHelper.getToken(request).getUid();
        String menuId = request.getParameter("menuId");

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(menuId)){
            return sysOpers;
        }
        sysOpers = iSysOperServiceSoa.findOpersByMenuId(userId , menuId);

        return sysOpers;
    }
}
