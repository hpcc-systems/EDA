package com.lexisnexis.ui.datasummary;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class DataSummaryRecordList {

	List<DataSummaryRow> arlDataSummaryRecords;
	
	public DataSummaryRecordList(String fileName) {
		arlDataSummaryRecords = addRecords(fileName);
		
	}
	
	public List<DataSummaryRow> addRecords(String fileName) {
		return readFromCSV(fileName);
	}
	
	public List<DataSummaryRow> getDataSummaryRecordsList() {
		return Collections.unmodifiableList(arlDataSummaryRecords);
	}
	
	public List<DataSummaryRow> readFromCSV(String fileName){
		//"C:\\Dinesh\\Workspaces\\HPCC_WorkSpaces\\DataProfilingUI\\src\\com\\lexisnexis\\ui\\datasummary\\Dataprofiling_SummaryReport.csv"
		System.out.println("+++++OPENING FILE: " + fileName);
		File file = new File(fileName);
		FileInputStream fstream;
		DataInputStream in;
		BufferedReader br;
		CSVReader reader = null;
		List<DataSummaryRow> arlList = new ArrayList<DataSummaryRow>();
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
				}
			}
			
			for (int i = 2; i < arrayHeaders.length; i+=3) {
				DataSummaryRow obj = new DataSummaryRow();
				obj.setRecordLength(arrayValues[1]);
				obj.setFieldName(arrayHeaders[i].split("_")[1]); //The FieldName is always in the form populated_FIELDNAME_pcnt
				obj.setPopulated(arrayValues[i]);
				obj.setMaxLength(arrayValues[i+1]);
				obj.setAverageLength(arrayValues[i+2]);
				
				arlList.add(obj);
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
}
