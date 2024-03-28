package com.gardenia.blog.admin.controller;

import com.gardenia.blog.admin.model.params.PageParam;
import com.gardenia.blog.admin.pojo.Permission;
import com.gardenia.blog.admin.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.Result;

/**
 * @author sq ♥ovo♥
 * @date 2024/3/5 - 16:01
 */
@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private PermissionService permissionService;

    @PostMapping("permission/permissionList")
    public Result listPermission(@RequestBody PageParam pageParam){
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
