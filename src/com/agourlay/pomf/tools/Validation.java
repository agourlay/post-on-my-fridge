package com.agourlay.pomf.tools;

public class Validation {

	public static boolean isNoTNullOrEmpty(String target){
		return target != null && !target.trim().isEmpty();
	}
}
