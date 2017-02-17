package com.qybx.mapper;

import java.util.List;

import com.qybx.po.ImportData;
import com.qybx.po.PacxDiagnosis;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年11月30日 上午10:21:21   
 */
public interface ImportDataMapperExt {

	void addBatchwithImportData(List<ImportData> list);

	void addBatchPacxDiagnosisData(List<PacxDiagnosis> list);  

}
