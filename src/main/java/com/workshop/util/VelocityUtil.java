package com.workshop.util;

import java.util.Date;

import org.springframework.util.StringUtils;

public class VelocityUtil {

	public boolean isContains(String bigString, String smallString) {
		if (!StringUtils.hasLength(bigString) || !StringUtils.hasLength(smallString) ) {
			return false;
		}
		if (bigString.indexOf(smallString) >= 0) {
			return true;
		}else {
			return false;
		}
	}
	
	public String formatDate(Date date) {
		return CalendarUtil.getDateString(date, CalendarUtil.SIMPLE_DATE_FORMAT);
	}
}
