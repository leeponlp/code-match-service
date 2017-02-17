package com.qybx.mapper;

import java.util.List;

import com.qybx.po.ICD9CM3;
import com.qybx.po.IcdIndex;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年11月29日 下午2:10:51   
 */
public interface Icd9CM3MapperExt {

	void addBatchwithICD9CM3(List<ICD9CM3> list);

	List<IcdIndex> selectAll();  

}
