package com.lexisnexis.ui.clustercounts;

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

public class ClusterCountsRecordList {
	
	List<ClusterCountsRow> arlClusterCountsRecords;
	
	public ClusterCountsRecordList(String fileName) {
		arlClusterCountsRecords = addRecords(fileName);
		
	}
	
	public List<ClusterCountsRow> addRecords(String fileName) {
		return readFromCSV(fileName);
	}
	
	public List<ClusterCountsRow> getClusterCountsRecordsList() {
		return Collections.unmodifiableList(arlClusterCountsRecords);
	}
	
	public List<ClusterCountsRow> readFromCSV(String fileName){
		
		File file = new File(fileName);
		FileInputStream fstream;
		DataInputStream in;
		BufferedReader br;
		CSVReader reader = null;
		List<ClusterCountsRow> arlList = new ArrayList<ClusterCountsRow>();
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
					ClusterCountsRow obj = new ClusterCountsRow();
			    	for (int i = 0; i < lineArr.length; i++) {
			    		switch (i) {
						case 0:
							obj.setIncluster(lineArr[i]);
							break;
						case 1:
							obj.setNumberOfClusters(lineArr[i]);
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
		
		List<ClusterCountsRow> list = new ClusterCountsRecordList("C:\\Dinesh\\Workspaces\\HPCC_WorkSpaces\\PentahoSpoonIntegration\\src\\com\\lexisnexis\\ui\\clustercounts\\Cluster_Counts.csv").getClusterCountsRecordsList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ClusterCountsRow sourceOutliersRow = (ClusterCountsRow) iterator.next();
			System.out.println("INCLUSTER: "+sourceOutliersRow.getIncluster());
			System.out.println("NUMBER OF CLUSTERS: "+sourceOutliersRow.getNumberOfClusters());
		}
	}
	
}
