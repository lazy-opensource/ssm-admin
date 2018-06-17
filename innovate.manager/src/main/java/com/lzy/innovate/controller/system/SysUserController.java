package com.lzy.innovate.controller.system;

import com.baomidou.kisso.SSOToken;
import com.baomidou.kisso.common.encrypt.SaltEncoder;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lzy.innovate.controller.base.BaseController;
import com.lzy.innovate.controller.common.Pagin;
import com.lzy.innovate.controller.common.ParseFrontParamtHelper;
import com.lzy.innovate.controller.common.WebJsonResult;
import com.lzy.innovate.controller.enums.SysUserStatusEnum;
import com.lzy.innovate.dubbo.system.*;
import com.lzy.innovate.entity.*;
import com.lzy.innovate.permission.MenuPermission;
import com.lzy.innovate.permission.OpertionPermission;
import com.lzy.innovate.utils.Sets;
import com.lzy.innovate.utils.date.DateUtil;
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
 * Created by laizhiyuan on 2017/3/7.
 * 用户控制器
 */
@Controller
@RequestMapping("/user")
public class SysUserController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger("innovate_web_log");

    @Autowired
    private ISysUserServiceSoa iSysUserServiceSoa;

    @Autowired
    private ISysRoleUserServiceSoa iSysRoleUserServiceSoa;

    @Autowired
    private ISysRoleGroupServiceSoa iSysRoleGroupServiceSoa;

    @Autowired
    private ISysRoleServiceSoa iSysRoleServiceSoa;

    @Autowired
    private ISysGroupServiceSoa iSysGroupServiceSoa;

    /**
     * 去用户列表页面，权限控制，菜单级别
     *
     * @return
     */
    @MenuPermission(code = "menu_sys:user:1")
    @RequestMapping(value = "/toList/{uuid}", method = RequestMethod.GET)
    public String toList(@PathVariable("uuid") String uuid, Model model) {

        model.addAttribute("menuId", uuid);
        return "system/user";
    }

    /**
     * 添加用户，权限控制，按钮级别
     * @param sysUser
     * @return
     */
    @OpertionPermission(code = "oper_sys:user:add:2")
    @ResponseBody
    @RequestMapping(value = "/add", produces = "application/json", method = {RequestMethod.POST})
    public WebJsonResult add(SysUser sysUser){
        return edit(sysUser);
    }

    /**
     * 新增，编辑用户
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/edit", produces = "application/json", method = {RequestMethod.POST})
    public WebJsonResult edit(SysUser sysUser) {
        WebJsonResult webResult = new WebJsonResult();
        if (!isLegal() || sysUser == null) {
            return returnParamEmpty();
        }

        if (StringUtils.isEmpty(sysUser.getUuid())) {
            /**
             * add user
             */
            try{
                /**
                 * 新增用户默认加入当前用户组
                 */
                String groupId = getCurrentGroupId();
                String uuid = UUIDGenerate.generateUuidByTime();
                sysUser.setUuid(uuid);
                sysUser.setCreateTime(DateUtil.getCurrentTime());
                sysUser.setUpdateTime(sysUser.getCreateTime());
                sysUser.setGroupId(groupId);
                sysUser.setStatus(SysUserStatusEnum.ILLEGAL.getStatus());
                sysUser.setPassword(SaltEncoder.md5SaltEncode(sysUser.getLoginName(), sysUser.getPassword()));
                isSuccess = iSysUserServiceSoa.insert(sysUser);
                return handleResult(true);
            }catch (Exception ex){
                logger.error(ex.toString());
                return returnException();
            }

        } else { //edit
            sysUser.setUpdateTime(DateUtil.getCurrentTime());
            isSuccess = iSysUserServiceSoa.updateById(sysUser);
            return handleResult(isSuccess);
        }
    }

    /**
     * 获得待修改用户数据，权限控制，按钮级别
     *
     * @return
     */
    @OpertionPermission(code = "oper_sys:user:edit:2")
    @ResponseBody
    @RequestMapping(value = "/toEdit/{uuid}", produces = "application/json", method = {RequestMethod.POST})
    public SysUser toEdit(@PathVariable("uuid") String uuid) {

        return byId(uuid);
    }

    /**
     * 删除用户，可批量，权限控制，按钮级别
     *
     * @return
     */
    @OpertionPermission(code = "oper_sys:user:del:2")
    @ResponseBody
    @RequestMapping(value = "/del/{ids}", method = {RequestMethod.POST})
    public WebJsonResult del(@PathVariable("ids") String ids) {
        WebJsonResult webResult = new WebJsonResult();
        if (StringUtils.isEmpty(ids)) {
            return returnParamEmpty();
        }

        String[] idsArr = ids.split(",");
        isSuccess = iSysUserServiceSoa.delete(new EntityWrapper<SysUser>().in("uuid", idsArr));
        iSysRoleUserServiceSoa.delete(new EntityWrapper<SysRoleUser>().in("user_uuid", idsArr));

        return handleResult(isSuccess);
    }

    /**
     * 获得用户列表数据，总数
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findUserListByPagin", produces = "application/json", method = RequestMethod.POST)
    public Map<String, Object> findUserListByPagin() {

        Pagin<SysUser> page = new Pagin();
        ParseFrontParamtHelper helper = new ParseFrontParamtHelper(page);

        helper.handleSearchParam(request);
        helper.handlePaginAndSort(request);
        Map<String, Object> map = Sets.map();
        map.put("page", page);
        List<SysUser> data = iSysUserServiceSoa.findUserListByPagin(map);

        page.setAll(true);
        page.setSort(false);
        map.put("page", page);
        int count = iSysUserServiceSoa.findUserCount(map);

        page.setTotal(count);
        page.setRows(data);
        map = helper.handleResult(page);

        return map;
    }

    /**
     * 根据用户ID查询
     *
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findUserById/{uuid}", method = {RequestMethod.POST})
    public SysUser findUserById(@PathVariable("uuid") String uuid) {

        return byId(uuid);
    }

    /**
     * 根据id 查询
     *
     * @param uuid
     * @return
     */
    private SysUser byId(String uuid) {
        SysUser sysUser = new SysUser();
        if (StringUtils.isEmpty(uuid)) {
            return sysUser;
        }

        sysUser = iSysUserServiceSoa.selectById(uuid);
        return sysUser;
    }

    /**
     * 去关联角色
     *
     * @param groupId
     * @param uuid
     * @return
     */
    @OpertionPermission(code = "oper_sys:user:toContactRoles:2")
    @ResponseBody
    @RequestMapping(value = "/toContactRoles/{uuid}/{groupId}", method = {RequestMethod.POST})
    public WebJsonResult toContactRoles(@PathVariable("groupId") String groupId, @PathVariable("uuid") String uuid) {

        Map<String, List<SysRole>> result = Sets.map();
        if (!isLegal() || StringUtils.isEmpty(uuid) || StringUtils.isEmpty(groupId)) {
            return returnParamEmpty();
        }

        /**
         * 用户所在组的角色集合
         */
        List<SysRole> canContactRoles = findUserRolesByGroupId(groupId);

        /**
         * 当前用户已经关联的角色
         */
        List<SysRole> haveContactRoles = findUserRolesByUserId(uuid);

        result.put("canContactRoles", canContactRoles);
        result.put("haveContactRoles", haveContactRoles);

        return returnSuccess(result, null);
    }

    /**
     * 关联角色
     *
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/contactRoles/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult contactRoles(@PathVariable("uuid") String uuid) {

        String roleIds = request.getParameter("roleIds");
        if (StringUtils.isEmpty(uuid)) {
            return returnParamEmpty();
        }
        if (roleIds == null) {
            roleIds = "";
        }

        /**
         * 清空原来关联的角色
         */
        iSysRoleUserServiceSoa.delete(new EntityWrapper<SysRoleUser>().eq("user_uuid", uuid));

        /**
         * 重置角色
         */
        if (roleIds != "") {
            try{
                SysRoleUser sysRoleUser = null;
                String[] roleIdsArr = roleIds.split(",");
                List<SysRoleUser> sysRoleUsers = Sets.list();
                for (String roleId : roleIdsArr) {
                    sysRoleUser = new SysRoleUser();
                    sysRoleUser.setUuid(UUIDGenerate.generateUuidByTime());
                    sysRoleUser.setRoleUuid(roleId);
                    sysRoleUser.setUserUuid(uuid);
                    sysRoleUsers.add(sysRoleUser);
                }
                isSuccess = iSysRoleUserServiceSoa.insertBatch(sysRoleUsers);

                /**
                 * 更新状态和修改时间
                 */
                SysUser sysUser = new SysUser();
                sysUser.setStatus(SysUserStatusEnum.ACTIVATE.getStatus());
                sysUser.setUpdateTime(DateUtil.getCurrentTime());
                sysUser.setUuid(uuid);
                iSysUserServiceSoa.updateById(sysUser);
                return handleResult(true);
            }catch (Exception ex){
                logger.error(ex.toString());
                return returnException();
            }

        }

        return handleResult(true);
    }


    /**
     * 去关联用户组，权限控制，按钮级别
     *
     * @param uuid
     * @return
     */
    @ResponseBody
    @OpertionPermission(code = "oper_sys:user:toContactGroup:2")
    @RequestMapping(value = "/toContactGroup/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult toContactGroup(@PathVariable("uuid") String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            return returnParamEmpty();
        }

        /**
         * 获得用户组id
         */
        SysUser sysUser = byId(uuid);
        WebJsonResult webJsonResult = new WebJsonResult();
        webJsonResult.setObject(sysUser.getGroupId());
        return webJsonResult;
    }

    /**
     * 关联用户组
     *
     * @param uuids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/contactGroup/{uuids}", method = {RequestMethod.POST})
    public WebJsonResult contactGroup(@PathVariable("uuids") String uuids) {

        if (StringUtils.isEmpty(uuids)) {
            return returnParamEmpty();
        }
        String groupId = request.getParameter("groupId");
        if (groupId == null) {
            groupId = "";
        }

        String[] uuidsArr = uuids.split(",");
        SysUser sysUser = null;
        List<SysUser> sysUsers = Sets.list();
        try {
            for (String uuid : uuidsArr) {
                sysUser = new SysUser();
                sysUser.setUuid(uuid);
                sysUser.setGroupId(groupId);
                sysUser.setUpdateTime(DateUtil.getCurrentTime());
                sysUsers.add(sysUser);
            }
            isSuccess = iSysUserServiceSoa.updateBatchById(sysUsers);
            return handleResult(isSuccess);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return returnException();
        }
    }

    /**
     * 获得用户组列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findUserGroups", method = {RequestMethod.POST})
    public WebJsonResult findUserGroups() {
        List<SysGroup> sysGroups = Sets.list();
        sysGroups = iSysGroupServiceSoa.selectList(new EntityWrapper<SysGroup>());

        return returnSuccess(null, sysGroups);
    }

    /**
     * 找到当前用户名字
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findCurrentName", method = {RequestMethod.POST})
    public WebJsonResult findCurrentName() {
        WebJsonResult webJsonResult = new WebJsonResult();
        if (!isLegal()) {
            return webJsonResult;
        }
        SSOToken sso = getSSOToken();
        String temp = sso.getData();
        String[] arr = temp.split("@");
        webJsonResult.setObject(arr[0]);
        return webJsonResult;
    }

    @ResponseBody
    @RequestMapping(value = "/detail/{uuid}", method = {RequestMethod.POST})
    public WebJsonResult detail(@PathVariable("uuid") String uuid) {
        String groupId = request.getParameter("groupId");

        if (StringUtils.isEmpty(uuid)) {
            return returnParamEmpty();
        }

        if (groupId == null){
            groupId = "";
        }

        WebJsonResult webJsonResult = new WebJsonResult();
        List<SysRole> roles = findUserRolesByUserId(uuid);
        SysGroup sysGroup = iSysGroupServiceSoa.selectById(groupId);

        Map<String, Object> result = Sets.map();
        result.put("roles" , roles);
        result.put("group" , sysGroup);
        webJsonResult.setMap(result);
        return webJsonResult;
    }

    /**
     * 根据组ID 查角色
     *
     * @param groupId
     * @return
     */
    private List<SysRole> findUserRolesByGroupId(String groupId) {

        if (StringUtils.isEmpty(groupId)) {
            return new ArrayList<SysRole>();
        }

        List<Object> roleIds = iSysRoleGroupServiceSoa.selectObjs(new EntityWrapper<SysRoleGroup>().setSqlSelect(" role_uuid ").eq("group_uuid", groupId));
        if (roleIds != null && roleIds.size() == 0) {
            roleIds.add("");
        }
        List<SysRole> roles = iSysRoleServiceSoa.selectList(new EntityWrapper<SysRole>().in("uuid", roleIds));
        return roles;
    }

    /**
     * 根据用户Id查角色
     * @param userId
     * @return
     */
    private List<SysRole> findUserRolesByUserId(String userId){

        if (StringUtils.isEmpty(userId)) {
            return new ArrayList<SysRole>();
        }

        List<Object> roleIds = iSysRoleUserServiceSoa.selectObjs(new EntityWrapper<SysRoleUser>().setSqlSelect("role_uuid ").eq("user_uuid", userId));
        if (roleIds != null && roleIds.size() == 0) {
            roleIds.add("");
        }
        List<SysRole> roles = iSysRoleServiceSoa.selectList(new EntityWrapper<SysRole>().in("uuid", roleIds));

        return roles;
    }
}
