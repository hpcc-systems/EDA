/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclfiltersimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryDialogInterface;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.job.entry.JobEntryDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import org.hpccsystems.recordlayout.CreateTable;
import org.hpccsystems.eclguifeatures.ErrorNotices;
import org.hpccsystems.recordlayout.RecordBO;
import org.hpccsystems.recordlayout.RecordList;
import org.hpccsystems.eclguifeatures.AutoPopulate;
import org.hpccsystems.mapper.*;
import org.hpccsystems.ecljobentrybase.*;
/**
 *
 * @author ShetyeD
 */
public class ECLFilterSimpleDialog extends ECLJobEntryDialog{//extends JobEntryDialog implements JobEntryDialogInterface {

    private ECLFilterSimple jobEntry;
    
    private Text jobEntryName;
    
    private Text recordsetName;
  //  private inputs;
    private Combo inRecordName;
   
    private Text filterFormat;
    //private Text parameterName;
    
    private MainMapperForFilter tblMapper = null;
    private RecordList recordList = new RecordList();
    private MapperRecordList mapperRecList = new MapperRecordList();
    
    private Button wOK, wCancel;
    private boolean backupChanged;
    private SelectionAdapter lsDef;
    List people = new ArrayList();
    
    public Button chkBox;
    public static Text outputName;
    public static Label label;
    private String persist;
    private Composite composite;
    private String jobName;

    public ECLFilterSimpleDialog(Shell parent, JobEntryInterface jobEntryInt, Repository rep, JobMeta jobMeta) {
        super(parent, jobEntryInt, rep, jobMeta);
        jobEntry = (ECLFilterSimple) jobEntryInt;
        if (this.jobEntry.getName() == null) {
            this.jobEntry.setName("Filter");
        }
    }

    public JobEntryInterface open() {
        Shell parentShell = getParent();
        Display display = parentShell.getDisplay();

        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);

        
        
        props.setLook(shell);
        JobDialog.setShellImage(shell, jobEntry);
        
        
        String datasets[] = null;
        AutoPopulate ap = new AutoPopulate();
        try{
            //Object[] jec = this.jobMeta.getJobCopies().toArray();
            
            datasets = ap.parseDatasetsRecordsets(this.jobMeta.getJobCopies());
            jobName = ap.getGlobalVariable(this.jobMeta.getJobCopies(), "jobName");
        }catch (Exception e){
            System.out.println("Error Parsing existing Datasets");
            System.out.println(e.toString());
            datasets = new String[]{""};
        }

        ModifyListener lsMod = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                jobEntry.setChanged();
            }
        };

        backupChanged = jobEntry.hasChanged();

        FormLayout formLayout = new FormLayout();
        //formLayout.marginWidth = Const.FORM_MARGIN; //5
        //formLayout.marginHeight = Const.FORM_MARGIN; //5
        
        shell.setLayout(formLayout);    
        shell.setSize(740,650); //800 X 600 (width X Height)

        int middle = props.getMiddlePct(); //35. This value is defined in org.pentaho.di.core.Const.
        int margin = Const.MARGIN; //4. This value is defined in org.pentaho.di.core.Const.

        shell.setLayout(formLayout);
        shell.setText("Define an ECL Filter");
        
        //Create a Tab folder and add an event which gets the updated recordlist and populates the Variable Name drop down. 
        final TabFolder tabFolder = new TabFolder (shell, SWT.FILL | SWT.RESIZE | SWT.MIN | SWT.MAX);
        FormData data = new FormData();
        
        data.height = 525;
        data.width = 650;
        tabFolder.setLayoutData(data);
        tabFolder.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
        		if(tabFolder.getSelectionIndex() == 1){
        			AutoPopulate ap = new AutoPopulate();
    				try{
    					RecordList fields = ap.rawFieldsByDataset(inRecordName.getText(), jobMeta.getJobCopies());

	        			if(fields != null && fields.getRecords().size() > 0) {
	        				String[] cmbValues = new String[fields.getRecords().size()];
	        				int count = 0;
	        				for (Iterator<RecordBO> iterator = fields.getRecords().iterator(); iterator.hasNext();) {

	        					RecordBO obj = (RecordBO) iterator.next();
	        					cmbValues[count] = obj.getColumnName();
	        					count++;
	        				}
	                      
	        				tblMapper.getCmbVariableName().removeAll();
	        				tblMapper.getCmbVariableName().setItems(cmbValues);
	        			}
    				}catch (Exception e){
    					System.out.println(e);
    				}
        		}
            }
        });

        //Start of code for Tabs

        FormData tabFolderData = new FormData();
        tabFolderData.height = 500;
        tabFolderData.width = 700;
        tabFolderData.top = new FormAttachment(0, 0);
        tabFolderData.left = new FormAttachment(0, 0);
        tabFolderData.right = new FormAttachment(100, 0);
        tabFolderData.bottom = new FormAttachment(90, 0);
        tabFolder.setLayoutData(tabFolderData);
        
        //Tab Item 1 for "General" Tab
        //CTabItem item1 = new CTabItem(tabFolder, SWT.NONE);
        TabItem item1 = new TabItem(tabFolder, SWT.NULL);
        ScrolledComposite sc = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
        Composite compForGrp = new Composite(sc, SWT.NONE);
        compForGrp.setLayout(new FormLayout());
        
        compForGrp.setBackground(new Color(sc.getDisplay(),255,255,255));
        
        sc.setContent(compForGrp);

        // Set the minimum size
        sc.setMinSize(650, 450);

        // Expand both horizontally and vertically
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        
        item1.setText ("General");
        item1.setControl(sc);
        
        
        
        FormLayout groupLayout = new FormLayout();
        groupLayout.marginWidth = 10;
        groupLayout.marginHeight = 10;

        // Stepname line
        Group generalGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(generalGroup);
        generalGroup.setText("General Details");
        generalGroup.setLayout(groupLayout);
        
        FormData generalGroupFormat = new FormData();
        generalGroupFormat.width = 400;
        generalGroupFormat.height = 110;
        generalGroupFormat.top = new FormAttachment(10, margin);
        generalGroupFormat.left = new FormAttachment(30, 0);
        /*generalGroupFormat.left = new FormAttachment(0, 5);
        generalGroupFormat.right = new FormAttachment(100, -5);*/
        generalGroup.setLayoutData(generalGroupFormat);
        
        jobEntryName = buildText("Job Entry Name", null, lsMod, middle, margin, generalGroup);

        //All other contols
        //Filter Declaration
        Group distributeGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(distributeGroup);
        distributeGroup.setText("Filter Details");
        distributeGroup.setLayout(groupLayout);
        
        FormData datasetGroupFormat = new FormData();
        /*datasetGroupFormat.top = new FormAttachment(generalGroup, 5);
        datasetGroupFormat.bottom = new FormAttachment(generalGroup, 200);
        datasetGroupFormat.left = new FormAttachment(generalGroup, 0, SWT.LEFT);
        datasetGroupFormat.right = new FormAttachment(generalGroup, 0, SWT.RIGHT);*/
        datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
        datasetGroupFormat.width = 400;
        datasetGroupFormat.height = 110;
        datasetGroupFormat.left = new FormAttachment(30, 0);
        distributeGroup.setLayoutData(datasetGroupFormat);
        
       

        recordsetName = buildText("Resulting Recordset", null, lsMod, middle, margin, distributeGroup);
        
        inRecordName = buildCombo("In Record Name", recordsetName, lsMod, middle, margin, distributeGroup,datasets);
        //listner
        inRecordName.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				AutoPopulate ap = new AutoPopulate();
				try{
					RecordList fields = ap.rawFieldsByDataset(inRecordName.getText(), jobMeta.getJobCopies());
					//tblOutput.setParentLayout(fields);
				}catch (Exception e){
					System.out.println(e);
				}
			}
        	
        });
        //tblOutput
         
        Group perGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(perGroup);
        perGroup.setText("Persist");
        perGroup.setLayout(groupLayout);
        FormData perGroupFormat = new FormData();
        perGroupFormat.top = new FormAttachment(distributeGroup, margin);
        perGroupFormat.width = 400;
        perGroupFormat.height = 80;
        perGroupFormat.left = new FormAttachment(30, 0);
        //perGroupFormat.right = new FormAttachment(100, 0);
        perGroup.setLayoutData(perGroupFormat);
        
        composite = new Composite(perGroup, SWT.NONE);
        composite.setLayout(new FormLayout());
        composite.setBackground(new Color(null, 255, 255, 255));

        final Composite composite_1 = new Composite(composite, SWT.NONE);
        composite_1.setLayout(new GridLayout(2, false));
        final FormData fd_composite_1 = new FormData();
        fd_composite_1.top = new FormAttachment(0);
        fd_composite_1.left = new FormAttachment(0, 10);
        fd_composite_1.bottom = new FormAttachment(0, 34);
        fd_composite_1.right = new FormAttachment(0, 390);
        composite_1.setLayoutData(fd_composite_1);
        composite_1.setBackground(new Color(null, 255, 255, 255));
        
        label = new Label(composite_1, SWT.NONE);
        label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label.setText("Logical Name:");
        label.setBackground(new Color(null, 255, 255, 255));
        
       	outputName = new Text(composite_1, SWT.BORDER);
        outputName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        outputName.setEnabled(false);
        if(jobEntry.getPersistOutputChecked()!= null && jobEntry.getPersistOutputChecked().equals("true")){
        	outputName.setEnabled(true);
        }
        
        final Composite composite_2 = new Composite(composite, SWT.NONE);
        composite_2.setLayout(new GridLayout(1, false));
        final FormData fd_composite_2 = new FormData();
        fd_composite_2.top = new FormAttachment(0, 36);
        fd_composite_2.bottom = new FormAttachment(100, 0);
        fd_composite_2.right = new FormAttachment(0, 390);
        fd_composite_2.left = new FormAttachment(0, 10);
        composite_2.setLayoutData(fd_composite_2);
        composite_2.setBackground(new Color(null, 255, 255, 255));

        chkBox = new Button(composite_2, SWT.CHECK);
        chkBox.setText("Persist Ouput");
        chkBox.setBackground(new Color(null, 255, 255, 255));
        
        chkBox.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	Button button = (Button) e.widget;
            	if(button.getSelection()){
            		persist = "true";
            		outputName.setEnabled(true);
            	}
            	else{
            		persist = "false";
            		outputName.setText("");
            		outputName.setEnabled(false);
            	}
            }
        });
        //End 
        
		//Tab Item 3 for "Transform Format" Tab
        //CTabItem item3 = new CTabItem(tabFolder, SWT.NONE);
		TabItem item3 = new TabItem(tabFolder, SWT.NULL);
		
		ScrolledComposite sc3 = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		final Composite compForGrp3 = new Composite(sc3, SWT.NONE);
		sc3.setContent(compForGrp3);

		// Set the minimum size
		sc3.setMinSize(650, 450);

		// Expand both horizontally and vertically
		sc3.setExpandHorizontal(true);
		sc3.setExpandVertical(true);
        
		item3.setText ("Filter");
        item3.setControl(sc3);
		
        GridLayout mapperCompLayout = new GridLayout();
        mapperCompLayout.numColumns = 1;
        GridData mapperCompData = new GridData();
		mapperCompData.grabExcessHorizontalSpace = true;
		compForGrp3.setLayout(mapperCompLayout);
		compForGrp3.setLayoutData(mapperCompData);   
		
		Map<String, String[]> mapDataSets = null;
		try {
			mapDataSets = ap.parseDefExpressionBuilder(this.jobMeta.getJobCopies());
		} catch(Exception e) {
			e.printStackTrace();
		}
		   
		//Create a Mapper
		String[] cmbValues = {""};
		tblMapper = new MainMapperForFilter(compForGrp3, mapDataSets, cmbValues, "filter",jobEntry.getPeople());
		if (jobEntry.getFilterStatement() != null) {
			tblMapper.setFilterStatement(jobEntry.getFilterStatement());//populate current
			tblMapper.setOldFilterStatement(jobEntry.getFilterStatement());//populate old if save isn't hit
			System.out.println("opening filter statement : " + tblMapper.getFilterStatement());
        }
		
		//tblMapper.setLayoutStyle("filter");
		//Add the existing Mapper RecordList
        if(jobEntry.getMapperRecList() != null){  
        	mapperRecList = jobEntry.getMapperRecList();
            tblMapper.setMapperRecList(jobEntry.getMapperRecList());
        }
		
        tblMapper.reDrawTable();
        
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

      //Define buttons since they are used for component alignment 
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");
        BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, margin, tabFolder);

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
        
        this.inRecordName.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                   System.out.print("inRecordName Listner");
		        try{
		        	populateDatasets();
		        }catch (Exception excep){
		        	System.out.println("Failed to load datasets");
		        }
            };
        });



        //if (jobEntry.getJobName() != null) {
        //    jobEntryName.setText(jobEntry.getJobName());
        //}
        if (jobEntry.getName() != null) {
            jobEntryName.setText(jobEntry.getName());
        }

       
        if (jobEntry.getInRecordName() != null) {
            inRecordName.setText(jobEntry.getInRecordName());
            try{
            	populateDatasets();
            }catch (Exception e){
            	System.out.println("Failed to load datasets");
            }
			
        }
         if (jobEntry.getRecordsetName() != null) {
            recordsetName.setText(jobEntry.getRecordsetName());
        }
       
         if(jobEntry.getPeople() != null){
         	people = jobEntry.getPeople();
         	MainMapperForFilter.tv.setInput(people);
         }
         
         if (jobEntry.getPersistOutputChecked() != null && chkBox != null) {
         	chkBox.setSelection(jobEntry.getPersistOutputChecked().equals("true")?true:false);
         }
         
         if(chkBox != null && chkBox.getSelection()){
         	for (Control control : composite_1.getChildren()) {
         		if(!control.isDisposed()){
 					if (jobEntry.getOutputName() != null && outputName != null) {
 			        	outputName.setText(jobEntry.getOutputName());
 					}
 					if (jobEntry.getLabel() != null && label != null) {
 			    		label.setText(jobEntry.getLabel());
 					}
         		}
         	}
 		}
         if(jobEntry.getJobName() != null){
         	jobName = jobEntry.getJobName();
         } 
         
        //shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        return jobEntry;

    }
    
    private void populateDatasets() throws Exception{
    	AutoPopulate ap = new AutoPopulate();
    	Map<String, String[]> mapDataSets = null;
		try {
			mapDataSets = ap.parseDefExpressionBuilder(this.jobMeta.getJobCopies(), inRecordName.getText());
		} catch(Exception e) {
			e.printStackTrace();
		}
		//(tblMapper.getTreeInputDataSet()).clearAll(false);
		(tblMapper.getTreeInputDataSet()).removeAll();
		Utils.fillTree(tblMapper.getTreeInputDataSet(), mapDataSets, false);
		//System.out.println("Updated Input Recordsets");
        tblMapper.reDrawTable();
    }

    private boolean validate(){
    	boolean isValid = true;
    	String errors = "";
    	
    	//only need to require a entry name
    	if(this.jobEntryName.getText().equals("")){
    		//one is required.
    		isValid = false;
    		errors += "You must provide a \"Job Entry Name\"!\r\n";
    	}
    	
    	if(this.recordsetName.getText().equals("")){
    		//one is required.
    		isValid = false;
    		errors += "You must provide a \"Resulting Recordset\"!\r\n";
    	}
    	
    	if(this.inRecordName.getText().equals("")){
    		//one is required.
    		isValid = false;
    		errors += "You must provide a \"In Record Name\"!\r\n";
    	}
    
    	
    	//requre outputformat to atleast not be ""
    	
    	
    	//TODO: update this doesn't seem to work.
    	//require transform format to atleast not be ""
    	if(tblMapper.getMapperRecList().equals("")){
    		isValid = false;
    		errors += "You must provide a \"Transform Format\"!\r\n";
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
        jobEntry.setRecordsetName(recordsetName.getText()); 
        jobEntry.setInRecordName(inRecordName.getText()); 
        jobEntry.setFilterStatement(tblMapper.getFilterStatement());
        System.out.println("setting filter statement : " + tblMapper.getFilterStatement());
        tblMapper.setOldexpression(tblMapper.getFilterStatement());//update filter statement that its changed and oked
        jobEntry.setPeople(tblMapper.getPeople());
        if(chkBox.getSelection() && outputName != null){
        	jobEntry.setOutputName(outputName.getText());
        }
        
        if(chkBox.getSelection() && label != null){
        	jobEntry.setLabel(label.getText());
        }
        
        if(chkBox != null){
        	jobEntry.setPersistOutputChecked(chkBox.getSelection()?"true":"false");
        }
        if(jobName.trim().equals("")){
        	jobName = "Spoon-job";
        }
        jobEntry.setJobName(jobName);
        dispose();
    }

    private void cancel() {
        jobEntry.setChanged(backupChanged);
        jobEntry = null;
        dispose();
    }

}
