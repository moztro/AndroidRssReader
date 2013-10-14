/**
 * 
 */
package com.moztrodev.app.rssreader.utils;

/**
 * @author DANIEL
 *
 */
public abstract class StringUtils {

	public static String removeNBSPs(String texto){		
		return texto.replace("nbsp", "");
	}
}
