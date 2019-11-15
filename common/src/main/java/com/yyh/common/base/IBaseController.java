package com.yyh.common.base;


public interface IBaseController<T extends SuperVO> {
	
	Result selectByPrimaryKey(BaseRequest<T> requestParam);

	Result selectList(BaseRequest<T> requestParam);

	Result addSelective(BaseRequest<T> requestParam);

	Result deleteByIdFalse(BaseRequest<T> requestParam);

	Result updateByPrimaryKeySelective(BaseRequest<T> requestParam);


}
