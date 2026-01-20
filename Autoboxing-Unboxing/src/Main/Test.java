package Main;

import java.util.HashMap;
import java.util.Map;

public class Test {
	
	public static void main(String[] args) {
		Summation sum = new Summation();
		
		Map<String, Double> shopping = new HashMap<String, Double>();
		
		shopping.put("Yoghurt", 3.40);
		shopping.put("Strawberries", 2.40);
		shopping.put("Orange", 1.40);
		shopping.put("Mango", 3.40);
		shopping.put("Water", 0.80);
		
		double average = sum.average(shopping);
		System.out.println(average);
	}
}
