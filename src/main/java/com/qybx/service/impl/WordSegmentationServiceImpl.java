package com.qybx.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.qybx.po.PrintExcel;
import com.qybx.po.WordSegment;
import com.qybx.service.WordSegmentationService;
import com.qybx.util.AnalyseUtil;
import com.qybx.util.ChineseUtil;
import com.qybx.util.CsvUtil;
import com.qybx.util.ExportExcelUtil;
import com.qybx.util.FilenameEncodeUtil;

/**
 * This class is used for ...
 * 
 * @author leepon1990
 * @version 1.0, 2016年12月9日 上午11:20:32
 */
@Service
public class WordSegmentationServiceImpl implements WordSegmentationService {

	@Override
	public void getWordSegmentationBatch(String filename, HttpServletRequest request, HttpServletResponse response) {
		File file = new File("upload" + File.separator + filename);
		List<List<String>> csvList = CsvUtil.readCsv(file, 0, "GBK");
		List<String> list = new ArrayList<>();

		for (List<String> csvlist : csvList) {
			String chinese = ChineseUtil.getChinese(csvlist.get(0).trim());
			// 判断清楚特殊符号后的字符串非空
			if (StringUtils.isNotEmpty(chinese)) {
				list.add(chinese);
			}
		}

		List<PrintExcel> printlist = new ArrayList<>();
		for (String str : list) {
			PrintExcel pe = new PrintExcel();
			String segstr = AnalyseUtil.ChineseWordSegmentation(str, true);
			pe.setValue1(str);
			pe.setValue2(segstr); 
			printlist.add(pe); 
		}
		
		String[] headers = new String[]{"诊断结果","词性分析"};

		ExportExcelUtil<PrintExcel> ee = new ExportExcelUtil<>();
		HSSFWorkbook workbook= ee.exportExcel("医生诊断数据词性解析", headers, printlist);

		// 文件下载
		response.setCharacterEncoding("utf-8");
		// 文件名
		String fileName = "医生诊断数据词性解析.xls";
		response.setContentType("application/msexcel");
		response.setBufferSize(10240);

		// 客户端
		String agent = request.getHeader("User-Agent");
		try {
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			String encodefilename = FilenameEncodeUtil.encodeFilename(fileName, agent);
			response.setHeader("Content-Disposition", "attachment;filename=" + encodefilename);
			workbook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public WordSegment getWordSegmentationData(String keyword) {
		String chinese = ChineseUtil.getChinese(keyword.trim());
		String[] stringQuery = AnalyseUtil.analyzeChinese(chinese, true);
		WordSegment ws = new WordSegment();
		ws.setKeyword(keyword);
		ws.setSegment(stringQuery); 
		return ws;
	}

}
