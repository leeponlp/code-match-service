package demo;

import java.util.Map;

import org.junit.Test;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**   
 * This class is used for ...   
 * @author leepon1990  
 * @version   
 *       1.0, 2016年11月30日 下午1:57:30   
 */
public class TestCase {
	
	@Test
	public void demo1(){
		Map<String, String> map = new LinkedCaseInsensitiveMap<>();
		map.put("Aso", "测试Aso");
		map.put("AcD", "测试AcD");
		map.put("ABc", "测试ABc");
		
		System.err.println(map.get("abc"));
	}

}
