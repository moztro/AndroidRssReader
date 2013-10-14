package com.moztrodev.app.rssreader;


import com.moztrodev.app.rssreader.model.Post;
import com.moztrodev.app.rssreader.utils.StringUtils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class PostViewActivity extends Activity {
	private WebView webView;
	private Button share;
	private TextView title,pubDate;
	private Post post;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_view);
		
		post = (Post)getIntent().getParcelableExtra("post");
		
		init();
		
		showPost();
		
		share.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				share();
			}
		});
	}
	
	private void init(){
		webView = (WebView)findViewById(R.id.webview);
		share = (Button)findViewById(R.id.social_button);
		title = (TextView)findViewById(R.id.title);
		pubDate = (TextView)findViewById(R.id.pub_date);
	}
	
	private void showPost(){
		title.setText(post.Title);
		pubDate.setText("Pub date: "+post.PubDate);
		
		WebSettings webSett = webView.getSettings();
		webSett.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSett.setDefaultTextEncodingName("UTF-8");
		webSett.setBuiltInZoomControls(true);
		webView.loadDataWithBaseURL(post.Link,
				StringUtils.removeNBSPs(post.toString()),
				"text/html",
				"UTF-8",
				null);
	}
	
	private void share(){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/html");
		i.putExtra(Intent.EXTRA_SUBJECT, webView.getTitle());
		i.putExtra(Intent.EXTRA_TEXT, webView.getContentDescription());
		
		startActivity(Intent.createChooser(i, "MoztroDev Rss Reader"));
	}

}
