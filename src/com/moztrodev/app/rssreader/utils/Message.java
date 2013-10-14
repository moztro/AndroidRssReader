/**
 * 
 */
package com.moztrodev.app.rssreader.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author DANIEL
 *
 */
public abstract class Message {

	public static void show(Context context, String message){
		Toast.makeText(context, 
				message, 
				Toast.LENGTH_LONG).show();
	}
}
