package com.lexisnexis.ui.datasummary;

public class DataSummaryRow {
	
	private String fieldName;
	
	private String populated;
	
	private String maxLength;
	
	private String averageLength;
	
	private String recordLength;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getPopulated() {
		return populated;
	}

	public void setPopulated(String populated) {
		this.populated = populated;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getAverageLength() {
		return averageLength;
	}

	public void setAverageLength(String averageLength) {
		this.averageLength = averageLength;
	}
	
	public String getRecordLength() {
		return recordLength;
	}

	public void setRecordLength(String recordLength) {
		this.recordLength = recordLength;
	}
	
}
