package com.qybx.service;

import org.springframework.web.multipart.MultipartFile;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年11月21日 下午5:06:43   
 */
public interface UploadService {

	String uploadCSV(MultipartFile importFile);


}
