package org.hpccsystems.sortui.table;

import org.hpccsystems.sortui.table.SortColumnRecordList;

public class SortColumnEntryBO {
	
	
	private SortColumnRecordList recordList;
	
	private int entryIndex = 0;
	
	public SortColumnEntryBO(){}
	public SortColumnEntryBO(String conceptCSV){
		//this.conceptName = conceptName;
		this.fromCSV(conceptCSV);
	}

	
	
	
	
	
	public int getEntryIndex() {
		return entryIndex;
	}
	public void setEntryIndex(int entryIndex) {
		this.entryIndex = entryIndex;
	}
	
	public SortColumnRecordList getRecordList() {
		return recordList;
	}
	public void setRecordList(SortColumnRecordList recordList) {
		this.recordList = recordList;
	}
	public String toCSV(){
		String csv = new String();
		String delm = ",";
		
		if(recordList != null){
			csv += recordList.saveListAsString();//9
		}
		
		
        return csv;
    }
    public String fromCSVtoXML(String in){
    	 String[] strArr = in.split("[,]");
    	 String xml = "";
         try{
        		recordList = new SortColumnRecordList();
        	 xml = "<hyg:concept-def>\r\n" +
        				recordList.fromStringToXML(strArr[9]) +
        			"</hyg:concept-def>\r\n";
        	 
         }catch (Exception e){
         	System.out.println("ConceptEntryBO: Failed to open fromCSV");
         	System.out.println(in);
         	System.out.println(e.toString());
         	e.printStackTrace();
         }
         return xml;
    }
    public void fromCSV(String in){
        String[] strArr = in.split("[,]");
        try{
        	
        	recordList = new SortColumnRecordList();
        	recordList.openFromString(strArr[9]);
        
        
        
        }catch (Exception e){
        	System.out.println("ConceptEntryBO: Failed to open fromCSV");
        	System.out.println(in);
        	System.out.println(e.toString());
        	e.printStackTrace();
        }
        
    }
	
}
