package com.myc.hrmanager.service;


import com.myc.hrmanager.mapper.MenuMapper;
import com.myc.hrmanager.mapper.MenuRoleMapper;
import com.myc.hrmanager.model.Hr;
import com.myc.hrmanager.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Service
//因为获取到的二级菜单基本不变所以设置缓存
@CacheConfig(cacheNames = "menus_cache")
public class MenuService {

    @Autowired
    MenuMapper menuMapper;
    @Autowired
    MenuRoleMapper menuRoleMapper;

    /**
     * @return
     */
    public List<Menu> getMenusByHrId() {
        //要传入id了，id从哪里来，我们登录的用户信息保存到security里面
        return menuMapper.getMenusByHrId(((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        //SecurityContextHolder里面有一个getContext()方法.getAuthentication()它里面的getPrincipal()，Principal它是当前登录的用户对象，然后强转成Hr对象再获取它里面的id
    }

    /**
     * 获取所有的菜单角色   一对多 一个菜单项有多个角色
     *
     * @return
     */
//    @Cacheable
    public List<Menu> getAllMenusWithRole() {
        return menuMapper.getAllMenusWithRole();
    }
//    ======================over1===========================================================

    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }

    @Transactional
    public boolean updateMenuRole(Integer rid, Integer[] mids) {
        menuRoleMapper.deleteByRid(rid);
        if (mids == null || mids.length == 0) {
            return true;
        }
        Integer result = menuRoleMapper.insertRecord(rid, mids);
        return result==mids.length;
    }
    public List<Integer> getMidsByRid(Integer rid) {
        return menuMapper.getMidsByRid(rid);
    }

}