/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecltabulate;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.hpccsystems.eclguifeatures.AutoPopulate;
import org.hpccsystems.eclguifeatures.ErrorNotices;
import org.hpccsystems.ecljobentrybase.ECLJobEntryDialog;
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
	
	public static final String COLUMN = "Column"; 
	public static final String OP = "Op";
	public static final String PERCENT = "Percentage";
	
	public static final String[] PROP = { COLUMN, OP, PERCENT};
	
	private ArrayList<String> Settings;
	ArrayList<Player> fields;
	ArrayList<Player> rows;
	ArrayList<Player> columns;
	ArrayList<Player> layers;
	
	
    private ECLTabulate jobEntry;
    private Text jobEntryName;
    private Combo datasetName;
   
    private Button wOK, wCancel, wSettings;
    private boolean backupChanged;
    public Button chkBox;
    public static Text outputName;
    public static Label label;
    private String persist;
    private Composite composite;
    private String defJobName;
   
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
        	
            datasets = ap.parseDatasetsRecordsets(this.jobMeta.getJobCopies());
            defJobName = ap.getGlobalVariable(this.jobMeta.getJobCopies(), "jobName");

        }catch (Exception e){
            System.out.println("Error Parsing existing Datasets");
            System.out.println(e.toString());
            datasets = new String[]{""};
        }


        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
        rows = new ArrayList<Player>();
        columns = new ArrayList<Player>();
        layers = new ArrayList<Player>();
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
        groupLayout.marginWidth = 14;
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
		
		jobEntryName = buildText("Job Entry Name :", null, lsMod, middle-10, margin, generalGroup);
		
		//All other contols
        //Dataset Declaration
        Group datasetGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(datasetGroup);
        datasetGroup.setText("Dataset Details");
        datasetGroup.setLayout(groupLayout);
        FormData datasetGroupFormat = new FormData();
        datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
        datasetGroupFormat.width = 600;
        datasetGroupFormat.height = 400;
        datasetGroupFormat.left = new FormAttachment(0, 0);
        datasetGroupFormat.right = new FormAttachment(100, 0);
        datasetGroup.setLayoutData(datasetGroupFormat);
		
		
        datasetName = buildCombo("Dataset Name :", jobEntryName, lsMod, middle-10, margin, datasetGroup, datasets);

        final TableViewer tv = new TableViewer(datasetGroup,  SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);

	    tv.setContentProvider(new PlayerContentProvider());
	    tv.setLabelProvider(new PlayerLabelProvider());
	    final Table table = tv.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    final TableColumn tc0 = new TableColumn(table, SWT.LEFT);
	    tc0.setText("Columns");
	    tc0.setWidth(150);
	    TableColumn tvR2 = new TableColumn(table, SWT.LEFT);
	    tvR2.setResizable(false);
	    tvR2.setWidth(0);
	    
	    
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
	    tcR.setWidth(230);
	    final TableColumn tcR2 = new TableColumn(RowTable, SWT.LEFT);
	    tcR2.setResizable(false);
	    tcR2.setWidth(0);
	    
	    final TableViewer Columns = new TableViewer(datasetGroup, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
	    Columns.setContentProvider(new PlayerContentProvider());
	    Columns.setLabelProvider(new PlayerLabelProvider());
	    final Table ColumnTable = Columns.getTable();
	    ColumnTable.setHeaderVisible(true);
	    final TableColumn tcC = new TableColumn(ColumnTable, SWT.LEFT);
	    tcC.setText("Column(s)");
	    tcC.setWidth(120);
	    final TableColumn tcC2 = new TableColumn(ColumnTable, SWT.LEFT);
	    tcC2.setResizable(true);
	    tcC2.setWidth(100);
	    tcC2.setText("Operators");
	    Columns.setInput(columns);
	    
	    
	    
	    
	    final TableViewer Layer = new TableViewer(datasetGroup, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	    Layer.setContentProvider(new PlayerContentProvider());
	    Layer.setLabelProvider(new PlayerLabelProvider());
	    final Table LayerTable = Layer.getTable();
	    LayerTable.setHeaderVisible(true);
	    final TableColumn tcL = new TableColumn(LayerTable, SWT.LEFT);
	    tcL.setText("Layer(s)");
	    tcL.setWidth(230); 
	    final TableColumn tcL2 = new TableColumn(LayerTable, SWT.LEFT);
	    tcL2.setResizable(false);
	    tcL2.setWidth(0);
	    
	    
	    FormData data = new FormData(150,280);
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
		data.top = new FormAttachment(OutRow,70);
		data.left = new FormAttachment(table,40);
		InColumn.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(InColumn,2);
		data.left = new FormAttachment(table,40);
		OutColumn.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(OutColumn,70);
		data.left = new FormAttachment(table,40);
		InLayer.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(InLayer,2);
		data.left = new FormAttachment(table,40);
		OutLayer.setLayoutData(data);
	    
		data = new FormData(260,70);
		data.top = new FormAttachment(datasetName,30);
		data.left = new FormAttachment(InRow,40);
		data.right = new FormAttachment(100,0);
		RowTable.setLayoutData(data);
		data = new FormData(260,70);
		data.top = new FormAttachment(RowTable,25);
		data.left = new FormAttachment(InColumn,40);
		data.right = new FormAttachment(100,0);
		//data.bottom = new FormAttachment(LayerTable,15);
		ColumnTable.setLayoutData(data);
		data = new FormData(260,70);
		data.top = new FormAttachment(ColumnTable,25);
		data.left = new FormAttachment(InLayer,40);
		data.right = new FormAttachment(100,0);
		LayerTable.setLayoutData(data);
		
		Button button = new Button(datasetGroup, SWT.PUSH);
	    button.setText("Add Columns");
	    data = new FormData();
		data.top = new FormAttachment(table, 15);
		data.left = new FormAttachment(0,0);
		button.setLayoutData(data);
		
	    datasetName.addModifyListener(new ModifyListener(){
        	
            public void modifyText(ModifyEvent e){
            	fields = new ArrayList<Player>();
            	tv.refresh();
            	tv.setInput(fields);  
            	rows = new ArrayList<Player>();
            	Row.refresh();
            	Row.setInput(rows);
            	columns = new ArrayList<Player>();
            	Columns.refresh();
            	Columns.setInput(columns);
            	layers = new ArrayList<Player>();
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
							obj.setOP(0);
							
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
	    		for(int i = 0; i<table.getItemCount(); i++){
	    			if(table.getItem(i).getChecked()){
	    				Player P = new Player();
	    				P.setIndex(i);
	    				P.setOP(0);
	    				
	    				P.setFirstName(table.getItem(i).getText());
	    				if(rows.isEmpty() && (!columns.contains(P)) &&(!layers.contains(P))){
	    					rows.add(P);
	    				}
	    				else if((!rows.contains(P)) && (!columns.contains(P)) &&(!layers.contains(P))){
	    					rows.add(P);
	    				}
	    				table.getItem(i).setChecked(false);
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
				for(int i=0; i<RowTable.getItemCount(); i++){
					if(RowTable.getItem(i).getChecked()){
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
	    		for(int i = 0; i<table.getItemCount(); i++){
	    			if(table.getItem(i).getChecked()){
	    				Player P = new Player();
	    				P.setIndex(i);
	    				P.setOP(Integer.valueOf("0"));
	    				
	    				P.setFirstName(table.getItem(i).getText());
	    				if(columns.isEmpty() && (!rows.contains(P)) &&(!layers.contains(P))){
	    					columns.add(P);
	    				}
	    				else if((!rows.contains(P)) && (!columns.contains(P)) &&(!layers.contains(P))){
	    					columns.add(P); 
	    				}
	    				table.getItem(i).setChecked(false);
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
				for(int i=0; i<ColumnTable.getItemCount(); i++){
					if(ColumnTable.getItem(i).getChecked()){
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
	    		for(int i = 0; i<table.getItemCount(); i++){
	    			if(table.getItem(i).getChecked()){
	    				Player P = new Player();
	    				P.setIndex(i);
	    				P.setOP(0);
	    				
	    				P.setFirstName(table.getItem(i).getText());
	    				if(layers.isEmpty() && (!rows.contains(P)) && (!columns.contains(P))){
	    					layers.add(P);
	    				}
	    				else if((!rows.contains(P)) && (!columns.contains(P)) &&(!layers.contains(P))){
	    					layers.add(P);
	    				}
	    				table.getItem(i).setChecked(false);
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
				for(int i=0; i<LayerTable.getItemCount(); i++){
					if(LayerTable.getItem(i).getChecked()){
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
	    
	    CellEditor[] editors = new CellEditor[2];
	    editors[0] = new TextCellEditor(Columns.getTable());
   	    editors[1] = new ComboBoxCellEditor(Columns.getTable(),  OperatorOption.INSTANCES, SWT.READ_ONLY);
   	    
   	    
   	    Columns.setColumnProperties(PROP);
   	    Columns.setCellModifier(new PersonCellModifier(Columns));
   	    Columns.setCellEditors(editors);
	    
	    Group perGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(perGroup);
        perGroup.setText("Persist");
        perGroup.setLayout(groupLayout);
        FormData perGroupFormat = new FormData();
        perGroupFormat.top = new FormAttachment(datasetGroup, margin);
        perGroupFormat.width = 400;
        perGroupFormat.height = 80;
        perGroupFormat.left = new FormAttachment(0, 0);
        perGroupFormat.right = new FormAttachment(100, 0);
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
        fd_composite_1.right = new FormAttachment(0, 360);
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
        fd_composite_2.right = new FormAttachment(0, 360);
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
        
	    wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");
        wSettings = new Button(shell, SWT.PUSH);
        wSettings.setText("Percentage");

        BaseStepDialog.positionBottomButtons(shell, new Button[]{wSettings, wOK, wCancel}, margin, perGroup);

        //BaseStepDialog.positionBottomButtons(shell, new Button[]{ wOK, wCancel}, margin, perGroup);        // Add listeners
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
						        
				final Button B2 = new Button(generalGroup1, SWT.CHECK);
				B2.setText("Parent Total");
				if(Settings.contains("parent"))
					B2.setSelection(true);
				B2.setBackground(new Color(null, 255,255,255));
				final Button B3 = new Button(generalGroup1, SWT.CHECK);
				B3.setText("Grand Total");
				if(Settings.contains("total"))
					B3.setSelection(true);
				B3.setBackground(new Color(null, 255,255,255));
				
				FormData dat = new FormData();											 
				dat.top = new FormAttachment(null,5);
				dat.left = new FormAttachment(0,0);
				B2.setLayoutData(dat);
				dat = new FormData();
				dat.top = new FormAttachment(B2,10);
				dat.left = new FormAttachment(0,0);
				B3.setLayoutData(dat);
				
		        
				B2.addListener(SWT.Selection, new Listener(){
					@Override
					public void handleEvent(Event arg0) {
						if(B2.getSelection())
							Settings.add("parent");
						else{
							int idx = Settings.indexOf("parent");
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
				
				Button wOKS = new Button(shellSettings, SWT.PUSH);
		        wOKS.setText("OK");
		        				
		        BaseStepDialog.positionBottomButtons(shellSettings, new Button[]{wOKS}, 5, generalGroup1);
		        
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
        if(jobEntry.getDefJobName() != null){
        	defJobName = jobEntry.getDefJobName();
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
        if(chkBox.getSelection() && outputName != null){
        	jobEntry.setOutputName(outputName.getText());
        }
        if(chkBox.getSelection() && label != null){
        	jobEntry.setLabel(label.getText());
        }
        if(chkBox != null){
        	jobEntry.setPersistOutputChecked(chkBox.getSelection()?"true":"false");
        }
        if(defJobName.trim().equals("")){
        	defJobName = "Spoon-job";
        }
        jobEntry.setDefJobName(defJobName);
        
        dispose();
    }

    private void cancel() {
        jobEntry.setChanged(backupChanged);
        jobEntry = null;
        dispose();
    }
    
}

class PersonCellModifier implements ICellModifier {
	  private Viewer viewer;

	  public PersonCellModifier(Viewer viewer) {
	    this.viewer = viewer;
	  }

	  public boolean canModify(Object element, String property) {
	    // Allow editing of all values
	    return true;
	  }
	  public Object getValue(Object element, String property) {
	    Player p = (Player) element;
	    if (ECLTabulateDialog.COLUMN.equals(property))
	      return p.getFirstName();
	    else if (ECLTabulateDialog.OP.equals(property))
	      return p.getOP();
	    
	    else
	      return null;
	  }

	  public void modify(Object element, String property, Object value) {
	    if (element instanceof Item)
	      element = ((Item) element).getData();

	    Player p = (Player) element;
	    if (ECLTabulateDialog.COLUMN.equals(property))
	      p.setFirstName((String) value);
	    else if (ECLTabulateDialog.OP.equals(property))
	      p.setOP((Integer) value);
	    
	    // Force the viewer to refresh
	    viewer.refresh();
	  }
}

class Player {
	
	  private String firstName;
	  private Integer OP;
	  
	  public Integer getOP() {
		return OP;
	  }

	public void setOP(Integer OP) {
		this.OP = OP;
	}

	public String getFirstName() {
		  return firstName;
	  }

	  public void setFirstName(String firstName) {
		  this.firstName = firstName;
	  }
	  
	  private int index;

	  public int getIndex() {
		return index;
	  }

	  public void setIndex(int index) {
		  this.index = index;
	  }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((OP == null) ? 0 : OP.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (OP == null) {
			if (other.OP != null)
				return false;
		} else if (!OP.equals(other.OP))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (index != other.index)
			return false;
		return true;
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
	  case 1:
		  return OperatorOption.INSTANCES[values.getOP().intValue()];
	  
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

class OperatorOption {	 	  
	  public static final String COUNT = "Count";
	
	  public static final String SUM = "Sum";
	  
	  public static final String AVE = "Ave";
	
	  public static final String VARIANCE = "Variance";
	  
	  public static final String SD = "Std Dev";
	  
	  public static final String MAX = "Max";
	  
	  public static final String MIN = "Min";	  	

	  public final static String[] INSTANCES = { COUNT, SUM, AVE, VARIANCE, SD, MAX, MIN };
	
}

