package com.qybx.web.listener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import com.qybx.service.impl.IndexServiceImpl;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年8月25日 下午2:26:18   
 */
public class IndexListener implements ApplicationListener<ContextRefreshedEvent>{
	
	
	private Logger logger = Logger.getLogger(IndexListener.class);


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		logger.info("===================构建索引===================");
		IndexServiceImpl bean = event.getApplicationContext().getBean(IndexServiceImpl.class);
		//bean.buildBaseIndex();
		//bean.buildHistoryIndex();
		//bean.buildHospitalIndex();
		bean.buildICDIndex();
		bean.buildPacxDiagnosisIndex();
		logger.info("===================构建完成===================");
		
	}

}
