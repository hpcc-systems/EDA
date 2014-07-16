package org.pentaho.di.plugins.perspectives.eclmyperspective;

import java.util.Comparator;

public class PlotData implements Comparable<PlotData>{

	private String rowName;
	private String colName;
	
 
	public PlotData(String rowName, String colName, int quantity) {
		super();
		this.rowName = rowName;
		this.colName = colName;
		
	}
 
	public String getRowName() {
		return rowName;
	}
	public void setRowName(String rowName) {
		this.rowName = rowName;
	}
	public String getColDesc() {
		return colName;
	}
	public void setColDesc(String colName) {
		this.colName = colName;
	}
	public static Comparator<PlotData> RowNameComparator  = new Comparator<PlotData>() {

		public int compare(PlotData fruit1, PlotData fruit2) {
	
			String fruitName1 = fruit1.getRowName().toUpperCase();
			String fruitName2 = fruit2.getRowName().toUpperCase();
	
			//ascending order
			return fruitName1.compareTo(fruitName2);
			
			//descending order
			//return fruitName2.compareTo(fruitName1);
			}
	
		};


	@Override
	public int compareTo(PlotData arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
