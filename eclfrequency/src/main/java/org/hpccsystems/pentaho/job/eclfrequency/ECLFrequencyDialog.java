/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclfrequency;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
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
import org.hpccsystems.recordlayout.CreateTable;
import org.hpccsystems.recordlayout.RecordBO;
import org.hpccsystems.recordlayout.RecordLabels;
import org.hpccsystems.recordlayout.RecordList;
import org.hpccsystems.ecljobentrybase.*;
/**
 *
 * @author KeshavS
 */
public class ECLFrequencyDialog extends ECLJobEntryDialog{
	
	private String normlist = "";
	private String desc = "";
    private ECLFrequency jobEntry;
    private Text jobEntryName;
    private Text TableName;
    private Combo datasetName;
    private Combo sort;
    private CreateTable ct;
    RecordList recordlist = new RecordList();
    ArrayList<String> Fieldfilter = new ArrayList<String>();

   
    private Button wOK, wCancel, add, ASC, DESC;
    private boolean backupChanged;
    
    
	private SelectionAdapter lsDef;

	
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
        ct = new CreateTable(shell);
        ct.setColumnNames(new String[]{"Column Name","Default Value", "Column Type", "Sort Option"});
        
        TabFolder tab = new TabFolder(shell, SWT.FILL | SWT.RESIZE | SWT.MIN | SWT.MAX);
        FormData datatab = new FormData();
        
        datatab.height = 525;
        datatab.width = 650;
        tab.setLayoutData(datatab);
        
        Composite compForGrp = new Composite(tab, SWT.NONE);
        //compForGrp.setLayout(new FillLayout(SWT.VERTICAL));
        compForGrp.setBackground(new Color(tab.getDisplay(),255,255,255));
        compForGrp.setLayout(new FormLayout());
        TabItem item1 = new TabItem(tab, SWT.NULL);
        
        item1.setText ("General");
        props.setLook(shell);
        JobDialog.setShellImage(shell, jobEntry);

        ModifyListener lsMod = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                jobEntry.setChanged();
            }
        };
        backupChanged = jobEntry.hasChanged();
        
        FormLayout layout = new FormLayout();
		layout.marginWidth = Const.FORM_MARGIN;
		layout.marginHeight = Const.FORM_MARGIN;
		
		int middle = props.getMiddlePct();
        int margin = Const.MARGIN;
        
		shell.setLayout(layout);
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
        generalGroupFormat.width = 400;
        generalGroupFormat.height = 105;
        generalGroupFormat.left = new FormAttachment(middle, 0);
        generalGroup.setLayoutData(generalGroupFormat);
		
		jobEntryName = buildText("Job Name :      ", null, lsMod, middle, margin*2, generalGroup);
		
		//All other contols
        //Dataset Declaration
        Group datasetGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(datasetGroup);
        datasetGroup.setText("Dataset & Frequency Details");
        datasetGroup.setLayout(groupLayout);
        FormData datasetGroupFormat = new FormData();
        datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
        datasetGroupFormat.width = 400;
        datasetGroupFormat.height = 300;
        datasetGroupFormat.left = new FormAttachment(middle, 0);
        datasetGroup.setLayoutData(datasetGroupFormat);
		
		
		datasetName = buildCombo("Dataset :       ", jobEntryName, lsMod, middle, margin*2, datasetGroup, datasets);
		
		TableName = buildText("Outname :  ", datasetName, lsMod, middle, 25, datasetGroup);
		
        sort = buildCombo("Sort :   ", TableName, lsMod, middle, 25, datasetGroup, new String[]{"NO", "COLUMN", "VALUE"});
		
        ASC = new Button(datasetGroup, SWT.RADIO);
        ASC.setText("ASC");
        ASC.setEnabled(false);
        DESC = new Button(datasetGroup, SWT.RADIO);
        DESC.setText("DESC");
        DESC.setEnabled(false);
        FormData dat = new FormData();
        dat.top = new FormAttachment(sort, 25);
        dat.left = new FormAttachment(0, 145);
        //dat.right = new FormAttachment(100, 0);
        ASC.setLayoutData(dat);
        ASC.setBackground(new Color(tab.getDisplay(),255,255,255));
        
        dat = new FormData();
        dat.top = new FormAttachment(sort,25);
        dat.left = new FormAttachment(ASC, 15);
        //dat.right = new FormAttachment(100, 0);
        DESC.setLayoutData(dat);
        DESC.setBackground(new Color(tab.getDisplay(),255,255,255));
        
		add = buildButton("Add Columns", ASC, lsMod, middle, 25, datasetGroup);         
		
		sort.addModifyListener(new ModifyListener(){
        	
            public void modifyText(ModifyEvent e){
            	if(!sort.getText().equals("NO") && !sort.getText().equals("")){
            		ASC.setEnabled(true);
            		DESC.setEnabled(true);
            		Listener DESCL = new Listener(){
						@Override
						public void handleEvent(Event arg0) {
							if(DESC.getSelection())
								desc = "YES";
							else
								desc = "NO";
						}
            			
            		};
            		DESC.addListener(SWT.Selection, DESCL);
            	}
            	if(sort.getText().equals("NO")){
            		ASC.setEnabled(false);
            		DESC.setEnabled(false);
            	}
            	
            }
        });
        
        Listener addlisten = new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				// Need to preserve checked status

				final Shell shellFilter = new Shell(display);
				FormLayout layoutFilter = new FormLayout();
				layoutFilter.marginWidth = 25;
				layoutFilter.marginHeight = 25;
				shellFilter.setLayout(layoutFilter);
				shellFilter.setText("Filter Columns");
				
				Label filter = new Label(shellFilter, SWT.NONE);
				filter.setText("Filter: ");
				final Text NameFilter = new Text(shellFilter, SWT.SINGLE | SWT.BORDER);
				
				final ArrayList<String[]> field = new ArrayList<String[]>();
				final Tree tab = new Tree(shellFilter, SWT.CHECK | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
				tab.setHeaderVisible(true);
				tab.setLinesVisible(true);
				
			    final TreeColumn column1 = new TreeColumn(tab, SWT.LEFT);
			    column1.setText("Fields");
			    column1.setWidth(200);
			    column1.setImage(RecordLabels.getImage("unchecked"));
			    column1.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(Event event) {
				        boolean checkBoxFlag = false;
				        for (int i = 0; i < tab.getItemCount(); i++) {
				            if (tab.getItems()[i].getChecked()) {
				                checkBoxFlag = true;
				                
				            }
				        }
				        if (checkBoxFlag) {
				            for (int m = 0; m < tab.getItemCount(); m++) {
				                tab.getItems()[m].setChecked(false);
				                column1.setImage(RecordLabels.getImage("unchecked"));				                
				                tab.deselectAll();
				            }
				        } else {
				            for (int m = 0; m < tab.getItemCount(); m++) {
				                tab.getItems()[m].setChecked(true);
				                column1.setImage(RecordLabels.getImage("checked"));
				                tab.selectAll();
				            }
				        } 		
				        for(int m = 0; m<tab.getItemCount(); m++){
				        	if(tab.getItem(m).getChecked()){
				        		String st = tab.getItem(m).getText();
				        		int idx = 0; 
				 	   	      	 for(Iterator<String[]> it2 = field.iterator(); it2.hasNext(); ){
				 	   	     	 	 String[] s = it2.next();
				 	   	     		 if(s[0].equalsIgnoreCase(st)){
				 	   	     				idx = field.indexOf(s);
				 	   	     				break;
				 	   	     		 }
				 	   	     	 }
				 	   	     	 field.remove(idx);
				 	   	     	 field.add(idx,new String[]{st,"true"});
				 	   	     	 // to find index of the selected item in the original field array list
				        	}
				        }
		                tab.redraw();
				    } 
				});
			    
			    Button okFilter = new Button(shellFilter, SWT.PUSH);
				okFilter.setText("     OK     ");
				Button CancelFilter = new Button(shellFilter, SWT.PUSH);
				CancelFilter.setText("   Cancel   ");
			    				
				AutoPopulate ap = new AutoPopulate();
                try{
            		
                    String[] items = ap.fieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
                    
                    for(int i = 0; i < items.length; i++){
                		TreeItem item = new TreeItem(tab, SWT.NONE);
                		item.setText(items[i].toLowerCase());
                		field.add(new String[]{items[i].toLowerCase(),"false"});
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
		        
		        dat = new FormData(200,200);
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
		            		for(Iterator<String[]> it1 = field.iterator(); it1.hasNext(); ){
		            			String[] s = it1.next();
		            			if(s[0].startsWith(NameFilter.getText())){
		            				TreeItem I = new TreeItem(tab, SWT.NONE);
		            				I.setText(0,s[0]);
		            				if(s[1].equalsIgnoreCase("true")) 
		            					I.setChecked(true);
		            			}
		            		}
		            		tab.setRedraw(true);
		            		
		            }
		        });
		        
		       
		        
		        tab.addListener(SWT.Selection, new Listener() {
		    	     public void handleEvent(Event event) {
		    	    	 String st = ((TreeItem)event.item).getText();
		    	    	 boolean f = ((TreeItem)event.item).getChecked();
		 	   	      	 int idx = 0; 
		 	   	      	 for(Iterator<String[]> it2 = field.iterator(); it2.hasNext(); ){
		 	   	     	 	 String[] s = it2.next();
		 	   	     		 if(s[0].equalsIgnoreCase(st)){
		 	   	     				idx = field.indexOf(s);
		 	   	     				break;
		 	   	     		 }
		 	   	     	 }
		 	   	     	 field.remove(idx);
		 	   	     	 if(f)
		 	   	     		 field.add(idx,new String[]{st,"true"});
		 	   	     	 else
		 	   	     		 field.add(idx,new String[]{st,"false"});
		    	   	}
		       });
		        
		        Listener okfilter = new Listener(){

					
					@Override
					public void handleEvent(Event arg0) {
						
						ArrayList<String[]> Str = new ArrayList<String[]>();
						
						for(Iterator<RecordBO> iterator = recordlist.getRecords().iterator(); iterator.hasNext();){
							String[] s1 = {iterator.next().getColumnName(),"STRING",""};
							Str.add(s1);
		            	}
						
		            	for(int i = 0; i<tab.getItemCount(); i++){		            		
	            			if(tab.getItem(i).getChecked()){
	            				boolean flag = true;
	            				recordlist = ct.getRecordList();
		    	 				if(recordlist.getRecords() != null && recordlist.getRecords().size() > 0) {
		    	 					for(Iterator<RecordBO> iterator = recordlist.getRecords().iterator(); iterator.hasNext();){
		    	 						if(tab.getItem(i).getText().equals(iterator.next().getColumnName()))
		    	 							{flag = false; break;}
		    	 					}
		    	 				}
		    	 				if(flag){
		    	 					String[] S = {tab.getItem(i).getText(),"STRING",""};
		    	 					Str.add(S);
		    	 				}
	            			}	            				            		
		            	}
		            		            	
		            	ct.setRecordList(jobEntry.ArrayListToRecordList(Str));
		            	ct.redrawTable(true);
		            	recordlist = ct.getRecordList();
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
        
        item1.setControl(compForGrp);
        
        if(jobEntry.getRecordList() != null){
            recordlist = jobEntry.getRecordList();
            ct.setRecordList(jobEntry.getRecordList());
            
            if(recordlist.getRecords() != null && recordlist.getRecords().size() > 0) {
                    System.out.println("Size: "+recordlist.getRecords().size());
                    for (Iterator<RecordBO> iterator = recordlist.getRecords().iterator(); iterator.hasNext();) {
                            RecordBO obj = (RecordBO) iterator.next();
                    }
            }
        }
        
        TabItem item2 = ct.buildDefTab("Fields", tab);
        ct.redrawTable(true);
        
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");

        BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, margin, tab);
        // Add listeners
        Listener cancelListener = new Listener() {

            public void handleEvent(Event e) {
                cancel();
            }
        };
        Listener okListener = new Listener() {

            public void handleEvent(Event e) {
            	//Fieldfilter = new ArrayList<String>();
            	normlist = new String();
				recordlist = ct.getRecordList();
				if(recordlist.getRecords() != null && recordlist.getRecords().size() > 0) {
					for(Iterator<RecordBO> iterator = recordlist.getRecords().iterator(); iterator.hasNext();){
						String s = iterator.next().getColumnName();
						normlist += s+",";
						//Fieldfilter.add(s);
					}
						
				}
				if(DESC.getSelection())
					desc = "YES";
				else
					desc = "NO";
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
        
        if (jobEntry.getsortlist() != null) {
        	sort.setText(jobEntry.getsortlist());
        }
        
        if (jobEntry.getdesc() != null) {
        	desc = jobEntry.getdesc();
        }
        
        if (jobEntry.gettablename() != null) {
        	TableName.setText(jobEntry.gettablename());
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
    	if(this.TableName.getText().equals("")){
    		isValid = false;
    		errors += "\"Table Name\" is a required field!\r\n";
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
        jobEntry.setRecordList(this.recordlist);
        jobEntry.setsortlist(this.sort.getText());
        jobEntry.setdesc(this.desc);
        jobEntry.settablename(this.TableName.getText());

        dispose();
    }

    private void cancel() {
        jobEntry.setChanged(backupChanged);
        jobEntry = null;
        dispose();
    }
    
    
}
