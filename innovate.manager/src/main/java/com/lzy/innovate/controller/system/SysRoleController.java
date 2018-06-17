package com.lzy.innovate.controller.system;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lzy on 2017/3/16.
 */
@Controller
@RequestMapping("/role")
public class SysRoleController extends BaseController {

    @Autowired
    private ISysRoleServiceSoa iSysRoleServiceSoa;

    @Autowired
    private ISysRoleGroupServiceSoa iSysRoleGroupServiceSoa;

    @Autowired
    private ISysRoleMenuServiceSoa iSysRoleMenuServiceSoa;

    @Autowired
    private ISysRoleOperServiceSoa iSysRoleOperServiceSoa;

    @Autowired
    private ISysGroupOperServiceSoa iSysGroupOperServiceSoa;

    @Autowired
    private ISysGroupMenuServiceSoa iSysGroupMenuServiceSoa;

    @Autowired
    private ISysOperServiceSoa iSysOperServiceSoa;

    @Autowired
    private ISysMenuServiceSoa iSysMenuServiceSoa;

    @Autowired
    private ISysGroupServiceSoa iSysGroupServiceSoa;

    @Autowired
    private ISysRoleUserServiceSoa iSysRoleUserServiceSoa;

    /**
     * 去列表
     * @param model
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/toList/{uuid}", method = {RequestMethod.GET})
    public String toList(Model model, @PathVariable("uuid")String uuid){
        model.addAttribute("menuId", uuid);
        return "system/role";
    }

    /**
     * 添加角色，权限控制，按钮级别
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:role:add:3")
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public WebJsonResult add(SysRole sysRole){
        if (sysRole == null){
            return returnParamEmpty();
        }
        return edit(sysRole);
    }

    /**
     * 去编辑，权限控制，按钮级别
     * @param uuid
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:role:edit:3")
    @RequestMapping(value = "/toEdit/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult toEdit(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }

        SysRole sysRole = iSysRoleServiceSoa.selectById(uuid);
        WebJsonResult webJsonResult = new WebJsonResult();
        webJsonResult.setObject(sysRole);
        return webJsonResult;
    }

    /**
     * 编辑角色，权限控制，按钮级别
     * @param sysRole
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    public WebJsonResult edit(SysRole sysRole){
        if (sysRole == null){
            return returnParamEmpty();
        }
        if (!isLegal()){
            return returnNotIegal();
        }
        String uuid = sysRole.getUuid();

        /**
         * add
         * 新增角色，超级用户组添加，当前用户组添加.所有新增的角色默认拥有菜单列表这个顶级根菜单
         */
        if (StringUtils.isEmpty(uuid)){
            try{
                uuid = UUIDGenerate.generateUuidByTime();
                sysRole.setUuid(uuid);
                sysRole.setCreateTime(DateUtil.getCurrentTime());
                sysRole.setUpdateTime(sysRole.getCreateTime());
                if (StringUtils.isEmpty(sysRole.getStatus())){
                    /**
                     * 默认启用
                     */
                    sysRole.setStatus("1");
                }
                List<SysRoleGroup> sysRoleGroupList = Sets.list();
                SysRoleGroup sysRoleGroup = new SysRoleGroup();
                sysRoleGroup.setUuid(UUIDGenerate.generateUuidByTime());
                /**
                 * 默认加入当前用户所在组
                 */
                sysRoleGroup.setGroupUuid(getCurrentGroupId());
                sysRoleGroup.setRoleUuid(uuid);
                sysRoleGroupList.add(sysRoleGroup);

                /**
                 * 批量新增
                 */
                iSysRoleGroupServiceSoa.insertBatch(sysRoleGroupList);
                iSysRoleServiceSoa.insert(sysRole);
                return handleResult(true);
            }catch (Exception ex){
                logger.error(ex.toString());
                return returnException();
            }

        }else{
            /**
             * edit
             */
            sysRole.setUpdateTime(DateUtil.getCurrentTime());
            isSuccess = iSysRoleServiceSoa.updateById(sysRole);
            return handleResult(isSuccess);
        }
    }

    /**
     * 删除角色，权限控制，按钮级别
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:role:del:3")
    @RequestMapping(value = "/del/{ids}", method = {RequestMethod.POST})
    public WebJsonResult del(@PathVariable("ids")String ids){
        if (StringUtils.isEmpty(ids)){
            return returnParamEmpty();
        }
        String[] idsArr = ids.split(",");
        /**
         * 删除角色，将会删除该角色关联的操作，组，菜单，以及用户
         */
        try{
            iSysRoleGroupServiceSoa.delete(new EntityWrapper<SysRoleGroup>().in("role_uuid", idsArr));
            iSysRoleMenuServiceSoa.delete(new EntityWrapper<SysRoleMenu>().in("role_uuid", idsArr));
            iSysRoleOperServiceSoa.delete(new EntityWrapper<SysRoleOper>().in("role_uuid", idsArr));
            iSysRoleUserServiceSoa.delete(new EntityWrapper<SysRoleUser>().in("role_uuid", idsArr));
            iSysRoleServiceSoa.delete(new EntityWrapper<SysRole>().in("uuid", idsArr));
            return handleResult(true);
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }
    }

    /**
     * 查角色的关联组Ids
     * @param roleId
     * @return
     */
    private List<Object> findGroupIdsByRoleId(String roleId){
        if (StringUtils.isEmpty(roleId)){
            return new ArrayList<Object>();
        }
        return iSysRoleGroupServiceSoa.selectObjs(new EntityWrapper<SysRoleGroup>().eq("role_uuid", roleId).setSqlSelect("group_uuid"));
    }

    /**
     * 通过操作Ids查操作列表
     * @param ids
     * @return
     */
    private List<SysOper> findOpersByIds(List<Object> ids){
        List<String> list = Sets.list();
        List<SysOper> contactOpers = Sets.list();
        if (ids != null && ids.size() > 0){
            list = objectListConversionStringList(ids);
            contactOpers = iSysOperServiceSoa.selectBatchIds(list);
        }
        return contactOpers;
    }

    /**
     * 去关联操作，权限控制，按钮级别
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:role:toContactOper:3")
    @RequestMapping(value = "/toContactOper/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult toContactOper(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }
        try{
            /**
             * 角色关联操作，先获取该角色已经关联的操作，然后获取可以关联的操作，前提是需要先为角色关联组
             */
            List<Object> contactGroupIds = findGroupIdsByRoleId(uuid);
            /**
             * 当未关联组时，不能进行任何的操作关联
             */
            if (contactGroupIds == null && contactGroupIds.size() < 1){
                return returnNotContactGroup();
            }
            /**
             * 去关联操作，需要查出该角色已经关联的操作，以及可以关联的操作，还要告诉操作者每个操作对应的菜单
             */
            Map<String, Object> result = Sets.map();
            List<Object> canContactOperIds = iSysGroupOperServiceSoa.selectObjs(new EntityWrapper<SysGroupOper>().in("group_uuid",contactGroupIds).setSqlSelect("oper_uuid"));
            List<String> canContactOperIdsArr = objectListConversionStringList(canContactOperIds);
            Map<String, List<SysOper>> canContactOpers = Sets.map();
            if (canContactOperIdsArr != null && canContactOperIdsArr.size() > 0){
                canContactOpers = iSysOperServiceSoa.generateOperBelongMenuMap(canContactOperIdsArr);
            }
            result.put("canContactOpers", canContactOpers);
            /**
             * 查出已经关联的操作
             */
            List<Object> contactOperIds = iSysRoleOperServiceSoa.selectObjs(new EntityWrapper<SysRoleOper>().eq("role_uuid", uuid).setSqlSelect("oper_uuid"));
            List<SysOper> contactOpers = findOpersByIds(contactOperIds);
            result.put("contactOpers", contactOpers);
            WebJsonResult webJsonResult = new WebJsonResult();
            webJsonResult.setMap(result);
            return webJsonResult;
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }
    }

    /**
     * 角色关联操作
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/contactOper/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult contactOper(@PathVariable("uuid")String uuid){
        String operIds = request.getParameter("operIds");
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }

        /**
         * 关联操作时，先清除原来关联的操作(注意这里不区分哪个组的)，之后重新关联选择的操作
         */
        try{
            iSysRoleOperServiceSoa.delete(new EntityWrapper<SysRoleOper>().eq("role_uuid",uuid));
            if (!StringUtils.isEmpty(operIds)){
                String[] operIdsArr = operIds.split(",");
                SysRoleOper sysRoleOper = null;
                List<SysRoleOper> sysRoleOpers = Sets.list();
                for (String s : operIdsArr){
                    sysRoleOper = new SysRoleOper();
                    sysRoleOper.setUuid(UUIDGenerate.generateUuidByTime());
                    sysRoleOper.setOperUuid(s);
                    sysRoleOper.setRoleUuid(uuid);
                    sysRoleOpers.add(sysRoleOper);
                }
                iSysRoleOperServiceSoa.insertBatch(sysRoleOpers);
            }
            return handleResult(true);
        }catch (Exception ex){
            logger.error(ex.toString());
            return handleResult(false);
        }

    }

    /**
     * 角色去关联菜单，权限控制，按钮级别
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:role:toContactMenu:3")
    @RequestMapping(value = "/toContactMenu/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult toContactMenu(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(getCurrentGroupId())){
            return returnParamEmpty();
        }
        try{
            /**
             * 关联菜单时，需要先将该角色已经关联的菜单查出，以及查出该角色可以关联的菜单（角色所在组的菜单）
             */
            Map<String, Object> result = Sets.map();
            List<Object> roleContactGroupIds = findGroupIdsByRoleId(uuid);
            if (roleContactGroupIds == null || roleContactGroupIds.size() < 1){
                return returnNotContactGroup();
            }
            /**
             * 可关联
             */
            List<Object> groupContactMenuIds = iSysGroupMenuServiceSoa.selectObjs(new EntityWrapper<SysGroupMenu>().in("group_uuid", roleContactGroupIds).setSqlSelect("menu_uuid"));
            List<String> tempIds1 = objectListConversionStringList(groupContactMenuIds);
            List<SysMenu> canContactMenus = Sets.list();
            if (tempIds1.size() > 0){
                canContactMenus = iSysMenuServiceSoa.selectBatchIds(tempIds1);
            }
            List<BootstrapTreeView> tree = Sets.list();
            tree.add(generatorTree(canContactMenus));
            result.put("canContactMenus",tree);

            /**
             * 已关联
             */
            List<Object> contactMenuIds = iSysRoleMenuServiceSoa.selectObjs(new EntityWrapper<SysRoleMenu>().eq("role_uuid",uuid).setSqlSelect("menu_uuid"));
            List<String> tempIds2 = objectListConversionStringList(contactMenuIds);
            List<SysMenu> contactMenus = Sets.list();
            if (tempIds2.size() > 0){
                contactMenus = iSysMenuServiceSoa.selectBatchIds(tempIds2);
            }
            result.put("contactMenus",contactMenus);

            WebJsonResult webJsonResult = new WebJsonResult();
            webJsonResult.setMap(result);
            return webJsonResult;
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }
    }

    /**
     * 角色关联菜单
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/contactMenus/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult contactMenu(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return  returnParamEmpty();
        }
        String menuIds = request.getParameter("menuIds");
        /**
         * 角色关联菜单，先清除已经关联的菜单，再将新的菜单关联上去
         */
        try{
            iSysRoleMenuServiceSoa.delete(new EntityWrapper<SysRoleMenu>().eq("role_uuid", uuid));

            if (!StringUtils.isEmpty(menuIds)){
                String[] menuIdsArr = menuIds.split(",");
                SysRoleMenu sysRoleMenu = null;
                List<SysRoleMenu> sysRoleMenus = Sets.list();
                for (String s : menuIdsArr){
                    sysRoleMenu = new SysRoleMenu();
                    sysRoleMenu.setUuid(UUIDGenerate.generateUuidByTime());
                    sysRoleMenu.setRoleUuid(uuid);
                    sysRoleMenu.setMenuUuid(s);
                    sysRoleMenus.add(sysRoleMenu);
                }
                iSysRoleMenuServiceSoa.insertBatch(sysRoleMenus);
            }
            /**
             * 没有异常返回成功
             */
            return handleResult(true);
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }
    }

    /**
     * 去关联组，权限控制，按钮级别
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:role:toContactGroup:3")
    @RequestMapping(value = "/toContactGroup/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult toContactGroup(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }

        /**
         * 角色去关联组，先查出该角色已经关联的组，再查出所有可关联的组
         */
        Map<String, Object> result = Sets.map();
        try{
            List<Object> contactGroupIds = iSysRoleGroupServiceSoa.selectObjs(new EntityWrapper<SysRoleGroup>().eq("role_uuid", uuid).setSqlSelect("group_uuid"));
            List<String> temp1 = objectListConversionStringList(contactGroupIds);
            List<SysGroup> contactGroups = Sets.list();
            if (temp1 != null && temp1.size() > 0){
                contactGroups = iSysGroupServiceSoa.selectBatchIds(temp1);
            }
            result.put("contactGroups", contactGroups);

            List<SysGroup> canContactGroups = iSysGroupServiceSoa.selectList(new EntityWrapper<SysGroup>());
            result.put("canContactGroups", canContactGroups);

            WebJsonResult webJsonResult = new WebJsonResult();
            webJsonResult.setMap(result);
            return webJsonResult;
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }
    }

    /**
     * 角色关联组
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/contactGroup/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult contactGroup(@PathVariable("uuid")String uuid){
        if (StringUtils.isEmpty(uuid)){
            return returnParamEmpty();
        }
        String groupIds = request.getParameter("groupIds");

        /**
         * 角色关联组，先清除已经关联的组，再将新的组关联上去
         */
        try{
            iSysRoleGroupServiceSoa.delete(new EntityWrapper<SysRoleGroup>().eq("role_uuid", uuid));
            if (!StringUtils.isEmpty(groupIds)){
                String[] groupIdsArr = groupIds.split(",");
                SysRoleGroup sysRoleGroup = null;
                List<SysRoleGroup> sysRoleGroupList = Sets.list();
                for (String s : groupIdsArr){
                    sysRoleGroup = new SysRoleGroup();
                    sysRoleGroup.setUuid(UUIDGenerate.generateUuidByTime());
                    sysRoleGroup.setGroupUuid(s);
                    sysRoleGroup.setRoleUuid(uuid);
                    sysRoleGroupList.add(sysRoleGroup);
                }
                iSysRoleGroupServiceSoa.insertBatch(sysRoleGroupList);
            }
            return handleResult(true);
        }catch (Exception ex){
            logger.error(ex.toString());
            return returnException();
        }

    }

    /**
     * 检索总数，列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findRoleListByPagin" , produces = "application/json", method = {RequestMethod.POST})
    public Map<String, Object> findRoleListByPagin(){
        Map<String, Object> params = Sets.map();
        Pagin pagin = new Pagin();
        ParseFrontParamtHelper helper = new ParseFrontParamtHelper(pagin);
        helper.handleSearchParam(request);
        helper.handlePaginAndSort(request);

        params.put("page" , pagin);
        List<SysRole> sysOperList = iSysRoleServiceSoa.findRolesByPagin(params);

        pagin.setAll(true);
        pagin.setSort(false);
        params.put("page" , pagin);
        int count = iSysRoleServiceSoa.findRoleCount(params);

        pagin.setRows(sysOperList);
        pagin.setTotal(count);

        params = helper.handleResult(pagin);
        return params;
    }
}
