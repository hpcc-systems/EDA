package com.lexisnexis.ui.clustersources;

import java.util.List;

public class ClusterSourcesRow {
	
	private String source;
	
	private String totalCount;
	
	private String occurPcnt;
	
	private List<SourcesRow> arlSourcesRow;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getOccurPcnt() {
		return occurPcnt;
	}

	public void setOccurPcnt(String occurPcnt) {
		this.occurPcnt = occurPcnt;
	}
	
	public List<SourcesRow> getArlSourcesRow() {
		return arlSourcesRow;
	}

	public void setArlSourcesRow(List<SourcesRow> arlSourcesRow) {
		this.arlSourcesRow = arlSourcesRow;
	}
}
