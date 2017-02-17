package com.qybx.util;

import java.io.File;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年8月31日 下午1:40:14   
 */
public class FileUtil {
	
	public static boolean mkdir(String dirName){
        if (dirName == null || dirName.isEmpty()) {
            return false;
        }
 
        File folder = new File(dirName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();

	}
	

}
