package com.yyh.common.web;


import com.yyh.common.base.BaseRequest;
import com.yyh.common.base.Result;
import com.yyh.common.base.SuperVO;

public interface IBaseController<T extends SuperVO> {
	
	Result selectByPrimaryKey(BaseRequest<T> requestParam);

	Result selectList(BaseRequest<T> requestParam);

	Result addSelective(BaseRequest<T> requestParam);

	Result deleteByIdFalse(BaseRequest<T> requestParam);

	Result updateByPrimaryKeySelective(BaseRequest<T> requestParam);


}
