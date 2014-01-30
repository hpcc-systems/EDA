package org.hpccsystems.pentaho.job.ecldataset;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.hpccsystems.eclguifeatures.HPCCFileBrowser;
import org.hpccsystems.javaecl.ECLSoap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class test {
	 private List<String> fileNames;
	 test(){
		 fileNames = new ArrayList<String>(); 
	 }
	 
	 static String serverHost = "192.168.115.128";
	 static int serverPort = 8010;
	 static String user = "hpccdemo";
	 static String pass = "hpccdemo";
	 
	 public static InputStream doSoap(String xmldata, String path){
	       //ArrayList<?> response = new ArrayList();
	       //String xml = "";
	       URLConnection conn = null;
	       //boolean isError = false;
	       boolean isSuccess = false;
	       
	       int errorCnt = 0;
	       InputStream is = null;
	       boolean isLogonFail = false;
		while(errorCnt < 5 && !isSuccess && !isLogonFail){
		       try {
		
		            String host = "http://"+serverHost+":"+serverPort+path;
		            URL url = new URL(host);
		            conn = url.openConnection();
		            conn.setDoOutput(true);
		            if(!user.equals("")){
		            	String authStr = user + ":" + pass;
		            }
		            conn.setRequestProperty("Post", path + " HTTP/1.0");
		            conn.setRequestProperty("Host", serverHost);
		            conn.setRequestProperty("Content-Length", ""+xmldata.length() );
		            conn.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
		
		            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		            wr.write(xmldata);
		            wr.flush();
		            if(conn instanceof HttpURLConnection){
		            	HttpURLConnection httpConn = (HttpURLConnection)conn;
		            	int code = httpConn.getResponseCode();
		            	
		            	if(code == 200){
		            		is =  conn.getInputStream();
		            		isSuccess = true;
		            		
		            	}else if (code == 401){
		            		isSuccess = false;
		            		isLogonFail = true;
		            		
		            	}
		            }
		            
		        } catch (Exception e) {
		            e.printStackTrace();
		            errorCnt++;
		        }finally{
		        	if(conn != null){
		        		
		        	}
		        }
		        if(!isSuccess){
		        	try{
		        		Thread.sleep(3500);
		        	}catch (Exception e){
		        		System.out.println("couldn't sleep thread");
		        	}
		        }
	       }
	       // return new HashMap<String, String>();
	          return is;
	    }
	 
	 
	 public  ArrayList<Map<String,String>> fetchScope(String scope){
			ArrayList<Map<String,String>> scopeList = null;
			InputStream is = soapCall(scope);
			if(is != null){
				try{
					scopeList = processFileMeta(is);	
				}catch (Exception e){
					
				}	
			}else{
				scopeList = new ArrayList();
				
			}
			return scopeList;
		}
		
		public static InputStream soapCall (String scope){
			
			String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
								"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
									"<soap:Body>" +
										"<DFUFileView xmlns=\"urn:hpccsystems:ws:wsdfu\">" +
											"<Scope>" + scope + "</Scope>" +
										"</DFUFileView>" +
									"</soap:Body>" +
								"</soap:Envelope>";
			
			String path = "/WsDfu/DFUFileView?ver_=1.2";
			
			InputStream is = doSoap(xml, path);
			return is;
		}
		
		
		public ArrayList<Map<String,String>> processFileMeta(InputStream is) throws Exception{
			ArrayList<Map<String,String>> results = new ArrayList<Map<String,String>>();
			
			String xml = "";
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	      
	        Document dom = db.parse(is);

	        Element docElement = dom.getDocumentElement();
	        //System.out.println(dom.getTextContent());
	        NodeList dfuResponse = docElement.getElementsByTagName("DFUFileViewResponse");
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
	              NodeList rowList = ds.getElementsByTagName("DFULogicalFile");
	              //System.out.println("Node:" + rowList.getLength());
	              if (rowList != null && rowList.getLength() > 0) {

	                  for (int j = 0; j < rowList.getLength(); j++) {
	                      
	                  	Element row = (Element) rowList.item(j);
	                  	String val = "";
	                  	Map<String, String> map = new HashMap<String, String>();
	                  	 String isDir = row.getElementsByTagName("isDirectory").item(0).getTextContent();
	                  	if(isDir.equals("1")){
	                  		val = row.getElementsByTagName("Directory").item(0).getTextContent();
	                  		map.put("name", val);
	                  		map.put("type", "Directory");
	                  	}else{
	                  		val = row.getElementsByTagName("Name").item(0).getTextContent();
	                  		map.put("name", val);
	                  		map.put("type", "File");
	                  	}
	                  	results.add(map);
	                      //byte[] decoded = javax.xml.bind.DatatypeConverter.parseBase64Binary(data);
	                      //xml =  new String(decoded);   
	                  }
	              }

	          }
	      }
	      //System.out.println("XML: " + xml);
	      //results = parseRecordXML(xml);
	     // Iterator iterator = results.iterator();
	      return results;
		}
		
	/**
	 * 
	 * @param myscope
	 * @param mytype
	 * @param myname
	 * @return
	 * Recursive method for finding logical file names
	 */

	public  int chaprasi(String myscope, String mytype, String myname){
		if(mytype.equals("File")){
			fileNames.add(myname);
			return 0;
		}
			
		ArrayList<Map<String,String>> topScope = fetchScope(myscope);
	    for (int i = 0; i < topScope.size(); i++) {
	    	String name = (String)topScope.get(i).get("name");
	    	 
	    	String type = (String)topScope.get(i).get("type");
	    	//System.out.println(name+" "+type);
	    	if(myscope != "")
    			myscope += "::"+name;
    		else
    			myscope += name;
	    	
	    	int Jlo = chaprasi(myscope, type, name);  //AWESOME RECURSION
	    	 
	    	String[] S0 = name.split("::");
	    	String[] S = myscope.split("::");
	    	myscope = "";
	    	for(int k = 0; k<S.length-S0.length; k++){
	    		if(myscope != "")
	    			myscope += "::"+S[k];
	    		else
	    			myscope += S[k];
		    	
	    	} 	    	 
	    }
		return 0;
	}

	public static ArrayList<String[]> fetchFileMeta(String fileName) throws Exception{
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
				"		<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
				"		<soap:Body>" +
				"			<DFUDefFile xmlns=\"urn:hpccsystems:ws:wsdfu\">" +
				"				<Name>" + fileName + "</Name>" +
				"				<Format>xml</Format>" +
				"			</DFUDefFile>" +
				"		</soap:Body>" +
				"</soap:Envelope>";

		String path = "/WsDfu/DFUDefFile?ver_=1.2";
		
		InputStream is = doSoap(xml, path);
		
		try{
			boolean isLogonFail = false;
			if(isLogonFail ){
				//isLogonFail = soap.isLogonFail;
				System.out.println("Authentication Failed, or you don't have permissions to read this file");
			}else{
				if(is != null){
					return processFileMeta2(is);
				}
			}
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
		return null;
	}

	
	public static ArrayList<String[]> processFileMeta2(InputStream is) throws Exception{
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
                        		System.out.println("code: " + code);
                        		if(code.equals("3")){
                        			boolean isLogonFail = true;
                        		}
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
	
	public static ArrayList<String[]> parseRecordXML(String xml) throws Exception{
		ArrayList<String[]> results = new ArrayList<String[]>();
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
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
	       return results;
	}

	
	public static void main(String[] args){
		test obj = new test();
		int K = obj.chaprasi("","","");
		System.out.println(obj.fileNames);
		
		System.out.println("Enter your choice of index < "+obj.fileNames.size());
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String readLine = null;
		try {
			readLine = reader.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int idx = Integer.parseInt(readLine);
		System.out.println(obj.fileNames.get(idx));  
		String s = obj.fileNames.get(idx);
		ArrayList<String[]> file = null;
		try {
			file = fetchFileMeta("~"+s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(file != null && file.size()==1){
			if(file.get(0)[0].equals("line")){
				//nor rec
				file = new ArrayList<String[]>();
			}
		}
		for(Iterator<String[]> it = file.iterator(); it.hasNext();){
			String[] S = it.next();
			System.out.println("Next Record");
			System.out.println(S[0]);
			System.out.println(S[1]+"\n");			
		}
	}
}