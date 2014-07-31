package com.lexisnexis.ui.clustersources;

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

public class ClusterSourcesRecordList {
	
	private List<ClusterSourcesRow> arlClusterSourcesRecords;
	
	public ClusterSourcesRecordList(String fileName){
		arlClusterSourcesRecords = new ArrayList<ClusterSourcesRow>();
		arlClusterSourcesRecords = readFromCSV(fileName);
	}
	
	public List<ClusterSourcesRow> getClusterSourcesRecordList() {
		return Collections.unmodifiableList(arlClusterSourcesRecords);
	}
	
	public List<ClusterSourcesRow> readFromCSV(String fileName) {
		File file = new File(fileName);
		FileInputStream fstream;
		DataInputStream in;
		BufferedReader br;
		CSVReader reader = null;
		List<ClusterSourcesRow> arlClusterSources = new ArrayList<ClusterSourcesRow>();
		List<SourcesRow> listSourcesRow = null;
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
					if(lineArr[3].length() >= firstRowLen){
						ClusterSourcesRow objClusterSourcesRow =  new ClusterSourcesRow();
						objClusterSourcesRow.setSource(lineArr[0]);
						objClusterSourcesRow.setTotalCount(lineArr[1]);
						objClusterSourcesRow.setOccurPcnt(lineArr[2]);
						len = lineArr[3];
						listSourcesRow = new ArrayList<SourcesRow>();
						objClusterSourcesRow.setArlSourcesRow(listSourcesRow);
						arlClusterSources.add(objClusterSourcesRow);
						
					} else {
						if(!len.equals("")) {	//Len
							int thisLen = lineArr.length;  //2
							String subStr = "";
							
							SourcesRow objSourcesRow = new SourcesRow();
							for(int i = 0; i < thisLen; i++){
								subStr += lineArr[i];
							}
							objSourcesRow.setSource(lineArr[0]);
							objSourcesRow.setCooccurPcnt(lineArr[1]);
							objSourcesRow.setCooccur(lineArr[2]);
							objSourcesRow.setExpectedcooccur(lineArr[3]);
							
							len = len.substring(subStr.length(),len.length());
							
							listSourcesRow.add(objSourcesRow);
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
		
		return arlClusterSources;
	}
	
	
	public static void main(String[] args) {
		
		ClusterSourcesRecordList obj = new ClusterSourcesRecordList("C:\\Dinesh\\Workspaces\\HPCC_WorkSpaces\\PentahoSpoonIntegration\\src\\com\\lexisnexis\\ui\\clustersources\\ClusterSources.csv");
		if(obj.getClusterSourcesRecordList() != null && obj.getClusterSourcesRecordList().size() > 0) {
			for (Iterator<ClusterSourcesRow> iterator = obj.getClusterSourcesRecordList().iterator(); iterator.hasNext();) {
				ClusterSourcesRow objClusterSourcesRow = iterator.next();
				System.out.println("SOURCE: "+objClusterSourcesRow.getSource());
				System.out.println("TOTAL COUNT: "+objClusterSourcesRow.getTotalCount());
				System.out.println("OCCUR PCNT: "+objClusterSourcesRow.getOccurPcnt());
				List<SourcesRow> arl = objClusterSourcesRow.getArlSourcesRow();
				if(arl != null && arl.size() > 0) {
					for(Iterator<SourcesRow> itr = arl.iterator(); itr.hasNext(); ){
						SourcesRow objSourcesRow = itr.next();
						System.out.println("SRC: "+objSourcesRow.getSource());
						System.out.println("COOCCUR PCNT: "+objSourcesRow.getCooccurPcnt());
					}
				}
			}
		}
	}
}
