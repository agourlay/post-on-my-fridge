package com.agourlay.pomf.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.ocpsoft.pretty.time.PrettyTime;

public class Utils {

	public static Date stringToDate(String sDate, String sFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
        try {
			return sdf.parse(sDate);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String getPrettyElapsedTime(Date date){
		if (date == null){
			return "";
		}
		PrettyTime p = new PrettyTime(new Locale(""));
		return p.format(date);
	}
}
