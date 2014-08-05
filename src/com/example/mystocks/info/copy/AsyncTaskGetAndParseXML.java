package com.example.mystocks.info.copy;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;
import android.util.Log;


public class AsyncTaskGetAndParseXML extends AsyncTask<String, String, String>
{

	final AbstractOnXMLParseAction postAction;
	HashMap<EStockAttributes, String> xmlPullParserResults = new HashMap<EStockAttributes, String>();

	
	public AsyncTaskGetAndParseXML(AbstractOnXMLParseAction postAction)
	{
		this.postAction = postAction;
	}

	protected String doInBackground(String... args)
	{
		try
		{
			Log.d("test", "In XmlPullParser");

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);

			XmlPullParser parser = factory.newPullParser();

			parser.setInput(new InputStreamReader(getUrlData(args[0])));
			beginDocument(parser, "query");
			int eventType = parser.getEventType();

			do
			{

				nextElement(parser);
				String key = parser.getName();
				parser.next();

				eventType = parser.getEventType();
				if (eventType == XmlPullParser.TEXT)
				{
					String valueFromXML = parser.getText();
					
					if (EStockAttributes.fidnByName(key) != null)
					{
						xmlPullParserResults.put(EStockAttributes.fidnByName(key), valueFromXML);
					}
				}

			}
			while (eventType != XmlPullParser.END_DOCUMENT);

		}

		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (XmlPullParserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally
		{
		}

		return null;
	}

	public InputStream getUrlData(String url) throws URISyntaxException, ClientProtocolException, IOException
	{

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet method = new HttpGet(new URI(url));

		HttpResponse res = client.execute(method);

		return res.getEntity().getContent();
	}

	public final void beginDocument(XmlPullParser parser, String firstElementName) throws XmlPullParserException, IOException
	{
		int type;
		while ((type = parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT)
		{
			;
		}

		if (type != XmlPullParser.START_TAG)
		{
			throw new XmlPullParserException("No start tag found");
		}

		if (!parser.getName().equals(firstElementName))
		{
			throw new XmlPullParserException("Unexpected start tag: found " + parser.getName() + ", expected " + firstElementName);
		}
	}

	public final void nextElement(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		int type;
		while ((type = parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT)
		{
			;
		}
	}

	protected void onPostExecute(String result)
	{
		postAction.doAction(xmlPullParserResults);
	}
}