package com.fort.mapper.asset;

import org.apache.ibatis.annotations.Param;

import com.fort.module.asset.Account;

public interface AccountMapper {

	/**
	 * 根据账号名称和设备ID查询账号信息
	 * @param assetId 设备ID
	 * @param name 账号名称
	 * @return
	 */
	public Account queryByName(@Param("assetId") long assetId, @Param("name") String name);
	
	/**
	 * 根据账号ID修改账号信息
	 * @param acc
	 * @return
	 */
	public int updateById(Account acc);
	
	/**
	 * 添加账号信息
	 * @param acc
	 * @return
	 */
	public int insert(Account acc);
	
	/**
	 * 根据账号ID删除账号信息
	 * @param id
	 * @return
	 */
	public int deleteById(long id);
}
