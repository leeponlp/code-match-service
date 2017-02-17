package com.qybx.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;


/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年12月8日 上午9:29:34   
 */
public class ChineseUtil {
	
	public static String getChinese(String str){
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		char[] charArray = str.toCharArray();
		for (char c : charArray) {
			if (Pattern.matches("[\u4e00-\u9fa5a-zA-Z0-9]", String.valueOf(c))) {
				sb.append(c);
			}
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String chinese = getChinese("1，支气管肺炎2、急性上呼吸道感染、急性单纯性胃炎3，甲状腺Ca,4成人发病型糖尿病其他特殊类型糖尿病");
		System.err.println(chinese);
		String[] strings = AnalyseUtil.analyzeChinese(chinese, true);
		List<String> list = new ArrayList<>();
		for (String str : strings) {
			if (str.getBytes().length==1) {
				continue;
			}else{
				list.add(str);
			}
		}
		System.err.println(list);
		BooleanQuery bq = new BooleanQuery();
		for (String str : list) {
			Query query = new TermQuery(new Term("icdName", str));
			bq.add(query, Occur.SHOULD);
		}
		
		
	}

}
