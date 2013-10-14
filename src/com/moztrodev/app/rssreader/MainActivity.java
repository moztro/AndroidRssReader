package com.moztrodev.app.rssreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.moztrodev.app.rssreader.adapter.PostAdapter;
import com.moztrodev.app.rssreader.model.Post;
import com.moztrodev.app.rssreader.enums.RSSXMLTag;
import com.moztrodev.app.rssreader.utils.*;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends ListActivity {
	private ArrayList<Post> postList = new ArrayList<Post>();
	private PostAdapter postAdapter;
	private static String RSS = "";
	private static int ITEMS_ON_VIEW = 0;
	private boolean isMainLoad = true;
	private boolean isPullRefresh = false;
	private boolean isLoadMore = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RSS = this.getString(R.string.rss_cnn);
		ITEMS_ON_VIEW = 20;
		
		getRssData();
		
		updateAdapter();
		
		((PullAndLoadListView)getListView()).setOnRefreshListener(new OnRefreshListener(){
			@Override
			public void onRefresh() {
				isPullRefresh = true;
				getRssData();
			}			
		});
	
		((PullAndLoadListView)getListView()).setOnLoadMoreListener(new OnLoadMoreListener() {			
			@Override
			public void onLoadMore() {
				isLoadMore = true;
				ITEMS_ON_VIEW += 10;
				getRssData();
			}
		});
	
		((PullAndLoadListView)getListView()).setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Post post = postList.get(pos-1);
				
				Intent i = new Intent(MainActivity.this, PostViewActivity.class);
				i.putExtra("post", post);
				startActivity(i);
			}			
		});
	}

	private void getRssData(){
		if(haveInternet()){
			new GetRssData().execute(RSS);
		}else{
			Message.show(this, "Oops! apparently you have no internet connection. Try again later.");
		}
	}
	
	private void updateAdapter(){
		postAdapter = new PostAdapter(this, R.layout.item, postList);
		this.setListAdapter(postAdapter);
	}
	
	private class GetRssData extends AsyncTask<String, Integer, ArrayList<Post>>{
		private RSSXMLTag currentTag;
		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute(){
			if(isMainLoad){
				dialog = new ProgressDialog(MainActivity.this);
				dialog.setMessage("Loading news...");
				dialog.setCancelable(false);
				dialog.show();
			}
		}
		
		@Override
		protected ArrayList<Post> doInBackground(String... params) {

			if(isCancelled()){
				return null;
			}
			
			String strURL = params[0];
			InputStream inputStream = null;
			ArrayList<Post> items = new ArrayList<Post>();
			try{
				URL url = new URL(strURL);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setReadTimeout(10000);
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				conn.connect();
				int response = conn.getResponseCode();
				Log.e("debug", "Response: " + response);
				inputStream = conn.getInputStream();
				
				XmlPullParserFactory xppFactory = XmlPullParserFactory.newInstance();
				xppFactory.setNamespaceAware(true);
				XmlPullParser xpp = xppFactory.newPullParser();
				xpp.setInput(inputStream, "UTF_8");
				
				int eventType = xpp.getEventType();
				Post post = null;
				
				while((items.size() < ITEMS_ON_VIEW) && 
						(eventType != XmlPullParser.END_DOCUMENT)){
					if(eventType == XmlPullParser.START_DOCUMENT){
						
					}else if(eventType == XmlPullParser.START_TAG){
							if(xpp.getName().equals("item")){
								post = new Post();
								currentTag = RSSXMLTag.IGNORETAG;
							}else if(xpp.getName().equals("title")){
								currentTag = RSSXMLTag.TITLE;
							}else if(xpp.getName().equals("description")){
								currentTag = RSSXMLTag.CONTENT;
							}else if(xpp.getName().equals("pubDate")){
								currentTag = RSSXMLTag.PUB_DATE;
							}else if(xpp.getName().equals("link")){
								currentTag = RSSXMLTag.LINK;
							}
						}else if(eventType == XmlPullParser.END_TAG){
							if (xpp.getName().equals("item")) {
								String date = DateUtils.formatDateDay(post.PubDate);
								post.PubDate = date;
								items.add(post);
							} else {
								currentTag = RSSXMLTag.IGNORETAG;
							}
						}else if(eventType == XmlPullParser.TEXT){
							String content = xpp.getText().trim();
							Log.d("debug", content);
							if(post != null){
								switch(currentTag){
								case TITLE:
									if (content.length() != 0) {
										if (post.Title != null) {
											post.Title += content;
										} else {
											post.Title = content;
										}
									}
									break;
								case IMAGE:
									if (content.length() != 0) {
										if (post.Image != null) {
											post.Image += content;
										} else {
											post.Image = content;
										}
									}
									break;
								case CONTENT:
									if (content.length() != 0) {
										if (post.Content != null) {
											post.Content += content;
										} else {
											post.Content = content;
										}
									}
									break;
								case LINK:
									if(content.length() != 0){
										if(post.Link != null){
											post.Link += content;
										}else{
											post.Link = content;
										}
									}
									break;
								case PUB_DATE:
									if (content.length() != 0) {
										if (post.PubDate != null) {
											post.PubDate += content;
										} else {
											post.PubDate = content;
										}
									}
									break;
								default:
									break;
								}
							}
						}
						eventType = xpp.next();
					}
					Log.v("Items in view", String.valueOf(items.size()));
				}catch(MalformedURLException e){
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}catch(XmlPullParserException e){
					e.printStackTrace();
				}
			return items;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Post> result){
			postList.clear();
			for(int i=0; i < result.size(); i++){
				postList.add(result.get(i));
			}
			postAdapter.notifyDataSetChanged();
			if(isMainLoad){
				dialog.dismiss();
				isMainLoad = false;
			}
			if(isPullRefresh){
				((PullAndLoadListView)getListView()).onRefreshComplete();
			}else if(isLoadMore){
				((PullAndLoadListView)getListView()).onLoadMoreComplete();
			}
			isPullRefresh = false;
			isLoadMore = false;
		}
		
		@Override
		protected void onCancelled(){
			((PullAndLoadListView)getListView()).onLoadMoreComplete();
		}
	}

	private boolean haveInternet(){
		ConnectivityManager connManager = (ConnectivityManager)
				getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = null;
		
		if(connManager != null){
			network = connManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(!network.isAvailable()){
				network = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			}
		}
		return network==null ? false : network.isConnected();
    }
}
