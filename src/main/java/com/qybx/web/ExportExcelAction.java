package com.qybx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.qybx.service.ExportExcelService;

/**
 * This class is used for ...
 * 
 * @author leepon1990
 * @version 1.0, 2016年11月21日 下午4:59:51
 */
@Controller
public class ExportExcelAction {

	@Autowired
	ExportExcelService exportExcelService;

	@RequestMapping(value = "/download/icd")
	public void downloadICD(HttpServletRequest request, HttpServletResponse response) {

		exportExcelService.downloadICD(request, response);

	}

}
