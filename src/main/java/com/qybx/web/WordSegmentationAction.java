package com.qybx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qybx.po.WordSegment;
import com.qybx.service.WordSegmentationService;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年12月9日 上午11:17:31   
 */

@Controller
@RequestMapping("/word")
public class WordSegmentationAction {
	
	@Autowired
	WordSegmentationService wordSegmentationService;
	
	@RequestMapping("/segment")
	public void WordSegmentationBatch(HttpServletRequest request,HttpServletResponse response){
		String filename = "佳木斯大学附属口腔医院.csv";
		wordSegmentationService.getWordSegmentationBatch(filename,request,response);
		
	}
	
	
	@RequestMapping("/segment/{keyword}")
	@ResponseBody
	public WordSegment WordSegmentation(@PathVariable String keyword){
		return wordSegmentationService.getWordSegmentationData(keyword);
		
	}

}
