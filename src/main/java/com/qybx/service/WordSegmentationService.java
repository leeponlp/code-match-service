package com.qybx.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qybx.po.WordSegment;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年12月9日 上午11:20:20   
 */
public interface WordSegmentationService {
 
	void getWordSegmentationBatch(String filename, HttpServletRequest request, HttpServletResponse response);

	WordSegment getWordSegmentationData(String keyword);   

}
