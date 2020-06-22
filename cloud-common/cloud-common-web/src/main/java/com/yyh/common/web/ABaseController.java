package com.yyh.common.web;

import com.github.pagehelper.PageInfo;
import com.yyh.common.base.BaseRequest;
import com.yyh.common.base.Result;
import com.yyh.common.base.SuperVO;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class ABaseController <T extends SuperVO> implements IBaseController<T> {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpServletResponse response;
	@Autowired
    protected HttpSession session;

    protected abstract IBaseService<T> getBaseService();

	@ApiOperation(value = "查询列表")
	@RequestMapping(value="/queryList",method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Override
	public Result selectList(@RequestBody BaseRequest<T> param) {
		Result result;
		try{
			PageInfo<T> data = getBaseService().selectList(param.getData(), param.getPageNum(), param.getPageSize());
			result = Result.success(data.getList(), data.getTotal());
		}catch(Exception e){
			log.error(e.getMessage(),e);
			result = Result.error(e.getMessage());
		}
		return result;
	}

	@ApiOperation(value = "查询单条数据")
	@RequestMapping(value="/queryById", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Override
	public Result selectByPrimaryKey(@RequestBody BaseRequest<T> param) {
		Result result;
		try{
			if (param.getData() == null || param.getData().getId() == null) {
				return Result.error("主键id 不能为空");
			}
			result = Result.success(getBaseService().selectByPrimaryKey(param.getData().getId()));
		}catch(Exception e){
			log.error(e.getMessage(),e);
			result = Result.success(e.getMessage());
		}
		return result;
	}


	@ApiOperation(value = "添加")
	@RequestMapping(value="/add",method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Override
	public Result addSelective(@RequestBody BaseRequest<T> param) {
		Result result;
		try{
			getBaseService().addSelective(param.getData());
			result = Result.SUCCESS;
		}catch(Exception e){
			log.error(e.getMessage(),e);
			result = Result.error(e.getMessage());
		}
		return result;
	}

	@ApiOperation(value = "修改")
	@RequestMapping(value="/update",method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Override
	public Result updateByPrimaryKeySelective(@RequestBody BaseRequest<T> param) {
		Result result;
		try{
			getBaseService().updateByPrimaryKeySelective(param.getData());
			result = Result.SUCCESS;
		}catch(Exception e){
			log.error(e.getMessage(),e);
			result = Result.error(e.getMessage());
		}
		return result;
	}

	@ApiOperation(value = "删除")
	@RequestMapping(value="/deleteByIdFalse",method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Override
	public Result deleteByIdFalse(@RequestBody BaseRequest<T> param) {
		Result result;
		try{
			if (param.getData() == null || param.getData().getId() == null) {
				return Result.error("主键id 不能为空");
			}
			getBaseService().deleteByIdFalse(param.getData().getId());
			result = Result.SUCCESS;
		}catch(Exception e){
			log.error(e.getMessage(),e);
			result = Result.error(e.getMessage());
		}
		return result;
	}
	
}
