package com.yyh.common.web;


import com.github.pagehelper.PageInfo;
import com.yyh.common.base.SuperVO;


public interface IBaseService<T extends SuperVO> {

	T selectByPrimaryKey(Long id);

	T queryOne(T param);

	PageInfo<T> selectList(T param, int pageNum, int pageSize);

	void addSelective(T entity);

	int deleteByPrimaryKey(Long id);

	int deleteByIdFalse(Long id);

	int updateByPrimaryKeySelective(T entity);

}
