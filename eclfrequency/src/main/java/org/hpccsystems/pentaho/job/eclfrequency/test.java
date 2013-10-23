package org.hpccsystems.pentaho.job.eclfrequency;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import sun.misc.BASE64Encoder;


@SuppressWarnings("restriction")
public class test{// extends ApplicationWindow {
	
	private static String hostname = "192.168.115.128";
	private static int port = 8010;
	private static String user = "";
	private static String pass = "";
	public static boolean isLogonFail = false;
	
	
	public static InputStream doSoap(String xmldata, String path){
	       //ArrayList<?> response = new ArrayList();
	       //String xml = "";
	       URLConnection conn = null;
	       //boolean isError = false;
	       boolean isSuccess = false;
	       
	       int errorCnt = 0;
	       InputStream is = null;
	       while(errorCnt < 5 && !isSuccess && !isLogonFail){
		       try {
		            //String encoding = new sun.misc.BASE64Encoder().encode ((user+":"+pass).getBytes());
		            String host = "http://"+hostname+":"+port+path;
		            //System.out.println("HOST: " + host);
		            URL url = new URL(host);
		            
		            
		             // Send data
		            conn = url.openConnection();
		            conn.setDoOutput(true);
		            //added back in since Authenticator isn't allways called and the user wasn't passed if the server didn't require auth
		            if(!user.equals("")){
		            	String authStr = user + ":" + pass;
		            	//System.out.println("USER INFO: " + authStr);
		            	BASE64Encoder encoder = new BASE64Encoder();
		            	String encoded = encoder.encode(authStr.getBytes());
		            	
		            	
		            	conn.setRequestProperty("Authorization","Basic "+encoded);
		            }
		            
		            conn.setRequestProperty("Post", path + " HTTP/1.0");
		            conn.setRequestProperty("Host", hostname);
		            conn.setRequestProperty("Content-Length", ""+xmldata.length() );
		            conn.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
		
		            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		            wr.write(xmldata);
		            wr.flush();
		            //wr.close();
		           // if(conn.get)
		            if(conn instanceof HttpURLConnection){
		            	HttpURLConnection httpConn = (HttpURLConnection)conn;
		            	int code = httpConn.getResponseCode();
		            	System.out.println("Connection code: " + code);
		            	if(code == 200){
		            		is =  conn.getInputStream();
		            		isSuccess = true;
		            		System.out.println("Connection success code 200 ");
		            	}else if (code == 401){
		            		isSuccess = false;
		            		isLogonFail = true;
		            		System.out.println("Permission Denied");
		            	}
		            }
		            //return conn.getInputStream();
		            
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
		//soap = new ECLSoap();
		
		//soap.setHostname(serverHost);
		//soap.setPort(serverPort);
		//soap.setUser(user);
		//soap.setPass(pass);
		
		String path = "/WsDfu/DFUDefFile?ver_=1.2";
		
		InputStream is = doSoap(xml, path);
		
		//if(is != null){
			//return processFileMeta(is);
	//}
				
		try{
			if(isLogonFail){
				//isLogonFail = soap.isLogonFail;
				System.out.println("Authentication Failed, or you don't have permissions to read this file");
			}else{
				if(is != null){
					return processFileMeta(is);
				}
			}
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
		return null;
	}
	public static ArrayList<String[]> processFileMeta(InputStream is) throws Exception{
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
                        			isLogonFail = true;
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

	public static ArrayList<String[]> fetchFileDetails(String fileName) throws Exception{
		//FileInfoSoap c = new FileInfoSoap(hostname,port,user,pass);
		ArrayList<String[]> file = fetchFileMeta(fileName);
		if(file != null && file.size()==1){
			if(file.get(0)[0].equals("line")){
				//nor rec
				file = new ArrayList<String[]>();
			}
		}
		//isLogonFail = isLogonFail;
		return file;
	}
	
	public static void main(String[] args) throws Exception{
		ArrayList<String[]> list = fetchFileDetails("~thor::intro::ks::class::uidpersons");
		for(Iterator<String[]> it = list.iterator(); it.hasNext();){
			String[] S = it.next();
			System.out.println("Next Record");
			System.out.println(S[0]);
			System.out.println(S[1]+"\n");			
		}
	}
}

