package com.qybx.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.qybx.service.UploadService;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年11月21日 下午5:06:59   
 */
@Service
public class UploadServiceImpl implements UploadService {
	
	@Override
	public String uploadCSV(MultipartFile importFile) {
		
		String filename = importFile.getOriginalFilename();
		String directory = "upload";
		File fileDir = new File(directory);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		String filepath = Paths.get(directory, filename).toString();
		try {
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(importFile.getBytes());
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		
		return "success";
	}

}
