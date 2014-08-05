package com.example.mystocks.info;

public enum EStockAttributes 
{
	AverageDailyVolume, 
	Change, 
	DaysLow,
	DaysHigh,
	YearLow,
	YearHigh,
	MarketCapitalization,
	LastTradePriceOnly,
	DaysRange,
	Name,
	Symbol,
	Volume,
	StockExchange;
	
	public static boolean isValidKey(String key)
	{
		if (fidnByName(key) != null)
		{
			return true;
		}
		return false;
	}
	
	public static EStockAttributes fidnByName(String name)
	{
		for (EStockAttributes value : values())
		{
			if (value.name().equalsIgnoreCase(name))
			{
				return value;
			}
		}
		return null;
	}
	
}
