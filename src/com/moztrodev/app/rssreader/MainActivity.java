package com.moztrodev.app.rssreader;

import java.util.ArrayList;

import com.moztrodev.app.rssreader.adapter.PostAdapter;
import com.moztrodev.app.rssreader.model.Post;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;

public class MainActivity extends ListActivity {
	private Post post;
	private ArrayList<Post> postList = new ArrayList<Post>();
	private PostAdapter postAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		populateList();
		
		postAdapter = new PostAdapter(this, R.layout.item, postList);
		this.setListAdapter(postAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void populateList(){
		for(int i=0; i<50; i++){
			post = new Post("This is the testing post #"+i, "01 Jan 2013");
			postList.add(post);
		}
	}

}
