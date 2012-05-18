package com.agourlay.pomf.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static Date stringToDate(String sDate, String sFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
        try {
			return sdf.parse(sDate);
		} catch (ParseException e) {
			return null;
		}
	}
}
