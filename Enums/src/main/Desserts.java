package main;

public enum Desserts {
	FRANCE("Creme Brulee", 6.7), ROMANIA("Chec", 2.3), BULGARIA("Mekitzi", 8.9);
	
	private String dessert = null;
	private double rating = 0;
	
	private Desserts(String dessert) {
		this.dessert = dessert;
	}
	
	private Desserts(String dessert, double rating) {
		this.dessert = dessert;
		this.rating = rating;
	}
	
	public String getDessert() {
		return this.dessert;
	}
	
	public double getRating() {
		return this.rating;
	}
}
