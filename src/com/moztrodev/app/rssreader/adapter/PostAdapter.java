/**
 * 
 */
package com.moztrodev.app.rssreader.adapter;

import java.util.ArrayList;

import com.moztrodev.app.rssreader.R;
import com.moztrodev.app.rssreader.model.Post;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author DANIEL
 *
 */
public class PostAdapter extends ArrayAdapter<Post> {
	private Activity mContext;
	private ArrayList<Post> mObjects;
	
	public PostAdapter(Context context, int resource, ArrayList<Post> objects) {
		super(context, resource, objects);
		mContext = (Activity)context;
		mObjects = objects;
	}
	
	static class ViewHolder{
		TextView vTitle, vPubDate;
		ImageView vImage;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;
		
		if(convertView == null){
			LayoutInflater inflater = mContext.getLayoutInflater();
			convertView = inflater.inflate(R.layout.item, null);
			
			viewHolder = new ViewHolder();
			viewHolder.vTitle = (TextView)convertView.findViewById(R.id.title);
			viewHolder.vPubDate = (TextView)convertView.findViewById(R.id.pub_date);
			viewHolder.vImage = (ImageView)convertView.findViewById(R.id.image);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		Post post = mObjects.get(position);
		if(post != null){
			viewHolder.vTitle.setText(post.Title);
			viewHolder.vPubDate.setText(post.PubDate);
			viewHolder.vImage.setBackgroundResource(R.drawable.moztrodev);
		}
		return convertView;
	}

}
