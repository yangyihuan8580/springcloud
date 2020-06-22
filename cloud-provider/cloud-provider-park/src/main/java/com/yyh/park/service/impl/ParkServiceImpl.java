package com.yyh.park.service.impl;

import com.yyh.common.web.ABaseServiceImpl;
import com.yyh.common.web.IBaseDao;
import com.yyh.park.dao.ParkDao;
import com.yyh.park.entity.Park;
import com.yyh.park.service.ParkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * <br>
 * <b>功能：</b>ParkService<br>
 */
@Service("parkService")
public class ParkServiceImpl extends ABaseServiceImpl<Park> implements ParkService {
  private final static Logger log= LoggerFactory.getLogger(ParkServiceImpl.class);
	

	@Autowired
    private ParkDao parkDao;
	

	@Override
	protected IBaseDao<Park> getBaseDao() {
		return parkDao;
	}

}
