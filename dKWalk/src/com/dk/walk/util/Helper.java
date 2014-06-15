package com.dk.walk.util;

import java.text.DecimalFormat;

public class Helper {
	public static String formatTime(Integer mills){
		DecimalFormat format = new DecimalFormat("0.00");
		
		Integer sec = mills / 1000;
		if(sec < 60){
			return sec.toString() + " sec.";
		}else if(sec < 3600){
			Float min = (float) (sec / 60.0);
			return format.format(min) + " min.";
		}else{
			Float hour = (float) (sec / 60.0 / 60.0);
			return format.format(hour)+ "St.";
		}
	}
}
