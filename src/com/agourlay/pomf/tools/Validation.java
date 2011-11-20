package com.agourlay.pomf.tools;

import java.util.Date;

import com.ocpsoft.pretty.time.PrettyTime;

public class Validation {

	public static boolean isNoTNullOrEmpty(String target){
		return target != null && !target.trim().isEmpty();
	}
	
	public static String checkNull(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}
	
	public static String getPrettyElapsedTime(Date date){
		PrettyTime p = new PrettyTime();
		return p.format(date);
	}
}
