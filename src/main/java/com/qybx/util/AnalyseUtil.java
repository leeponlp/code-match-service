package com.qybx.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

/**
 * This class is used for ...
 * 
 * @author leepon1990
 * @version 1.0, 2016年8月23日 上午9:15:27
 */
public class AnalyseUtil {
	
	private static Logger logger = Logger.getLogger(AnalyseUtil.class);

	/**
	 * 
	 * @Title: analyzeChinese
	 * @Description: TODO <进行中文拆分>
	 * @param input
	 * @param smart
	 *            true-智能分词 false-细粒度
	 * @throws IOException
	 * @return String
	 */
	public static String[] analyzeChinese(String keyword, boolean smart) {
		StringBuffer sb = new StringBuffer();
		String[] split = null;
		List<String> list = new ArrayList<>();
		try {
			StringReader reader = new StringReader(keyword.trim());
			IKSegmenter ikSeg = new IKSegmenter(reader, smart);
			for (Lexeme lexeme = ikSeg.next(); lexeme != null; lexeme = ikSeg.next()) {
				sb.append(lexeme.getLexemeText()).append(" ");
			}
			if (StringUtils.isNotEmpty(sb.toString())) {
				split = sb.toString().split(" ");
				for (String str : split) {
					if (str.getBytes().length == 1) {
						continue;
					} else {
						list.add(str);
					}
				}
				split= list.toArray(new String[list.size()]);
			} else {
				split = new String[] { keyword };
			}

		} catch (Exception e) {
			logger.info("中文分词出现异常："+e.getMessage());
		}
		return split;
	}
	
	/**
	 * 
	 * @Title: ChineseWordSegmentation 
	 * @Description: TODO <中文分词>
	 * @param keyword
	 * @param smart
	 * @return String  
	 * @throws
	 */
	public static String ChineseWordSegmentation(String keyword, boolean smart) {
		StringBuffer sb = new StringBuffer();
		try {
			StringReader reader = new StringReader(keyword.trim());
			IKSegmenter ikSeg = new IKSegmenter(reader, smart);
			for (Lexeme lexeme = ikSeg.next(); lexeme != null; lexeme = ikSeg.next()) {
				sb.append(lexeme.getLexemeText()).append("  ");
			}

		} catch (Exception e) {
			logger.info("中文分词出现异常："+e.getMessage());
		}
		return sb.toString();
	}

	/**
	 * 
	 * @Title: getTokenStream @Description: TODO
	 * <采用IK分词器，并返回TokenStream，将对象以Reader的方式输入分词为fieldName字段> @param
	 * input @param fieldName <建索引的时候对应的字段名> @return TokenStream @throws
	 */
	private static TokenStream getTokenStream(String fieldName, String input) {
		Analyzer analyzer = LuceneUtil.getAnalyzer();
		TokenStream tokenStream = analyzer.tokenStream(fieldName, new StringReader(input));
		return tokenStream;
	}

	/**
	 * 
	 * @Title: keywordSegmentation @Description: TODO
	 * <将TokenStream拼成一个字符串数组，交给IndexSearcher来处理> @param @param
	 * fieldName @param @param keyword @param @return @return String[] @throws
	 */
	public static String[] keywordSegmentation(String fieldName, String keyword) {
		TokenStream ts = getTokenStream(fieldName, keyword);
		List<String> list = null;
		String[] array = null;
		try {
			CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
			ts.reset();
			list = new ArrayList<>();
			while (ts.incrementToken()) {
				String token = termAttr.toString();
				list.add(token);
			}
			if (!CollectionUtils.isEmpty(list)) {
				array = list.toArray(new String[list.size()]);
			}
			ts.end();
			ts.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return array;
	}

	public static void main(String[] args) {
		String keyword = "胆囊炎胆囊结石";
		String[] analyzeChinese = analyzeChinese(keyword, true);
		for (String string : analyzeChinese) {
			System.err.println(string);
		}
	}

}
