package com.gardenia.blog.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gardenia.blog.admin.mapper.AdminMapper;
import com.gardenia.blog.admin.pojo.Admin;
import com.gardenia.blog.admin.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sq ♥ovo♥
 * @date 2024/3/5 - 17:15
 */
@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public Admin findAdminByUserName(String username){
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername,username);
        queryWrapper.last("limit 1");
        Admin admin = adminMapper.selectOne(queryWrapper);
        return admin;
    }

    public List<Permission> findPermissionsByAdminId(Long adminId) {

        //select * from ms_permission where id in (select permission_id from ms_admin_permission where admin_id = 1)
        return adminMapper.findPermissionById(adminId);
    }
}
