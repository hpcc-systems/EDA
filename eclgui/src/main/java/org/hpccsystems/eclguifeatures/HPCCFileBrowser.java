package org.hpccsystems.eclguifeatures;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.widgets.Display;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.hpccsystems.javaecl.ECLSoap;
public class HPCCFileBrowser {
	ECLSoap soap;
	
	private String serverHost;
	private int serverPort;
	private String user;
	private String pass;
	private Tree tree;
	private String selectedFile;
  
	
	public HPCCFileBrowser(String serverHost,int serverPort,String user, String pass){
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.user=user;
		this.pass=pass;
	}

	public String run(Shell parentShell, String currFile){
	    //Display display = new Display();
	    final Shell shell = new Shell(parentShell);
	    shell.setLayout(new FillLayout());
	    shell.setText("Logical File Browser");
	    Group treeGroup = new Group(shell, SWT.SHADOW_NONE);
       
	    tree = new Tree(treeGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	    tree.setHeaderVisible(false);
	    tree.setSize(500, 500);
	    ArrayList<Map<String,String>> topScope = fetchScope("");
	    for (int i = 0; i < topScope.size(); i++) {
	      TreeItem item = new TreeItem(tree, SWT.NONE);
	      item.setText(new String[] { (String)topScope.get(i).get("name")});
	      if(((String)topScope.get(i).get("type")).equals("Directory")){
		      for (int j = 0; j < 1; j++) {
		        TreeItem subItem = new TreeItem(item, SWT.NONE);
		        subItem.setText(new String[] { "" });
		        
		      }
	      }
	    }
	    tree.addTreeListener(new TreeListener(){

			@Override
			public void treeCollapsed(TreeEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void treeExpanded(TreeEvent e) {
				// TODO Auto-generated method stub
				TreeItem ti = (TreeItem) e.item;
				expandBranch(ti);
			}
	    	
	    });
	    tree.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("Mouse Double Clicked");
				//System.out.println(((TreeItem)tree.getSelection()[0]).getText());
				//System.out.println(e.getSource());
				TreeItem ti = ((TreeItem)tree.getSelection()[0]);
				String sel = ti.getText();
				if(ti.getItemCount()==0){
					selectedFile = sel;
					shell.dispose();
				}
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
	    	
	    });
	    expandTreeToCurrentFile(currFile);
	    shell.pack();
	    shell.open();
	    while (!shell.isDisposed()) {
		      if (!shell.getDisplay().readAndDispatch()) {
		    	  shell.getDisplay().sleep();
		      }
		    }

	    return selectedFile;
  }
	public void expandBranch(TreeItem ti){
		  
		//System.out.println("Tree expanded:: " + ti.getText());
		String scope = ti.getText();
		TreeItem parent = ti.getParentItem();
		while(parent != null){
			//System.out.println("Parent" + parent);
			String p = parent.getText();
			scope = p + "::" + scope;
			//System.out.println("Scope: " + scope);
			parent = parent.getParentItem();
		}
		ArrayList<Map<String,String>> thisScope = fetchScope(scope);
		//String[] thisScope = fetchScope(scope);
		ti.removeAll();
		//tree.removeAll();
		
		for (int j = 0; j < thisScope.size(); j++) {
			//System.out.println("Adding: " + thisScope[j]);
	        TreeItem subItem = new TreeItem(ti, SWT.NONE);

	        subItem.setText(new String[] { (String)thisScope.get(j).get("name")});
	        if(((String)thisScope.get(j).get("type")).equals("Directory")){
	        	for (int k = 0; k < 1; k++) {
	        		TreeItem subItem2 = new TreeItem(subItem, SWT.NONE);
		        	subItem2.setText(new String[] { "" });
		        
	        	}
	        }
		}
		
		tree.layout();
  }
  
  public void expandTreeToCurrentFile(String currFile){
	  TreeItem[] ti = tree.getItems();
	  //System.out.println("Curr File: " + currFile);
	  if(currFile.startsWith("~")){
		  currFile = currFile.substring(1, currFile.length());
	  }
	  String[] parts = currFile.split("::");
	  TreeItem[] lastBranch = null;
	  for (int j =0; j<parts.length; j++){
		  //System.out.println("Part (" + j + "): " + parts[j]);
		  TreeItem[] thisBranch = ti;
		  
		  for (int i = 0; i<thisBranch.length; i++){
			  if(thisBranch[i].getText().equals(parts[j])){
				  
				  //System.out.println("cert loc: " + i + " : " + thisBranch[i].getText());
				  thisBranch[i].setExpanded(true);
				  expandBranch(thisBranch[i]);
				  ti = thisBranch[i].getItems();
				  //System.out.println(thisBranch[i].getItemCount());
				  lastBranch = ti;
			  }
		  }
		  //System.out.println(thisBranch.length);
		  //if(thisBranch.length==0){
			//  System.out.println("Selected");
		  //}
		  
		 
		  
	  }
	  if(lastBranch != null){
		  for (int i = 0; i<lastBranch.length; i++){
			 // System.out.println(lastBranch[i].getText());
			  if(currFile.equals(lastBranch[i].getText())){
				  tree.setSelection(lastBranch[i]);
			  }
		  }
	  }
  }
	
	 public ArrayList<Map<String,String>> fetchScope(String scope){
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
		
		public InputStream soapCall (String scope){
			soap = new ECLSoap();
			
			soap.setHostname(serverHost);
			soap.setPort(this.serverPort);
			soap.setUser(user);
			soap.setPass(pass);
			
			String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
								"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
									"<soap:Body>" +
										"<DFUFileView xmlns=\"urn:hpccsystems:ws:wsdfu\">" +
											"<Scope>" + scope + "</Scope>" +
										"</DFUFileView>" +
									"</soap:Body>" +
								"</soap:Envelope>";
			
			String path = "/WsDfu/DFUFileView?ver_=1.2";
			
			InputStream is = soap.doSoap(xml, path);
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
}
