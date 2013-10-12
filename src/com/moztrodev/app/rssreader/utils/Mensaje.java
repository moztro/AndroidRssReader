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
public abstract class Mensaje {

	public static void mostrar(Context context, String mensaje){
		Toast.makeText(context, 
					   mensaje, 
					   Toast.LENGTH_LONG).show();
	}
}
