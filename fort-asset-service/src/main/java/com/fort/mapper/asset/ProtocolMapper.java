package com.fort.mapper.asset;

import com.fort.module.asset.Protocol;

public interface ProtocolMapper {

	/**
	 * 根据协议类型和设备ID查询协议
	 * @param type 协议类型
	 * @param assetId 设备ID
	 * @return
	 */
	public Protocol queryByType(int type,long assetId);
	
	/**
	 * 根据协议ID修改协议信息
	 * @param pro
	 * @return
	 */
	public int updateById(Protocol pro);
	
	/**
	 * 添加协议信息
	 * @param pro
	 * @return
	 */
	public int insert(Protocol pro);
	
	/**
	 * 根据协议ID删除协议信息
	 * @param id
	 * @return
	 */
	public int deleteById(long id);
}
