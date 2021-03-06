package com.yyh.common.web;


import com.yyh.common.base.SuperVO;

import java.util.List;

public interface IBaseDao<T extends SuperVO> {

	T selectOne(T param);

	T selectByPrimaryKey(Long id);

	List<T> selectList(T param);

	int addSelective(T entity);

	int deleteByPrimaryKey(Long id);

	int deleteByIdFalse(Long id);
	
	int updateByPrimaryKeySelective(T entity);
}
