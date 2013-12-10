package org.pentaho.di.plugins.perspectives.eclresults;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.pentaho.di.core.EngineMetaInterface;
import org.pentaho.di.core.gui.SpoonFactory;
import org.pentaho.di.ui.spoon.Spoon;
import org.pentaho.di.ui.spoon.SpoonPerspective;
import org.pentaho.di.ui.spoon.SpoonPerspectiveListener;
import org.pentaho.ui.xul.XulOverlay;
import org.pentaho.ui.xul.impl.DefaultXulOverlay;
import org.pentaho.ui.xul.impl.XulEventHandler;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import org.eclipse.swt.widgets.Composite;

import java.io.*;
import java.lang.reflect.Method;

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;


import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;


import au.com.bytecode.opencsv.CSVReader;

import com.lexisnexis.ui.clustercounts.ClusterCountsTable;
import com.lexisnexis.ui.datahygiene.DataHygieneTable;
import com.lexisnexis.ui.dataprofiling.DataProfileTable;
import com.lexisnexis.ui.datasummary.DataSummaryTable;
import com.lexisnexis.ui.optimizedlayout.OptimizedLayoutMain;
import com.lexisnexis.ui.sourceoutliers.SourceOutliersTable;
import com.lexisnexis.ui.clustersources.ClusterSourcesTable;
import com.lexisnexis.ui.sourceprofiles.SourceProfilesTable;

public class ECLResultsSwtPerspective implements SpoonPerspective {

  private Composite comp;
  private static ECLResultsSwtPerspective instance = new ECLResultsSwtPerspective();
  
  private String dataIn;
  private String fileName;
  private int fileCount;

  

  private int middle = 100;
  private int margin = 25;

  private Label lbl;
  private Shell parentShell;
  private Table table;
  
  private CTabFolder folder;
  
  private boolean isActive;
  
  private String wuid = "";
  private String jobname = "";
  private String serverAddress = "";

  public void setFileName(String fn){
      fileName = fn;
      lbl.setText("Data returned from HPCC " + fileName);

  }
  public String getFileName(){
      return fileName;
  }
  
  private ECLResultsSwtPerspective(){  
   // System.out.println("create eclResults ECLResultsSwtPerspective");
    createUI();
  }

  private void createUI(){
    String newFile = "";
    
    
    parentShell = ((Spoon) SpoonFactory.getInstance()).getShell();
    
    Display display = parentShell.getDisplay();
   
    comp = new Composite(((Spoon) SpoonFactory.getInstance()).getShell(), SWT.BORDER);
    comp.setLayout(new GridLayout());
    comp.setLayoutData(new GridData(GridData.FILL_BOTH));

    lbl = new Label(comp, SWT.CENTER | SWT.TOP);
   
    GridData ldata = new GridData(SWT.CENTER, SWT.TOP, true, false);
    lbl.setLayoutData(ldata);
    lbl.setText("Preview data returned from HPCC");

    Button fileButton = new Button(comp, SWT.PUSH | SWT.SINGLE | SWT.TOP);
 
    fileButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
    fileButton.setText("OPEN FILE");
  
    //Listener for the file open button (fileButton)
    Listener fileOpenListener = new Listener() {

        public void handleEvent(Event e) {
            String newFile = buildFileDialog();
            if(newFile != ""){
                fileName = newFile;
                //TODO: create new tab for file
                //openFile(fileName);
                openResultsXML(fileName);
                
                //int len = folder.getChildren().length;
                int len = folder.getItemCount();
                System.out.println("Number of tabs: " + len);
                folder.setSelection(len-1);
            }
        }
    };
     
  fileButton.addListener(SWT.Selection, fileOpenListener);
  
  folder = new CTabFolder(comp, SWT.CLOSE);
  folder.setSimple(false);
  folder.setBorderVisible(true);
  folder.setLayoutData(new GridData(GridData.FILL_BOTH));
  
     
  }
  public void buildWUIDTab(String wuid){
	  	CTabItem tab2 = new CTabItem(folder, SWT.NONE);
	    tab2.setText(wuid);
	    
	    Table table = new Table (folder, SWT.VIRTUAL | SWT.BORDER);
	    table.setLinesVisible (true);
	    table.setHeaderVisible (true);
	    table.clearAll();
	    GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
	    data.heightHint = 200;
	    table.setLayoutData(data);
	    
	    //openFile(fileName,table);
	    
	    tab2.setControl(table);
  }
  public void openResultsXML(String fileName){
		
      String outStr;
      try{
    	  
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = (Document) dBuilder.parse(fileName);
			doc.getDocumentElement().normalize();
			
			if(doc.getElementsByTagName("wuid") != null){
				wuid =  doc.getElementsByTagName("wuid").item(0).getTextContent();
			}
			if(doc.getElementsByTagName("jobname") != null){
				jobname =  doc.getElementsByTagName("jobname").item(0).getTextContent();
			}
			if(doc.getElementsByTagName("serverAddress") != null){
				serverAddress =  doc.getElementsByTagName("serverAddress").item(0).getTextContent();
			}
			//WUID
			//System.out.println(wuid);
			//lets create a tab for the wuid
			CTabItem wuidTab = new CTabItem(folder, SWT.NONE);
			wuidTab.setText(jobname + " " + wuid);
			
			folder.setSelection(folder.indexOf(wuidTab));
			 System.out.println("BUILDTAB--------" + folder.indexOf(wuidTab));
			Composite tabHolder = new Composite(folder, SWT.NONE);
			tabHolder.setLayout(new GridLayout());
			tabHolder.setLayoutData(new GridData(GridData.FILL_BOTH));
			final String thisWuid = wuid;
			final String thisServerAddress = serverAddress;
			//add link here
			//Label link = new Label();
			Link link = new Link(tabHolder, SWT.NONE);
			link.setText("<a>View Workunit in the Default Web Browser</a>");
			link.addListener (SWT.Selection, new Listener () {
		        public void handleEvent(Event event) {
		        	openUrl(thisServerAddress + "/WsWorkunits/WUInfo?Wuid=" + thisWuid);
		        }
		    });
			
			
			CTabFolder subfolder = new CTabFolder(tabHolder, SWT.CLOSE);
			subfolder.setSimple(false);
			subfolder.setBorderVisible(true);
			subfolder.setLayoutData(new GridData(GridData.FILL_BOTH));
			
			NodeList results = doc.getElementsByTagName("result");
          
		  for (int temp = 0; temp < results.getLength(); temp++) {
			  Node result = results.item(temp);
			  NamedNodeMap att = result.getAttributes();
			  //type
			  String resType = att.getNamedItem("resulttype").getTextContent();
			  //System.out.println("resType: |" + resType + "|");
			  //filename
			  String filePath = result.getTextContent();
			  //System.out.println(filePath);
			  
			  //CTabItem resultTab = new CTabItem(subfolder, SWT.NONE);
			  //resultTab.setText(resType);
			  //so here we use type and decide what tab to open and pass filename
			 // buildTab(filePath,resType,subfolder);
			  if(resType != null && filePath != null){
				  if(resType.equalsIgnoreCase("ClusterCounts")){
					  buildClusterCountsTab(filePath,resType,subfolder);
				  }else if(resType.equalsIgnoreCase("SrcProfiles")){
					  buildSrcProfilesTab(filePath,resType,subfolder);
				  }else if(resType.equalsIgnoreCase("Hygiene_ValidityErrors")){
					  buildHygieneValidityErrorsTab(filePath,resType,subfolder);
				  }else if(resType.equalsIgnoreCase("ClusterSrc")){
					  buildClusterSrcTab(filePath,resType,subfolder);
				  }else if(resType.equalsIgnoreCase("SrcOutliers")){
					  buildSrcOutliersTab(filePath,resType,subfolder);
				  }else if(resType.equalsIgnoreCase("Dataprofiling_AllProfiles")){
					  buildProfileTab(filePath,resType,subfolder);
				  }else if(resType.equalsIgnoreCase("Dataprofiling_SummaryReport")){
					  buildSummaryTab(filePath,resType,subfolder);
				  }else if(resType.equalsIgnoreCase("Dataprofiling_OptimizedLayout")){
					  buildOptimizedLayoutTab(filePath,resType,subfolder);
				  }else if(resType.toLowerCase().startsWith("PieChart".toLowerCase()) || resType.toLowerCase().startsWith("LineChart".toLowerCase()) ||
						   resType.toLowerCase().startsWith("BarChart".toLowerCase()) || resType.toLowerCase().startsWith("ScatterChart".toLowerCase())){
					  buildTab(filePath,resType,subfolder);
					  buildBrowser(resType, subfolder, thisServerAddress, thisWuid);
				  }else{//CleanedData
					  buildTab(filePath,resType,subfolder);
				  }
			  }
		  }
		  wuidTab.setControl(tabHolder);
    }catch (Exception e){//Catch exception if any
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
     }
  
      
      
  }
  public void buildBrowser(String resType, CTabFolder subfolder, String ServerAddress, String Wuid){
	  	System.out.println("buildBrowser");        
	    CTabItem tab2 = new CTabItem(subfolder, SWT.NONE);
	    tab2.setText(resType);
	    
	    final Browser browser;
		try {
			browser = new Browser(subfolder, SWT.NONE);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			subfolder.dispose();
			return;
		}
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.horizontalSpan = 3;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		browser.setLayoutData(data);
		
		final Label status = new Label(subfolder, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		status.setLayoutData(data);

		final ProgressBar progressBar = new ProgressBar(subfolder, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		progressBar.setLayoutData(data);
		
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
					if (event.total == 0) return;                            
					int ratio = event.current * 100 / event.total;
					progressBar.setSelection(ratio);
			}
			public void completed(ProgressEvent event) {
				progressBar.setSelection(0);
			}
		});
		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent event) {
				status.setText(event.text);	
			}
		});
		
		browser.setUrl(ServerAddress + "/WsWorkunits/WUResultView?Wuid=" + Wuid + "&ResultName="+resType+"&ViewName=Google%20Chart%20by%20name");
		tab2.setControl(browser);
	      
  }
  public void buildTab(String filename,String resType,CTabFolder subfolder){
    System.out.println("buildTab");        
    CTabItem tab2 = new CTabItem(subfolder, SWT.NONE);
    tab2.setText(resType);
    
    Table table = new Table (subfolder, SWT.VIRTUAL | SWT.BORDER);
    table.setLinesVisible (true);
    table.setHeaderVisible (true);
    table.clearAll();
    GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
    data.heightHint = 200;
    table.setLayoutData(data);
    
    openFile(filename,table);
    
    tab2.setControl(table);
    System.out.println("BUILDSUBTAB--------" + subfolder.indexOf(tab2));
    subfolder.setSelection(subfolder.indexOf(tab2));
  }
  
  public void buildOptimizedLayoutTab(String thefilename,String resType,CTabFolder subfolder){
	System.out.println("buildOptimizedLayoutTab");
	OptimizedLayoutMain olm = new OptimizedLayoutMain(thefilename);
	CTabItem tabOL = new CTabItem(subfolder,SWT.NONE);
	tabOL.setText("Optimized Layout");
	Text t = olm.createContents(subfolder);
	tabOL.setControl(t);
	subfolder.setSelection(subfolder.getItemCount()-1);
  }
  
  public void buildSummaryTab(String thefilename,String resType,CTabFolder subfolder){
	  System.out.println("buildSummaryTab");
	CTabItem tabSumm = new CTabItem(subfolder, SWT.NONE);
	tabSumm.setText("Data Summary");
	DataSummaryTable dst = new DataSummaryTable(thefilename);
	Composite prof = dst.createContents(subfolder);
	tabSumm.setControl(prof);
	subfolder.setSelection(subfolder.getItemCount()-1);
  }
  
  public void buildProfileTab(String filename,String resType,CTabFolder subfolder){
	  System.out.println("buildProfileTab");
	CTabItem tabDP = new CTabItem(subfolder, SWT.NONE);
	tabDP.setText("Data Profile");
	DataProfileTable dpt = new DataProfileTable(filename);
	dpt.setFileName(filename);
	Composite prof = dpt.createContents(subfolder);
	tabDP.setControl(prof);
	subfolder.setSelection(subfolder.getItemCount()-1);
  }
  
  public void buildSrcOutliersTab(String thefilename,String resType,CTabFolder subfolder){
	  System.out.println("buildSrcOutliersTab");
	    SourceOutliersTable table = new SourceOutliersTable(thefilename);
	    table.createContents(subfolder); 
	    subfolder.setSelection(subfolder.getItemCount()-1);
  }
  public void buildClusterSrcTab(String thefilename,String resType,CTabFolder subfolder){
	  System.out.println("buildClusterSrcTab");
	  ClusterSourcesTable table = new ClusterSourcesTable(thefilename);
	   table.createContents(subfolder);
	   subfolder.setSelection(subfolder.getItemCount()-1);
	   //buildTab(thefilename,resType,subfolder);
  }
  public void buildClusterCountsTab(String thefilename,String resType,CTabFolder subfolder){
	  System.out.println("buildClusterCountsTab");
	    ClusterCountsTable table = new ClusterCountsTable(thefilename);
	    table.createContents(subfolder);
	    subfolder.setSelection(subfolder.getItemCount()-1);
  }
  public void buildSrcProfilesTab(String thefilename,String resType,CTabFolder subfolder){
	  System.out.println("buildSrcProfilesTab");
	    SourceProfilesTable table = new SourceProfilesTable(thefilename);
	    table.createContents(subfolder);
	    subfolder.setSelection(subfolder.getItemCount()-1);
	  //buildTab(thefilename,resType,subfolder);
  }
  public void buildHygieneValidityErrorsTab(String thefilename,String resType,CTabFolder subfolder){
	  System.out.println("buildHygieneValidityErrorsTab");
	    DataHygieneTable table = new DataHygieneTable(thefilename);
	    table.createContents(subfolder);
	    subfolder.setSelection(subfolder.getItemCount()-1);
  }

  public static ECLResultsSwtPerspective getInstance(){
       // System.out.println("ECLResultsSwtPerspective");
    return instance;
  }

  public void setActive(boolean b) {
      //the function is called 3 times when leaving this perspective this odd
      //logic is here so it doesn't reload the data when leaving this perspective
      if(b){
         if(!this.isActive){
            System.out.println("create eclResults setActive");
            if(System.getProperties().getProperty("resultsFile") != null && !System.getProperties().getProperty("resultsFile").equals("")){
            	String xmlFile = System.getProperties().getProperty("resultsFile");
            	ArrayList<String> resultFiles = parseData("resultsFile");
            	for(int i =0; i< resultFiles.size(); i++){
            		openResultsXML(resultFiles.get(i));
          	  	}
            	//openResultsXML(xmlFile);
            	System.getProperties().setProperty("resultsFile","");
            }
            int len = folder.getItemCount();
            folder.setSelection(len-1);
            this.isActive = true;
         }else{
             this.isActive = false;
         }
          
      }else{
           System.out.println("create eclResults setActive -- deactivate");
      }
  }
  
  public ArrayList parseData(String propName){
	  ArrayList<String> files = new ArrayList();
	  String saltData = "";
	  	 try{
	  		 if(System.getProperty(propName) != null){
	  			 saltData = System.getProperty(propName);
	  		 }
	  		StringTokenizer fileTokens = new StringTokenizer(saltData,",");
		     	while (fileTokens.hasMoreElements()) {
		     		String file = fileTokens.nextToken();
		     		
		     		if(file != null && !file.equals("null") && !file.equals("")){
		     			System.out.println("Built tab from list" + file);
		     			
		     			files.add(file);
		     		}
		     	}
		     	 saltData = "";
	  	 }catch (Exception e){
	  		 System.out.println("Failed to open files ");
	  	 }
	  	 //just incase it was a signle node
	  	 if(saltData != null && !saltData.equals("null") && !saltData.equals("")){
	  		System.out.println("Built tab from single " + saltData);
	  		files.add(saltData);
	  	 }
	  	System.setProperty(propName,"");
	  	return files;
  }
  
  
  public List<XulOverlay> getOverlays() {
    return Collections.singletonList((XulOverlay) new DefaultXulOverlay("org/pentaho/di/plugins/perspectives/eclresults/res/spoon_perspective_overlay.xul"));
  }

  public List<XulEventHandler> getEventHandlers() {
    return Collections.singletonList((XulEventHandler) new ECLResultsPerspectiveHandler());
  }

  public void addPerspectiveListener(SpoonPerspectiveListener spoonPerspectiveListener) {
       System.out.println("addPerspectiveListner");
  }

  public String getId() {
    //System.out.println("getId");
    return "eclresults";
  }

  
  // Whatever you pass out will be reparented. Don't construct the UI in this method as it may be called more than once.
  public Composite getUI() {
       //System.out.println("getUI");
    return comp;
  }

  public String getDisplayName(Locale locale) {
       //System.out.println("getDisplayName");
    return "ECL Results";
  }

  public InputStream getPerspectiveIcon() {
    ClassLoader loader = getClass().getClassLoader();
    return loader.getResourceAsStream("org/pentaho/di/plugins/perspectives/eclresults/res/blueprint.png");
  }

  /**
   * This perspective is not Document based, therefore there is no EngineMeta to save/open.
   * @return
   */
  public EngineMetaInterface getActiveMeta() {
       System.out.println("getActiveMeta");
    return null;
  }
  
  private void openFile(String fileName, Table table){
      String outStr;
      try{
    	  
    	  CSVReader reader = new CSVReader(new FileReader(fileName),',','"');
          String [] strLineArr;
          
          // Open the file that is the first 
          // command line parameter
          FileInputStream fstream = new FileInputStream(fileName);
          // Get the object of DataInputStream
          DataInputStream in = new DataInputStream(fstream);
          BufferedReader br = new BufferedReader(new InputStreamReader(in));
          String strLine;

          table.clearAll();
          table.removeAll();
          table.redraw();
          
          
          int length = 0;
          boolean first = true;
          //Read File Line By Line
          while ((strLineArr = reader.readNext()) != null) {
          //while ((strLine = br.readLine()) != null)   {
          // Print the content on the console
              //outStr = strLine;
              TableItem item = null;
              
              if(first){
                  length = strLineArr.length;
              }else{
                   item = new TableItem (table, SWT.NONE);
              }
              
              int thisLen = strLineArr.length;
              if(thisLen<=length){
                  //String[] line = new String[length];
                  for(int i =0; i<thisLen; i++){
                      //line[i] = st.nextToken();
                      //item.setText (i, st.nextToken());
                      if(first){
                          TableColumn column = new TableColumn (table, SWT.NONE);
                          column.setText(strLineArr[i]);
                      }else{
                         
                          //System.out.println("-- "+i+" -- " + strLineArr[i]);
                          item.setText(i,strLineArr[i]);
                      }
                  }
              }
              first = false;
              
          }
          
          //System.out.println("Finisehd file");
         
          for (int i=0; i<length; i++) {
		table.getColumn (i).pack ();
	  }
          //System.out.println("finished packing");
          //Close the input stream
          in.close();
    }catch (Exception e){//Catch exception if any
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
     }
  
      
      
  }
  
  private String buildFileDialog() {
    
        //file field
        FileDialog fd = new FileDialog(parentShell, SWT.SAVE);

        fd.setText("Open");
        fd.setFilterPath("C:/");
        String[] filterExt = { "*.xml","*.csv", "*.*" };
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
        if(!(fd.getFileName()).equalsIgnoreCase("")){
            return fd.getFilterPath() + System.getProperty("file.separator") + fd.getFileName();
        }else{
            return "";
        }
        
    }
  
  
  public void openUrl(String url){
		String os = System.getProperty("os.name");
		Runtime runtime=Runtime.getRuntime();
		try{
			// Block for Windows Platform
			if (os.startsWith("Windows")){
				String cmd = "rundll32 url.dll,FileProtocolHandler "+ url;
				Process p = runtime.exec(cmd);
			
			//Block for Mac OS
			}else if(os.startsWith("Mac OS")){
				Class fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] {String.class});
				openURL.invoke(null, new Object[] {url});
			
			//Block for UNIX Platform
			}else {
				String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++)
					if (runtime.exec(new String[] {"which", browsers[count]}).waitFor() == 0)
						browser = browsers[count];
				
				if (browser == null)
					throw new Exception("Could not find web browser");
				else
					runtime.exec(new String[] {browser, url});
			}
		}catch(Exception x){
			System.err.println("Exception occurd while invoking Browser!");
			x.printStackTrace();
		}
	}
 
  
  
  
  
}
