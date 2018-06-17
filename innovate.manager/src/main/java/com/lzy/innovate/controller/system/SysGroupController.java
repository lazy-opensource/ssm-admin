package com.lzy.innovate.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lzy.innovate.controller.base.BaseController;
import com.lzy.innovate.controller.common.Pagin;
import com.lzy.innovate.controller.common.ParseFrontParamtHelper;
import com.lzy.innovate.controller.common.WebJsonResult;
import com.lzy.innovate.controller.enums.SysUserStatusEnum;
import com.lzy.innovate.dubbo.system.*;
import com.lzy.innovate.entity.*;
import com.lzy.innovate.permission.OpertionPermission;
import com.lzy.innovate.utils.Sets;
import com.lzy.innovate.utils.date.DateUtil;
import com.lzy.innovate.utils.tree.BootstrapTreeView;
import com.lzy.innovate.utils.uuid.UUIDGenerate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lzy on 2017/3/16.
 */
@Controller
@RequestMapping("/group")
public class SysGroupController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger("innovate_web_log");

    @Autowired
    private ISysGroupServiceSoa iSysGroupServiceSoa;

    @Autowired
    private ISysGroupMenuServiceSoa iSysGroupMenuServiceSoa;

    @Autowired
    private ISysGroupOperServiceSoa iSysGroupOperServiceSoa;

    @Autowired
    private ISysRoleGroupServiceSoa iSysRoleGroupServiceSoa;

    @Autowired
    private ISysUserServiceSoa iSysUserServiceSoa;

    @Autowired
    private ISysMenuServiceSoa iSysMenuServiceSoa;

    @Autowired
    private ISysOperServiceSoa iSysOperServiceSoa;

    @Autowired
    private ISysRoleUserServiceSoa iSysRoleUserServiceSoa;

    /**
     * 去列表
     * @param model
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/toList/{uuid}" , method = {RequestMethod.GET})
    public String toList(Model model, @PathVariable("uuid")String uuid){
        model.addAttribute("menuId" , uuid);
        return "system/group";
    }

    /**
     * 去添加, 按钮级别权限控制
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:group:add:6")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public WebJsonResult add(SysGroup sysGroup){
        return edit(sysGroup);
    }

    /**
     * 编辑，包含新增和修改
     * @param sysGroup
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    public WebJsonResult edit(SysGroup sysGroup){
        if (sysGroup == null){
            return returnParamEmpty();
        }

        String uuid = sysGroup.getUuid();
        if (StringUtils.isEmpty(uuid)){
            try{
                /**
                 * 添加
                 * 添加组默认将根菜单添加到组中
                 */
                sysGroup.setUuid(UUIDGenerate.generateUuidByTime());
                sysGroup.setCreateTime(DateUtil.getCurrentTime());
                sysGroup.setUpdateTime(sysGroup.getCreateTime());
                iSysGroupServiceSoa.insert(sysGroup);

                return handleResult(true);
            }catch (Exception ex){
                logger.error(ex.toString());
                return returnException();
            }
        }else{
            /**
             * 编辑
             */
            sysGroup.setUpdateTime(DateUtil.getCurrentTime());
            isSuccess = iSysGroupServiceSoa.updateById(sysGroup);
            return handleResult(isSuccess);
        }
    }

    /**
     * 去编辑，权限控制
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:group:edit:6")
    @RequestMapping(value  ="/toEdit/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult toEdit(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }
        SysGroup sysGroup = iSysGroupServiceSoa.selectById(uuid);
        WebJsonResult webJsonResult = new WebJsonResult();
        webJsonResult.setObject(sysGroup);
        return webJsonResult;
    }

    /**
     * 删除用户组，按钮级别权限控制
     * @param ids
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:group:del:6")
    @RequestMapping(value = "/del/{ids}", method = {RequestMethod.POST})
    public WebJsonResult del(@PathVariable("ids")String ids){
        if (StringUtils.isEmpty(ids)){
            return returnParamEmpty();
        }
        //是否删除用户数据
        String isDelUser = request.getParameter("isDelUser");
        String[] idsArr = ids.split(",");
        try{
            /**
             * 删除组的同时将会将组的操作，菜单，角色，或者用户关联数据一并删除
             */
            iSysGroupMenuServiceSoa.delete(new EntityWrapper<SysGroupMenu>().in("group_uuid", idsArr));
            iSysGroupOperServiceSoa.delete(new EntityWrapper<SysGroupOper>().in("group_uuid", idsArr));
            iSysRoleGroupServiceSoa.delete(new EntityWrapper<SysRoleGroup>().in("group_uuid", idsArr));
            iSysGroupServiceSoa.delete(new EntityWrapper<SysGroup>().in("uuid", idsArr));
            if ("1".equals(isDelUser)){
                /**
                 * 将组下的用户一并删除，并且删除用户的角色关联数据
                 */
                List<Object> userIds = iSysUserServiceSoa.selectObjs(new EntityWrapper<SysUser>().in("groupId", idsArr));
                List<String> userIdsStr = objectListConversionStringList(userIds);
                if (userIdsStr != null && userIdsStr.size() > 0){
                    iSysRoleUserServiceSoa.delete(new EntityWrapper<SysRoleUser>().in("user_uuid", userIdsStr));
                }
                iSysUserServiceSoa.delete(new EntityWrapper<SysUser>().in("groupId", idsArr));
            }else{
                SysUser sysUser = new SysUser();
                sysUser.setStatus(SysUserStatusEnum.ILLEGAL.getStatus());
                iSysUserServiceSoa.update(sysUser, new EntityWrapper<SysUser>().in("groupId", idsArr));
            }
            return handleResult(true);
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }
    }

    /**
     * 去关联菜单，按钮级别权限控制
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:group:toContactMenu:6")
    @RequestMapping(value = "/toContactMenu/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult toContactMenu(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }
        if (!isLegal() || StringUtils.isEmpty(getCurrentGroupId())){
            return returnNotIegal();
        }

        /**
         * 拿当前登录用户所在组的菜单权限集合
         */
        try{
            Map<String, Object> result = Sets.map();
            String currentGroupId = getCurrentGroupId();
            List<BootstrapTreeView> canContactMenus = Sets.list();
            canContactMenus.add(generatorTree(findMenusByGroupId(currentGroupId)));
            result.put("canContactMenus", canContactMenus);
            result.put("contactMenus", findMenusByGroupId(uuid));
            WebJsonResult webJsonResult = new WebJsonResult();
            webJsonResult.setMap(result);
            return webJsonResult;
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }
    }

    /**
     * 通过组Id查菜单
     * @param id
     * @return
     */
    private List<SysMenu> findMenusByGroupId(String id){
        if (StringUtils.isEmpty(id)){
            return new ArrayList<SysMenu>();
        }
        List<Object> menuIds = iSysGroupMenuServiceSoa.selectObjs(new EntityWrapper<SysGroupMenu>().eq("group_uuid", id).setSqlSelect("menu_uuid"));
        List<String> menuIdsStr = objectListConversionStringList(menuIds);
        List<SysMenu> menus = Sets.list();
        if (menuIdsStr != null && menuIdsStr.size() > 0){
            menus = iSysMenuServiceSoa.selectBatchIds(menuIdsStr);
        }
        return menus;
    }

    /**
     * 组关联菜单
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/contactMenu/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult contactMenu(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }
        String menuIds = request.getParameter("menuIds");

        /**
         * 关联菜单，先清除已经关联的菜单，再将新的菜单关联上去
         */
        try{
            iSysGroupMenuServiceSoa.delete(new EntityWrapper<SysGroupMenu>().eq("group_uuid", uuid));
            if (!StringUtils.isEmpty(menuIds)){
                String[] menuIdsArr = menuIds.split(",");
                SysGroupMenu sysGroupMenu = null;
                List<SysGroupMenu> sysGroupMenus = Sets.list();
                for (String s : menuIdsArr){
                    sysGroupMenu = new SysGroupMenu();
                    sysGroupMenu.setUuid(UUIDGenerate.generateUuidByTime());
                    sysGroupMenu.setGroupUuid(uuid);
                    sysGroupMenu.setMenuUuid(s);
                    sysGroupMenus.add(sysGroupMenu);
                }
                iSysGroupMenuServiceSoa.insertBatch(sysGroupMenus);
            }
            return handleResult(true);
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }
    }

    /**
     * 组去关联操作
     * @param uuid
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:group:toContactOper:6")
    @RequestMapping(value = "/toContactOper/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult toContactOper(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }
        if (!isLegal() || StringUtils.isEmpty(getCurrentGroupId())){
            return returnNotIegal();
        }
        try{
            /**
             * 关联操作，查出当前用户所在组的所有操作和已经关联的操作
             */
            Map<String, Object> result = Sets.map();
            List<Object> canContactOperIds = iSysGroupOperServiceSoa.selectObjs(new EntityWrapper<SysGroupOper>().eq("group_uuid",getCurrentUserId()).setSqlSelect("oper_uuid"));
            List<String> canContactOperIdsArr = objectListConversionStringList(canContactOperIds);
            Map<String, List<SysOper>> canContactOpers = Sets.map();
            if (canContactOperIdsArr != null && canContactOperIdsArr.size() > 0){
                canContactOpers = iSysOperServiceSoa.generateOperBelongMenuMap(canContactOperIdsArr);
            }
            result.put("canContactOpers", canContactOpers);
            result.put("contactOpers", findOpersByGroupId(uuid));
            WebJsonResult webJsonResult = new WebJsonResult();
            webJsonResult.setMap(result);
            return webJsonResult;
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }
    }

    /**
     * 通过组Id查操作
     * @param id
     * @return
     */
    private List<SysOper> findOpersByGroupId(String id){
        if (StringUtils.isEmpty(id)){
            return new ArrayList<SysOper>();
        }
        List<Object> operIds = iSysGroupOperServiceSoa.selectObjs(new EntityWrapper<SysGroupOper>().eq("group_uuid", id).setSqlSelect("oper_uuid"));
        List<String> operIdsStr = objectListConversionStringList(operIds);
        List<SysOper> opers = Sets.list();
        if (operIdsStr != null && operIdsStr.size() > 0){
            opers = iSysOperServiceSoa.selectBatchIds(operIdsStr);
        }
        return opers;
    }

    /**
     * 组关联操作
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/contactOper/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult contactOper(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }
        try{
            /**
             * 删除已经关联的操作，关联新的操作
             */
            iSysGroupOperServiceSoa.delete(new EntityWrapper<SysGroupOper>().eq("group_uuid", uuid));
            String operIds = request.getParameter("operIds");
            if (!StringUtils.isEmpty(operIds)){
                String[] operIdsArr = operIds.split(",");
                List<SysGroupOper> sysGroupOpers = Sets.list();
                SysGroupOper sysGroupOper = null;
                for (String s : operIdsArr){
                    sysGroupOper = new SysGroupOper();
                    sysGroupOper.setUuid(UUIDGenerate.generateUuidByTime());
                    sysGroupOper.setGroupUuid(uuid);
                    sysGroupOper.setOperUuid(s);
                    sysGroupOpers.add(sysGroupOper);
                }
                iSysGroupOperServiceSoa.insertBatch(sysGroupOpers);
            }
            return handleResult(true);
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }
    }

    /**
     * 检索列表，总数
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findGroupListByPagin" , produces = "application/json", method = {RequestMethod.POST})
    public Map<String, Object> findGroupListByPagin(){
        Map<String, Object> map = Sets.map();
        Pagin<SysGroup> pagin = new Pagin();
        ParseFrontParamtHelper helper = new ParseFrontParamtHelper(pagin);
        helper.handleSearchParam(request);
        helper.handlePaginAndSort(request);

        map.put("page" , pagin);
        List<SysGroup> sysGroupList = iSysGroupServiceSoa.findGroupListByPagin(map);
        pagin.setAll(true);
        pagin.setSort(false);
        map.put("page" , pagin);
        int count = iSysGroupServiceSoa.findGroupCount(map);
        pagin.setRows(sysGroupList);
        pagin.setTotal(count);

        map = helper.handleResult(pagin);
        return map;
    }
}
