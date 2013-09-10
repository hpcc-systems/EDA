/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclfrequency;

import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

import org.hpccsystems.eclguifeatures.AutoPopulate;
import org.hpccsystems.eclguifeatures.ErrorNotices;
import org.hpccsystems.recordlayout.RecordBO;
import org.hpccsystems.recordlayout.RecordList;
import org.hpccsystems.ecljobentrybase.*;
/**
 *
 * @author KeshavS
 */
public class ECLFrequencyDialog extends ECLJobEntryDialog{
	
	private String normlist = "";
    private ECLFrequency jobEntry;
    private Text jobEntryName;
    private Combo datasetName;

    ArrayList<String> Field = new ArrayList<String>();
    ArrayList<String[]> details = new ArrayList<String[]>();
    String[] s = new String[20];
   
    private Button wOK, wCancel, wadd;
    private boolean backupChanged;
    
	private SelectionAdapter lsDef;

	// no idea what this constructor is for... need to access "jobEntry.java" file
    public ECLFrequencyDialog(Shell parent, JobEntryInterface jobEntryInt,
			Repository rep, JobMeta jobMeta) {
		super(parent, jobEntryInt, rep, jobMeta);
		jobEntry = (ECLFrequency) jobEntryInt;
        if (this.jobEntry.getName() == null) {
            this.jobEntry.setName("Frequency");
        }
	}

    

	// Building GUI
	public JobEntryInterface open() {
        Shell parentShell = getParent();
        Display display = parentShell.getDisplay();
        String datasets[] = null;
        AutoPopulate ap = new AutoPopulate();
        try{
            //Object[] jec = this.jobMeta.getJobCopies().toArray();
            
            datasets = ap.parseDatasetsRecordsets(this.jobMeta.getJobCopies());
        }catch (Exception e){
            System.out.println("Error Parsing existing Datasets");
            System.out.println(e.toString());
            datasets = new String[]{""};
        }

        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
        //ct = new CreateTable(shell);
        
        TabFolder tabFolder = new TabFolder (shell, SWT.FILL | SWT.RESIZE | SWT.MIN | SWT.MAX);
        FormData data = new FormData();
        
        data.height = 525;
        data.width = 650;
        tabFolder.setLayoutData(data);
        
        Composite compForGrp = new Composite(tabFolder, SWT.NONE);
        //compForGrp.setLayout(new FillLayout(SWT.VERTICAL));
        compForGrp.setBackground(new Color(tabFolder.getDisplay(),255,255,255));
        compForGrp.setLayout(new FormLayout());
        
        TabItem item1 = new TabItem(tabFolder, SWT.NULL);
        
        item1.setText("General");
        props.setLook(shell);
        JobDialog.setShellImage(shell, jobEntry);

        final ModifyListener lsMod = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                jobEntry.setChanged();
            }
        };

        backupChanged = jobEntry.hasChanged();
		
        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = 5;//Const.FORM_MARGIN;
        formLayout.marginHeight = 5;//Const.FORM_MARGIN;

        final int middle = props.getMiddlePct();
        final int margin = Const.MARGIN;

        shell.setLayout(formLayout);
        shell.setText("Compute Frequencies");

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
        generalGroupFormat.width = 600;
        generalGroupFormat.height = 65;
        //generalGroupFormat.left = new FormAttachment(middle, 0);
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
        datasetGroupFormat.width = 600;
        datasetGroupFormat.height = 65;
        //datasetGroupFormat.left = new FormAttachment(middle, 0);
        datasetGroup.setLayoutData(datasetGroupFormat);

        datasetName = buildCombo("Dataset Name", null, lsMod, middle, margin, datasetGroup,datasets);
        
        final Group fieldsGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(fieldsGroup);
        fieldsGroup.setText("Frequency of Fields");
        fieldsGroup.setLayout(groupLayout);
        FormData fieldsGroupFormat = new FormData();
        fieldsGroupFormat.top = new FormAttachment(datasetGroup, margin);
        fieldsGroupFormat.width = 600;
        fieldsGroupFormat.height = 300;
        //fieldsGroupFormat.left = new FormAttachment(middle, 0);
        fieldsGroup.setLayoutData(fieldsGroupFormat);
        
        
		final Table table = new Table(fieldsGroup, SWT.CHECK | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setItemCount(10);
        TableColumn[] column = new TableColumn[1];
        column[0] = new TableColumn(table, SWT.NONE);
        column[0].setWidth(200);
        column[0].setText("Fields");

        wadd = buildButton("Populate Columns", datasetName, lsMod, middle+10, margin, fieldsGroup);
        
        Listener waddlisten = new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				AutoPopulate ap = new AutoPopulate();
                try{
            		System.out.println("Load items for select");
                    String[] items = ap.fieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
                    System.out.println("++++++"+items.length+"+++++");
                    filltable(table, items);                    
                }catch (Exception ex){
                    System.out.println("failed to load record definitions");
                    System.out.println(ex.toString());
                    ex.printStackTrace();
                }
				
			}
        	
        };
        wadd.addListener(SWT.Selection, waddlisten);
            
       
       table.addListener(SWT.Selection, new Listener() {
    	     public void handleEvent(Event event) {
    	    	 int l = event.item.toString().length();
    	    	 if(((TableItem)event.item).getChecked()){
    	    		 Field.add(event.item.toString().substring(11,l-1)+",");
    	       	
    	    	 }
    	    	 else{
    	    		 if(!((TableItem)event.item).getChecked()){
   	       				Field.remove(event.item.toString().substring(11,l-1)+",");
    	    		 }
    	    	 }
    	   	}
       });

       item1.setControl(compForGrp);
            
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
        
        if (jobEntry.getDatasetName() != null) {
            datasetName.setText(jobEntry.getDatasetName());
        }
        
        if (jobEntry.getnormList() != null) {
        	normlist = jobEntry.getnormList();
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
   		if(this.normlist.equals("")){
   			isValid = false;
   			errors += "You need to Enter Some Field to compute Frequency";
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
		normlist = "";
		for(Iterator<String> it = Field.iterator(); it.hasNext();){
			String s = (String)it.next();
			normlist += s;
		}	
		
		
    	if(!validate()){
    		return;
    	}
        jobEntry.setName(jobEntryName.getText());
        jobEntry.setDatasetName(this.datasetName.getText());
        jobEntry.setnormList(this.normlist);

        dispose();
    }

    private void cancel() {
        jobEntry.setChanged(backupChanged);
        jobEntry = null;
        dispose();
    }
    
    public void filltable(Table table, String[] details){
    	table.setRedraw(false);
    	table.setItemCount(0);
    	
    	for(int i = 0; i < details.length; i++){
    		TableItem item = new TableItem(table, SWT.NONE);
    		item.setText(details[i]);
    	}
    	
    	table.setRedraw(true);
    }
}
