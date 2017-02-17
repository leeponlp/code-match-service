package com.qybx.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.qybx.service.UploadService;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年11月21日 下午5:05:10   
 */
@Controller
public class UploadAction {
	
	@Autowired
	UploadService uploadService;
	
	@RequestMapping("/upload")
	public String upload(){
		
		return "upload";
	}
	

	@RequestMapping(value="/upload/csv")
	@ResponseBody
	public String uploadCSV(@RequestParam("importFile") MultipartFile importFile){
		return uploadService.uploadCSV(importFile);
	}
	

}
