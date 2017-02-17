package com.qybx.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.qybx.po.CodeMatch;
import com.qybx.po.CodeMatchView;
import com.qybx.po.MatchView;
import com.qybx.po.PacxMatchView;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年8月16日 下午5:01:00   
 */
public interface IndexService {
	
	
	/**
	 * 
	 * @Title: buildBaseIndex 
	 * @Description: TODO <建立基础数据索引>
	 * @return void  
	 * @throws
	 */
	public void buildBaseIndex();
	
	
	/**
	 * 
	 * @Title: buildAllHistoryIndex 
	 * @Description: TODO <建立历史数据索引>
	 * @return void  
	 * @throws
	 */
	public void buildHistoryIndex();
	
	
	/**
	 * 
	 * @Title: buildHospitalIndex 
	 * @Description: TODO <建立医院数据索引>
	 * @param  
	 * @return void  
	 * @throws
	 */
	public void buildHospitalIndex();
	
	/**
	 * 
	 * @Title: buildICDIndex 
	 * @Description: TODO <建立icd索引库>
	 * @param  
	 * @return void  
	 * @throws
	 */
	public void buildICDIndex();
	
	/**
	 * 
	 * @Title: buildPacxDiagnosisData 
	 * @Description: TODO <构建平安产险诊断数据索引>
	 * @param  
	 * @return void  
	 * @throws
	 */
	public void buildPacxDiagnosisIndex();
	
	/**
	 * 
	 * @Title: exportHospitalAnalyseExcel 
	 * @Description: TODO <分析医院数据并生成excel报告>
	 * @param request
	 * @param response 
	 * @return void  
	 * @throws
	 */
	public void exportHospitalAnalyseExcel(HttpServletRequest request,HttpServletResponse response);
	
	
	/**
	 * 
	 * @Title: analyse 
	 * @Description: TODO <分析数据并生成excel报告>
	 * @param request
	 * @param response 
	 * @return void  
	 * @throws
	 */
	public void exportAnalyseExcel(HttpServletRequest request,HttpServletResponse response);
	
	
	/**
	 * 
	 * @Title: searchBase 
	 * @Description: TODO <关键字查询>
	 * @param @param keyword
	 * @return List<CodeMatch>  
	 * @throws
	 */
	public List<CodeMatch> searchBase(String keyword);
	
	
	
	/**
	 * 
	 * @Title: searchHistory 
	 * @Description: TODO <关键字查询>
	 * @param @param keyword
	 * @return List<CodeMatch>  
	 * @throws
	 */
	public List<CodeMatch> searchHistory(String keyword);
	
	
	/**
	 * 
	 * @Title: searchIcd10 
	 * @Description: TODO <关键字查询>
	 * @param @param keyword
	 * @param @return 
	 * @return List<CodeMatch>  
	 * @throws
	 */
	public List<CodeMatch> searchICD(String keyword);
	
	/**
	 * 
	 * @Title: searchICD2 
	 * @Description: TODO <关键字查询>
	 * @param @param keyword
	 * @param @return 
	 * @return List<List<String>>  
	 * @throws
	 */
	List<List<String>> searchICD2(String keyword);  
	
	/**
	 * 
	 * @Title: searchPacxDia 
	 * @Description: TODO <关键字查询>
	 * @param @param keyword
	 * @param @return 
	 * @return List<PacxDiagnosis>  
	 * @throws
	 */
	public List<PacxMatchView> searchPacxDia(String keyword);
	
	
	
	/**
	 * 
	 * @Title: match 
	 * @Description: TODO <关键字匹配前三结果>
	 * @param @param keyword
	 * @param @return 
	 * @return MatchView  
	 * @throws
	 */
	public MatchView match(String keyword);
	
	
	/**
	 * 
	 * @Title: match 
	 * @Description: TODO <list匹配>
	 * @param @param list
	 * @param @return 
	 * @return List<CodeMatchView>  
	 * @throws
	 */
	public List<CodeMatchView> match (List<CodeMatch> list );


	/**
	 * 
	 * @Title: analyseICD10 
	 * @Description: TODO <分析导出ICD-10的匹配结果>
	 * @param list
	 * @param request
	 * @param response 
	 * @return List<List<String>>
	 * @throws
	 */
	public List<List<String>> analyseICD(List<String> list);
	
	
	public List<CodeMatch> queryView(String keyword);


	public List<List<String>> analyseICD2(List<String> list);


	
	
	

}
