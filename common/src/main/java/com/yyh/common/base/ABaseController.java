package com.yyh.common.base;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public abstract class ABaseController <T extends SuperVO> implements IBaseController<T> {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpServletResponse response;
	@Autowired
    protected HttpSession session;

    protected abstract IBaseService<T> getBaseService();

    
	@RequestMapping(value="/queryList",method= RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
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


    
	@RequestMapping(value="/queryById", method=RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@Override
	public Result queryById(@RequestBody BaseRequest<T> param) {
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



	@RequestMapping(value="/add",method=RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
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

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/update",method=RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
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

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/deleteByIdFalse",method=RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
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
