package com.qybx.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qybx.service.ImportService;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年11月29日 下午2:01:41   
 */
@Controller
public class ImportExcelAction {
	
	@Autowired
	ImportService importService;
	
	@RequestMapping("/import/csv")
	public String importCSV(){
		String path = "upload/诊断_20160809_固定版.csv";
		//importService.importICD9CM3(path);
		//importService.importLogogram(path);
		importService.importPacxDiagnosisData(path);
		return "success";
	}

}
