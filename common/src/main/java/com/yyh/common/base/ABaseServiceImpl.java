package com.yyh.common.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yyh.common.util.SnowFlakeStrategy;

import java.util.List;


public abstract class ABaseServiceImpl<T extends SuperVO> implements IBaseService<T> {

	protected abstract IBaseDao<T> getBaseDao();

	@Override
	public T selectByPrimaryKey(Long id) {
		checkId(id);
		return getBaseDao().selectByPrimaryKey(id);
	}


	@Override
	public T queryOne(T param) {
		return getBaseDao().selectOne(param);
	}


	@Override
	public void addSelective(T entity) {
		if (entity == null) {
			throw new NullPointerException("entity 不能为空");
		}
		/** id统一在此生成 */
		entity.setId(SnowFlakeStrategy.getPrimaryId());
		getBaseDao().addSelective(entity);
	}

	@Override
	public int updateByPrimaryKeySelective(T entity) {
		checkId(entity.getId());
		return getBaseDao().updateByPrimaryKeySelective(entity);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		checkId(id);
		return getBaseDao().deleteByPrimaryKey(id);
	}

	@Override
	public int deleteByIdFalse(Long id) {
		checkId(id);
		return getBaseDao().deleteByIdFalse(id);
	}

	@Override
	public PageInfo<T> selectList(T param, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<T> list = getBaseDao().selectList(param);
		return new PageInfo<>(list);
	}

	private void checkId(Long id) {
		if (id == null) {
			throw new NullPointerException("主键id不能为空");
		}
	}
}
