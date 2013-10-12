/**
 * 
 */
package com.moztrodev.app.rssreader.utils;

/**
 * @author DANIEL
 *
 */
public abstract class StringUtils {

	public static String quitarNBSPs(String texto){		
		return texto.replace("nbsp", "");
	}
}
