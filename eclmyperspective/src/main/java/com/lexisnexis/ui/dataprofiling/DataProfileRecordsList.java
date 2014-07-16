package com.lexisnexis.ui.dataprofiling;

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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.lexisnexis.ui.constants.Constants;

import au.com.bytecode.opencsv.CSVReader;

public class DataProfileRecordsList {

	List<DataProlifeRow> arlDataProfileRecords;
	Map<String, Map<String, Map<String, String>>> mapParent;
	
	public DataProfileRecordsList(String fileName) {
		arlDataProfileRecords = new ArrayList<DataProlifeRow>();
		this.mapParent = readFromCSV(fileName);
		addRecords();
	}
	
	public Map<String, Map<String, Map<String, String>>> readFromCSV(String fileName){
		File file = new File(fileName);
		FileInputStream fstream;
		DataInputStream in;
		BufferedReader br;
		CSVReader reader = null;
		Map<String, Map<String, Map<String, String>>> mapParent = new TreeMap<String, Map<String, Map<String, String>>>();
		Map<String, Map<String, String>> mapforEachRow = null;
		try {
			fstream = new FileInputStream(file);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			reader = new CSVReader(br,',','"','\\');
			boolean first = true;
			int firstRowLen = 0;
			String len = "";
			String words = "";
			String characters = "";
			String patterns = "";
			String frequentTerms = "";
			
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
					if(lineArr.length >= firstRowLen){
						if(mapforEachRow != null && mapforEachRow.size() > 0) {
							mapforEachRow = new TreeMap<String, Map<String, String>>();
							mapParent.put(lineArr[1], mapforEachRow);
						} else {
							mapforEachRow = new TreeMap<String, Map<String, String>>();
							mapParent.put(lineArr[1], mapforEachRow);
						}
						
						len = lineArr[3];
						words = lineArr[4];
						characters = lineArr[5];
						patterns = lineArr[6];
						frequentTerms = lineArr[7];
						
						Map<String, String> mapCardinality = new TreeMap<String, String>();
						mapCardinality.put(Constants.KEY_CARDINALITY, lineArr[2]);
						mapforEachRow.put(Constants.KEY_CARDINALITY, mapCardinality);
						mapforEachRow.put(Constants.KEY_LENGTH, new TreeMap<String, String>());
						mapforEachRow.put(Constants.KEY_WORDS, new TreeMap<String, String>());
						mapforEachRow.put(Constants.KEY_CHARACTERS, new TreeMap<String, String>());
						mapforEachRow.put(Constants.KEY_PATTERNS, new TreeMap<String, String>());
						mapforEachRow.put(Constants.KEY_FREQUENT_TERMS, new TreeMap<String, String>());
					} else {
						if(!len.equals("")) {	//Len
							int thisLen = lineArr.length;  //2
							String subStr = "";
							TreeMap<String, String> mapLen = (TreeMap<String, String>)mapforEachRow.get(Constants.KEY_LENGTH);
							for(int i = 0; i < thisLen; i++){
								subStr += lineArr[i];
							}
							mapLen.put(lineArr[0], lineArr[1]);
							len = len.substring(subStr.length(),len.length()); 
						} else if(!words.equals("")) { //words
							int thisLen = lineArr.length;
							String subStr = "";
							TreeMap<String, String> mapLen = (TreeMap<String, String>)mapforEachRow.get(Constants.KEY_WORDS);
							for(int i = 0; i < thisLen; i++){
								subStr += lineArr[i];
							}
							mapLen.put(lineArr[0], lineArr[1]);
							words = words.substring(subStr.length(),words.length());
						} else if(!characters.equals("")) { //characters
							int thisLen = lineArr.length;
							String subStr = "";
							TreeMap<String, String> mapLen = (TreeMap<String, String>)mapforEachRow.get(Constants.KEY_CHARACTERS);
							for(int i = 0; i < thisLen; i++){
								subStr += lineArr[i];
							}
							mapLen.put(lineArr[0], lineArr[1]);
							characters = characters.substring(subStr.length(),characters.length());
						} else if(!patterns.equals("")) { //patterns
							int thisLen = lineArr.length;
							String subStr = "";
							TreeMap<String, String> mapLen = (TreeMap<String, String>)mapforEachRow.get(Constants.KEY_PATTERNS);
							for(int i = 0; i < thisLen; i++){
								subStr += lineArr[i];
							}
							mapLen.put(lineArr[0], lineArr[1]);
							patterns = patterns.substring(subStr.length(),patterns.length());
						} else if(!frequentTerms.equals("")) { //frequentTerms
							int thisLen = lineArr.length;
							String subStr = "";
							TreeMap<String, String> mapLen = (TreeMap<String, String>)mapforEachRow.get(Constants.KEY_FREQUENT_TERMS);
							for(int i = 0; i < thisLen; i++){
								subStr += lineArr[i];
							}
							mapLen.put(lineArr[0], lineArr[1]);
							frequentTerms = frequentTerms.substring(subStr.length(),frequentTerms.length());
							
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
		
		return mapParent;
	}
	
	public void addRecords() {
		
		if(this.mapParent != null) {
			Map<String, Map<String, Map<String, String>>> mapParent = this.mapParent;
			Iterator<String> iter = mapParent.keySet().iterator();
			int count = 1;
			while(iter.hasNext()) {
				DataProlifeRow obj = new DataProlifeRow();
				String key = iter.next();
				//System.out.println("Key: "+key);
				obj.setFieldNumber(count);
				obj.setFieldName(key);
				Map<String, Map<String, String>> val = (Map<String, Map<String, String>>)mapParent.get(key);
				//System.out.println("Value: "+val);
				obj.setMapRowDetails(val);
				Map<String, String> innerKey = (Map<String, String>)val.get(Constants.KEY_CARDINALITY);
				obj.setCardinality(Integer.parseInt(innerKey.get(Constants.KEY_CARDINALITY)));
				arlDataProfileRecords.add(obj);
				count++;
			}
		}
	}
	
	public List<DataProlifeRow> getDataProfileRecordsList() {
		return Collections.unmodifiableList(arlDataProfileRecords);
	}
	
	public static void main(String[] args) {
		String fn = "C:\\Documents and Settings\\ChambeJX.RISK\\My Documents\\spoon-plugins\\spoon-plugins\\perspectives\\saltresults\\src\\main\\java\\com\\lexisnexis\\ui\\dataprofiling\\Dataprofiling_AllProfiles.csv";
		List<DataProlifeRow> arlRowSize = new DataProfileRecordsList(fn).arlDataProfileRecords;
		for (Iterator iterator = arlRowSize.iterator(); iterator.hasNext();) {
			DataProlifeRow dataProlifeRow = (DataProlifeRow) iterator.next();
			System.out.println(dataProlifeRow.getFieldNumber()); 
			System.out.println(dataProlifeRow.getFieldName());
			System.out.println(dataProlifeRow.getCardinality());
			
			Map<String, Map<String, String>> map = dataProlifeRow.getMapRowDetails();
			Set<Entry<String, Map<String, String>>> setEntry = map.entrySet();
			Iterator<Entry<String, Map<String, String>>> itr = setEntry.iterator();
			while(itr.hasNext()){
				Entry<String, Map<String, String>> entry = itr.next();
				System.out.println(entry.getKey());
				Map<String, String> mapInner = (Map<String, String>)entry.getValue();
				
				Set<Entry<String, String>> entrySetInnerMap = (Set<Entry<String, String>>)mapInner.entrySet();
				Iterator<Entry<String, String>> itrInner = entrySetInnerMap.iterator();
				while(itrInner.hasNext()) {
					Entry<String, String> entryInner = itrInner.next();
					System.out.println("KEY: "+entryInner.getKey());
					System.out.println("VALUE: "+entryInner.getValue());
					System.out.println();
					System.out.println();
				}
				
			}
		}
	}
	
}
