package com.example.mystocks;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mystocks.attributes.AbstractOnXMLParseAction;
import com.example.mystocks.attributes.AsyncTaskGetAndParseXML;
import com.example.mystocks.attributes.EStockAttributes;

public class AddStockActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_stock);
		defineListenersForButtons();
	}

	private void defineListenersForButtons()
	{
		((Button) findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});

		((Button) findViewById(R.id.addNew)).setEnabled(false);
		((Button) findViewById(R.id.addNew)).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				TextView tv = (TextView) findViewById(R.id.newSym);
				Intent intent = new Intent();
				intent.putExtra("result", tv.getEditableText().toString());
				setResult(Activity.RESULT_OK, intent);
				finish();

			}
		});

		((Button) findViewById(R.id.refresh)).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				EditText tv = (EditText) findViewById(R.id.newSym);

				if (tv.getText().length() > 0)
				{
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);

					String yahooURLFirst = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
					String yahooURLSecond = "%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

					final String yqlURL = yahooURLFirst + tv.getText().toString() + yahooURLSecond;
					final TextView lastValue = (TextView) findViewById(R.id.lastVal);
					final TextView companyName = (TextView) findViewById(R.id.compName);

					new AsyncTaskGetAndParseXML(new AbstractOnXMLParseAction()
					{
						@Override
						public void doAction(HashMap<EStockAttributes, String> xmlPullParserResults)
						{
							lastValue.setText(xmlPullParserResults.get(EStockAttributes.LastTradePriceOnly));
							companyName.setText(xmlPullParserResults.get(EStockAttributes.Name));
							((Button) findViewById(R.id.addNew)).setEnabled(true);
						}
					}).execute(yqlURL);
				}
				else
				{

					// Create an alert dialog box
					AlertDialog.Builder builder = new AlertDialog.Builder(AddStockActivity.this);
					builder.setTitle(R.string.invalid_stock_symbol);
					builder.setMessage(R.string.missing_stock_symbol);
					AlertDialog theAlertDialog = builder.create();
					theAlertDialog.show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_stock, menu);
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
