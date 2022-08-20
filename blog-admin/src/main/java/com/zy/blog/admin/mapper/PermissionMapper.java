package com.zy.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zy.blog.admin.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 张岩
 * @version 1.0
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    @Select("select * from ms_permission where id in (select permission_id from ms_admin_permission where admin_id=#{adminId})")
    List<Permission> findPermissionsByAdminId(Long adminId);
}
