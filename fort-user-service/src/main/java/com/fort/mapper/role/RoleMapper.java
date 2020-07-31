package com.fort.mapper.role;

import org.springframework.stereotype.Repository;

import com.fort.module.role.Role;

public interface RoleMapper {

	public Role queryById(long roleId);
}
