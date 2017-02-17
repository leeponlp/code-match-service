package com.qybx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qybx.mapper.LogogramMapperExt;
import com.qybx.po.ImportData;
import com.qybx.service.LogogramService;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年11月30日 上午10:42:15   
 */
@Service
public class LogogramServiceImpl implements LogogramService{
	
	@Autowired
	LogogramMapperExt logogramMapperExt;

	@Override
	public List<ImportData> selectAll() {
		
		return logogramMapperExt.selectAll();
	}

}
