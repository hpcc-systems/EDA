package com.lexisnexis.ui.sourceoutliers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class SourceOutliersRecordList {
	
	List<SourceOutliersRow> arlSourceOutliersRecords;
	
	public SourceOutliersRecordList(String fileName) {
		arlSourceOutliersRecords = addRecords(fileName);
		
	}
	
	public List<SourceOutliersRow> addRecords(String fileName) {
		return readFromCSV(fileName);
	}
	
	public List<SourceOutliersRow> getSourceOutliersRecordsList() {
		return Collections.unmodifiableList(arlSourceOutliersRecords);
	}
	
	public List<SourceOutliersRow> readFromCSV(String fileName){
		
		File file = new File(fileName);
		FileInputStream fstream;
		DataInputStream in;
		BufferedReader br;
		CSVReader reader = null;
		List<SourceOutliersRow> arlList = new ArrayList<SourceOutliersRow>();
		try {
			fstream = new FileInputStream(file);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			reader = new CSVReader(br,',','"','\\');
			boolean first = true;
			//how to iterate through
			String [] nextLine;
			String [] arrayHeaders = null;
			String [] arrayValues = null;
			while ((nextLine = reader.readNext()) != null) {
				//nextLine will be an array with your columns
				String[] lineArr = nextLine;
				//Ignore the first line as it contains headers.
				if(first) {
					arrayHeaders = lineArr;
					first = false;
				} else {
					arrayValues = lineArr;
					SourceOutliersRow obj = new SourceOutliersRow();
			    	for (int i = 0; i < lineArr.length; i++) {
			    		switch (i) {
						case 0:
							obj.setFieldName(lineArr[i]);
							break;
						case 1:
							obj.setFieldValue(lineArr[i]);
							break;
						case 2:
							obj.setSource(lineArr[i]);
							break;
						case 3:
							obj.setCount(lineArr[i]);
							break;
						default:
							break;
						} 
			    		
					}
			    	arlList.add(obj);
				}
			}
						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return arlList;
	}
	
	public static void main(String[] args) {
		
		List<SourceOutliersRow> list = new SourceOutliersRecordList("C:\\Dinesh\\Workspaces\\HPCC_WorkSpaces\\PentahoSpoonIntegration\\src\\com\\lexisnexis\\ui\\sourceoutliers\\Source_Outliers.csv").getSourceOutliersRecordsList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			SourceOutliersRow sourceOutliersRow = (SourceOutliersRow) iterator.next();
			System.out.println("FIELDNAME: "+sourceOutliersRow.getFieldName());
			System.out.println("FIELDVALUE: "+sourceOutliersRow.getFieldValue());
			System.out.println("SOURCE: "+sourceOutliersRow.getSource());
			System.out.println("COUNT: "+sourceOutliersRow.getCount());
		}
	}
	
}
