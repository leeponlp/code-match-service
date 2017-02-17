package demo;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;


/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年10月20日 下午5:18:21   
 */
@SuppressWarnings("all")
public class AnalyzerAnalyze {
	
	public static void main(String[] args) {
		
		String text = "1、高血压2、糖尿病";
		Analyzer analyzer3 = new IKAnalyzer();
		testAnalyzer(analyzer3, text);
		
	}
	
	
	private static void testAnalyzer(Analyzer analyzer, String text){
		System.out.println("分词器：" + analyzer.getClass().getSimpleName());
		TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
		tokenStream.addAttribute(TermAttribute.class);
		try {
			while (tokenStream.incrementToken()) {
				TermAttribute termAttribute = tokenStream.getAttribute(TermAttribute.class);
				System.out.println(termAttribute.term());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
