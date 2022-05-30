package com.myc.hrmanager.controller;

import com.myc.hrmanager.model.Menu;
import com.myc.hrmanager.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author
 * @Description：
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController {

    @Autowired
    MenuService menuService;;
//导航栏
    @GetMapping("/menu")
    public List<Menu> getMenusByHrId(){
        return menuService.getMenusByHrId();
    }
}
//    ======================over1===========================================================
