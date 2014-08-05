package com.example.mystocks.info;

import java.util.HashMap;

public abstract class AbstractOnXMLParseAction
{
	public abstract void doAction(HashMap<EStockAttributes, String> xmlPullParserResults);
}
