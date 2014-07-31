package com.lexisnexis.ui.datahygiene;

public class DataHygieneRow {
	
	private String source;
	
	private String fieldName;
	
	private String errorMessage;
	
	private String count;
	
	private String sourceGroupCount;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getSourceGroupCount() {
		return sourceGroupCount;
	}

	public void setSourceGroupCount(String sourceGroupCount) {
		this.sourceGroupCount = sourceGroupCount;
	}
}
