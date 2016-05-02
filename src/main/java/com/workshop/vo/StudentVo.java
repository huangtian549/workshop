package com.workshop.vo;

import java.io.Serializable;

import com.workshop.po.Student;

public class StudentVo extends Student implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3106397823674486718L;

	
	private String endDateString;
	
	private String createDateString;

	public String getEndDateString() {
		return endDateString;
	}

	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}

	public String getCreateDateString() {
		return createDateString;
	}

	public void setCreateDateString(String createDateString) {
		this.createDateString = createDateString;
	}
	
	
	
	
}
