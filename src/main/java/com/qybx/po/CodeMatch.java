package com.qybx.po;


/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年8月22日 下午9:50:01   
 */
public class CodeMatch { //implements Comparable<CodeMatch> 
	
	private String itermCode;
	
	private String itermName;

	public String getItermCode() {
		return itermCode;
	}

	public void setItermCode(String itermCode) {
		this.itermCode = itermCode;
	}

	public String getItermName() {
		return itermName;
	}

	public void setItermName(String itermName) {
		this.itermName = itermName;
	}

	@Override
	public String toString() {
		return itermCode + "-" + itermName;
	}

	
	
	public CodeMatch() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CodeMatch(String itermCode, String itermName) {
		super();
		this.itermCode = itermCode;
		this.itermName = itermName;
	}

//	@Override
//	public int compareTo(CodeMatch o) {
//		
//		if (itermName.length()<o.getItermName().length()) {
//			return -1;
//		}else if (itermName.length()>o.getItermName().length()) {
//			return 1;
//		}else{
//			return 0;
//		}
//
//	}
	
	
//	public static void main(String[] args) {
//		List<CodeMatch> list = new ArrayList<>();
//		list.add(new CodeMatch("1", "胆囊息肉"));
//		list.add(new CodeMatch("2", "妊娠合并胆囊息肉"));
//		list.add(new CodeMatch("3", "反流性食管炎"));
//		list.add(new CodeMatch("4", "胃-食管反流性疾病,不伴有食管炎"));
//		list.add(new CodeMatch("5", "食管炎"));
//		
//		Collections.sort(list);
//		
//		for (CodeMatch codeMatch : list) {
//			System.err.println(codeMatch);
//		}
//	}
	
	
	
	

}
