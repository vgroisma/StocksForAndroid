package com.example.mystocks.attributes;

import java.util.HashMap;

public abstract class AbstractOnXMLParseAction
{
	public abstract void doAction(HashMap<EStockAttributes, String> xmlPullParserResults);
}
