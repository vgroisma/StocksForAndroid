package com.example.mystocks.info.copy;

import java.util.HashMap;

public abstract class AbstractOnXMLParseAction
{
	abstract void doAction(HashMap<EStockAttributes, String> xmlPullParserResults);
}
