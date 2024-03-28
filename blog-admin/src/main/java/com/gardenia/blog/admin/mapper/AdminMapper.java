package com.gardenia.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gardenia.blog.admin.pojo.Admin;
import com.gardenia.blog.admin.pojo.Permission;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author sq ♥ovo♥
 * @date 2024/3/5 - 17:19
 */
@Repository
public interface AdminMapper extends BaseMapper<Admin> {

    @Select("select * from ms_permission where id in (select permission_id from ms_admin_permission where admin_id = #{adminId})")
    List<Permission> findPermissionById(Long adminId);
}
