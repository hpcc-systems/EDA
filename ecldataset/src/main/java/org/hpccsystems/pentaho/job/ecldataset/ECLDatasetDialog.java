/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecldataset;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;

import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryDialogInterface;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.job.entry.JobEntryDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.hpccsystems.javaecl.HPCCServerInfo;
import org.hpccsystems.eclguifeatures.AutoPopulate;
import org.hpccsystems.eclguifeatures.ErrorNotices;
import org.hpccsystems.eclguifeatures.HPCCFileBrowser;
import org.hpccsystems.recordlayout.CreateTable;
import org.hpccsystems.recordlayout.RecordBO;
import org.hpccsystems.recordlayout.RecordList;
import org.hpccsystems.ecljobentrybase.*;
/**
 *
 * @author ChambersJ
 */
public class ECLDatasetDialog extends ECLJobEntryDialog{//extends JobEntryDialog implements JobEntryDialogInterface {

	private String currentLogicalFile = "";
	private Element[] eElement;
	private ArrayList<Integer> list;
	private String xml = "";
    private ECLDataset jobEntry;
    private Text jobEntryName;
    private Text recordName;
   // private Text recordDef;
    private Text recordSet;
    //private Combo fileName;
    private Text datasetName;
    private Button wOK, wCancel;
    private boolean backupChanged;
    private SelectionAdapter lsDef;
    private Text logicalFile;
    private Combo fileType;
    
    private Table table;
    private TableViewer tableViewer;

    // Create a RecordList and assign it to an instance variable
    private RecordList recordList = new RecordList();
    private CreateTable ct = null;
    
    
    private String serverHost = "";
    private int serverPort = 8010;
    private String user = "";
    private String pass = "";
    
    private Combo hasHeaderRow;
    //private Text csvSeparator;
    //private Text csvQuote;
    //private Text csvTerminator;
    
    private String manualEntryText = "";

    public ECLDatasetDialog(Shell parent, JobEntryInterface jobEntryInt, Repository rep, JobMeta jobMeta) {
        super(parent, jobEntryInt, rep, jobMeta);
        jobEntry = (ECLDataset) jobEntryInt;
        if (this.jobEntry.getName() == null) {
            this.jobEntry.setName("Dataset");
        }
    }

    public JobEntryInterface open() {
        Shell parentShell = getParent();
        final Display display = parentShell.getDisplay();

        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
        ct = new CreateTable(shell);
        ct.setIncludeCopyParent(false);
        TabFolder tabFolder = new TabFolder (shell, SWT.FILL | SWT.RESIZE | SWT.MIN | SWT.MAX);
        FormData data = new FormData();
        
        data.height = 525;
        data.width = 650;
        tabFolder.setLayoutData(data);
        
        final Composite compForGrp = new Composite(tabFolder, SWT.NONE);
        //compForGrp.setLayout(new FillLayout(SWT.VERTICAL));
        compForGrp.setBackground(new Color(tabFolder.getDisplay(),255,255,255));
        compForGrp.setLayout(new FormLayout());
        
        TabItem item1 = new TabItem(tabFolder, SWT.NULL);
        
        item1.setText ("General");
        props.setLook(shell);
        JobDialog.setShellImage(shell, jobEntry);

        ModifyListener lsMod = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                jobEntry.setChanged();
            }
        };

        backupChanged = jobEntry.hasChanged();

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;

        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        shell.setLayout(formLayout);
        shell.setText("Define an ECL Dataset");

        FormLayout groupLayout = new FormLayout();
        groupLayout.marginWidth = 10;
        groupLayout.marginHeight = 10;

        // Stepname line
        Group generalGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(generalGroup);
        generalGroup.setText("General Details");
        generalGroup.setLayout(groupLayout);
        FormData generalGroupFormat = new FormData();
        generalGroupFormat.top = new FormAttachment(0, margin);
        generalGroupFormat.width = 400;
        generalGroupFormat.height = 65;
        generalGroupFormat.left = new FormAttachment(middle, 0);
        generalGroup.setLayoutData(generalGroupFormat);
        
        jobEntryName = buildText("Job Entry Name", null, lsMod, middle, margin, generalGroup);

        //All other contols
        //Dataset Declaration
        Group datasetGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(datasetGroup);
        datasetGroup.setText("Dataset Details");
        datasetGroup.setLayout(groupLayout);
        FormData datasetGroupFormat = new FormData();
        datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
        datasetGroupFormat.width = 400;
        datasetGroupFormat.height = 220;
        datasetGroupFormat.left = new FormAttachment(middle, 0);
        datasetGroup.setLayoutData(datasetGroupFormat);

        
        AutoPopulate ap = new AutoPopulate();
        String[] fileList = new String[0];
        
        
        try{
            //Object[] jec = this.jobMeta.getJobCopies().toArray();
                
                serverHost = ap.getGlobalVariable(this.jobMeta.getJobCopies(),"server_ip");
                serverPort = Integer.parseInt(ap.getGlobalVariable(jobMeta.getJobCopies(),"server_port"));
                user = ap.getGlobalVariable(jobMeta.getJobCopies(),"user_name");
                pass = ap.getGlobalVariableEncrypted(jobMeta.getJobCopies(),"password");
                
               
            }catch (Exception e){
                System.out.println("Error Parsing existing Global Variables ");
                System.out.println(e.toString());
                
            }

        final HPCCServerInfo hsi = new HPCCServerInfo(serverHost,serverPort,user,pass);
        final HPCCFileBrowser fileBrowser = new HPCCFileBrowser(serverHost,serverPort,user,pass);
        //fileList = hsi.fetchFileList();
        try{
        	ArrayList<String> files = ap.getLogicalFileName(this.jobMeta.getJobCopies());
        	if(files != null){
        		fileList = ap.merge(fileList, files.toArray(new String[files.size()]));
        	}
        }catch(Exception ex){
        	System.out.println("Unable to fetch file names from sprays: " + ex.toString());
        }
        ArrayList<String> tempFiles = new ArrayList<String>();
        for (int i = 0; i<fileList.length; i++){
        	if(!tempFiles.contains(fileList[i])){
        		tempFiles.add(fileList[i]);
        	}
        }
        Collections.sort(tempFiles);
        fileList = tempFiles.toArray(new String[tempFiles.size()]);
       
        datasetName = buildText("Dataset Name", null, lsMod, middle, margin, datasetGroup);
        logicalFile = buildText("Logical File", datasetName, lsMod, middle, margin, datasetGroup);
        
        Button fileBrowse  = buildButton("Browse", logicalFile, lsMod, middle, margin, datasetGroup);
        //Button fileBrowse = new Button(datasetGroup, SWT.PUSH);
        //fileBrowse.moveBelow(logicalFile);
        //fileBrowse.setText("Browse");

        // Add listeners
        Listener logicalFileListener = new Listener() {

            public void handleEvent(Event e) {
            	String newFile = fileBrowser.run(shell,logicalFile.getText());
            	if(newFile != null && !newFile.equals("")){
	                logicalFile.setText("~" + newFile);
	               // recordSet.setEnabled(false);
					//manualEntryText = recordSet.getText();
					
	                String file = logicalFile.getText();
					if(!currentLogicalFile.equalsIgnoreCase(file)){
						currentLogicalFile = file;
						//value has changed
						//System.out.println("fetch field list: " + file);
						//ArrayList<String[]> details = hsi.fetchFileDetails(file);
						ArrayList<String[]> details = Details(file,hsi,"");
						if(hsi.isLogonFail){
							ErrorNotices en = new ErrorNotices();
							en.openSaveErrorDialog(getParent(), "Permission Denied!\r\nCan not fetch field definitions, please make sure you have permissions to that file\r\nYou should also verify your user/pass in Global Variables");
							//en.openDialog("Permission Denied", "You either do not have permissions to access this file or your credentials for the server are wrong.", "");
						}
						if(details != null && details.size()>0){
							ErrorNotices en = new ErrorNotices();
							if(en.openComfirmDialog(getParent(), "Do you want to replace your current Record Structure on the\r\nFields tab with the one stored on the server?")){
								ct.setRecordList(jobEntry.ArrayListToRecordList(details));
								ct.createExpand(display,eElement, list);
								ct.redrawTable(true);
							}
						}else{
							ErrorNotices en = new ErrorNotices();
							if(en.openComfirmDialog(getParent(), "Does the file have a header row, if so would you like to import the field list?")){

								details=jobEntry.parseFileHeader(serverHost,serverPort,user,pass,file);
								if(details.size()> 0){
									ct.setRecordList(jobEntry.ArrayListToRecordList(details));
									ct.redrawTable(true);
								}else{
									ErrorNotices en2 = new ErrorNotices();
									en2.openSaveErrorDialog(getParent(), "Sorry, unable to parse the field definitions from the file");
									
								}
								
							}
						}						
						
					}
            	}else{
            		//System.out.println("No text in logica File button");
					//recordSet.setEnabled(true);
					//recordSet.setText(manualEntryText);
            	}
            }
        };
        fileBrowse.addListener(SWT.Selection, logicalFileListener);
        
        ModifyListener logicalFileChange = new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent arg0) {
				Color grey = shell.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
				Color white = shell.getDisplay().getSystemColor(SWT.COLOR_WHITE);
				// TODO Auto-generated method stub
				System.out.println("Modified LogicalFile");
				if(logicalFile.getText().equals("")){
					//no logical file
					System.out.println("No text in logica File");
					recordSet.setEnabled(true);
					recordSet.setText(manualEntryText);
					recordSet.setBackground(white);
				}else{
					//no manual entry
					System.out.println("Text in logical file");
					recordSet.setEnabled(false);
					if(!recordSet.getText().equals("")){
						manualEntryText = recordSet.getText();
					}
					recordSet.setText("");
					recordSet.setBackground(grey);
					
				}
			}
        	
        };
        logicalFile.addModifyListener(logicalFileChange);
        
        //fileName = buildCombo("Logical File Name", logicalFile, lsMod, middle, margin, datasetGroup, fileList);
        fileType = buildCombo("File Type", fileBrowse, lsMod, middle, margin, datasetGroup,new String[]{"CSV", "THOR"});
        ModifyListener fileTypeChange = new ModifyListener(){
        	@Override
			public void modifyText(ModifyEvent arg0) {
        		Color grey = shell.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
				Color white = shell.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        		if(fileType.getText().equalsIgnoreCase("CSV")){
        			//csvSeparator.setEnabled(true);
                	//csvQuote.setEnabled(true);
                	//csvTerminator.setEnabled(true);
                	hasHeaderRow.setEnabled(true);
                	//csvSeparator.setBackground(white);
                	//csvQuote.setBackground(white);
                	//csvTerminator.setBackground(white);
                	hasHeaderRow.setBackground(white);
        		}else{
        			//csvSeparator.setEnabled(false);
                	//csvQuote.setEnabled(false);
                	//csvTerminator.setEnabled(false);
                	hasHeaderRow.setEnabled(false);
                	//csvSeparator.setBackground(grey);
                	//csvQuote.setBackground(grey);
                	//csvTerminator.setBackground(grey);
                	hasHeaderRow.setBackground(grey);
        		}
        	}
        };
        fileType.addModifyListener(fileTypeChange);
        
        recordSet = buildMultiText("Manual Record Entry", fileType, lsMod, middle, margin, datasetGroup);

        //Record Declaration
        Group recordGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(recordGroup);
        recordGroup.setText("Define Dataset Record");
        recordGroup.setLayout(groupLayout);
        FormData recordGroupFormat = new FormData();
        recordGroupFormat.top = new FormAttachment(datasetGroup, margin);
        recordGroupFormat.height = 45;
        recordGroupFormat.width = 400;
        recordGroupFormat.left = new FormAttachment(middle, 0);
        recordGroup.setLayoutData(recordGroupFormat);

        recordName = buildText("Record Name", null, lsMod, middle, margin, recordGroup);
        
        
        
      //Record Declaration
        Group layoutGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(layoutGroup);
        layoutGroup.setText("Define CSV Parameters");
        layoutGroup.setLayout(groupLayout);
        FormData layoutGroupFormat = new FormData();
        layoutGroupFormat.top = new FormAttachment(recordGroup, margin);
        layoutGroupFormat.height = 45;//125
        layoutGroupFormat.width = 400;
        layoutGroupFormat.left = new FormAttachment(middle, 0);
        layoutGroup.setLayoutData(layoutGroupFormat);

        hasHeaderRow = buildCombo("First row is Header", null, lsMod, middle, margin, layoutGroup,new String[]{"No", "Yes"});
        //csvSeparator = buildText("CSV Separator", hasHeaderRow, lsMod, middle, margin, layoutGroup);
        //csvQuote = buildText("CSV Quote", csvSeparator, lsMod, middle, margin, layoutGroup);
        //csvTerminator = buildText("CSV Terminator", csvQuote, lsMod, middle, margin, layoutGroup);
        
        item1.setControl(compForGrp);
        
         if(jobEntry.getRecordList() != null){
            recordList = jobEntry.getRecordList();
            ct.setRecordList(jobEntry.getRecordList());
            
            if(recordList.getRecords() != null && recordList.getRecords().size() > 0) {
                    System.out.println("Size: "+recordList.getRecords().size());
                    for (Iterator<RecordBO> iterator = recordList.getRecords().iterator(); iterator.hasNext();) {
                            RecordBO obj = (RecordBO) iterator.next();
                    }
            }
        }
        TabItem item2 = ct.buildDefTab("Fields", tabFolder);
        ct.redrawTable(true);
        
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");

        BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, margin, tabFolder);

        // Add listeners
        Listener cancelListener = new Listener() {

            public void handleEvent(Event e) {
                cancel();
            }
        };
        Listener okListener = new Listener() {

            public void handleEvent(Event e) {
                ok();
            }
        };

        wCancel.addListener(SWT.Selection, cancelListener);
        wOK.addListener(SWT.Selection, okListener);

        lsDef = new SelectionAdapter() {

            public void widgetDefaultSelected(SelectionEvent e) {
                ok();
            }
        };


        // Detect X or ALT-F4 or something that kills this window...

        shell.addShellListener(new ShellAdapter() {

            public void shellClosed(ShellEvent e) {
                cancel();
            }
        });

        if (jobEntry.getName() != null) {
            jobEntryName.setText(jobEntry.getName());
        }
        if (jobEntry.getLogicalFileName() != null) {
        	logicalFile.setText(jobEntry.getLogicalFileName());
        }

        if (jobEntry.getDatasetName() != null) {
            datasetName.setText(jobEntry.getDatasetName());
        }
        
        if (jobEntry.getRecordName() != null) {
            recordName.setText(jobEntry.getRecordName());
        }
        
        if (jobEntry.getXml() != null) {
            xml = jobEntry.getXml();
            if(!xml.equals("")){
            	ArrayList<String[]> details = Details(logicalFile.getText(),hsi,xml);
            	if(details != null && details.size()>0)
            		ct.createExpand(display,eElement, list);    		
            }            
        }
        
        if (jobEntry.getRecordSet() != null) {
            recordSet.setText(jobEntry.getRecordSet());
        }
        
        if (jobEntry.getFileType() != null) {
            fileType.setText(jobEntry.getFileType());
        }
        

        if (jobEntry.getCsvSeparator() != null) {
        //	csvSeparator.setText(jobEntry.getCsvSeparator());
        }
        if (jobEntry.getCsvTerminator() != null) {
        //	csvTerminator.setText(jobEntry.getCsvTerminator());
        }
        if (jobEntry.getCsvQuote() != null) {
        //	csvQuote.setText(jobEntry.getCsvQuote());
        }
        if(jobEntry.getHasHeaderRow() != null){
        	hasHeaderRow.setText(jobEntry.getHasHeaderRow());
        }

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        return jobEntry;

    }

    private boolean validate(){
    	boolean isValid = true;
    	String errors = "";
    	
    	
    	
    	if(this.jobEntryName.getText().equals("")){
    		isValid = false;
    		errors += "\"Job Entry Name\" is a required field!\r\n";
    	}
    	
    	if(this.datasetName.getText().equals("")){
    		isValid = false;
    		errors += "\"Dataset Name\" is a required field!\r\n";
    	}
    	String fields = jobEntry.resultListToString(ct.getRecordList());
    	if(!this.recordSet.getText().equals("")){
    		//manual entry
    		
    		//we can't have fields or logical file name or File Type
    		if(!this.logicalFile.getText().equals("")){
    			isValid = false;
        		errors += "\"Logical File Name\" is not allowed for manual entry!\r\n";
    		}
    		if(!this.fileType.getText().equals("")){
    			isValid = false;
        		errors += "\"File Type\" is not allowed for manual entry!\r\n";
    		}
    		if(!fields.equals("")){
    			isValid = false;
        		errors += "Values on the \"Fields Tab\" is not allowed for manual entry!\r\n";
    		}
    	}else{
    		//using fields
    		if(this.logicalFile.getText().equals("")){
    			isValid = false;
        		errors += "\"Logical File Name\" is a required field!\r\n";
    		}
    		if(this.fileType.getText().equals("")){
    			isValid = false;
        		errors += "\"File Type\" is a required field!\r\n";
    		}
    		if(this.recordName.getText().equals("")){
    			isValid = false;
        		errors += "\"Record Name\" is a required field!\r\n";
    		}
    		if(fields.equals("")){
    			isValid = false;
        		errors += "Values on the \"Fields Tab\" is a required!\r\n";
    		}else{
    			//we need to validate the fields
    			String e = jobEntry.fieldsValid(ct.getRecordList());
    			if(!e.equals("")){
    				errors += e;
    				isValid = false;
    			}
    		}
    	}
    	
    	if(!isValid){
    		ErrorNotices en = new ErrorNotices();
    		errors += "\r\n";
    		errors += "If you continue to save with errors you may encounter compile errors if you try to execute the job.\r\n\r\n";
    		isValid = en.openValidateDialog(getParent(),errors);
    	}
    	return isValid;
    	
    }
    
    private void ok() {
    	if(!validate()){
    		return;
    	}
    	
        jobEntry.setName(jobEntryName.getText());
        jobEntry.setLogicalFileName(logicalFile.getText());
        jobEntry.setDatasetName(datasetName.getText());
        jobEntry.setRecordName(recordName.getText());
       // jobEntry.setRecordDef(recordDef.getText());
        jobEntry.setRecordSet(recordSet.getText());
        jobEntry.setRecordList(ct.getRecordList());
        jobEntry.setFileType(fileType.getText());
       // jobEntry.setCsvSeparator(csvSeparator.getText());
        //jobEntry.setCsvTerminator(csvTerminator.getText());
        //jobEntry.setCsvQuote(csvQuote.getText());
        jobEntry.setHasHeaderRow(hasHeaderRow.getText());
        jobEntry.setXml(xml);
        dispose();
    }

    private void cancel() {
        jobEntry.setChanged(backupChanged);
        jobEntry = null;
        dispose();
    }
    
    private ArrayList<String[]> Details(String file, HPCCServerInfo hsi, String Xml){
		if(Xml.equals(""))
			xml = hsi.fetchXML(file);
		else
			xml = Xml;
		ArrayList<String[]> details = new ArrayList<String[]>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        Document dom = null;
		try {
			 db = dbf.newDocumentBuilder();
			 InputSource is = new InputSource(new StringReader(xml));
		     dom = db.parse(is);
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
       
        if(dom == null){
        	 return null;
        }
        
        Element docElement = dom.getDocumentElement();				       
        NodeList nList = docElement.getElementsByTagName("Field");
        
        eElement = new Element[nList.getLength()];
		list = new ArrayList<Integer>();
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				eElement[temp] = (Element) nNode;
				
				System.out.println("Label : " + eElement[temp].getElementsByTagName("Field").getLength());
				if(eElement[temp].getElementsByTagName("Field").getLength()>0){
					if(eElement[temp].getAttribute("isDataset").equals("1")){
						String[] S = new String[5];
						S[0] = eElement[temp].getAttribute("label").toString();
						S[1] = "DATASET";
						S[2] = "";
						S[3] = eElement[temp].getAttribute("size").toString();
						S[4] = eElement[temp].getAttribute("size").toString();
						details.add(S);
					}
					list.add(temp);
					temp += eElement[temp].getElementsByTagName("Field").getLength();
					
				}
				else{
					System.out.println(eElement[temp].getAttribute("ecltype"));
					list.add(temp);
					String[] S = new String[5];
					S[0] = eElement[temp].getAttribute("label").toString();
					S[1] = eElement[temp].getAttribute("ecltype").toString();
					S[2] = "";
					S[3] = eElement[temp].getAttribute("size").toString();
					S[4] = eElement[temp].getAttribute("size").toString();
					details.add(S);
				}
			}
		
		}
    	return details;
    }

}
