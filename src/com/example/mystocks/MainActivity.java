package com.example.mystocks;

import java.util.Arrays;
import java.util.HashMap;

import com.example.mystocks.info.AbstractOnXMLParseAction;
import com.example.mystocks.info.AsyncTaskGetAndParseXML;
import com.example.mystocks.info.EStockAttributes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener
{

	public final static String STOCK_SYMBOL = "com.example.stockquotes.STOCK";

	protected static final int ADD_STOCK_WINDOW = 1;

	private SharedPreferences stockSymbolsEntered;
	private TableLayout stockTableScrollView;
	
	Button deleteStocksdata;
	Button addNew;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		stockSymbolsEntered = getSharedPreferences("stockList", MODE_PRIVATE);

		stockTableScrollView = (TableLayout) findViewById(R.id.stockTableScrollView);
		deleteStocksdata = (Button) findViewById(R.id.deleteStocksButton);
		deleteStocksdata.setOnClickListener(this);
		
		addNew = (Button) findViewById(R.id.addNew);
		
		addNew.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent(MainActivity.this, AddStockActivity.class);
				startActivityForResult(intent, ADD_STOCK_WINDOW);
			}
		});
		
		 ((Button)findViewById(R.id.refresh)).setOnClickListener(new View.OnClickListener() 
		 {
			
			@Override
			public void onClick(View v) {
				String[] stocks = stockSymbolsEntered.getAll().keySet().toArray(new String[0]);
				for (int i=0; i<stocks.length ; i++)
				{
					String sym = stocks[i];
					View row = stockTableScrollView.getChildAt(i);
					
					TextView lastVal = (TextView)row.findViewById(R.id.stockSymbolLastValue);
					TextView compName = (TextView)row.findViewById(R.id.stockSymCompanyName);
					execAsyncAndUpdateValues(sym, compName, lastVal);
				}
			}
		});

		updateSavedStockList(null);
	}
	
	private void execAsyncAndUpdateValues(String symbol, final TextView companyName, final TextView lastValue)
	{
		String yahooURLFirst = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
		String yahooURLSecond = "%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
		final String yqlURL = yahooURLFirst + symbol + yahooURLSecond;
		new AsyncTaskGetAndParseXML(new AbstractOnXMLParseAction()
		{
			@Override
			public void doAction(HashMap<EStockAttributes, String> xmlPullParserResults)
			{
				lastValue.setText(xmlPullParserResults.get(EStockAttributes.LastTradePriceOnly));
				companyName.setText(xmlPullParserResults.get(EStockAttributes.Name));
			}
		}).execute(yqlURL);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (null != data)
		{
			saveStockSymbol(data.getStringExtra("result"));
//			((TextView)findViewById(R.id.returnTxtValue)).setText(data.getStringExtra("result"));
			
		}
		super.onActivityResult(requestCode, resultCode, data);
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

	private void updateSavedStockList(String newStockSymbol)
	{

		String[] stocks = stockSymbolsEntered.getAll().keySet().toArray(new String[0]);
		Arrays.sort(stocks, String.CASE_INSENSITIVE_ORDER);

		if (newStockSymbol != null)
		{
			insertStockInScrollView(newStockSymbol, Arrays.binarySearch(stocks, newStockSymbol));
		}
		else
		{
			for (int i = 0; i < stocks.length; ++i)
			{

				insertStockInScrollView(stocks[i], i);
			}
		}
	}

	private void saveStockSymbol(String newStock)
	{
		String isTheStockNew = stockSymbolsEntered.getString(newStock, null);

		SharedPreferences.Editor preferencesEditor = stockSymbolsEntered.edit();
		preferencesEditor.putString(newStock, newStock);
		preferencesEditor.apply();

		if (isTheStockNew == null)
		{
			updateSavedStockList(newStock);
		}
	}

	private void insertStockInScrollView(String stock, int arrayIndex)
	{
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View newStockRow = inflater.inflate(R.layout.stock_quote, null);

		TextView newStockTextView = (TextView) newStockRow.findViewById(R.id.stockSymbolTextView);

		newStockTextView.setText(stock);

		Button stockQuoteButton = (Button) newStockRow.findViewById(R.id.stockQuoteButton);
		stockQuoteButton.setOnClickListener(getStockActivityListener);
		stockTableScrollView.addView(newStockRow, arrayIndex);

	}
	
	private void deleteAllStocks()
	{
		stockTableScrollView.removeAllViews();
	}

	public OnClickListener getStockActivityListener = new OnClickListener() {

		public void onClick(View v)
		{

			TableRow tableRow = (TableRow) v.getParent();
			TextView stockTextView = (TextView) tableRow.findViewById(R.id.stockSymbolTextView);
			String stockSymbol = stockTextView.getText().toString();
			Intent intent = new Intent(MainActivity.this, StockInfoActivity.class);

			intent.putExtra(STOCK_SYMBOL, stockSymbol);

			startActivity(intent);

		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub

		switch (arg0.getId())
		{
//		case R.id.enterStockSymbolButton:
//			if (stockSymbolEditText.getText().length() > 0)
//			{
//
//				saveStockSymbol(stockSymbolEditText.getText().toString());
//
//				stockSymbolEditText.setText(""); // Clear EditText box
//
//				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//				imm.hideSoftInputFromWindow(stockSymbolEditText.getWindowToken(), 0);
//			}
//			else
//			{
//
//				// Create an alert dialog box
//				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//
//				builder.setTitle(R.string.invalid_stock_symbol);
//				builder.setMessage(R.string.missing_stock_symbol);
//				AlertDialog theAlertDialog = builder.create();
//				theAlertDialog.show();
//
//			}
//			break;

		case R.id.deleteStocksButton:
			deleteAllStocks();

			SharedPreferences.Editor preferencesEditor = stockSymbolsEntered.edit();
			preferencesEditor.clear();
			preferencesEditor.apply();

			break;
		}
	}
}
