package com.lexisnexis.ui.datahygiene;

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

public class DataHygieneRecordList {
	
	List<DataHygieneRow> arlDataHygieneRecords;
	
	public DataHygieneRecordList(String fileName) {
		arlDataHygieneRecords = addRecords(fileName);
		
	}
	
	public List<DataHygieneRow> addRecords(String fileName) {
		return readFromCSV(fileName);
	}
	
	public List<DataHygieneRow> getDataHygieneRecordsList() {
		return Collections.unmodifiableList(arlDataHygieneRecords);
	}
	
	public List<DataHygieneRow> readFromCSV(String fileName){
		
		File file = new File(fileName);
		FileInputStream fstream;
		DataInputStream in;
		BufferedReader br;
		CSVReader reader = null;
		List<DataHygieneRow> arlList = new ArrayList<DataHygieneRow>();
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
					DataHygieneRow obj = new DataHygieneRow();
			    	for (int i = 0; i < lineArr.length; i++) {
			    		switch (i) {
						case 0:
							obj.setSource(lineArr[i]);
							break;
						case 1:
							obj.setFieldName(lineArr[i]);
							break;
						case 2:
							obj.setErrorMessage(lineArr[i]);
							break;
						case 3:
							obj.setCount(lineArr[i]);
							break;
						case 4:
							obj.setSourceGroupCount(lineArr[i]);
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
		
		List<DataHygieneRow> list = new DataHygieneRecordList("C:\\Dinesh\\Workspaces\\HPCC_WorkSpaces\\PentahoSpoonIntegration\\src\\com\\lexisnexis\\ui\\datahygiene\\Hygiene_ValidityErrors.csv").getDataHygieneRecordsList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			DataHygieneRow dataHygieneRow = (DataHygieneRow) iterator.next();
			System.out.println("SOURCE: "+dataHygieneRow.getSource());
			System.out.println("FIELDNAME: "+dataHygieneRow.getFieldName());
			System.out.println("ERROR MESSAGE: "+dataHygieneRow.getErrorMessage());
			System.out.println("COUNT: "+dataHygieneRow.getCount());
			System.out.println("SOURCE GRP COUNT: "+dataHygieneRow.getSourceGroupCount());
		}
	}
}
