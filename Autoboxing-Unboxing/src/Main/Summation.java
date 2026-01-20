package Main;

import java.util.List;
import java.util.Map;

public class Summation {
	
	public int sumEven(List<Integer> list) {
		int sum = 0;
		
		for (Integer value: list) {
			if (value % 2 == 0) {
				sum += value.intValue();
			}
		}
		
		return sum;
	}
	
	public double average(Map<String, Double> map) {
		double sum = 0;
		
		for (Double value: map.values()) {
			sum += value.doubleValue(); // unboxing
		}
		
		return (int)(sum / map.size()); // casting to int
	}
}
