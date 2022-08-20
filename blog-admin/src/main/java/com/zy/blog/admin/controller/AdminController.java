package com.zy.blog.admin.controller;

import com.zy.blog.admin.pojo.PageParam;
import com.zy.blog.admin.pojo.Permission;
import com.zy.blog.admin.service.PermissionService;
import com.zy.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 张岩
 * @version 1.0
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private PermissionService permissionService;

    @PostMapping("/permission/permissionList")
    public Result permissionList(@RequestBody PageParam pageParam){
        return permissionService.listPermission(pageParam);
    }
    @PostMapping("permission/add")
    public Result add(@RequestBody Permission permission){
        return permissionService.add(permission);
    }

    @PostMapping("permission/update")
    public Result update(@RequestBody Permission permission){
        return permissionService.update(permission);
    }

    @GetMapping("permission/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return permissionService.delete(id);
    }


}
