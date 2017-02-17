package com.qybx.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 
 * @ClassName: LuceneUtil
 * @Description: TODO
 * @author leepon1990
 * @date 2016年8月16日 上午10:35:25
 *
 */
public class LuceneUtil {
	
	private static Logger logger = Logger.getLogger(LuceneUtil.class);
	
	private volatile static IndexReader baseReader;
	
	private volatile static IndexReader historyReader;
	
	private volatile static IndexReader icdReader;
	
	private volatile static IndexReader pacxReader;
	
	
	private volatile static IndexSearcher baseSearcher;
	
	private volatile static IndexSearcher historySearcher;
	
	private volatile static IndexSearcher icdSearcher;
	
	private volatile static IndexSearcher pacxSearcher;
	
	

	private static Version version;

	private static Analyzer analyzer;
	
	static {
		
		try {
			
			version = Version.LUCENE_36;
			
			analyzer = new IKAnalyzer();
			
		} catch (Exception e) {
			
			logger.error(e.getMessage());
		}
	}
	
	// 分词器
	public static Analyzer getAnalyzer() {
		return analyzer;
	}

	
	//索引目录
	public static Directory getDirectory(File path){
		Directory directory = null;
		try {
			directory = FSDirectory.open(path);
		} catch (IOException e) {
			logger.info("获取Directory异常，异常信息："+e.getMessage());
		}
		return directory;
	}


	// 提供获取IndexWriterConfig对象
	public static IndexWriterConfig getIndexWriterConfig() {
		IndexWriterConfig iwc = new IndexWriterConfig(version, analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		return iwc;
	}
	

	// 提供获取IndexWriter对象
	public static IndexWriter getIndexWriter(Directory directory) {
		IndexWriter indexWriter = null;
		try {
			IndexWriterConfig iwc = getIndexWriterConfig();
			indexWriter = new IndexWriter(directory, iwc);
		} catch (Exception e) {
			logger.info("获取IndexWriter实例异常，异常信息："+e.getMessage());
		}
		return indexWriter;
	}
	
	/**
	 * 获取IndexReader对象
	 * @param dir
	 * @param enableNRTReader  是否开启NRTReader
	 * @return
	 */
	public static IndexReader getBaseIndexReader(Directory dir,boolean enableNRTReader) {
		if(null == dir) {
			throw new IllegalArgumentException("Directory can not be null!");
		}
		try {
			if(null == baseReader){
				baseReader = IndexReader.open(dir);
			} else {
				if(enableNRTReader && baseReader instanceof IndexReader) {
					//开启近实时Reader,能立即看到动态添加/删除的索引变化
					IndexReader reader_new = IndexReader.openIfChanged(baseReader);
					if(reader_new !=null && reader_new!=baseReader ){
						baseReader=reader_new;
					}
					
				}
			}
		} catch (IOException e) {
			logger.error("获取baseIndexReader实例异常，异常信息："+e.getMessage());
		}
		return baseReader;
	}
	
	/**
	 * 获取IndexReader对象
	 * @param dir
	 * @param enableNRTReader  是否开启NRTReader
	 * @return
	 */
	public static IndexReader getHistoryIndexReader(Directory dir,boolean enableNRTReader) {
		if(null == dir) {
			throw new IllegalArgumentException("Directory can not be null!");
		}
		try {
			if(null == historyReader){
				historyReader = IndexReader.open(dir);
			} else {
				if(enableNRTReader && historyReader instanceof IndexReader) {
					//开启近实时Reader,能立即看到动态添加/删除的索引变化
					IndexReader reader_new = IndexReader.openIfChanged(historyReader);
					if(reader_new !=null && reader_new!=historyReader ){
						historyReader=reader_new;
					}
					
				}
			}
		} catch (IOException e) {
			logger.error("获取historyIndexReader实例异常，异常信息："+e.getMessage());
		}
		return historyReader;
	}
	
	
	/**
	 * 获取IndexReader对象
	 * @param dir
	 * @param enableNRTReader  是否开启NRTReader
	 * @return
	 */
	public static IndexReader getIcdIndexReader(Directory dir,boolean enableNRTReader) {
		if(null == dir) {
			throw new IllegalArgumentException("Directory can not be null!");
		}
		try {
			if(null == icdReader){
				icdReader = IndexReader.open(dir);
			} else {
				if(enableNRTReader && icdReader instanceof IndexReader) {
					//开启近实时Reader,能立即看到动态添加/删除的索引变化
					IndexReader reader_new = IndexReader.openIfChanged(icdReader);
					if(reader_new !=null && reader_new!=icdReader ){
						icdReader=reader_new;
					}
					
				}
			}
		} catch (IOException e) {
			logger.error("获取baseIndexReader实例异常，异常信息："+e.getMessage());
		}
		return icdReader;
	}
	
	
	/**
	 * 获取IndexReader对象
	 * @param dir
	 * @param enableNRTReader  是否开启NRTReader
	 * @return
	 */
	public static IndexReader getPacxIndexReader(Directory dir,boolean enableNRTReader) {
		if(null == dir) {
			throw new IllegalArgumentException("Directory can not be null!");
		}
		try {
			if(null == pacxReader){
				pacxReader = IndexReader.open(dir);
			} else {
				if(enableNRTReader && pacxReader instanceof IndexReader) {
					//开启近实时Reader,能立即看到动态添加/删除的索引变化
					IndexReader reader_new = IndexReader.openIfChanged(pacxReader);
					if(reader_new !=null && reader_new!=pacxReader){
						pacxReader=reader_new;
					}
					
				}
			}
		} catch (IOException e) {
			logger.error("获取baseIndexReader实例异常，异常信息："+e.getMessage());
		}
		return pacxReader;
	}
	
	
	/**
	 * 获取IndexSearcher对象
	 * @param reader 
	 * @return
	 */
	public static IndexSearcher getBaseIndexSearcher(IndexReader reader) {
		if(null == reader) {
			throw new IllegalArgumentException("The indexReader can not be null!");
		}
		if(null == baseSearcher){
			baseSearcher = new IndexSearcher(reader);
		}
		return baseSearcher;
	}
	
	/**
	 * 获取IndexSearcher对象
	 * @param reader 
	 * @return
	 */
	public static IndexSearcher getHistoryIndexSearcher(IndexReader reader) {
		if(null == reader) {
			throw new IllegalArgumentException("The indexReader can not be null!");
		}
		if(null == historySearcher){
			historySearcher = new IndexSearcher(reader);
		}
		return historySearcher;
	}
	
	
	/**
	 * 获取IndexSearcher对象
	 * @param reader 
	 * @return
	 */
	public static IndexSearcher getIcdIndexSearcher(IndexReader reader) {
		if(null == reader) {
			throw new IllegalArgumentException("The indexReader can not be null!");
		}
		if(null == icdSearcher){
			icdSearcher = new IndexSearcher(reader);
		}
		return icdSearcher;
	}
	
	
	/**
	 * 获取IndexSearcher对象
	 * @param reader 
	 * @return
	 */
	public static IndexSearcher getPacxIndexSearcher(IndexReader reader) {
		if(null == reader) {
			throw new IllegalArgumentException("The indexReader can not be null!");
		}
		if(null == pacxSearcher){
			pacxSearcher = new IndexSearcher(reader);
		}
		return pacxSearcher;
	}

	

	/**
	 * 
	 * @Title: buildIndex 
	 * @Description: TODO <构建单文档索引> 
	 * @param doc 
	 * @return void 
	 * @throws
	 */
	public static void buildIndex(Directory directory,Document doc) {
		IndexWriter writer = getIndexWriter(directory);
		try {
			writer.addDocument(doc);
			commit(writer);
		} catch (Exception e) {
			rollback(writer);
			e.printStackTrace();
		} finally {
			close(writer);
		}

	}

	/**
	 * 
	 * @Title: rebuildAllIndex 
	 * @Description: TODO <构建全部文档索引> 
	 * @param doclist
	 * @return void 
	 * @throws
	 */
	public static void buildAllIndex(Directory directory,List<Document> doclist) {
		
		IndexWriter writer = null;
		try {
			writer = getIndexWriter(directory);
			for (Document document : doclist) {
				writer.addDocument(document);
			}
			commit(writer);
		} catch (Exception e) {
			rollback(writer);
			e.printStackTrace();
		} finally {
			close(writer);
		}
	}

	/**
	 * 
	 * @Title: deleteAllIndex 
	 * @Description: TODO <删除全部索引> 
	 * @param void
	 * @throws
	 */
	public static void deleteAllIndex(Directory directory) {

		IndexWriter writer = getIndexWriter(directory);
		try {
			writer.deleteAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(writer);
		}

	}

	/**
	 * 
	 * @Title: deleteIndex 
	 * @Description: TODO <删除索引> 
	 * @param terms 
	 * @return void
	 * @throws
	 */
	public static void deleteIndex(Directory directory,Term... terms) {

		IndexWriter writer = null;
		try {
			writer = getIndexWriter(directory);
			writer.deleteDocuments(terms);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(writer);
		}

	}

	/**
	 * 
	 * @Title: updateIndex 
	 * @Description: TODO <更新文档索引> 
	 * @param term
	 * @param doc 
	 * @return void 
	 * @throws
	 */
	public static void updateIndex(Directory directory,Term term, Document doc) {
		IndexWriter writer = null;
		try {
			writer = getIndexWriter(directory);
			writer.updateDocument(term, doc);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(writer);
		}
	}
	
	/**
	 * 
	 * @Title: searchTotalRecord 
	 * @Description: TODO <索引命中数>
	 * @param query
	 * @return int  
	 * @throws
	 */
	public static int searchTotalRecord(IndexSearcher indexSearcher,Query query) {
		ScoreDoc[] docs = null;
		try {
			TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
			if (topDocs == null || topDocs.scoreDocs == null || topDocs.scoreDocs.length == 0) {
				return 0;
			}
			docs = topDocs.scoreDocs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docs.length;
	}
	
	
	/**
	 * 
	 * @Title: searchRows 
	 * @Description: TODO <索引文档查询>
	 * @param query
	 * @param n <取得分最高的前n条记录>
	 * @return List<Document>  
	 * @throws
	 */
	public static List<Document> searchRows(IndexSearcher indexSearcher,Query query,int n){
		List<Document> doclist = new ArrayList<>();
		try {
			TopDocs topDocs = indexSearcher.search(query,n);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			//logger.info("搜索结果 " + topDocs.totalHits + "条");
			for (ScoreDoc scoreDoc : scoreDocs) {
				int doc = scoreDoc.doc;
				Document document = indexSearcher.doc(doc);
				doclist.add(document);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doclist;
	}
	
	/**
	 * 
	 * @Title: searchRows 
	 * @Description: TODO <索引文档查询并排序>
	 * @param query
	 * @param n 默认取得分最高的前n条记录
	 * @param sort
	 * @return List<Document>  
	 * @throws
	 */
	public static List<Document> searchRows(IndexSearcher indexSearcher,Query query,int n,Sort sort){
		List<Document> doclist = new ArrayList<>();
		try {
			//默认取得分最高的前n条记录
			TopDocs topDocs = indexSearcher.search(query,n,sort);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			//logger.info("搜索结果 " + topDocs.totalHits + "条");
			for (ScoreDoc scoreDoc : scoreDocs) {
				int doc = scoreDoc.doc;
				Document document = indexSearcher.doc(doc);
				doclist.add(document);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doclist;
	}
	
	/**
	 * 
	 * @Title: searchRows 
	 * @Description: TODO <索引文档查询并排序>
	 * @param query
	 * @param n 默认取得分最高的前n条记录
	 * @param sort
	 * @return List<Document>  
	 * @throws
	 */
	public static List<Document> searchRowsFilter(IndexSearcher indexSearcher,Query query,Filter filter,int n,Sort sort){
		List<Document> doclist = new ArrayList<>();
		try {
			//默认取得分最高的前n条记录
			TopDocs topDocs = indexSearcher.search(query,filter,n,sort);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			//logger.info("搜索结果 " + topDocs.totalHits + "条");
			for (ScoreDoc scoreDoc : scoreDocs) {
				int doc = scoreDoc.doc;
				Document document = indexSearcher.doc(doc);
				doclist.add(document);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doclist;
	}

	
	/**
	 * 
	 * @Title: pageQuery 
	 * @Description: TODO <分页查询> 
	 * @param query 
	 * @param start
	 * @param limit 
	 * @return List<Document> 
	 * @throws
	 */
	public static List<Document> pageQuery(IndexSearcher indexSearcher,Query query, int start, int limit) {
		List<Document> doclist = new ArrayList<>();
		if (start <= 0)start = 1;
		try {
			TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			//logger.info("搜索结果 " + topDocs.totalHits + "条");

			// 查询起始记录位置
			int begin = (start - 1) * limit;
			if (begin > topDocs.totalHits) {
				throw new Exception("pageQueryException:起始位置大于总记录数");
			}
			
			ScoreDoc scoreDoc=null;  
			if (begin >0) {
				scoreDoc = scoreDocs[begin-1];
			}
			TopDocs hits = indexSearcher.searchAfter(scoreDoc, query, limit);

			for (int i = 0; i < hits.scoreDocs.length; i++) {
				int doc = hits.scoreDocs[i].doc;
				Document document = indexSearcher.doc(doc);
				doclist.add(document);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doclist;
	}

	/**
	 * 
	 * @Title: pageQuery 
	 * @Description: TODO <分页查询并排序>
	 * @param query
	 * @param start
	 * @param limit
	 * @param sort 
	 * @return List<Document>  
	 * @throws
	 */
	public static List<Document> pageQuery(IndexSearcher indexSearcher,Query query, int start, int limit, Sort sort) {
		
		List<Document> doclist = new ArrayList<>();
		if (start <= 0)start = 1;
		try {
			TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE,sort);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			//logger.info("搜索结果 " + topDocs.totalHits + "条");

			// 查询起始记录位置
			int begin = (start - 1) * limit;
			if (begin > topDocs.totalHits) {
				throw new Exception("pageQueryException:起始位置大于总记录数");
			}
			
			ScoreDoc scoreDoc=null;  
			if (begin >0) {
				scoreDoc = scoreDocs[begin-1];
			}
			TopDocs hits = indexSearcher.searchAfter(scoreDoc, query, limit);

			for (int i = 0; i < hits.scoreDocs.length; i++) {
				int doc = hits.scoreDocs[i].doc;
				Document document = indexSearcher.doc(doc);
				doclist.add(document);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doclist;
	}
	
	

	// 提交IndexWriter
	public static void commit(IndexWriter writer) {

		try {
			if (writer != null) {
				writer.commit();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 回滚IndexWriter
	public static void rollback(IndexWriter writer) {

		try {
			if (writer != null) {
				writer.rollback();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 关闭IndexWriter
	public static void close(IndexWriter writer) {

		try {
			if (writer != null) {
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
