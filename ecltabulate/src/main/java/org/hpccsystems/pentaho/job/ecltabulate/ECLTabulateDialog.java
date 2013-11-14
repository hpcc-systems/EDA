/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecltabulate;

import java.util.ArrayList;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.hpccsystems.eclguifeatures.AutoPopulate;
import org.hpccsystems.eclguifeatures.ErrorNotices;
import org.hpccsystems.ecljobentrybase.ECLJobEntryDialog;

import org.hpccsystems.recordlayout.RecordList;
import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
/**
 *
 * @author KeshavS
 */
public class ECLTabulateDialog extends ECLJobEntryDialog{
	
	java.util.List fields;
	java.util.List rows;
	java.util.List columns;
	java.util.List layers;
	
	private ArrayList<String> Settings;
    private ECLTabulate jobEntry;
    private Text jobEntryName;
    private Combo datasetName;
   
    private Button wOK, wCancel, wSettings;
    private boolean backupChanged;
    
    
	private SelectionAdapter lsDef;

	
    public ECLTabulateDialog(Shell parent, JobEntryInterface jobEntryInt,
			Repository rep, JobMeta jobMeta) {
		super(parent, jobEntryInt, rep, jobMeta);
		jobEntry = (ECLTabulate) jobEntryInt;
        if (this.jobEntry.getName() == null) {
            this.jobEntry.setName("Tabulate");
        }
	}

    

	// Building GUI
	public JobEntryInterface open() {
        Shell parentShell = getParent();
        final Display display = parentShell.getDisplay();
        
        String datasets[] = null;
        
        final AutoPopulate ap = new AutoPopulate();
        try{
        	
            datasets = ap.parseDatasets(this.jobMeta.getJobCopies());

        }catch (Exception e){
            System.out.println("Error Parsing existing Datasets");
            System.out.println(e.toString());
            datasets = new String[]{""};
        }


        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
        rows = new ArrayList();
        columns = new ArrayList();
        layers = new ArrayList();
        Settings = new ArrayList<String>();
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
        shell.setText("Tabulate");

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
        generalGroupFormat.right = new FormAttachment(100, 0);
        generalGroup.setLayoutData(generalGroupFormat);
		
		jobEntryName = buildText("Job Name :", null, lsMod, middle-15, margin, generalGroup);
		
		//All other contols
        //Dataset Declaration
        Group datasetGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(datasetGroup);
        datasetGroup.setText("Dataset Details");
        datasetGroup.setLayout(groupLayout);
        FormData datasetGroupFormat = new FormData();
        datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
        datasetGroupFormat.width = 350;
        datasetGroupFormat.height = 450;
        datasetGroupFormat.left = new FormAttachment(0, 0);
        datasetGroupFormat.right = new FormAttachment(100, 0);
        datasetGroup.setLayoutData(datasetGroupFormat);
		
		
        datasetName = buildCombo("Dataset :", jobEntryName, lsMod, middle-20, margin, datasetGroup, datasets);

        final TableViewer tv = new TableViewer(datasetGroup,  SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);

	    tv.setContentProvider(new PlayerContentProvider());
	    tv.setLabelProvider(new PlayerLabelProvider());
	    final Table table = tv.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    final TableColumn tc0 = new TableColumn(table, SWT.LEFT);
	    tc0.setText("Columns");
	    tc0.setWidth(100);
	    
	    Button InRow = new Button(datasetGroup, SWT.PUSH);
	    InRow.setText("  >>  ");
	    InRow.setBackground(new Color(null,255,255,255));
	    Button OutRow = new Button(datasetGroup, SWT.PUSH);
	    OutRow.setText("  <<  ");
	    OutRow.setBackground(new Color(null,255,255,255));
	    Button InColumn = new Button(datasetGroup, SWT.PUSH);
	    InColumn.setText("  >>  ");
	    InColumn.setBackground(new Color(null,255,255,255));
	    Button OutColumn = new Button(datasetGroup, SWT.PUSH);
	    OutColumn.setText("  <<  ");
	    OutColumn.setBackground(new Color(null,255,255,255));
	    Button InLayer = new Button(datasetGroup, SWT.PUSH);
	    InLayer.setText("  >>  ");
	    InLayer.setBackground(new Color(null,255,255,255));
	    Button OutLayer = new Button(datasetGroup, SWT.PUSH);
	    OutLayer.setText("  <<  ");
	    OutLayer.setBackground(new Color(null,255,255,255));
	    
	    final TableViewer Row = new TableViewer(datasetGroup, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	    Row.setContentProvider(new PlayerContentProvider());
	    Row.setLabelProvider(new PlayerLabelProvider());
	    final Table RowTable = Row.getTable();
	    RowTable.setHeaderVisible(true);
	    final TableColumn tcR = new TableColumn(RowTable, SWT.LEFT);
	    tcR.setText("Row(s)");
	    tcR.setWidth(100);
	    
	    final TableViewer Columns = new TableViewer(datasetGroup, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	    Columns.setContentProvider(new PlayerContentProvider());
	    Columns.setLabelProvider(new PlayerLabelProvider());
	    final Table ColumnTable = Columns.getTable();
	    ColumnTable.setHeaderVisible(true);
	    final TableColumn tcC = new TableColumn(ColumnTable, SWT.LEFT);
	    tcC.setText("Column(s)");
	    tcC.setWidth(100);
	    
	    final TableViewer Layer = new TableViewer(datasetGroup, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	    Layer.setContentProvider(new PlayerContentProvider());
	    Layer.setLabelProvider(new PlayerLabelProvider());
	    final Table LayerTable = Layer.getTable();
	    LayerTable.setHeaderVisible(true);
	    final TableColumn tcL = new TableColumn(LayerTable, SWT.LEFT);
	    tcL.setText("Layer(s)");
	    tcL.setWidth(100);
	    
	    FormData data = new FormData(100,300);
	    data.top = new FormAttachment(datasetName, 15);
		data.left = new FormAttachment(0,0);		
		table.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(datasetName,45);
		data.left = new FormAttachment(table,40);
		InRow.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(InRow,2);
		data.left = new FormAttachment(table,40);
		OutRow.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(OutRow,50);
		data.left = new FormAttachment(table,40);
		InColumn.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(InColumn,2);
		data.left = new FormAttachment(table,40);
		OutColumn.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(OutColumn,50);
		data.left = new FormAttachment(table,40);
		InLayer.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(InLayer,2);
		data.left = new FormAttachment(table,40);
		OutLayer.setLayoutData(data);
	    
		data = new FormData(100,50);
		data.top = new FormAttachment(datasetName,30);
		data.left = new FormAttachment(InRow,40);
		RowTable.setLayoutData(data);
		data = new FormData(100,50);
		data.top = new FormAttachment(RowTable,35);
		data.left = new FormAttachment(InColumn,40);
		ColumnTable.setLayoutData(data);
		data = new FormData(100,50);
		data.top = new FormAttachment(ColumnTable,35);
		data.left = new FormAttachment(InLayer,40);
		LayerTable.setLayoutData(data);
		
		Button button = new Button(datasetGroup, SWT.PUSH);
	    button.setText("Add Columns");
	    data = new FormData();
		data.top = new FormAttachment(table, 15);
		data.left = new FormAttachment(0,0);
		button.setLayoutData(data);
		
	    datasetName.addModifyListener(new ModifyListener(){
        	
            public void modifyText(ModifyEvent e){
            	fields = new ArrayList();
            	tv.refresh();
            	tv.setInput(fields);  
            	rows = new ArrayList();
            	Row.refresh();
            	Row.setInput(rows);
            	columns = new ArrayList();
            	Columns.refresh();
            	Columns.setInput(columns);
            	layers = new ArrayList();
            	Layer.refresh();
            	Layer.setInput(layers);

            }
	    });
	    
	    button.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  	String[] items = null;
					 
					if(datasetName.getText() != null || !datasetName.getText().equals("")){
						try {
							items = ap.fieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(fields.isEmpty()){
						for(int i = 0; i<items.length; i++){
							Player obj = new Player();
							obj.setFirstName(items[i]);
							fields.add(obj);
						}
					}
					tv.refresh();
					tv.setInput(fields);
		      }
	    });
	    
	    InRow.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				int cnt = 0;
	    		for(int i = 0; i<table.getItemCount(); i++){
	    			if(table.getItem(i).getChecked()){
	    				Player P = new Player();
	    				P.setFirstName(table.getItem(i).getText());
	    				rows.add(P);
	    				fields.remove(Math.abs(cnt - i));
						cnt++;
					}
	    		}
	    		tv.refresh();
	    		tv.setInput(fields);
	    		Row.refresh();
	    		Row.setInput(rows);	    		
			}
	    });
	    
	    OutRow.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				int cnt = 0;
				for(int i = 0; i<RowTable.getItemCount(); i++){
					if(RowTable.getItem(i).getChecked()){
	    				Player P = new Player();
	    				P.setFirstName(RowTable.getItem(i).getText());
	    				fields.add(P);
	    				rows.remove(Math.abs(cnt - i));
						cnt++;
					}
				}
	    		tv.refresh();
	    		tv.setInput(fields);
	    		Row.refresh();
	    		Row.setInput(rows);	    		
			}
	    });

	    InColumn.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				int cnt = 0;
	    		for(int i = 0; i<table.getItemCount(); i++){
	    			if(table.getItem(i).getChecked()){
	    				Player P = new Player();
	    				P.setFirstName(table.getItem(i).getText());
	    				columns.add(P);
	    				fields.remove(Math.abs(cnt - i));
						cnt++;
					}
	    		}
	    		tv.refresh();
	    		tv.setInput(fields);
	    		Columns.refresh();
	    		Columns.setInput(columns);	    		
			}
	    });

	    OutColumn.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				int cnt = 0;
				for(int i = 0; i<ColumnTable.getItemCount(); i++){
					if(ColumnTable.getItem(i).getChecked()){
	    				Player P = new Player();
	    				P.setFirstName(ColumnTable.getItem(i).getText());
	    				fields.add(P);
	    				columns.remove(Math.abs(cnt - i));
						cnt++;
					}
				}
	    		tv.refresh();
	    		tv.setInput(fields);
	    		Columns.refresh();
	    		Columns.setInput(columns);	    		
			}
	    });

	    InLayer.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				int cnt = 0;
	    		for(int i = 0; i<table.getItemCount(); i++){
	    			if(table.getItem(i).getChecked()){
	    				Player P = new Player();
	    				P.setFirstName(table.getItem(i).getText());
	    				layers.add(P);
	    				fields.remove(Math.abs(cnt - i));
						cnt++;
					}
	    		}
	    		tv.refresh();
	    		tv.setInput(fields);
	    		Layer.refresh();
	    		Layer.setInput(layers);	    		
			}
	    });
	    
	    OutLayer.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				int cnt = 0;
				for(int i = 0; i<LayerTable.getItemCount(); i++){
					if(LayerTable.getItem(i).getChecked()){
	    				Player P = new Player();
	    				P.setFirstName(LayerTable.getItem(i).getText());
	    				fields.add(P);
	    				layers.remove(Math.abs(cnt - i));
						cnt++;
					}
				}
	    		tv.refresh();
	    		tv.setInput(fields);
	    		Layer.refresh();
	    		Layer.setInput(layers);	    		
			}
	    });


	    wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");
        wSettings = new Button(shell, SWT.PUSH);
        wSettings.setText("Settings");

        BaseStepDialog.positionBottomButtons(shell, new Button[]{wSettings, wOK, wCancel}, margin, datasetGroup);
        // Add listeners
        
        wSettings.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				final Shell shellSettings = new Shell(display);
				shellSettings.setBackground(new Color(null,255,255,255));
				FormLayout layoutSettings = new FormLayout();
				layoutSettings.marginWidth = 25;
				layoutSettings.marginHeight = 25;
				shellSettings.setLayout(layoutSettings);
				shellSettings.setText("Tabulate Settings");
				
				FormLayout groupLayout1 = new FormLayout();
		        groupLayout1.marginWidth = 10;
		        groupLayout1.marginHeight = 10;
				
				Group generalGroup1 = new Group(shellSettings, SWT.SHADOW_NONE);
		        props.setLook(generalGroup1);
		        generalGroup1.setText("Percentages");
		        generalGroup1.setBackground(new Color(null, 255,255,255));
		        generalGroup1.setLayout(groupLayout1);
		        FormData generalGroupFormat = new FormData();
		        generalGroupFormat.top = new FormAttachment(0, 5);
		        generalGroupFormat.width = 100;
		        generalGroupFormat.height = 100;
		        generalGroupFormat.left = new FormAttachment(0, 0);
		        generalGroupFormat.right = new FormAttachment(100, 0);
		        generalGroup1.setLayoutData(generalGroupFormat);
				
		        final Button B1 = new Button(generalGroup1, SWT.CHECK);
				B1.setText("Row");
				B1.setBackground(new Color(null, 255,255,255));
				if(Settings.contains("rows"))
					B1.setSelection(true);
				final Button B2 = new Button(generalGroup1, SWT.CHECK);
				B2.setText("Column");
				if(Settings.contains("cols"))
					B2.setSelection(true);
				B2.setBackground(new Color(null, 255,255,255));
				final Button B3 = new Button(generalGroup1, SWT.CHECK);
				B3.setText("Total");
				if(Settings.contains("total"))
					B3.setSelection(true);
				B3.setBackground(new Color(null, 255,255,255));
				
				FormData dat = new FormData();
				dat.top = new FormAttachment(null,5);
				dat.left = new FormAttachment(0,0);
				B1.setLayoutData(dat);				
				dat = new FormData(); 
				dat.top = new FormAttachment(B1,10);
				dat.left = new FormAttachment(0,0);
				B2.setLayoutData(dat);
				dat = new FormData();
				dat.top = new FormAttachment(B2,10);
				dat.left = new FormAttachment(0,0);
				B3.setLayoutData(dat);
				
		        Group CountGroup = new Group(shellSettings, SWT.SHADOW_NONE);
		        props.setLook(CountGroup);
		        CountGroup.setText("Count");
		        CountGroup.setBackground(new Color(null, 255,255,255));
		        CountGroup.setLayout(groupLayout1);
		        FormData CountGroupFormat = new FormData();
		        CountGroupFormat.top = new FormAttachment(generalGroup1, 5);
		        CountGroupFormat.width = 100;
		        CountGroupFormat.height = 65;
		        CountGroupFormat.left = new FormAttachment(0, 0);
		        CountGroupFormat.right = new FormAttachment(100, 0);
		        CountGroup.setLayoutData(CountGroupFormat);
				
		        final Button B4 = new Button(CountGroup, SWT.CHECK);
				B4.setText("Count");
				B4.setBackground(new Color(null, 255,255,255));
				if(Settings.contains("count"))
					B4.setSelection(true);
				dat = new FormData();
				dat.top = new FormAttachment(B3,10);
				dat.left = new FormAttachment(0,0);
				B4.setLayoutData(dat);
				
				B1.addListener(SWT.Selection, new Listener(){
					@Override
					public void handleEvent(Event arg0) {
						if(B1.getSelection())
							Settings.add("rows");
						else{
							int idx = Settings.indexOf("rows");
							Settings.remove(idx);
						}
					}
					
				});
				B2.addListener(SWT.Selection, new Listener(){
					@Override
					public void handleEvent(Event arg0) {
						if(B2.getSelection())
							Settings.add("cols");
						else{
							int idx = Settings.indexOf("cols");
							Settings.remove(idx);
						}
					}
					
				});
				B3.addListener(SWT.Selection, new Listener(){
					@Override
					public void handleEvent(Event arg0) {
						if(B3.getSelection())
							Settings.add("total");
						else{
							int idx = Settings.indexOf("total");
							Settings.remove(idx);
						}
					}
					
				});
				B4.addListener(SWT.Selection, new Listener(){
					@Override
					public void handleEvent(Event arg0) {
						if(B4.getSelection())
							Settings.add("count");
						else{
							int idx = Settings.indexOf("count");
							Settings.remove(idx);
						}
					}
					
				});
				Button wOKS = new Button(shellSettings, SWT.PUSH);
		        wOKS.setText("OK");
		        				
		        BaseStepDialog.positionBottomButtons(shellSettings, new Button[]{wOKS}, 5, CountGroup);
		        
		        wOKS.addListener(SWT.Selection, new Listener(){

					@Override
					public void handleEvent(Event arg0) {
						shellSettings.dispose();
						
					}
		        	
		        });
				
				shellSettings.pack();
		        shellSettings.open();
				while (!shellSettings.isDisposed()) {
					if (!display.readAndDispatch())
						display.sleep();
				}

			}
        });
        
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
        
        if(jobEntry.getPeople() != null){
        	fields = jobEntry.getPeople();
        	tv.refresh();
        	tv.setInput(fields);
        }
        if(jobEntry.getRows() != null){
        	rows = jobEntry.getRows();
        	Row.refresh();
        	Row.setInput(rows);
        }
        if(jobEntry.getCols() != null){
        	columns = jobEntry.getCols();
        	Columns.refresh();
        	Columns.setInput(columns);
        }
        if(jobEntry.getLayers() != null){
        	layers = jobEntry.getLayers();
        	Layer.refresh();
        	Layer.setInput(layers);
        }
        if(jobEntry.getSettings() != null){
        	Settings = jobEntry.getSettings();
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
   		if(this.Settings == null){
   			isValid = false;
   			errors += "Need to choose Settings!\r\n";
   		}
   		if(this.rows == null || this.columns == null){
   			isValid = false;
   			errors += "Need to Select Row or Column!\r\n";
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
        jobEntry.setPeople(this.fields);
        jobEntry.setRows(this.rows);
        jobEntry.setCols(this.columns);
        jobEntry.setLayers(this.layers);
        jobEntry.setSettings(this.Settings);
        jobEntry.setDatasetName(this.datasetName.getText());
 
        dispose();
    }

    private void cancel() {
        jobEntry.setChanged(backupChanged);
        jobEntry = null;
        dispose();
    }
    
}

class Player {
	  private String firstName;

	  public String getFirstName() {
		  return firstName;
	  }

	  public void setFirstName(String firstName) {
		  this.firstName = firstName;
	  }



}





class PlayerLabelProvider implements ITableLabelProvider {


// Constructs a PlayerLabelProvider
	public PlayerLabelProvider() {
	}


	public Image getColumnImage(Object arg0, int arg1) {
  
		return null;
	}


	public String getColumnText(Object arg0, int arg1) {
	  Player values = (Player) arg0;
	//  String text = "";
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

