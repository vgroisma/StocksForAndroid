package com.example.mystocks;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mystocks.attributes.AbstractOnXMLParseAction;
import com.example.mystocks.attributes.AsyncTaskGetAndParseXML;
import com.example.mystocks.attributes.EStockAttributes;

public class StockInfoActivity extends Activity
{
	private static final String TAG = "STOCKQUOTE";

	TextView companyNameTextView;
	TextView yearLowTextView;
	TextView yearHighTextView;
	TextView daysLowTextView;
	TextView daysHighTextView;
	TextView lastTradePriceOnlyTextView;
	TextView changeTextView;
	TextView daysRangeTextView;

	String yahooURLFirst = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
	String yahooURLSecond = "%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_info);
		Intent intent = getIntent();
		String stockSymbol = intent.getStringExtra(MainActivity.STOCK_SYMBOL);

		companyNameTextView = (TextView) findViewById(R.id.companyNameTextView);
		yearLowTextView = (TextView) findViewById(R.id.yearLowTextView);
		yearHighTextView = (TextView) findViewById(R.id.yearHighTextView);
		daysLowTextView = (TextView) findViewById(R.id.daysLowTextView);
		daysHighTextView = (TextView) findViewById(R.id.daysHighTextView);
		lastTradePriceOnlyTextView = (TextView) findViewById(R.id.lastTradePriceOnlyTextView);
		changeTextView = (TextView) findViewById(R.id.changeTextView);
		daysRangeTextView = (TextView) findViewById(R.id.daysRangeTextView);

		Log.d(TAG, "Before URL Creation " + stockSymbol);

		// Create the YQL query
		final String yqlURL = yahooURLFirst + stockSymbol + yahooURLSecond;
		
		
		new AsyncTaskGetAndParseXML(new AbstractOnXMLParseAction()
		{
			@Override
			public void doAction(HashMap<EStockAttributes, String> xmlPullParserResults)
			{
				companyNameTextView.setText(xmlPullParserResults.get(EStockAttributes.Name));
				yearLowTextView.setText("Year Low: " + xmlPullParserResults.get(EStockAttributes.YearLow));
				yearHighTextView.setText("Year High: " + xmlPullParserResults.get(EStockAttributes.YearHigh));
				daysLowTextView.setText("Days Low: " + xmlPullParserResults.get(EStockAttributes.DaysLow));
				daysHighTextView.setText("Days High: " + xmlPullParserResults.get(EStockAttributes.DaysHigh));
				lastTradePriceOnlyTextView.setText("Last Price: " + xmlPullParserResults.get(EStockAttributes.LastTradePriceOnly));
				changeTextView.setText("Change: " + xmlPullParserResults.get(EStockAttributes.Change));
				daysRangeTextView.setText("Daily Price Range: " + xmlPullParserResults.get(EStockAttributes.DaysRange));
			}
		}).execute(yqlURL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
