package com.qybx.service;
/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年11月29日 下午2:03:14   
 */
public interface ImportService {

	void importICD9CM3(String path);

	void importLogogram(String path);

	void importPacxDiagnosisData(String path);   

}
