package com.qybx.po;
/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年8月31日 上午10:32:09   
 */
public class HistoryIndex{
	
    private String historyCode;
	
	private String historyName;

	public String getHistoryCode() {
		return historyCode;
	}

	public void setHistoryCode(String historyCode) {
		this.historyCode = historyCode;
	}

	public String getHistoryName() {
		return historyName;
	}

	public void setHistoryName(String historyName) {
		this.historyName = historyName;
	}

	@Override
	public String toString() {
		return  historyCode + "-" + historyName;
	}

	public HistoryIndex() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HistoryIndex(String historyCode, String historyName) {
		super();
		this.historyCode = historyCode;
		this.historyName = historyName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((historyName == null) ? 0 : historyName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoryIndex other = (HistoryIndex) obj;
		if (historyName == null) {
			if (other.historyName != null)
				return false;
		} else if (!historyName.equals(other.historyName))
			return false;
		return true;
	}

		
	

		
	

}
