package com.fort.mapper.asset;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fort.module.asset.Asset;

/**
 * 设备信息管理接口
 * @author zhigongzhang
 *
 */
public interface AssetMapper{

	/**
	 * 自定义条件分页查询设备信息
	 * @param keyword 模糊查询条件
	 * @param offset 起始记录数
	 * @param limit 每页显示的条数
	 * @return
	 */
	public List<Asset> query(@Param("keyword") String keyword,@Param("offset") int offset,@Param("limit") int limit);
	
	/**
	 * 自定义条件分页统计设备总记录数
	 * @param keyword 模糊查询条件
	 * @return
	 */
	public int count(@Param("keyword") String keyword);
	
	/**
	 * 添加设备信息
	 * @param asset
	 * @return
	 */
	public int insert(Asset asset);
	
	/**
	 * 根据设备ID修改设备信息
	 * @param asset
	 * @return
	 */
	public int updateById(Asset asset);
	
	/**
	 * 根据设备ID查询设备信息
	 * @param id
	 * @return
	 */
	public Asset queryById(long id);
	
	/**
	 * 根据设备ID删除设备信息
	 * @param id
	 * @return
	 */
	public int deleteById(long id);
	
}
