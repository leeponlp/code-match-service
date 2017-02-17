package com.qybx.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**   
 * This class is used for <平安产险诊断结果数据表>
 * @author leepon1990  
 * @version   
 *       1.0, 2016年12月2日 下午2:07:26   
 */

@Getter
@Setter
@ToString
public class PacxDiagnosis {
	
	//编码
	private String code;
	
	//部位一
	private String position1;
	
	//部位二
	private String position2;
	
	//诊断
	private String diagnosis;
	
	//分类
	private String classification;
	
	public PacxDiagnosis(){
		this.code="";
		this.position1="";
		this.position2="";
		this.diagnosis="";
		this.classification="";
	}

}
