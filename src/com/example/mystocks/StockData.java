package com.example.mystocks;

public class StockData 
{
	public String companyName;
	public double yearLow;
	public double yearHigh;
	public double daysLow;
	public double daysHigh;
	public double lastTradePrice;
	public String datysRange;
	public final String symbol;

	public StockData(String symbol) 
	{
		this.symbol = symbol;
	}
}