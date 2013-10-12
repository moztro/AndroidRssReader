/**
 * 
 */
package com.moztrodev.app.rssreader.utils;

import android.annotation.SuppressLint;


/**
 * @author DANIEL
 *
 */
public abstract class DateUtils {

	@SuppressLint("DefaultLocale")
	public static String formatDateDay(String date){
		
		String day = date.substring(0, 3);
		day = day.toUpperCase();
		if(day.equals("MON")){
			day = "Monday";
		}else if(day.equals("TUE")){
			day = "Tuesday";
		}else if(day.equals("WED")){
			day = "Wednesday";
		}else if(day.equals("THU")){
			day = "Thursday";
		}else if(day.equals("FRI")){
			day = "Friday";
		}else if(day.equals("SAT")){
			day = "Saturday";
		}else if(day.equals("SUN")){
			day = "Sunday";
		}
		return day+", "+date.substring(5, 16);
	}
}
