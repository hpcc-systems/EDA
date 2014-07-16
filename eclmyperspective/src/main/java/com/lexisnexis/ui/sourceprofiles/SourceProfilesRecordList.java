package com.lexisnexis.ui.sourceprofiles;

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

public class SourceProfilesRecordList {
	
	private List<SourceProfilesRow> arlSourceProfilesRecords;
	
	public SourceProfilesRecordList(String fileName) {
		arlSourceProfilesRecords = new ArrayList<SourceProfilesRow>();
		arlSourceProfilesRecords = readFromCSV(fileName);
	}
	
	public List<SourceProfilesRow> getSourceProfilesRecordList() {
		return Collections.unmodifiableList(arlSourceProfilesRecords);
	}
	
	public List<SourceProfilesRow> readFromCSV(String fileName) {
		File file = new File(fileName);
		FileInputStream fstream;
		DataInputStream in;
		BufferedReader br;
		CSVReader reader = null;
		List<SourceProfilesRow> arlSourceProfiles = new ArrayList<SourceProfilesRow>();
		List<SrcProfilesFieldsRow> listSourceProfilesRow = null;
		try {
			fstream = new FileInputStream(file);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			reader = new CSVReader(br,',','"','\\');
			boolean first = true;
			int firstRowLen = 0;
			String len = "";
			
			//how to iterate through
			String [] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				//nextLine will be an array with your columns
				String[] lineArr = nextLine;
				//Ignore the first line as it contains headers.
				if(first) {
					firstRowLen = lineArr.length;
					//arrayHeaders = lineArr;
					first = false;
				} else {
					//if(lineArr[1].length() >= firstRowLen){
					if(lineArr.length == firstRowLen){
						SourceProfilesRow objSourceProfilesRow =  new SourceProfilesRow();
						objSourceProfilesRow.setSource(lineArr[0]);
						len = lineArr[1];
						
						listSourceProfilesRow = new ArrayList<SrcProfilesFieldsRow>();
						objSourceProfilesRow.setArlProfilesFieldsRow(listSourceProfilesRow);
						arlSourceProfiles.add(objSourceProfilesRow);
						
					} else {
						if(!len.equals("")) {	//Len
							int thisLen = lineArr.length;  //2
							String subStr = "";
							
							SrcProfilesFieldsRow objSrcProfilesFieldsRow = new SrcProfilesFieldsRow();
							for(int i = 0; i < thisLen; i++){
								subStr += lineArr[i];
							}
							objSrcProfilesFieldsRow.setField(lineArr[0]);
							objSrcProfilesFieldsRow.setUniqueValue(lineArr[1]);
							objSrcProfilesFieldsRow.setOnlyValue(lineArr[2]);
							objSrcProfilesFieldsRow.setGloballyUniqueValue(lineArr[3]);
							objSrcProfilesFieldsRow.setIdsWithSrc(lineArr[4]);
							
							len = len.substring(subStr.length(),len.length());
							listSourceProfilesRow.add(objSrcProfilesFieldsRow);
						} 
						
					}
				}
			}
			
			//System.out.println(mapParent);
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
		
		return arlSourceProfiles;
	}
	
	public static void main(String[] args) {
		SourceProfilesRecordList obj = new SourceProfilesRecordList("C:\\Dinesh\\Workspaces\\HPCC_WorkSpaces\\PentahoSpoonIntegration\\src\\com\\lexisnexis\\ui\\sourceprofiles\\SourceProfiles.csv");
		if(obj.getSourceProfilesRecordList() != null && obj.getSourceProfilesRecordList().size() > 0) {
			for (Iterator<SourceProfilesRow> iterator = obj.getSourceProfilesRecordList().iterator(); iterator.hasNext();) {
				SourceProfilesRow objSourceProfilesRow = iterator.next();
				System.out.println("SOURCE: "+objSourceProfilesRow.getSource());
				List<SrcProfilesFieldsRow> arl = objSourceProfilesRow.getArlProfilesFieldsRow();
				if(arl != null && arl.size() > 0) {
					for(Iterator<SrcProfilesFieldsRow> itr = arl.iterator(); itr.hasNext(); ){
						SrcProfilesFieldsRow objSrcProfilesFieldsRow = itr.next();
						System.out.println("FIELD: "+objSrcProfilesFieldsRow.getField());
						System.out.println("UNIQUE VALUE: "+objSrcProfilesFieldsRow.getUniqueValue());
						System.out.println("ONLY VALUE: "+objSrcProfilesFieldsRow.getOnlyValue());
						System.out.println("GLOBALLY UNIQUE VALUE: "+objSrcProfilesFieldsRow.getGloballyUniqueValue());
						System.out.println("IDS WITH SRC: "+objSrcProfilesFieldsRow.getIdsWithSrc());
					}
				}
			}
		}
	}
	
}
