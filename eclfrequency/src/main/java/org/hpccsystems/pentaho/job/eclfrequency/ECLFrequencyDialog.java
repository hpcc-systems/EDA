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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
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
    private Text list;
    RecordList recordlist = new RecordList();

    ArrayList<String> Field = new ArrayList<String>();
    ArrayList<String[]> details = new ArrayList<String[]>();
    ArrayList<String> Fieldfilter = new ArrayList<String>();
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
        final Display display = parentShell.getDisplay();
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
        FormLayout layout = new FormLayout();
		layout.marginWidth = 25;
		layout.marginHeight = 25;
		shell.setLayout(layout);
		shell.setText("Compute Frequencies");
        
		ModifyListener lsMod = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                jobEntry.setChanged();
            }
        };
		
        Label name = new Label(shell, SWT.NONE);
		name.setText("Job Name :");
		jobEntryName = new Text(shell, SWT.SINGLE | SWT.BORDER);
		
		Label data = new Label(shell, SWT.NONE);
		data.setText("Dataset :");
		datasetName = new Combo(shell, SWT.NONE);
		datasetName.setItems(datasets);
		
		Label norm = new Label(shell, SWT.NONE);
		norm.setText("normlist :");
		list = new Text(shell, SWT.SINGLE | SWT.BORDER);
		
		
		Button add = new Button(shell, SWT.PUSH);
		add.setText("Add Columns");
	     
		wOK = new Button(shell, SWT.PUSH);
		wOK.setText("     OK     ");
		wCancel = new Button(shell, SWT.PUSH);
		wCancel.setText("   Cancel   ");
		
		final Table table = new Table(shell, SWT.NONE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setItemCount(10);
		TableColumn[] column = new TableColumn[1];
		column[0] = new TableColumn(table, SWT.NONE);
        column[0].setWidth(200);
        column[0].setText("Fields");
        /*if(!normlist.equals("")){
        	table.setRedraw(false);
	       	String[] str = normlist.split(",");
	       	for(int j = 0; j<str.length; j++){
	       		TableItem itr = new TableItem(table, SWT.NONE);
	       		itr.setText(0, str[j]);
	       	}
	        table.setRedraw(true);
        }*/
        
        
        FormData dat = new FormData();
        dat.top = new FormAttachment(jobEntryName, 0, SWT.CENTER);
        name.setLayoutData(dat);
        dat = new FormData();
        dat.left = new FormAttachment(name, 75, SWT.LEFT);
        dat.right = new FormAttachment(100, 0);
        jobEntryName.setLayoutData(dat);
        
        dat = new FormData();
        dat.top = new FormAttachment(datasetName, 0, SWT.CENTER);
        data.setLayoutData(dat);
        dat = new FormData();
        dat.top = new FormAttachment(jobEntryName, 15);
        dat.left = new FormAttachment(jobEntryName, 0, SWT.LEFT);
        dat.right = new FormAttachment(100, 0);
        datasetName.setLayoutData(dat);
        
        dat = new FormData();
        dat.top = new FormAttachment(list, 0, SWT.CENTER);
        norm.setLayoutData(dat);
        dat = new FormData();
        dat.top = new FormAttachment(datasetName, 15);
        dat.left = new FormAttachment(datasetName, 0, SWT.LEFT);
        dat.right = new FormAttachment(100, 0);
        list.setLayoutData(dat);
        
        dat = new FormData();
        dat.top = new FormAttachment(norm, 25);
        dat.left = new FormAttachment(norm, 0, SWT.LEFT);
        add.setLayoutData(dat);
        	
        dat = new FormData();
        dat.top = new FormAttachment(add,25);
        dat.left = new FormAttachment(add, 0, SWT.LEFT);
        table.setLayoutData(dat);

        dat = new FormData();
        dat.top = new FormAttachment(table,25);
        dat.left = new FormAttachment(0, 45);
        wOK.setLayoutData(dat);
        
        dat = new FormData();
        dat.top = new FormAttachment(table,25);
        dat.left = new FormAttachment(wOK, 15);
        
        wCancel.setLayoutData(dat);
        
        

        Listener addlisten = new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				final Shell shellFilter = new Shell(display);
				FormLayout layoutFilter = new FormLayout();
				layoutFilter.marginWidth = 25;
				layoutFilter.marginHeight = 25;
				shellFilter.setLayout(layoutFilter);
				shellFilter.setText("Filter Columns");
				
				Label filter = new Label(shellFilter, SWT.NONE);
				filter.setText("Filter: ");
				final Text NameFilter = new Text(shellFilter, SWT.SINGLE | SWT.BORDER);
				
				final ArrayList<String> field = new ArrayList<String>();
				final Table tab = new Table(shellFilter, SWT.CHECK | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
				tab.setHeaderVisible(true);
				tab.setLinesVisible(true);
				
			    TableColumn column1 = new TableColumn(tab, SWT.LEFT);
			    column1.setText("Fields");
			    column1.setWidth(200);
			    
			    Button okFilter = new Button(shellFilter, SWT.PUSH);
				okFilter.setText("     OK     ");
				Button CancelFilter = new Button(shellFilter, SWT.PUSH);
				CancelFilter.setText("   Cancel   ");
			    				
				AutoPopulate ap = new AutoPopulate();
                try{
            		
                    String[] items = ap.fieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
                    
                    for(int i = 0; i < items.length; i++){
                		TableItem item = new TableItem(tab, SWT.NONE);
                		item.setText(items[i].toLowerCase());
                		field.add(items[i].toLowerCase());
                	}
                    
                    
                }catch (Exception ex){
                    System.out.println("failed to load record definitions");
                    System.out.println(ex.toString());
                    ex.printStackTrace();
                }
                FormData dat = new FormData();
		        dat.top = new FormAttachment(NameFilter, 0, SWT.CENTER);
		        filter.setLayoutData(dat);
		        dat = new FormData();
		        dat.left = new FormAttachment(filter, 75, SWT.LEFT);
		        dat.right = new FormAttachment(100, 0);
		        NameFilter.setLayoutData(dat);
		        
		        dat = new FormData();
		        dat.top = new FormAttachment(filter, 25);
		        dat.left = new FormAttachment(filter, 0, SWT.LEFT);
		        tab.setLayoutData(dat);
		        
		        dat = new FormData();
		        dat.top = new FormAttachment(tab, 25);
		        dat.left = new FormAttachment(0, 45);
		        okFilter.setLayoutData(dat);
		        
		        dat = new FormData();
		        dat.top = new FormAttachment(tab, 25);
		        dat.left = new FormAttachment(okFilter, 15);
		        CancelFilter.setLayoutData(dat);
	       
		        NameFilter.addModifyListener(new ModifyListener(){
		        	
		            public void modifyText(ModifyEvent e){
		            		
		            		tab.setItemCount(0);		            		
		            		for(Iterator<String> it1 = field.iterator(); it1.hasNext(); ){
		            			String s = it1.next();
		            			if(s.startsWith(NameFilter.getText())){
		            				TableItem I = new TableItem(tab, SWT.NONE);
		            				I.setText(s);
		            			}
		            		}
		            		tab.setRedraw(true);
		            }
		        });
		        
		        
		        tab.addListener(SWT.Selection, new Listener() {
		    	     public void handleEvent(Event event) {
		    	    	 int l = event.item.toString().length();
		    	    	 if(((TableItem)event.item).getChecked()){
		    	    		 Fieldfilter.add(event.item.toString().substring(11,l-1));
		    	    		 ((TableItem)event.item).setChecked(true);
		    	    	 }
		    	    	 else{
		    	    		 if(!((TableItem)event.item).getChecked()){
		   	       				Fieldfilter.remove(event.item.toString().substring(11,l-1));
		    	    		 }
		    	    	 }
		    	   	}
		       });
		        Fieldfilter = new ArrayList<String>();
		        Listener okfilter = new Listener(){

					@Override
					public void handleEvent(Event arg0) {
						table.setRedraw(false);
						table.setItemCount(0);
						for(Iterator<String> i = Fieldfilter.iterator(); i.hasNext();){
							TableItem it = new TableItem(table, SWT.NONE);
							String s = i.next();
							it.setText(s);
						}
						table.setRedraw(true);
						normlist = new String();
			    	 	for(int i = 0; i<table.getItemCount(); i++){
			    	 		int l1 = table.getItem(i).toString().length();
			    	 		normlist += table.getItem(i).toString().substring(11, l1-1)+",";
			    	 	}
			    	 	list.setRedraw(false);
			    	 	list.setText(normlist);
			    	 	list.setRedraw(true);
						shellFilter.dispose();
						
					}
		        	
		        };
		        okFilter.addListener(SWT.Selection, okfilter);

		        Listener cancelfilter = new Listener(){

					@Override
					public void handleEvent(Event arg0) {
						shellFilter.dispose();
						
					}
		        	
		        };
		        CancelFilter.addListener(SWT.Selection, cancelfilter);

				shellFilter.pack();
		        shellFilter.open();
				while (!shellFilter.isDisposed()) {
					if (!display.readAndDispatch())
						display.sleep();
				}
				
			}
        	
        };
        add.addListener(SWT.Selection, addlisten);
         
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
        //tabFolder.pack();
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
    
    
}
