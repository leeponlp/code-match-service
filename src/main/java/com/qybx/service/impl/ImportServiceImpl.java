package com.qybx.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qybx.mapper.Icd9CM3MapperExt;
import com.qybx.mapper.ImportDataMapperExt;
import com.qybx.po.ICD9CM3;
import com.qybx.po.ImportData;
import com.qybx.po.PacxDiagnosis;
import com.qybx.service.ImportService;
import com.qybx.util.CsvUtil;

/**
 * This class is used for ...
 * 
 * @author leepon1990
 * @version 1.0, 2016年11月29日 下午2:03:30
 */
@Service
@Transactional(propagation=Propagation.REQUIRED)
public class ImportServiceImpl implements ImportService {

	@Autowired
	Icd9CM3MapperExt icd9cm3MapperExt;

	@Autowired
	ImportDataMapperExt importDataMapperExt;

	@Override
	public void importICD9CM3(String path) {
		File file = new File(path);
		List<List<String>> csvList = CsvUtil.readCsv(file, 0, "GBK");
		List<ICD9CM3> list = new ArrayList<>();
		for (List<String> csvlist : csvList) {
			ICD9CM3 icd9cm3 = new ICD9CM3();
			String code = csvlist.get(0);
			String name = csvlist.get(1);
			String type = csvlist.get(2);
			icd9cm3.setOperationCode(code);
			icd9cm3.setOperationName(name);
			if ("诊断性操作".equals(StringUtils.trim(type))) {
				icd9cm3.setOperationType("1");
			} else if ("治疗性操作".equals(StringUtils.trim(type))) {
				icd9cm3.setOperationType("2");
			} else {
				icd9cm3.setOperationType("0");
			}
			list.add(icd9cm3);
		}

		icd9cm3MapperExt.addBatchwithICD9CM3(list);

	}

	@Override
	public void importLogogram(String path) {
		File file = new File(path);
		List<List<String>> csvList = CsvUtil.readCsv(file, 0, "GBK");

		List<ImportData> list = new ArrayList<>();
		for (List<String> csvlist : csvList) {

			ImportData importData = new ImportData();
			importData.setData1(csvlist.get(0).trim());
			importData.setData2(csvlist.get(1).trim());

			list.add(importData);
		}

		importDataMapperExt.addBatchwithImportData(list);
	}

	@Override
	public void importPacxDiagnosisData(String path) {

		File file = new File(path);
		List<List<String>> csvList = CsvUtil.readCsv(file, 0, "GBK");

		List<PacxDiagnosis> list = new ArrayList<>();
		
		for (List<String> csvlist : csvList) {
			PacxDiagnosis pd = new PacxDiagnosis();
			pd.setCode(csvlist.get(0).trim());
			pd.setPosition1(csvlist.get(1).trim());
			pd.setPosition2(csvlist.get(2).trim());
			pd.setDiagnosis(csvlist.get(3).trim());
			pd.setClassification(csvlist.get(4).trim());
			
			list.add(pd);
		}
		
		importDataMapperExt.addBatchPacxDiagnosisData(list);
	}

}
