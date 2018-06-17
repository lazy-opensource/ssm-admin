package com.lzy.innovate.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lzy.innovate.entity.SysMenu;
import com.lzy.innovate.entity.SysOper;
import com.lzy.innovate.mapper.SysOperMapper;
import com.lzy.innovate.service.ISysMenuService;
import com.lzy.innovate.service.ISysOperService;
import com.lzy.innovate.utils.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author laizy
 * @since 2017-02-27
 */
@Service
public class SysOperServiceImpl extends ServiceImpl<SysOperMapper, SysOper> implements ISysOperService {

    @Autowired
    private ISysMenuService iSysMenuService;

    /**
     * 查询当前用户的所有的操作列表
     * @param params 参数集合
     * @return 当前用户的所有按钮列表
     */
    @Override
    public List<SysOper> findOpersByPagin(Map<String, Object> params) {
        return baseMapper.findOpersByPagin(params);
    }

    /**
     * 查询当前用户某个菜单下的操作/按钮
     * @param userId 用户ID
     * @param menuId 菜单ID
     * @return 当前用户某个菜单下的按钮列表
     */
    @Override
    public List<SysOper> findOpersByMenuId(String userId, String menuId) {
        return baseMapper.findOpersByMenuId(userId , menuId);
    }

    /**
     * 检索总数
     * @param params
     * @return
     */
    @Override
    public int findOperCount(Map<String, Object> params) {
        return baseMapper.findOperCount(params);
    }

    /**
     * 生成操作对于的菜单Map
     * @param operIds
     * @return
     */
    @Override
    public Map<String, List<SysOper>> generateOperBelongMenuMap(List<String> operIds) {
        Map<String, List<SysOper>> map = Sets.map();
        if (operIds == null && operIds.size() < 0){
            return map;
        }

        /**
         * 将所有菜单，操作查出,后续需要优化，可以考虑缓存
         */
        List<SysMenu> sysMenus = iSysMenuService.selectList(null);
        List<SysOper> sysOpers = selectBatchIds(operIds);
        Map<String, List<SysOper>> result = Sets.map();

        String temp = "";
        List<SysOper> sysOperList = null;
        for (SysMenu sysMenu : sysMenus){
            sysOperList = Sets.list();
            for (SysOper sysOper : sysOpers){
                temp = sysOper.getMenuId();
                if (!StringUtils.isEmpty(temp)){
                    if (temp.equals(sysMenu.getUuid())){
                        sysOperList.add(sysOper);
                    }
                }
            }
            result.put(sysMenu.getName(), sysOperList);
        }
        return result;
    }
}
