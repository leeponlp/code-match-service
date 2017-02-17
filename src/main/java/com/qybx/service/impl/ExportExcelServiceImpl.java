package com.qybx.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;
import com.qybx.po.ImportData;
import com.qybx.service.ExportExcelService;
import com.qybx.service.IndexService;
import com.qybx.service.LogogramService;
import com.qybx.util.ChineseUtil;
import com.qybx.util.CsvUtil;
import com.qybx.util.FilenameEncodeUtil;
import com.qybx.util.StyleUtil;

/**
 * This class is used for ...
 * 
 * @author leepon1990
 * @version 1.0, 2016年11月18日 下午4:43:36
 */
@Service
public class ExportExcelServiceImpl implements ExportExcelService {

	@Autowired
	IndexService indexService;

	@Autowired
	LogogramService logogramService;

	@Override
	public void downloadICD(HttpServletRequest request, HttpServletResponse response) {

		List<ImportData> logolist = logogramService.selectAll();
		Map<String, String> logomap = new LinkedCaseInsensitiveMap<>();
		for (ImportData importData : logolist) {
			logomap.put(importData.getData1(), importData.getData2());
		}
		String filepath = request.getParameter("filepath");
		String filename = filepath.substring(filepath.lastIndexOf("\\") + 1);
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

		// 分析数据
		List<List<String>> analyselist = indexService.analyseICD2(list);

		// 创建workbook
		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);

		// 设置样式
		CellStyle headstyle = StyleUtil.getStyle(workbook);

		// 创建sheet
		Sheet sheet = workbook.createSheet("诊断数据转码分析");

		// 设置列宽
		sheet.setColumnWidth(0, 8000);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 8000);
		sheet.setColumnWidth(3, 8000);

		// 写数据
		Row headRow = sheet.createRow(0);
		Cell cell0 = headRow.createCell(0);
		cell0.setCellValue("诊断结果");
		cell0.setCellStyle(headstyle);

		Cell cell1 = headRow.createCell(1);
		cell1.setCellValue("转码分析_1");
		cell1.setCellStyle(headstyle);

		Cell cell2 = headRow.createCell(2);
		cell2.setCellValue("转码分析_2");
		cell2.setCellStyle(headstyle);

		Cell cell3 = headRow.createCell(3);
		cell3.setCellValue("转码分析_3");
		cell3.setCellStyle(headstyle);

		for (List<String> alyelist : analyselist) {
			Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);

			if (alyelist.size() <= 0) {
				continue;
			} else {
				for (int i = 0; i < alyelist.size(); i++) {
					String str = alyelist.get(i);
					dataRow.createCell(i).setCellValue(str);
				}
			}
		}

		// 文件下载
		response.setCharacterEncoding("utf-8");
		// 文件名
		String fileName = filename.substring(0,filename.indexOf("."))+".xlsx";
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
	
	public static void main(String[] args) {
		String filename = "诊断数据转码分析.csv";
		String fileName = filename.substring(0,filename.indexOf("."))+".xlsx";
		System.err.println(fileName);
	}

}
