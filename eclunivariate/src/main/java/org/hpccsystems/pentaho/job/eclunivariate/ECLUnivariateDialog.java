/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclunivariate;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

import org.hpccsystems.eclguifeatures.AutoPopulate;
import org.hpccsystems.eclguifeatures.ErrorNotices;
import org.hpccsystems.ecljobentrybase.*;

import org.hpccsystems.recordlayout.RecordLabels;
import org.hpccsystems.recordlayout.RecordList;


/**
 *
 * @author KeshavS
 */
public class ECLUnivariateDialog extends ECLJobEntryDialog{//extends JobEntryDialog implements JobEntryDialogInterface {

	public static final String NAME = "Name";
	public static final String[] PROP = { NAME };
	
	private String[] flags = new String[]{"false","false","false","false","false","false"};
	java.util.List people;
	
    private ECLUnivariate jobEntry;
    private Text jobEntryName;
    private Combo datasetName;
    private String fieldList;
   
    private Button wOK, wCancel;
    private boolean backupChanged;
    
    
	private SelectionAdapter lsDef;

	
    public ECLUnivariateDialog(Shell parent, JobEntryInterface jobEntryInt,
			Repository rep, JobMeta jobMeta) {
		super(parent, jobEntryInt, rep, jobMeta);
		jobEntry = (ECLUnivariate) jobEntryInt;
        if (this.jobEntry.getName() == null) {
            this.jobEntry.setName("Univariate");
        }
	}

    

	// Building GUI
	public JobEntryInterface open() {
        Shell parentShell = getParent();
        Display display = parentShell.getDisplay();
        
        String datasets[] = null;
    
        final AutoPopulate ap = new AutoPopulate();
        try{
            datasets = ap.parseDatasetsRecordsets(this.jobMeta.getJobCopies());
 
        }catch (Exception e){
            System.out.println("Error Parsing existing Datasets");
            System.out.println(e.toString());
            datasets = new String[]{""};
        }


        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
        people = new ArrayList();
        
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
        shell.setText("Proc UniVariate");

        FormLayout groupLayout = new FormLayout();
        groupLayout.marginWidth = 10;
        groupLayout.marginHeight = 10;



        // Stepname line
        Group generalGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(generalGroup);
        generalGroup.setText("General Details");
        generalGroup.setLayout(groupLayout);
        FormData generalGroupFormat = new FormData();
        generalGroupFormat.top = new FormAttachment(0, margin);
        generalGroupFormat.width = 400;
        generalGroupFormat.height = 65;
        generalGroupFormat.left = new FormAttachment(0, 0);
        generalGroup.setLayoutData(generalGroupFormat);
		
		jobEntryName = buildText("Job Name :", null, lsMod, middle-15, margin, generalGroup);
		
        Group datasetGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(datasetGroup);
        datasetGroup.setText("Dataset Details");
        datasetGroup.setLayout(groupLayout);
        FormData datasetGroupFormat = new FormData();
        datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
        datasetGroupFormat.width = 400;
        datasetGroupFormat.height = 400;
        datasetGroupFormat.left = new FormAttachment(0, 0);
        datasetGroup.setLayoutData(datasetGroupFormat);
		
		
        datasetName = buildCombo("Dataset :", jobEntryName, lsMod, middle-20, margin, datasetGroup, datasets);

        final TableViewer tv = new TableViewer(datasetGroup,  SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);

	    tv.setContentProvider(new PlayerContentProvider());
	    tv.setLabelProvider(new PlayerLabelProvider());
	    
	    final Table table = tv.getTable();
        Button button = new Button(datasetGroup, SWT.PUSH);
	    button.setText("Add Columns");
	    Button del = new Button(datasetGroup, SWT.PUSH);
	    del.setText("Delete");
	    
	    final Button Mean = new Button(datasetGroup, SWT.CHECK);
	    Mean.setText("Mean");
	    Mean.setBackground(new Color(null,255,255,255));	
	    final Button Median = new Button(datasetGroup, SWT.CHECK);
	    Median.setText("Median");
	    Median.setBackground(new Color(null,255,255,255));
	    final Button Mode = new Button(datasetGroup, SWT.CHECK);
	    Mode.setText("Mode");
	    Mode.setBackground(new Color(null,255,255,255));
	    final Button StdDev = new Button(datasetGroup, SWT.CHECK);
	    StdDev.setText("Std Dev");
	    StdDev.setBackground(new Color(null,255,255,255));
	    final Button Max = new Button(datasetGroup, SWT.CHECK);
	    Max.setText("Max");
	    Max.setBackground(new Color(null,255,255,255));
	    final Button Min = new Button(datasetGroup, SWT.CHECK);
	    Min.setText("Min");
	    Min.setBackground(new Color(null,255,255,255));

	    FormData data = new FormData();
	    data.top = new FormAttachment(datasetName, 15);
		data.left = new FormAttachment(0,0);
		//data.right = new FormAttachment(100,0);
		Mean.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(datasetName, 15);
		data.left = new FormAttachment(Mean,15);
		Median.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(datasetName, 15);
		data.left = new FormAttachment(Median,15);
		Mode.setLayoutData(data);
		
		data = new FormData();
	    data.top = new FormAttachment(datasetName, 15);
		data.left = new FormAttachment(Mode,15);
		//data.right = new FormAttachment(100,0);
		StdDev.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(datasetName, 15);
		data.left = new FormAttachment(StdDev,15);
		Max.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(datasetName, 15);
		data.left = new FormAttachment(Max,15);
		Min.setLayoutData(data);

	    data = new FormData(370,200);
	    data.top = new FormAttachment(Mean, 15);
		data.left = new FormAttachment(0,0);
		data.right = new FormAttachment(100,0);
		table.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(table, 15);
		data.left = new FormAttachment(0,0);
		button.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(table, 15);
		data.left = new FormAttachment(button,10);
		del.setLayoutData(data);
		
	    
	    final TableColumn tc0 = new TableColumn(table, SWT.LEFT);
	    tc0.setText("Columns");
	    tc0.setWidth(370);
	    tc0.setImage(RecordLabels.getImage("unchecked"));
	    tc0.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
		        boolean checkBoxFlag = false;
		        for (int i = 0; i < table.getItemCount(); i++) {
		            if (table.getItems()[i].getChecked()) {
		                checkBoxFlag = true;
		                
		            }
		        }
		        if (checkBoxFlag) {
		            for (int m = 0; m < table.getItemCount(); m++) {
		                table.getItems()[m].setChecked(false);
		                tc0.setImage(RecordLabels.getImage("unchecked"));				                
		                table.deselectAll();
		            }
		        } else {
		            for (int m = 0; m < table.getItemCount(); m++) {
		                table.getItems()[m].setChecked(true);
		                tc0.setImage(RecordLabels.getImage("checked"));
		                table.selectAll();
		            }
		        } 	
		        tv.refresh();
		        table.redraw();
		    } 
		});
	    
	    if(jobEntry.getPeople() != null)
            people = jobEntry.getPeople();
	    tv.setInput(people);
	    if(people != null && people.size() > 0) {
            
            for (Iterator iterator = people.iterator(); iterator.hasNext();) {
                    Cols obj = (Cols) iterator.next();
            }
	    }
        tv.setInput(people);
        table.setRedraw(true);
	    
       
	    del.addSelectionListener(new SelectionAdapter(){
	    	public void widgetSelected(SelectionEvent event){
	    		int cnt = 0;
	    		for(int i = 0; i<table.getItemCount(); i++){
	    			if(table.getItem(i).getChecked()){
	    				
	    				people.remove(Math.abs(cnt - i));
						cnt++;
					}
	    		}
	    		if(tc0.getImage().equals(RecordLabels.getImage("checked")))
	    			tc0.setImage(RecordLabels.getImage("unchecked")); 
	    		tv.refresh();
	    		tv.setInput(people);
	    		
	    	}
	    });
	    
	    datasetName.addModifyListener(new ModifyListener(){
        	
            public void modifyText(ModifyEvent e){
            	people = new ArrayList();
            	tv.refresh();
            	tv.setInput(people);            	
            }
	    });

	    Mean.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				if(Mean.getSelection())
					flags[0] = "true";
				else
					flags[0] = "false";
				
			}
	    	
	    });

	    Median.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				if(Median.getSelection())
					flags[1] = "true";
				else
					flags[1] = "false";
				
			}
	    	
	    });

	    Mode.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				if(Mode.getSelection())
					flags[2] = "true";
				else
					flags[2] = "false";
				
			}
	    	
	    });

	    StdDev.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				if(StdDev.getSelection())
					flags[3] = "true";
				else
					flags[3] = "false";
				
			}
	    	
	    });

	    Max.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				if(Max.getSelection())
					flags[4] = "true";
				else
					flags[4] = "false";
				
			}
	    	
	    });

	    Min.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				if(Min.getSelection())
					flags[5] = "true";
				else
					flags[5] = "false";
				
			}
	    	
	    });

	    // Add a listener to change the tableviewer's input
	    button.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	    	  String[] items = null;
				RecordList rec = null; 
				if(datasetName.getText() != null || !datasetName.getText().equals("")){
					try {
						items = ap.fieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
						rec = ap.rawFieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(people.isEmpty()){
						for(int i = 0; i<items.length; i++){
							if(rec.getRecords().get(i).getColumnType().startsWith("integer") || rec.getRecords().get(i).getColumnType().startsWith("real") || rec.getRecords().get(i).getColumnType().startsWith("unsigned")){				
								Cols obj = new Cols();
								obj.setFirstName(items[i]);
								people.add(obj);
							}
						}
						tv.refresh();
						tv.setInput(people);
					}
				}
	      }
					
	    });
	    
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    table.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event event) {
            	if(event.detail == SWT.CHECK){	
            		tv.refresh();
                    table.redraw();
            	}
            }
        });
	    
	    tv.setColumnProperties(PROP);
	    
	    

	    wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");

        BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, margin, datasetGroup);
        // Add listeners
        Listener cancelListener = new Listener() {

            public void handleEvent(Event e) {
                cancel();
            }
        };
        Listener okListener = new Listener() {

            public void handleEvent(Event e) {
            	fieldList = new String();
            	for(int i = 0; i<table.getItemCount(); i++){
            		if(table.getItem(i).getChecked())
            			fieldList += table.getItem(i).getText()+","; 
            	}
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
        
        if(jobEntry.getCheckList().length()>0){
        	flags = jobEntry.getCheckList().split(",");
        	
    	    if(flags[0].equals("false"))
    	    	Mean.setSelection(false);
    	    else
    	    	Mean.setSelection(true);
    	    if(flags[1].equals("false"))
    	    	Median.setSelection(false);
    	    else
    	    	Median.setSelection(true);
    	    if(flags[2].equals("false"))
    	    	Mode.setSelection(false);
    	    else
    	    	Mode.setSelection(true);
    	    if(flags[3].equals("false"))
    	    	StdDev.setSelection(false);
    	    else
    	    	StdDev.setSelection(true);
    	    if(flags[4].equals("false"))
    	    	Max.setSelection(false);
    	    else
    	    	Max.setSelection(true);
    	    if(flags[5].equals("false"))
    	    	Min.setSelection(false);
    	    else
    	    	Min.setSelection(true);

        }
        
        if(jobEntry.getFieldList() != null){
        	fieldList = jobEntry.getFieldList();
        }
        
        if(jobEntry.getPeople() != null){
        	people = jobEntry.getPeople();
        	tv.setInput(people);
        	for(int i = 0; i<table.getItemCount(); i++){
        		String[] S = fieldList.split(",");
        		for(int j = 0; j<S.length; j++){
        			if(S[j].equalsIgnoreCase(table.getItem(i).getText()))
        				{table.getItem(i).setChecked(true);break;}
        		}
        	}
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
   		
    	if(this.fieldList.isEmpty()){
    		isValid = false;
    		errors += "Please Select at least one Field\r\n";
    	}
    	if(this.flags[0].equals("false")&&this.flags[1].equals("false")&&this.flags[2].equals("false")&&this.flags[3].equals("false")&&this.flags[4].equals("false")&&this.flags[5].equals("false")){
    		isValid = false;
    		errors += "Please Select at least one Statistic";
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
        jobEntry.setDatasetName(this.datasetName.getText());
        jobEntry.setPeople(people);
        jobEntry.setCheckList(flags[0]+","+flags[1]+","+flags[2]+","+flags[3]+","+flags[4]+","+flags[5]);
        jobEntry.setFieldList(this.fieldList);

        dispose();
    }

    private void cancel() {
        jobEntry.setChanged(backupChanged);
        jobEntry = null;
        dispose();
    }
    
    
    
}

class Cols {
	  private String firstName;

	  public String getFirstName() {
		  return firstName;
	  }

	  public void setFirstName(String firstName) {
		  this.firstName = firstName;
	  }


}





class PlayerLabelProvider implements ITableLabelProvider {

	
	
	public PlayerLabelProvider() {
	}


	public Image getColumnImage(Object arg0, int arg1) {
		
		
		return null;
	}


	public String getColumnText(Object arg0, int arg1) {
	  Cols values = (Cols) arg0;
	  switch(arg1){
	  case 0:
	  	  return values.getFirstName();//text = values[0];
	  }
	  return null;
	}
	
	public void addListener(ILabelProviderListener arg0) {
	  // Throw it away
	}
	
	public void dispose() {
	 
	}
	
	public boolean isLabelProperty(Object arg0, String arg1) {
	  return false;
	}
	
	public void removeListener(ILabelProviderListener arg0) {
	  // Do nothing
	}
}


class PlayerContentProvider implements IStructuredContentProvider {

	public Object[] getElements(Object arg0) {
	  
	  return ((List) arg0).toArray();
	}
	
	public void dispose() {
	  // We don't create any resources, so we don't dispose any
	}
	
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	  // Nothing to do
	}
}

