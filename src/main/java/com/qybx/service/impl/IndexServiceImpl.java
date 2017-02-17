package com.qybx.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qybx.mapper.DrugBaseCodeMapperExt;
import com.qybx.mapper.DrugInListMapperExt;
import com.qybx.mapper.DrugOutListMapperExt;
import com.qybx.mapper.Icd10MapperExt;
import com.qybx.mapper.Icd9CM3MapperExt;
import com.qybx.mapper.PacxDiagnosisMapperExt;
import com.qybx.mapper.ServiceBaseCodeMapperExt;
import com.qybx.mapper.ServiceInListMapperExt;
import com.qybx.mapper.ServiceOutListMapperExt;
import com.qybx.po.BaseIndex;
import com.qybx.po.CodeMatch;
import com.qybx.po.CodeMatchView;
import com.qybx.po.HistoryIndex;
import com.qybx.po.IcdIndex;
import com.qybx.po.MatchView;
import com.qybx.po.PacxDiagnosis;
import com.qybx.po.PacxMatchView;
import com.qybx.service.IndexService;
import com.qybx.util.AnalyseUtil;
import com.qybx.util.ChineseUtil;
import com.qybx.util.CsvUtil;
import com.qybx.util.FilenameEncodeUtil;
import com.qybx.util.LuceneUtil;
import com.qybx.util.StringTool;
import com.qybx.util.StyleUtil;

/**
 * This class is used for ...
 * 
 * @author leepon1990
 * @version 1.0, 2016年8月25日 上午10:07:49
 */
@Service
public class IndexServiceImpl implements IndexService {

	private static Logger logger = Logger.getLogger(IndexServiceImpl.class);

	// 标准对码索引目录
	private static File baseFile;

	// 历史对码索引目录
	private static File historyFile;

	// 医院数据
	private static File hospitalFile;

	private static File icdFile;

	private static File pacxFile;

	static {
		baseFile = new File("index/base");
		historyFile = new File("index/history");
		hospitalFile = new File("index/hospital");
		icdFile = new File("index/icd");
		pacxFile = new File("index/pacx");
		// 创建目录
		mkdirs(baseFile);
		mkdirs(historyFile);
		mkdirs(hospitalFile);
		mkdirs(icdFile);
		mkdirs(pacxFile);

	}

	@Autowired
	DrugBaseCodeMapperExt drugBaseCodeMapperExt;

	@Autowired
	ServiceBaseCodeMapperExt serviceBaseCodeMapperExt;

	@Autowired
	DrugInListMapperExt drugInListMapperExt;

	@Autowired
	DrugOutListMapperExt drugOutListMapperExt;

	@Autowired
	ServiceInListMapperExt serviceInListMapperExt;

	@Autowired
	ServiceOutListMapperExt serviceOutListMapperExt;

	@Autowired
	Icd10MapperExt icd10MapperExt;

	@Autowired
	Icd9CM3MapperExt icd9cm3MapperExt;

	@Autowired
	PacxDiagnosisMapperExt pacxDiagnosisMapperExt;

	@Override
	public void buildBaseIndex() {
		List<BaseIndex> DrugBaseCodes = drugBaseCodeMapperExt.selectAll();
		List<BaseIndex> ServiceBaseCodes = serviceBaseCodeMapperExt.selectAll();

		List<BaseIndex> list = new ArrayList<>();
		list.addAll(DrugBaseCodes);
		list.addAll(ServiceBaseCodes);

		List<Document> doclist = new ArrayList<>();
		for (BaseIndex baseIndex : list) {
			// 创建文档对象
			Document document = new Document();
			// 将索引数据转换成文档对象
			document.add(new Field("baseCode", baseIndex.getBaseCode(), Store.YES, Index.NOT_ANALYZED));
			document.add(new Field("baseName", baseIndex.getBaseName(), Store.YES, Index.ANALYZED));
			doclist.add(document);
		}

		LuceneUtil.buildAllIndex(LuceneUtil.getDirectory(baseFile), doclist);
	}

	@Override
	public void buildICDIndex() {

		List<IcdIndex> icd10list = icd10MapperExt.selectAll();
		// List<IcdIndex> icd9cm3list = icd9cm3MapperExt.selectAll();
		List<IcdIndex> list = new ArrayList<>();
		list.addAll(icd10list);
		// list.addAll(icd9cm3list);
		List<Document> doclist = new ArrayList<>();
		for (IcdIndex icdIndex : list) {
			// 创建文档对象
			Document document = new Document();
			// 将索引数据转换成文档对象
			document.add(new Field("icdCode", icdIndex.getIcdCode(), Store.YES, Index.NOT_ANALYZED));
			document.add(new Field("icdName", icdIndex.getIcdName(), Store.YES, Index.ANALYZED));
			doclist.add(document);
		}

		LuceneUtil.buildAllIndex(LuceneUtil.getDirectory(icdFile), doclist);

	}

	@Override
	public void buildHistoryIndex() {

		List<HistoryIndex> drugInHistory = drugInListMapperExt.selectDrugInHistoryAll();

		List<HistoryIndex> drugOutHistory = drugOutListMapperExt.selectDrugOutHistoryAll();

		List<HistoryIndex> serviceInHistory = serviceInListMapperExt.selectServiceInHistoryAll();

		List<HistoryIndex> serviceOutHistory = serviceOutListMapperExt.selectServiceOutHistoryAll();

		Set<HistoryIndex> set = new HashSet<>();
		set.addAll(drugInHistory);
		set.addAll(drugOutHistory);
		set.addAll(serviceInHistory);
		set.addAll(serviceOutHistory);

		List<Document> doclist = new ArrayList<>();
		for (HistoryIndex historyIndex : set) {
			Document document = new Document();
			if (!StringUtils.isEmpty(historyIndex.getHistoryCode()))
				document.add(new Field("historyCode", historyIndex.getHistoryCode(), Store.YES, Index.NOT_ANALYZED));
			if (!StringUtils.isEmpty(historyIndex.getHistoryName()))
				document.add(new Field("historyName", historyIndex.getHistoryName(), Store.YES, Index.ANALYZED));
			doclist.add(document);
		}

		LuceneUtil.buildAllIndex(LuceneUtil.getDirectory(historyFile), doclist);

	}

	/**
	 * (非 Javadoc)
	 * <p>
	 * Title: buildPacxDiagnosisData
	 * </p>
	 * <p>
	 * Description: 构建平安产险诊断数据索引
	 * </p>
	 * 
	 * @see com.qybx.service.IndexService#buildPacxDiagnosisData()
	 */
	@Override
	public void buildPacxDiagnosisIndex() {
		List<PacxDiagnosis> list = pacxDiagnosisMapperExt.selectAll();
		// List<CodeMatch> matchlist = new ArrayList<>();
		// for (PacxDiagnosis pacxDiagnosis : list) {
		//
		// }

		List<Document> doclist = new ArrayList<>();

		for (PacxDiagnosis pacxDiagnosis : list) {
			Document document = new Document();
			// document.add(new Field("code", pacxDiagnosis.getCode(),
			// Store.YES, Index.NOT_ANALYZED));
			// document.add(new Field("position1", pacxDiagnosis.getPosition1(),
			// Store.YES, Index.NOT_ANALYZED));
			// document.add(new Field("position2", pacxDiagnosis.getPosition2(),
			// Store.YES, Index.NOT_ANALYZED));
			// document.add(new Field("diagnosis", pacxDiagnosis.getDiagnosis(),
			// Store.YES, Index.ANALYZED));
			// document.add(new
			// Field("classification",pacxDiagnosis.getClassification(),
			// Store.YES, Index.ANALYZED));
			document.add(new Field("itermCode", pacxDiagnosis.getCode(), Store.YES, Index.NOT_ANALYZED));
			document.add(
					new Field("itermName", pacxDiagnosis.getDiagnosis() + "|-|" + pacxDiagnosis.getClassification(),
							Store.YES, Index.ANALYZED));
			doclist.add(document);
		}
		LuceneUtil.buildAllIndex(LuceneUtil.getDirectory(pacxFile), doclist);
	}

	@Override
	public void buildHospitalIndex() {

		// 读取文件
		List<List<String>> readCsv = CsvUtil.readCsv(new File("file/住院已结算&住院日清单.csv"), 1, "GBK");
		List<String> hospitalNameList = new ArrayList<>();
		String hospitalName = "";
		for (List<String> list : readCsv) {

			for (int i = 0, l = list.size(); i < l; i++) {
				hospitalName = list.get(0);
			}
			hospitalNameList.add(hospitalName);
		}

		List<Document> doclist = new ArrayList<>();
		for (String str : hospitalNameList) {
			Document document = new Document();
			document.add(new Field("hospitalName", str, Store.YES, Index.ANALYZED));
			doclist.add(document);
		}

		LuceneUtil.buildAllIndex(LuceneUtil.getDirectory(hospitalFile), doclist);

	}

	@Override
	public void exportHospitalAnalyseExcel(HttpServletRequest request, HttpServletResponse response) {
		// 读取文件
		List<List<String>> readCsv = CsvUtil.readCsv(new File("file/平安医院.csv"), 1, "GBK");
		List<String> hospitalNameList = new ArrayList<>();
		String hospitalName = "";
		for (List<String> list : readCsv) {
			hospitalName = list.get(0);
			hospitalNameList.add(hospitalName);
		}
		List<List<String>> analyselist = new ArrayList<>();
		if (!CollectionUtils.isEmpty(hospitalNameList)) {
			for (String str : hospitalNameList) {
				String[] stringQuery = AnalyseUtil.keywordSegmentation("hospitalName", str);
				System.err.println(stringQuery);
				Occur[] occurs = new Occur[stringQuery.length];
				String[] fields = new String[stringQuery.length];
				for (int i = 0; i < stringQuery.length; i++) {
					occurs[i] = Occur.SHOULD;
					fields[i] = "hospitalName";
				}
				Analyzer analyzer = LuceneUtil.getAnalyzer();
				Query query = null;
				try {
					query = MultiFieldQueryParser.parse(Version.LUCENE_36, stringQuery, fields, analyzer);
				} catch (Exception e) {
					e.printStackTrace();
				}
				IndexReader indexReader = LuceneUtil.getBaseIndexReader(LuceneUtil.getDirectory(hospitalFile), true);
				IndexSearcher indexSearcher = LuceneUtil.getBaseIndexSearcher(indexReader);
				List<Document> rows = LuceneUtil.searchRows(indexSearcher, query, 3);
				List<String> list = new ArrayList<>();
				for (Document document : rows) {
					if (!StringUtils.isEmpty(document.get("hospitalName"))) {
						String name = document.get("hospitalName");
						list.add(name);
					}
				}
				list.add(0, str);
				analyselist.add(list);
			}
		}

		// 创建workbook
		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);

		// 设置样式
		CellStyle headstyle = StyleUtil.getStyle(workbook);

		// 创建sheet
		Sheet sheet = workbook.createSheet("保险公司医院名对码");

		// 设置列宽
		sheet.setColumnWidth(0, 8000);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 8000);
		sheet.setColumnWidth(3, 8000);

		// 写数据
		Row headRow = sheet.createRow(0);
		Cell cell0 = headRow.createCell(0);
		cell0.setCellValue("hospitalName");
		cell0.setCellStyle(headstyle);

		Cell cell1 = headRow.createCell(1);
		cell1.setCellValue("matchName");
		cell1.setCellStyle(headstyle);

		Cell cell2 = headRow.createCell(2);
		cell2.setCellValue("matchName");
		cell2.setCellStyle(headstyle);

		Cell cell3 = headRow.createCell(3);
		cell3.setCellValue("matchName");
		cell3.setCellStyle(headstyle);

		for (List<String> list : analyselist) {
			Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);

			if (list.size() <= 0) {
				continue;
			} else {
				for (int i = 0; i < list.size(); i++) {
					String str = list.get(i);
					dataRow.createCell(i).setCellValue(str);
				}
			}
		}

		// 文件下载
		response.setCharacterEncoding("utf-8");
		// 文件名
		String filename = "医院数据对码分析报告.xlsx";
		// 要下载的文件mimeType类型
		String mimeType = request.getSession().getServletContext().getMimeType(filename);
		response.setContentType(mimeType);
		response.setBufferSize(10240);

		// 客户端
		String agent = request.getHeader("User-Agent");
		try {
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			String encodefilename = FilenameEncodeUtil.encodeFilename(filename, agent);
			response.setHeader("Content-Disposition", "attachment;filename=" + encodefilename);
			workbook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exportAnalyseExcel(HttpServletRequest request, HttpServletResponse response) {

		// 读取文件
		List<List<String>> readCsv = CsvUtil.readCsv(new File("file/BYZ.csv"), 1, "GBK");
		List<CodeMatch> hmlist = new ArrayList<>();
		for (List<String> list : readCsv) {
			CodeMatch ci = new CodeMatch();
			for (int i = 0, l = list.size(); i < l; i++) {
				ci.setItermCode(list.get(0));
				ci.setItermName(list.get(1));
			}
			hmlist.add(ci);
		}

		// 分析数据
		List<List<CodeMatch>> analyselist = new ArrayList<>();
		if (!CollectionUtils.isEmpty(hmlist)) {
			for (CodeMatch hm : hmlist) {
				String str = hm.getItermName();
				String[] stringQuery = AnalyseUtil.keywordSegmentation("baseName", str);
				Occur[] occurs = new Occur[stringQuery.length];
				String[] fields = new String[stringQuery.length];
				for (int i = 0; i < stringQuery.length; i++) {
					occurs[i] = Occur.SHOULD;
					fields[i] = "baseName";
				}
				Analyzer analyzer = LuceneUtil.getAnalyzer();
				Query query = null;
				try {
					query = MultiFieldQueryParser.parse(Version.LUCENE_36, stringQuery, fields, analyzer);
				} catch (Exception e) {
					e.printStackTrace();
				}
				IndexReader indexReader = LuceneUtil.getBaseIndexReader(LuceneUtil.getDirectory(baseFile), true);
				IndexSearcher indexSearcher = LuceneUtil.getBaseIndexSearcher(indexReader);
				List<Document> rows = LuceneUtil.searchRows(indexSearcher, query, 3);
				List<CodeMatch> list = new ArrayList<>();
				for (Document document : rows) {
					CodeMatch cm = new CodeMatch();
					if (!StringUtils.isEmpty(document.get("baseCode")))
						cm.setItermCode(document.get("baseCode"));
					if (!StringUtils.isEmpty(document.get("baseName")))
						cm.setItermName(document.get("baseName"));
					list.add(cm);
				}
				list.add(0, hm);
				analyselist.add(list);
			}

		}

		// 创建workbook
		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);

		// 设置样式
		CellStyle headstyle = StyleUtil.getStyle(workbook);

		// 创建sheet
		Sheet sheet = workbook.createSheet("对码信息");

		// 设置列宽
		sheet.setColumnWidth(0, 8000);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 8000);
		sheet.setColumnWidth(3, 8000);
		sheet.setColumnWidth(4, 8000);
		sheet.setColumnWidth(5, 8000);
		sheet.setColumnWidth(6, 8000);
		sheet.setColumnWidth(7, 8000);

		// 写数据
		Row headRow = sheet.createRow(0);
		Cell cell0 = headRow.createCell(0);
		cell0.setCellValue("hospitalCode");
		cell0.setCellStyle(headstyle);

		Cell cell1 = headRow.createCell(1);
		cell1.setCellValue("hospitalName");
		cell1.setCellStyle(headstyle);

		Cell cell2 = headRow.createCell(2);
		cell2.setCellValue("baseCode");
		cell2.setCellStyle(headstyle);

		Cell cell3 = headRow.createCell(3);
		cell3.setCellValue("baseName");
		cell3.setCellStyle(headstyle);

		Cell cell4 = headRow.createCell(4);
		cell4.setCellValue("baseCode");
		cell4.setCellStyle(headstyle);

		Cell cell5 = headRow.createCell(5);
		cell5.setCellValue("baseName");
		cell5.setCellStyle(headstyle);

		Cell cell6 = headRow.createCell(6);
		cell6.setCellValue("baseCode");
		cell6.setCellStyle(headstyle);

		Cell cell7 = headRow.createCell(7);
		cell7.setCellValue("baseName");
		cell7.setCellStyle(headstyle);

		for (List<CodeMatch> list : analyselist) {
			Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);

			if (list.size() <= 0) {
				continue;
			} else {
				for (int i = 0; i < list.size(); i++) {
					CodeMatch codeMatch = list.get(i);
					dataRow.createCell(i * 2).setCellValue(codeMatch.getItermCode());
					dataRow.createCell(i * 2 + 1).setCellValue(codeMatch.getItermName());
				}
			}
		}

		// 文件下载
		response.setCharacterEncoding("utf-8");
		// 文件名
		String filename = "对码分析报告.xlsx";
		// 要下载的文件mimeType类型
		String mimeType = request.getSession().getServletContext().getMimeType(filename);
		response.setContentType(mimeType);
		response.setBufferSize(10240);

		// 客户端
		String agent = request.getHeader("User-Agent");
		try {
			OutputStream out = new BufferedOutputStream(response.getOutputStream());
			String encodefilename = FilenameEncodeUtil.encodeFilename(filename, agent);
			response.setHeader("Content-Disposition", "attachment;filename=" + encodefilename);
			workbook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<CodeMatch> searchBase(String keyword) {

		String[] stringQuery = AnalyseUtil.keywordSegmentation("baseName", keyword);
		Occur[] occurs = new Occur[stringQuery.length];
		String[] fields = new String[stringQuery.length];
		for (int i = 0; i < stringQuery.length; i++) {
			occurs[i] = Occur.SHOULD;
			fields[i] = "baseName";
		}
		// 获取分词器
		Analyzer analyzer = LuceneUtil.getAnalyzer();
		Query query = null;
		try {
			query = MultiFieldQueryParser.parse(Version.LUCENE_36, stringQuery, fields, analyzer);
		} catch (Exception e) {
			logger.error("获取Query对象异常，异常信息：" + e.getMessage());
		}
		IndexReader indexReader = LuceneUtil.getBaseIndexReader(LuceneUtil.getDirectory(baseFile), true);
		IndexSearcher indexSearcher = LuceneUtil.getBaseIndexSearcher(indexReader);
		List<Document> rows = LuceneUtil.searchRows(indexSearcher, query, 3);

		List<CodeMatch> list = new ArrayList<>();
		for (Document document : rows) {
			CodeMatch cm = new CodeMatch();
			if (!StringUtils.isEmpty(document.get("baseCode")))
				cm.setItermCode(document.get("baseCode"));
			if (!StringUtils.isEmpty(document.get("baseName")))
				cm.setItermName(document.get("baseName"));
			list.add(cm);
		}

		return list;
	}

	@Override
	public List<CodeMatch> searchHistory(String keyword) {

		String[] stringQuery = AnalyseUtil.keywordSegmentation("historyName", keyword);
		Occur[] occurs = new Occur[stringQuery.length];
		String[] fields = new String[stringQuery.length];
		for (int i = 0; i < stringQuery.length; i++) {
			occurs[i] = Occur.SHOULD;
			fields[i] = "historyName";
		}
		// 获取分词器
		Analyzer analyzer = LuceneUtil.getAnalyzer();
		Query query = null;
		try {
			query = MultiFieldQueryParser.parse(Version.LUCENE_36, stringQuery, fields, analyzer);
		} catch (Exception e) {
			logger.error("获取Query对象异常，异常信息：" + e.getMessage());
		}

		IndexReader indexReader = LuceneUtil.getHistoryIndexReader(LuceneUtil.getDirectory(historyFile), true);
		IndexSearcher indexSearcher = LuceneUtil.getHistoryIndexSearcher(indexReader);
		List<Document> rows = LuceneUtil.searchRows(indexSearcher, query, 3);

		List<CodeMatch> list = new ArrayList<>();
		for (Document document : rows) {
			CodeMatch cm = new CodeMatch();
			if (!StringUtils.isEmpty(document.get("historyCode")))
				cm.setItermCode(document.get("historyCode"));
			if (!StringUtils.isEmpty(document.get("historyName")))
				cm.setItermName(document.get("historyName"));
			list.add(cm);
		}

		return list;

	}

	@Override
	public List<CodeMatch> searchICD(String keyword) {

		String chinese = ChineseUtil.getChinese(keyword);
		String[] stringQuery = AnalyseUtil.analyzeChinese(chinese, true);
		Occur[] occurs = new Occur[stringQuery.length];
		String[] fields = new String[stringQuery.length];
		for (int i = 0; i < stringQuery.length; i++) {
			occurs[i] = Occur.SHOULD;
			fields[i] = "icdName";
		}
		// 获取分词器
		Analyzer analyzer = LuceneUtil.getAnalyzer();
		Query query = null;
		Filter filter = null;
		try {
			query = MultiFieldQueryParser.parse(Version.LUCENE_36, stringQuery, fields, occurs, analyzer);
			filter = new CachingWrapperFilter(new QueryWrapperFilter(query));
		} catch (Exception e) {
			logger.error("获取Query对象异常，异常信息：" + e.getMessage());
		}

		IndexReader indexReader = LuceneUtil.getIcdIndexReader(LuceneUtil.getDirectory(icdFile), true);
		IndexSearcher indexSearcher = LuceneUtil.getIcdIndexSearcher(indexReader);
		// Sort sort = new Sort(new SortField("icdName", new
		// MyFieldComparatorSource(), false));
		// 评分降序，评分一样时后索引的排前面
		Sort sort = new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("icdName", SortField.DOC, true) });
		List<Document> rows = LuceneUtil.searchRowsFilter(indexSearcher, query, filter, 10, sort);
		List<CodeMatch> list = new ArrayList<>();
		for (Document document : rows) {
			CodeMatch cm = new CodeMatch();
			if (!StringUtils.isEmpty(document.get("icdCode")))
				cm.setItermCode(document.get("icdCode"));
			if (!StringUtils.isEmpty(document.get("icdName")))
				cm.setItermName(document.get("icdName"));
			list.add(cm);
		}

		// Collections.sort(list);

		return list;

	}

	@Override
	public List<List<String>> searchICD2(String keyword) {
		List<String> resultlist = new ArrayList<>();
		List<List<String>> analyselist = new ArrayList<>();
		String chinese = ChineseUtil.getChinese(keyword);
		String[] stringQuery = AnalyseUtil.analyzeChinese(chinese, true);
		System.err.println(Arrays.asList(stringQuery).toString());
		Occur[] occurs = new Occur[stringQuery.length];
		String[] fields = new String[stringQuery.length];
		for (int i = 0; i < stringQuery.length; i++) {
			occurs[i] = Occur.SHOULD;
			fields[i] = "icdName";
		}

		Analyzer analyzer = LuceneUtil.getAnalyzer();
		Query query1 = null;
		try {
			query1 = MultiFieldQueryParser.parse(Version.LUCENE_36, stringQuery, fields, occurs,analyzer);
		} catch (ParseException e) {
			logger.info("MultiFieldQueryParser happened ParseException"+e.getMessage());
		}
		IndexReader indexReader = LuceneUtil.getIcdIndexReader(LuceneUtil.getDirectory(icdFile), true);
		IndexSearcher indexSearcher = LuceneUtil.getIcdIndexSearcher(indexReader);
		Sort sort = new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("icdName", SortField.DOC, true) });
		List<Document> mqrows = LuceneUtil.searchRows(indexSearcher, query1, 10, sort);
		if (CollectionUtils.isNotEmpty(mqrows)) {
			for (Document document : mqrows) {
				if (keyword.contains(document.get("icdName")) || keyword.equalsIgnoreCase(document.get("icdName"))) {
					String icdCode = document.get("icdCode");
					String icdName = document.get("icdName");
					if (resultlist.contains(icdCode+"-"+icdName)) {
						continue;
					}else{
						float similarity = StringTool.getSimilarity(keyword, icdName);
						if (similarity>=0.8f) {
							resultlist.add(document.get("icdCode") + "-" + document.get("icdName"));
							break;
						}
					}
				}
			}
		}
		
		try {
			for (String string : stringQuery) {
				BooleanQuery query = new BooleanQuery();
				Query query2 = new WildcardQuery(new Term("icdName", "*" + string + "*"));
				Query query3 = new FuzzyQuery(new Term("icdName", string),0.75f);
				query.add(query2, Occur.SHOULD);
				query.add(query3, Occur.SHOULD);
				List<Document> rows = LuceneUtil.searchRows(indexSearcher, query, 10, sort);
				if (CollectionUtils.isNotEmpty(rows)) {
					for (Document document : rows) {
						if (keyword.contains(document.get("icdName"))|| string.equalsIgnoreCase(document.get("icdName"))) {
							String icdCode = document.get("icdCode");
							String icdName = document.get("icdName");
							if (resultlist.contains(icdCode+"-"+icdName)) {
								continue;
							}else{
								float similarity = StringTool.getSimilarity(string, icdName);
								if (similarity>=0.8f) {
									resultlist.add(document.get("icdCode") + "-" + document.get("icdName"));
									break;
								}
							}
						}
					}
				}
			}
			resultlist.add(0, keyword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		analyselist.add(resultlist);

		return analyselist;

	}

	@Override
	public List<PacxMatchView> searchPacxDia(String keyword) {

		String[] stringQuery = AnalyseUtil.analyzeChinese(keyword, true);
		Occur[] occurs = new Occur[stringQuery.length];
		String[] fields = new String[stringQuery.length];
		for (int i = 0; i < stringQuery.length; i++) {
			occurs[i] = Occur.SHOULD;
			fields[i] = "itermName";
		}
		// 获取分词器
		Analyzer analyzer = LuceneUtil.getAnalyzer();
		Query query = null;
		Filter filter = null;
		try {
			// QueryParser queryParser = new QueryParser(Version.LUCENE_36,
			// "itermName", analyzer);
			// query = queryParser.parse(keyword);

			query = MultiFieldQueryParser.parse(Version.LUCENE_36, stringQuery, fields, occurs, analyzer);
			filter = new CachingWrapperFilter(new QueryWrapperFilter(query));
		} catch (Exception e) {
			logger.error("获取Query对象异常，异常信息：" + e.getMessage());
		}

		IndexReader indexReader = LuceneUtil.getPacxIndexReader(LuceneUtil.getDirectory(pacxFile), true);
		IndexSearcher indexSearcher = LuceneUtil.getPacxIndexSearcher(indexReader);
		// 评分降序，评分一样时后索引的排前面
		Sort sort = new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("itermName", SortField.DOC, true) });
		List<Document> rows = LuceneUtil.searchRowsFilter(indexSearcher, query, filter, 10, sort);
		List<PacxMatchView> list = new ArrayList<>();
		for (Document document : rows) {
			PacxMatchView pd = new PacxMatchView();
			if (!StringUtils.isEmpty(document.get("itermCode")))
				pd.setCode(document.get("itermCode"));
			if (!StringUtils.isEmpty(document.get("itermName"))) {
				String[] split = StringUtils.split(document.get("itermName"), "|-|");
				if (split.length == 2) {
					pd.setDiagnosis(split[0]);
					pd.setClassification(split[1]);
				} else {
					pd.setDiagnosis(split[0]);
					pd.setClassification("");
				}
			}
			list.add(pd);
		}
		return list;
	}

	@Override
	public MatchView match(String keyword) {

		String[] stringQuery = AnalyseUtil.keywordSegmentation("baseName", keyword);
		Occur[] occurs = new Occur[stringQuery.length];
		String[] fields = new String[stringQuery.length];
		for (int i = 0; i < stringQuery.length; i++) {
			occurs[i] = Occur.SHOULD;
			fields[i] = "baseName";
		}
		// 获取分词器
		Analyzer analyzer = LuceneUtil.getAnalyzer();
		Query query = null;
		try {
			query = MultiFieldQueryParser.parse(Version.LUCENE_36, stringQuery, fields, analyzer);
		} catch (Exception e) {
			logger.error("获取Query对象异常，异常信息：" + e.getMessage());
		}
		IndexReader indexReader = LuceneUtil.getHistoryIndexReader(LuceneUtil.getDirectory(baseFile), true);
		IndexSearcher indexSearcher = LuceneUtil.getBaseIndexSearcher(indexReader);
		List<Document> rows = LuceneUtil.searchRows(indexSearcher, query, 3);

		MatchView mv = new MatchView();

		if (rows.size() > 0 && rows.size() <= 3) {

			if (null != rows.get(0)) {
				if (!StringUtils.isEmpty(rows.get(0).get("baseCode")))
					mv.setMatchCode_1(rows.get(0).get("baseCode"));
				if (!StringUtils.isEmpty(rows.get(0).get("baseName")))
					mv.setMatchName_1(rows.get(0).get("baseName"));
			}
			if (null != rows.get(1)) {
				if (!StringUtils.isEmpty(rows.get(1).get("baseCode")))
					mv.setMatchCode_2(rows.get(1).get("baseCode"));
				if (!StringUtils.isEmpty(rows.get(1).get("baseName")))
					mv.setMatchName_2(rows.get(1).get("baseName"));
			}
			if (null != rows.get(2)) {
				if (!StringUtils.isEmpty(rows.get(2).get("baseCode")))
					mv.setMatchCode_3(rows.get(2).get("baseCode"));
				if (!StringUtils.isEmpty(rows.get(2).get("baseName")))
					mv.setMatchName_3(rows.get(2).get("baseName"));
			}

		}

		return mv;
	}

	@Override
	public List<CodeMatchView> match(List<CodeMatch> list) {

		List<CodeMatchView> matchViews = new ArrayList<>();

		if (!CollectionUtils.isEmpty(list)) {

			for (CodeMatch codeMatch : list) {

				CodeMatchView codeMatchView = new CodeMatchView();

				String itermCode = codeMatch.getItermCode();

				String itermName = codeMatch.getItermName();

				MatchView match = match(itermName);

				codeMatchView.setItermCode(itermCode);
				codeMatchView.setItermName(itermName);
				if (!StringUtils.isEmpty(match.getMatchCode_1()))
					codeMatchView.setMatchCode_1(match.getMatchCode_1());
				if (!StringUtils.isEmpty(match.getMatchName_1()))
					codeMatchView.setMatchName_1(match.getMatchName_1());
				if (!StringUtils.isEmpty(match.getMatchCode_2()))
					codeMatchView.setMatchCode_2(match.getMatchCode_2());
				if (!StringUtils.isEmpty(match.getMatchName_2()))
					codeMatchView.setMatchName_2(match.getMatchName_2());
				if (!StringUtils.isEmpty(match.getMatchCode_3()))
					codeMatchView.setMatchCode_3(match.getMatchCode_3());
				if (!StringUtils.isEmpty(match.getMatchName_3()))
					codeMatchView.setMatchName_3(match.getMatchName_3());

				matchViews.add(codeMatchView);

			}
		}

		return matchViews;
	}

	// 创建目录
	private static void mkdirs(File file) {
		if (file.exists()) {
			for (File subFile : file.listFiles()) {
				subFile.delete();
			}
		} else {
			file.mkdirs();
		}
	}

	@Override
	public List<List<String>> analyseICD(List<String> list) {

		List<List<String>> analyselist = new ArrayList<>();
		if (!CollectionUtils.isEmpty(list)) {
			Query query = null;
			for (String str : list) {
				if (StringUtils.isNotEmpty(StringUtils.trim(str))) {
					String[] stringQuery = null;
					Occur[] occurs = null;
					String[] fields = null;

					try {
						stringQuery = AnalyseUtil.analyzeChinese(str, true);
						occurs = new Occur[stringQuery.length];
						fields = new String[stringQuery.length];
						for (int i = 0; i < stringQuery.length; i++) {
							occurs[i] = Occur.SHOULD;
							fields[i] = "icdName";
						}
						Analyzer analyzer = LuceneUtil.getAnalyzer();
						query = MultiFieldQueryParser.parse(Version.LUCENE_36, stringQuery, fields, occurs, analyzer);
					} catch (Exception e) {
						System.err.println(str);
						e.printStackTrace();
					}
				}
				IndexReader indexReader = LuceneUtil.getIcdIndexReader(LuceneUtil.getDirectory(icdFile), true);
				IndexSearcher indexSearcher = LuceneUtil.getIcdIndexSearcher(indexReader);
				Sort sort = new Sort(
						new SortField[] { SortField.FIELD_SCORE, new SortField("icdName", SortField.DOC, true) });
				List<Document> rows = LuceneUtil.searchRows(indexSearcher, query, 3, sort);

				List<String> matchedlist = new ArrayList<>();
				for (Document document : rows) {
					if (!StringUtils.isEmpty(document.get("icdName"))) {
						String code = document.get("icdCode");
						String name = document.get("icdName");
						matchedlist.add(code + "|-|" + name);
					}
				}
				matchedlist.add(0, str);
				analyselist.add(matchedlist);
			}
		}

		return analyselist;

	}

	@Override
	public List<List<String>> analyseICD2(List<String> list) {

		List<List<String>> analyselist = new ArrayList<>();
		if (!CollectionUtils.isEmpty(list)) {
			for (String str : list) {
				List<String> resultlist = new ArrayList<>();
				String chinese = ChineseUtil.getChinese(str);
				String[] stringQuery = AnalyseUtil.analyzeChinese(chinese, true);
				//---------------------多字段多域方式检索-------------------------------------
				Occur[] occurs = new Occur[stringQuery.length];
				String[] fields = new String[stringQuery.length];
				for (int i = 0; i < stringQuery.length; i++) {
					occurs[i] = Occur.SHOULD;
					fields[i] = "icdName";
				}
				Analyzer analyzer = LuceneUtil.getAnalyzer();
				Query query1 = null;
				try {
					query1 = MultiFieldQueryParser.parse(Version.LUCENE_36, stringQuery, fields, occurs,analyzer);
				} catch (ParseException e) {
					logger.info("MultiFieldQueryParser happened ParseException"+e.getMessage());
				}
				IndexReader indexReader = LuceneUtil.getIcdIndexReader(LuceneUtil.getDirectory(icdFile), true);
				IndexSearcher indexSearcher = LuceneUtil.getIcdIndexSearcher(indexReader);
				Sort sort = new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("icdName", SortField.DOC, true) });
				List<Document> mqrows = LuceneUtil.searchRows(indexSearcher, query1, 10, sort);
				if (CollectionUtils.isNotEmpty(mqrows)) {
					for (Document document : mqrows) {
						if (str.contains(document.get("icdName")) || str.equalsIgnoreCase(document.get("icdName"))) {
							String icdCode = document.get("icdCode");
							String icdName = document.get("icdName");
							if (resultlist.contains(icdCode+"-"+icdName)) {
								continue;
							}else{
								float similarity = StringTool.getSimilarity(str, icdName);
								if (similarity>=0.5f) {
									resultlist.add(document.get("icdCode") + "-" + document.get("icdName"));
									break;
								}
							}
						}
					}
				
				}
				//----------------------模糊匹配和相似度匹配检索--------------------------------
				try {
					for (String string : stringQuery) {
						BooleanQuery query = new BooleanQuery();
						Query query2 = new WildcardQuery(new Term("icdName", "*" + string + "*"));
						Query query3 = new FuzzyQuery(new Term("icdName", string));
						query.add(query2, Occur.SHOULD);
						query.add(query3, Occur.SHOULD);
						List<Document> bqrows = LuceneUtil.searchRows(indexSearcher, query, 10, sort);
						if (CollectionUtils.isNotEmpty(bqrows)) {
							for (Document document : bqrows) {
								if (str.contains(document.get("icdName")) || string.equalsIgnoreCase(document.get("icdName"))) {
									String icdCode = document.get("icdCode");
									String icdName = document.get("icdName");
									if (resultlist.contains(icdCode+"-"+icdName)) {
										continue;
									}else{
										float similarity = StringTool.getSimilarity(string, icdName);
										if (similarity>=0.85f) {
											resultlist.add(document.get("icdCode") + "-" + document.get("icdName"));
											break;
										}
									}
								}
							}
						}
					}
					resultlist.add(0, str);
				} catch (Exception e) {
					e.printStackTrace();
				}
				analyselist.add(resultlist);

			}
		}

		return analyselist;

	}

	@Override
	public List<CodeMatch> queryView(String keyword) {

		String chinese = ChineseUtil.getChinese(keyword);
		System.err.println(chinese);
		String[] stringQuery = AnalyseUtil.analyzeChinese(chinese, true);

		IndexReader indexReader = LuceneUtil.getIcdIndexReader(LuceneUtil.getDirectory(icdFile), true);
		IndexSearcher indexSearcher = LuceneUtil.getIcdIndexSearcher(indexReader);
		Sort sort = new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("icdName", SortField.DOC, true) });

		List<CodeMatch> resultlist = new ArrayList<>();
		// QueryParser queryParser = new QueryParser(Version.LUCENE_36,
		// "icdName", LuceneUtil.getAnalyzer());
		try {
			for (String string : stringQuery) {
				BooleanQuery query = new BooleanQuery();
				Query query1 = new WildcardQuery(new Term("icdName", "*" + string + "*"));
				Query query2 = new FuzzyQuery(new Term("icdName", string));
				// Query query2 = queryParser.parse(string);
				query.add(query1, Occur.SHOULD);
				query.add(query2, Occur.SHOULD);
				List<Document> rows = LuceneUtil.searchRows(indexSearcher, query, 10, sort);
				CodeMatch cm = new CodeMatch();
				if (CollectionUtils.isNotEmpty(rows)) {
					for (Document document : rows) {
						if (string.contains(document.get("icdName"))
								|| string.equalsIgnoreCase(document.get("icdName"))) {
							cm.setItermCode(document.get("icdCode"));
							cm.setItermName(document.get("icdName"));
							resultlist.add(cm);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultlist;
	}

}
