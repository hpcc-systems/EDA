package com.lexisnexis.ui.sourceprofiles;

import java.util.List;

public class SourceProfilesRow {
	
	private String source;
	
	private List<SrcProfilesFieldsRow> arlProfilesFieldsRow;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<SrcProfilesFieldsRow> getArlProfilesFieldsRow() {
		return arlProfilesFieldsRow;
	}

	public void setArlProfilesFieldsRow(
			List<SrcProfilesFieldsRow> arlProfilesFieldsRow) {
		this.arlProfilesFieldsRow = arlProfilesFieldsRow;
	}
	
}
