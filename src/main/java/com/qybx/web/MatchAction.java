package com.qybx.web;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.qybx.po.CodeMatch;
import com.qybx.po.CodeMatchView;
import com.qybx.po.MatchView;
import com.qybx.po.PacxMatchView;
import com.qybx.service.IndexService;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年8月22日 下午6:50:27   
 */

@Controller
public class MatchAction {
	
	@Autowired
	IndexService indexService;
	
	
	@RequestMapping("/query/base/{keyword}")
	@ResponseBody
	public List<CodeMatch> searchBase(@PathVariable String keyword){
		
		return indexService.searchBase(keyword);
		
	}
	
	
	@RequestMapping("/query/history/{keyword}")
	@ResponseBody
	public List<CodeMatch> searchHistory(@PathVariable String keyword){
		
		return indexService.searchHistory(keyword);
		
	}
	
	
	@RequestMapping("/query/icd/{keyword}")
	@ResponseBody
	public List<CodeMatch> searchICD(@PathVariable String keyword){
		
		return indexService.searchICD(keyword);
		
	}
	
	
	@RequestMapping("/query/icd2/{keyword}")
	@ResponseBody
	public List<List<String>> searchICD2(@PathVariable String keyword){
		
		return indexService.searchICD2(keyword);
		
	}
	
	@RequestMapping("/query/icdview/{keyword}")
	@ResponseBody
	public List<CodeMatch> queryView(@PathVariable String keyword){
		
		return indexService.queryView(keyword);
		
	}
	
	
	@RequestMapping("/query/pacx/{keyword}")
	@ResponseBody
	public List<PacxMatchView> searchPacx(@PathVariable String keyword){
		
		return indexService.searchPacxDia(keyword);
		
	}

	
	
	@RequestMapping("/match/{keyword}")
	@ResponseBody
	public MatchView match(@PathVariable String keyword){
		
		return indexService.match(keyword);
		
	}
	
	
	@RequestMapping("/match/list")
	@ResponseBody
	public List<CodeMatchView> match(){
		List<CodeMatch> list = new ArrayList<>();
		list.add(new CodeMatch("X000000748B", "注射用辅酶A"));
		list.add(new CodeMatch("Q08ZC12L4", "佛手颗粒★"));
		list.add(new CodeMatch("Q12ZA05L4", "炮山甲颗粒★"));
		list.add(new CodeMatch("Q18ZL08L4", "荷叶颗粒★"));
		list.add(new CodeMatch("Q18ZC20L4", "乌梅颗粒★"));
		return indexService.match(list);
		
	}

	
	@RequestMapping("/export/analyse")
	public String analyse(HttpServletRequest request,HttpServletResponse response){
		
		//indexService.exportAnalyseExcel(request, response);
		indexService.exportHospitalAnalyseExcel(request, response);
		return "analyse";
		
	}
	

}
