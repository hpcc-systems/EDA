package org.hpccsystems.javaecl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class FileInfoSoap {
	ECLSoap soap;
		//eda
	private Boolean isHttps=false;
	private String serverHost;
	private int serverPort;
	private String user;
	private String pass;
	private Boolean allowInvalidCerts=false;	
	private boolean fetchLogicalFiles = true;
	private String numFilesToFetch = "1000";
	private String pageStart = "0";
	public boolean isLogonFail = false;
	private ArrayList<String> errors;
	public FileInfoSoap(String serverHost,int serverPort,String user, String pass){
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.user=user;
		this.errors=new ArrayList<String>();
		this.pass=pass;
	}


	public Boolean getAllowInvalidCerts() {
		return allowInvalidCerts;
	}

	public void setAllowInvalidCerts(Boolean allowInvalidCerts) {
		this.allowInvalidCerts = allowInvalidCerts;
	}

	public ArrayList<String> getErrors()
	{
		return errors;
	}
	public Boolean isHttps() {
		return isHttps;
	}


	public void setHttps(Boolean isHttps) {
		this.isHttps = isHttps;
	}


	public String buildSoapEnv (String fileName){
		//http://192.168.80.132:8010/WsDfu/DFUQuery?ver_=1.19&wsdl
				String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
						"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
						"<soap:Body><DFUQuery xmlns=\"urn:hpccsystems:ws:wsdfu\">" +
						"<Prefix></Prefix>" +
						"<ClusterName></ClusterName>" +
						"<LogicalName>" + fileName + "</LogicalName>" +
						"<Description></Description>" +
						"<Owner></Owner>" +
						"<StartDate></StartDate>" +
						"<EndDate></EndDate>" +
						"<FileType></FileType>" +
						"<FileSizeFrom></FileSizeFrom>" +
						"<FileSizeTo></FileSizeTo>" +
						"<FirstN>" + numFilesToFetch + "</FirstN>" +
						"<FirstNType></FirstNType>" +
						"<PageSize>" + numFilesToFetch + "</PageSize>" +
						"<PageStartFrom>" + pageStart + "</PageStartFrom>" +
						"<Sortby></Sortby>" +
						"<Descending></Descending>" +
						"<OneLevelDirFileReturn></OneLevelDirFileReturn>" +
						"</DFUQuery>" +
						"</soap:Body>" +
						"</soap:Envelope>";
		return soap;
	}
	
	public ArrayList<String> processReturn(InputStream is) throws Exception{
		ArrayList results = new ArrayList();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        Document dom = db.parse(is);

        Element docElement = dom.getDocumentElement();

        NodeList dsList = docElement.getElementsByTagName("DFULogicalFiles");
        if (dsList != null && dsList.getLength() > 0) {
        	
           //ArrayList dsArray = new ArrayList();

           //results = dsArray;

            for (int i = 0; i < dsList.getLength(); i++) {
            	System.out.println("Node");
                Element ds = (Element) dsList.item(i);
                NodeList rowList = ds.getElementsByTagName("DFULogicalFile");
                if (rowList != null && rowList.getLength() > 0) {

                    ArrayList rowArray = new ArrayList();

                    for (int j = 0; j < rowList.getLength(); j++) {
                        Element row = (Element) rowList.item(j);
                        
                        NodeList columnList = row.getChildNodes();
                        for (int k = 0; k < columnList.getLength(); k++) {
                            
                            
                            if(columnList.item(k).getNodeName().equals("Name")){
                                //System.out.println("Name: " + columnList.item(k).getNodeName());
                               // System.out.println("Value: " + columnList.item(k).getTextContent());
                                results.add("~"+columnList.item(k).getTextContent());
                            }  
                        }  
                    }
                }

            }
        }
        //Iterator iterator = results.iterator();
        return results;
	}
	
	/*
	 * fetchClusters
	 * 
	 * @accepts String ("ThorCluster", "RoxieCluster", "")
	 * @returns String[]
	 */
	public String[] fetchFiles(){
		if(!fetchLogicalFiles){
			return new String[0];
		}
		soap = new ECLSoap();
		
		soap.setHostname(serverHost);
		soap.setPort(this.serverPort);
		soap.setUser(user);
		soap.setPass(pass);
		soap.setHttps(this.isHttps);
		String path = "/WsDfu/DFUQuery";
        InputStream is=null;
        try {
			is = soap.doSoap(buildSoapEnv(""), path);
        } catch (Exception e)
        {
        	errors.add("Error sending soap request:" + e.getMessage());
        	e.printStackTrace();
        }
        
		try{

			ArrayList<String> c = processReturn(is);
			String[] clusters = new String[c.size()];
		    return c.toArray(clusters);
		}catch (Exception e){
			errors.add(e.getMessage());
			System.out.println("error");
			System.out.println(e);
		}
		return new String[0];
	}
	
	public ArrayList<String[]> fetchFileMeta(String fileName){
		errors=new ArrayList<String>();
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
				"		<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
				"		<soap:Body>" +
				"			<DFUDefFile xmlns=\"urn:hpccsystems:ws:wsdfu\">" +
				"				<Name>" + fileName + "</Name>" +
				"				<Format>xml</Format>" +
				"			</DFUDefFile>" +
				"		</soap:Body>" +
				"</soap:Envelope>";
		
		soap = new ECLSoap();

		soap.setHostname(serverHost);
		soap.setAllowInvalidCerts(allowInvalidCerts);
		soap.setPort(this.serverPort);
		soap.setUser(user);
		soap.setPass(pass);
		soap.setHttps(isHttps);
		
		String path = "/WsDfu/DFUDefFile?ver_=1.2";
		InputStream is=null;
		try {
			is = soap.doSoap(xml, path);
		} catch (Exception e)
		{
			errors.add("Error making soap request to HPCC web service " + path + ":" + e.getMessage() + " soap data:" + xml);
			return null;
		}
				
		try{
			if(soap.isLogonFail){
				isLogonFail = soap.isLogonFail;
				System.out.println("Authentication Failed, or you don't have permissions to read this file");
				errors.add("Authentication Failed, or you don't have permissions to read this file");
			}else{
				if(is != null){
					return processFileMeta(is);
				}
			}
		}catch(Exception e){
			System.out.println(e);
			errors.add(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<String[]> processFileMeta(InputStream is) throws Exception{
		ArrayList<String[]> results = new ArrayList<String[]>();
		String xml = "";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        Document dom = db.parse(is);

        Element docElement = dom.getDocumentElement();
        System.out.println(dom.getTextContent());
        NodeList dfuResponse = docElement.getElementsByTagName("DFUDefFileResponse");
        //DFUDefFileResponse
        //	defFile
        //  decode base64
        
        if (dfuResponse != null && dfuResponse.getLength() > 0) {
        	
           //ArrayList dsArray = new ArrayList();

           //results = dsArray;

            for (int i = 0; i < dfuResponse.getLength(); i++) {
            	//System.out.println("Node:" + dsList.item(i).getNodeName());
                Element ds = (Element) dfuResponse.item(i);
                //System.out.println(ds.getFirstChild().getNodeName());
                NodeList rowList = ds.getElementsByTagName("defFile");
                NodeList errorList = ds.getElementsByTagName("Exceptions");
                //System.out.println("Node:" + rowList.getLength());
                if (rowList != null && rowList.getLength() > 0) {

                    for (int j = 0; j < rowList.getLength(); j++) {
                        Element row = (Element) rowList.item(j);
                        String data = row.getTextContent();
                        byte[] decoded = javax.xml.bind.DatatypeConverter.parseBase64Binary(data);
                        xml =  new String(decoded);   
                    }
                }
                
                if (errorList != null && errorList.getLength() > 0) {
                	System.out.println("Has ERROR");
                	for (int j = 0; j < errorList.getLength(); j++) {
                		System.out.println("Looping error");
                        Element row = (Element) errorList.item(j);
                        NodeList messages = row.getElementsByTagName("Code");
                        if (messages != null && messages.getLength() > 0) {
                        	System.out.println("Found Code");
                        	for (int k = 0; k < messages.getLength(); k++) {
                        		Element messageRow = (Element) messages.item(j);
                        		String code = messageRow.getTextContent();
                        		//errors.add("HPCC Error " + code);
                        		System.out.println("code: " + code);
                        		if(code.equals("3")){
                        			isLogonFail = true;
                        			errors.add("Authentication Failed, or you don't have permissions to read this file");
                        		}
                        	}
                        }
                        NodeList messages2 = row.getElementsByTagName("Message");
                        if (messages2 != null && messages2.getLength() > 0) {
                        	for (int k = 0; k < messages2.getLength(); k++) {
                        		Element messageRow = (Element) messages2.item(j);
                        		String messagestr = "HPCC error:" + messageRow.getTextContent();
                        		errors.add(messagestr);
                        	}
                        }
                    }
                }

            }
        }
        System.out.println("XML: " + xml);
        if(!xml.equals("")){
        	results = parseRecordXML(xml);
        }
       // Iterator iterator = results.iterator();
        return results;
	}
	
	public String recordXML(String fileName){
		String recxml = "";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
				"		<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
				"		<soap:Body>" +
				"			<DFUDefFile xmlns=\"urn:hpccsystems:ws:wsdfu\">" +
				"				<Name>" + fileName + "</Name>" +
				"				<Format>xml</Format>" +
				"			</DFUDefFile>" +
				"		</soap:Body>" +
				"</soap:Envelope>";
		soap = new ECLSoap();
		
		soap.setHostname(serverHost);
		soap.setPort(this.serverPort);
		soap.setUser(user);
		soap.setPass(pass);
		
		String path = "/WsDfu/DFUDefFile?ver_=1.2";
		
		InputStream is = soap.doSoap(xml, path);
		try{
			if(soap.isLogonFail){
				isLogonFail = soap.isLogonFail;
				System.out.println("Authentication Failed, or you don't have permissions to read this file");
			}else{
				if(is != null){
					ArrayList<String[]> results = new ArrayList<String[]>();
					recxml = "";
			        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			        DocumentBuilder db = dbf.newDocumentBuilder();
			        
			        Document dom = db.parse(is);

			        Element docElement = dom.getDocumentElement();
			        System.out.println(dom.getTextContent());
			        NodeList dfuResponse = docElement.getElementsByTagName("DFUDefFileResponse");
			        //DFUDefFileResponse
			        //	defFile
			        //  decode base64
			        
			        if (dfuResponse != null && dfuResponse.getLength() > 0) {
			        	
			           //ArrayList dsArray = new ArrayList();

			           //results = dsArray;

			            for (int i = 0; i < dfuResponse.getLength(); i++) {
			            	//System.out.println("Node:" + dsList.item(i).getNodeName());
			                Element ds = (Element) dfuResponse.item(i);
			                //System.out.println(ds.getFirstChild().getNodeName());
			                NodeList rowList = ds.getElementsByTagName("defFile");
			                NodeList errorList = ds.getElementsByTagName("Exceptions");
			                //System.out.println("Node:" + rowList.getLength());
			                if (rowList != null && rowList.getLength() > 0) {

			                    for (int j = 0; j < rowList.getLength(); j++) {
			                        Element row = (Element) rowList.item(j);
			                        String data = row.getTextContent();
			                        byte[] decoded = javax.xml.bind.DatatypeConverter.parseBase64Binary(data);
			                        recxml =  new String(decoded);   
			                    }
			                }
			                
			                if (errorList != null && errorList.getLength() > 0) {
			                	System.out.println("Has ERROR");
			                	for (int j = 0; j < errorList.getLength(); j++) {
			                		System.out.println("Looping error");
			                        Element row = (Element) errorList.item(j);
			                        NodeList messages = row.getElementsByTagName("Code");
			                        if (messages != null && messages.getLength() > 0) {
			                        	System.out.println("Found Code");
			                        	for (int k = 0; k < messages.getLength(); k++) {
			                        		Element messageRow = (Element) messages.item(j);
			                        		String code = messageRow.getTextContent();
			                        		System.out.println("code: " + code);
			                        		if(code.equals("3")){
			                        			isLogonFail = true;
			                        		}
			                        	}
			                        }
			                    }
			                }

			            }
			        }
			        System.out.println("XML: " + recxml);
				}
			}
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
		
		return recxml;
	}
	
	public ArrayList<String[]> parseRecordXML(String xml) throws Exception{
		ArrayList<String[]> results = new ArrayList<String[]>();
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        try {
	        InputSource is = new InputSource(new StringReader(xml));
	        Document dom = db.parse(is);
	        
	        Element docElement = dom.getDocumentElement();
	        //System.out.println(docElement.getTextContent());
	        NodeList fields = docElement.getElementsByTagName("Field");
	       //System.out.println(fields.getLength());
	       if (fields != null && fields.getLength() > 0) {
	    	   
	    	   for (int i = 0; i < fields.getLength(); i++) {
	    		   NamedNodeMap attributes = fields.item(i).getAttributes();
	    		   String[] column = new String[5];
	               	column[0] = "";//label
	               	column[1] = "";//type
	               	column[2] = "";//value
	               	column[3] = "";//size
	               	column[4] = "";//maxsize
	    		   //<Field ecltype="decimal8_2" label="shelf_depth" name="shelf_depth" position="14" rawtype="327683" size="5" type="decimal8_"/>
	    		  
	    		   column[0] = attributes.getNamedItem("label").getTextContent();
	    		   column[1] = attributes.getNamedItem("ecltype").getTextContent();
	    		   column[2] = "";
	    		   column[3] = attributes.getNamedItem("size").getTextContent();
	    		   column[4] = attributes.getNamedItem("size").getTextContent();
	    		   
	    		   
	    		   
	    		   
               		results.add(column);
	    	   }
	       }
	        } catch (Exception e) {
	        	errors.add("Error parsing xml response from HPCC:" + e.getMessage() + " xml:" + xml);
	        	e.printStackTrace();
	        	
	        }
	       return results;
	}
	
	public ArrayList<String[]> fetchFileMeta_old(String fileName){
	 	String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
	 			"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
	 			"<soap:Body>" +
	 			"<DFUBrowseData xmlns=\"urn:hpccsystems:ws:wsdfu\">" +
	 			"<LogicalName>" + fileName + "</LogicalName>" +
	 			"<FilterBy></FilterBy>" +
	 			"<ShowColumns></ShowColumns>" +
	 			"<SchemaOnly>true</SchemaOnly>" +
	 			"<StartForGoback></StartForGoback>" +
	 			"<CountForGoback></CountForGoback>" +
	 			"<ChooseFile></ChooseFile>" +
	 			"<Cluster></Cluster>" +
	 			"<ClusterType></ClusterType>" +
	 			"<ParentName></ParentName>" +
	 			"<Start></Start>" +
	 			"<Count></Count>" +
	 			"<DisableUppercaseTranslation></DisableUppercaseTranslation>" +
	 			"</DFUBrowseData>" +
	 			"</soap:Body>" +
	 			"</soap:Envelope>";
	 	
	 	soap = new ECLSoap();
		
		soap.setHostname(serverHost);
		soap.setPort(this.serverPort);
		soap.setUser(user);
		soap.setPass(pass);
		soap.setHttps(isHttps);

		String path = "/WsDfu/DFUBrowseData?ver_=1.1";
		
		InputStream is = soap.doSoap(xml, path);
				
		/*
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String line = null;
        try {
			while((line = in.readLine()) != null) {
			  System.out.println(line);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		try{
			return processFileMeta_old(is);
		}catch(Exception e){
			System.out.println(e);
		}
		return null;
	}
	
	public ArrayList<String[]> processFileMeta_old(InputStream is) throws Exception{
		ArrayList<String[]> results = new ArrayList<String[]>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        Document dom = db.parse(is);

        Element docElement = dom.getDocumentElement();

        NodeList dsList = docElement.getElementsByTagName("DFUBrowseDataResponse");
        
        
        if (dsList != null && dsList.getLength() > 0) {
        	
           //ArrayList dsArray = new ArrayList();

           //results = dsArray;

            for (int i = 0; i < dsList.getLength(); i++) {
            	//System.out.println("Node:" + dsList.item(i).getNodeName());
                Element ds = (Element) dsList.item(i);
                //System.out.println(ds.getFirstChild().getNodeName());
                NodeList rowList = ds.getElementsByTagName("ColumnsHidden");
                //System.out.println("Node:" + rowList.getLength());
                if (rowList != null && rowList.getLength() > 0) {
                		
                	
                	
                   // ArrayList rowArray = new ArrayList();

                    for (int j = 0; j < rowList.getLength(); j++) {
                    	//System.out.println("Node:");
                        Element row = (Element) rowList.item(j);
                        
                        NodeList columnList = row.getChildNodes();
                        for (int k = 0; k < columnList.getLength(); k++) {
                        	//System.out.println("Name: " + columnList.item(k).getChildNodes());
                            
                        	NodeList colList = columnList.item(k).getChildNodes();
                        	String[] column = new String[5];
                        	column[0] = "";//label
                        	column[1] = "";//type
                        	column[2] = "";//value
                        	column[3] = "";//size
                        	column[4] = "";//maxsize
                        	boolean addToList = true;
                        	for(int c=0; c<colList.getLength(); c++){
                        		//System.out.println(colList.item(c).getNodeName() + " : " + colList.item(c).getTextContent());

                    			String nodeName = colList.item(c).getNodeName();
                    			
                    			if(nodeName.equalsIgnoreCase("columnLabel")){
                    				if(colList.item(c).getTextContent().equals("")){
                    					addToList=false;
                    				}
                    				
                    				if(colList.item(c).getTextContent().equalsIgnoreCase("__fileposition__")){
                    					addToList = false;
                    				}
                    				column[0] = colList.item(c).getTextContent();
                    			}else if(nodeName.equalsIgnoreCase("ColumnType")){
                    				column[1] = colList.item(c).getTextContent().replace("xs:", "").toUpperCase();
                    			}else if(nodeName.equalsIgnoreCase("ColumnValue")){
                    				column[2] = colList.item(c).getTextContent();
                    			}else if(nodeName.equalsIgnoreCase("ColumnSize")){
                    				column[3] = colList.item(c).getTextContent();
                    			}else if(nodeName.equalsIgnoreCase("MaxSize")){
                    				
                    				if(colList.item(c).getTextContent().equals("0")){
                    					column[4] = "";
                    				}else{
                    					column[4] = colList.item(c).getTextContent();
                    				}
                    			}
                    			
                    			/*version of java doesnt' do string in switch
                        		switch (colList.item(c).getNodeName()){
                        			
	                        		case "ColumnLabel":
	                        			column[0] = colList.item(c).getTextContent();
	                        			break;
	                        		case "ColumnType":
	                        			column[1] = colList.item(c).getTextContent();
	                        			break;
	                        		case "ColumnValue":
	                        			column[2] = colList.item(c).getTextContent();
	                        			break;
	                        		case "ColumnSize":
	                        			column[3] = colList.item(c).getTextContent();
	                        			break;
	                        		case "MaxSize":
	                        			column[4] = colList.item(c).getTextContent();
	                        			break;
                        		}
                        		*/
                        		
                        	}
                        	if(addToList)
                        		results.add(column);
     
                        }  
                    }
                }

            }
        }
        
       // Iterator iterator = results.iterator();
        return results;
	}
	

	
	public static void main(String[] args){
		FileInfoSoap c = new FileInfoSoap("10.239.227.6", 8010,"","");
		/*String[] test = c.fetchFiles();
		System.out.println("You have " + test.length + " Files");
		for (int i = 0; i<test.length; i++){
			System.out.println(test[i]);
		}*/
		
		ArrayList<String[]> s = c.fetchFileMeta("~saltdemo::sampleusersguideinputdata");
		ArrayList<String[]> s2 = c.fetchFileMeta("~thor::jc::test::product");
		//ArrayList<String[]> s = c.fetchFileMeta("~in::aircraft_reference");
		
		if(s!= null && s.size()==1){
			if(s.get(0)[0].equals("line")){
				//nor rec
				s = new ArrayList<String[]>();
			}
		}
		if(s!= null){
			for(int i = 0; i<s.size(); i++){
				System.out.println(s.get(i).length);
				System.out.println("Type: " + s.get(i)[1]);
				System.out.println("Label: " + s.get(i)[0]);
				System.out.println("Default Value: " + s.get(i)[2]);
			}
		}
		
		
		if(s2!= null && s2.size()==1){
			if(s2.get(0)[0].equals("line")){
				//nor rec
				s2 = new ArrayList<String[]>();
			}
		}
		if(s2!= null){
			for(int i = 0; i<s2.size(); i++){
				System.out.println("Type: " + s2.get(i)[1]);
				System.out.println("Label: " + s2.get(i)[0]);
				System.out.println("Default Value: " + s2.get(i)[2]);
			}
		}
		
	}
}
