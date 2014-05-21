package org.hpccsystems.pentaho.job.eclunivariate;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
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
	java.util.List Group;
    private ECLUnivariate jobEntry;
    private Text jobEntryName;
    private Combo datasetName;
    private String fieldList;
    //private String single = "";
    private Button wOK, wCancel;
    private boolean backupChanged;
    private String test = "";
    private int number = 1;
    private String flag = "true";
    
	private SelectionAdapter lsDef;
	public Button chkBox;
    public static Text outputName;
    public static Label label;
    private String persist;
    private Composite composite;
    private String defJobName;
	
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
        people = new ArrayList();
        Group = new ArrayList();
        test = jobEntry.getName().substring(jobEntry.getName().length()-1); 
        if(Character.isDigit(test.toCharArray()[0]) && flag.equals("true")){
        	number = Integer.parseInt(test);        	
        	flag = "false";
        }
        
        TabFolder tab = new TabFolder(shell, SWT.FILL | SWT.RESIZE | SWT.MIN | SWT.MAX);
        FormData datatab = new FormData();
        
        datatab.height = 400;
        datatab.width = 500;
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
		shell.setText("Univariate Stats");
		
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
        generalGroupFormat.left = new FormAttachment(middle-20, 0);
        generalGroup.setLayoutData(generalGroupFormat);
		
		jobEntryName = buildText("Job Name :", null, lsMod, middle, margin, generalGroup);
		
        Group datasetGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(datasetGroup);
        datasetGroup.setText("Dataset Details");
        datasetGroup.setLayout(groupLayout);
        FormData datasetGroupFormat = new FormData();
        datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
        datasetGroupFormat.width = 400;
        datasetGroupFormat.height = 140;
        datasetGroupFormat.left = new FormAttachment(middle-20, 0);
        datasetGroup.setLayoutData(datasetGroupFormat);
		
        datasetName = buildCombo("Dataset :", jobEntryName, lsMod, middle, margin, datasetGroup, datasets);
        

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
	    data.top = new FormAttachment(datasetName, 25);
		data.left = new FormAttachment(0,0);
		//data.right = new FormAttachment(100,0);
		Mean.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(datasetName, 25);
		data.left = new FormAttachment(Mean,15);
		Median.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(datasetName, 25);
		data.left = new FormAttachment(Median,15);
		Mode.setLayoutData(data);
		
		data = new FormData();
	    data.top = new FormAttachment(datasetName, 25);
		data.left = new FormAttachment(Mode,15);
		//data.right = new FormAttachment(100,0);
		StdDev.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(datasetName, 25);
		data.left = new FormAttachment(StdDev,15);
		Max.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(datasetName, 25);
		data.left = new FormAttachment(Max,15);
		Min.setLayoutData(data);  
	    

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
	    
	    Group perGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(perGroup);
        perGroup.setText("Persist");
        perGroup.setLayout(groupLayout);
        FormData perGroupFormat = new FormData();
        perGroupFormat.top = new FormAttachment(datasetGroup, margin);
        perGroupFormat.width = 400;
        perGroupFormat.height = 80;
        perGroupFormat.left = new FormAttachment(middle-20, 0);
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
        
        item1.setControl(compForGrp);
	    
	    TabItem item2 = new TabItem(tab, SWT.NULL);
        item2.setText("Fields Selected");
		
        ScrolledComposite sc2 = new ScrolledComposite(tab, SWT.H_SCROLL | SWT.V_SCROLL);
        Composite compForGrp2 = new Composite(sc2, SWT.NONE);
        compForGrp2.setLayout(new GridLayout(2, false));
        sc2.setContent(compForGrp2);

        // Set the minimum size
        sc2.setMinSize(300, 200);

        // Expand both horizontally and vertically
        sc2.setExpandHorizontal(true);
        sc2.setExpandVertical(true);
        item2.setControl(sc2);
        
        Button button = new Button(compForGrp2, SWT.PUSH);
        button.setText("Add Columns");
        button.setLayoutData(new GridData(GridData.FILL));
        button.setBackground(new Color(null,255,255,255));
        Button GroupBy = new Button(compForGrp2, SWT.PUSH);
        GroupBy.setText("Group By");
        GroupBy.setLayoutData(new GridData(GridData.FILL));
        GroupBy.setBackground(new Color(null,255,255,255));
        
	    final TableViewer tv = new TableViewer(compForGrp2,  SWT.CHECK | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

	    tv.setContentProvider(new PlayerContentProvider());
	    tv.setLabelProvider(new PlayerLabelProvider());
	    
	    final Table table = tv.getTable();
	    table.setLayoutData(new GridData(GridData.FILL_BOTH));
	    final TableColumn tc0 = new TableColumn(table, SWT.LEFT);
	    tc0.setText("Column Name");
	    tc0.setWidth(150);
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
        
        final TableViewer tvGrp = new TableViewer(compForGrp2,  SWT.CHECK | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

	    tvGrp.setContentProvider(new PlayerContentProvider());
	    tvGrp.setLabelProvider(new PlayerLabelProvider());
	    
	    final Table tableGrp = tvGrp.getTable();
	    tableGrp.setLayoutData(new GridData(GridData.FILL_BOTH));
	    final TableColumn tc0Grp = new TableColumn(tableGrp, SWT.LEFT);
	    tc0Grp.setText("Group");
	    tc0Grp.setWidth(150);
	    tc0Grp.setImage(RecordLabels.getImage("unchecked"));
	    tc0Grp.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
		        boolean checkBoxFlag = false;
		        for (int i = 0; i < tableGrp.getItemCount(); i++) {
		            if (tableGrp.getItems()[i].getChecked()) {
		                checkBoxFlag = true;
		                
		            }
		        }
		        if (checkBoxFlag) {
		            for (int m = 0; m < tableGrp.getItemCount(); m++) {
		                tableGrp.getItems()[m].setChecked(false);
		                tc0Grp.setImage(RecordLabels.getImage("unchecked"));				                
		                tableGrp.deselectAll();
		            }
		        } else {
		            for (int m = 0; m < tableGrp.getItemCount(); m++) {
		                tableGrp.getItems()[m].setChecked(true);
		                tc0Grp.setImage(RecordLabels.getImage("checked"));
		                tableGrp.selectAll();
		            }
		        } 	
		        tvGrp.refresh();
		        tableGrp.redraw();
		    } 
		});
	    
	    if(jobEntry.getGroup() != null)
            Group = jobEntry.getGroup();
	    tvGrp.setInput(Group);
	    if(Group != null && Group.size() > 0) {
            
            for (Iterator iterator = Group.iterator(); iterator.hasNext();) {
                    Cols obj = (Cols) iterator.next();
            }
	    }
        tvGrp.setInput(Group);
        tableGrp.setRedraw(true);
	    
        Button del = new Button(compForGrp2, SWT.PUSH);
	    del.setText("Delete");
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
	    Button delGrp = new Button(compForGrp2, SWT.PUSH);
	    delGrp.setText("Delete");
	    delGrp.addSelectionListener(new SelectionAdapter(){
	    	public void widgetSelected(SelectionEvent event){
	    		int cnt = 0;
	    		for(int i = 0; i<tableGrp.getItemCount(); i++){
	    			if(tableGrp.getItem(i).getChecked()){
	    				
	    				Group.remove(Math.abs(cnt - i));
						cnt++;
					}
	    		}
	    		if(tc0Grp.getImage().equals(RecordLabels.getImage("checked")))
	    			tc0Grp.setImage(RecordLabels.getImage("unchecked")); 
	    		tvGrp.refresh();
	    		tvGrp.setInput(Group);
	    		
	    	}
	    });
	    
	    datasetName.addModifyListener(new ModifyListener(){
        	
            public void modifyText(ModifyEvent e){
            	people = new ArrayList();
            	tv.refresh();
            	tv.setInput(people);    
            	Group = new ArrayList();
            	tvGrp.refresh();
            	tvGrp.setInput(Group); 
            }
	    });


	    // Add a listener to change the tableviewer's input
	    button.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	    	  createTableDialog(display,table,tv,"addcolumns");
	      }
		});
	    
	    GroupBy.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  createTableDialog(display,tableGrp,tvGrp,"group");
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
	    
	    tableGrp.setHeaderVisible(true);
	    tableGrp.setLinesVisible(true);
	    tableGrp.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event event) {
            	if(event.detail == SWT.CHECK){	
            		tvGrp.refresh();
                    tableGrp.redraw();
            	}
            }
        });
	    
	    tvGrp.setColumnProperties(PROP);
	    

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
            	fieldList = new String();
            	for(int i = 0; i<table.getItemCount(); i++){            		
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
        	
        }
        
        if(jobEntry.getGroup() != null){
        	Group = jobEntry.getGroup();
        	tvGrp.setInput(Group);
        }
        
        if(jobEntry.getflag()!= null){
        	flag = jobEntry.getflag();
        }
        
        if(jobEntry.getNumber().length()>=1){
        	number = Integer.parseInt(jobEntry.getNumber());
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
        jobEntry.setGroup(Group);
        jobEntry.setCheckList(flags[0]+","+flags[1]+","+flags[2]+","+flags[3]+","+flags[4]+","+flags[5]);
        jobEntry.setFieldList(this.fieldList);
        jobEntry.setflag(flag);
        jobEntry.setNumber(Integer.toString(number));
       	jobEntry.setSingle("UniStats_"+"Univariate"+number);
        jobEntry.setMode("ModeTable_"+"Univariate"+number);
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
    
    private void createTableDialog(Display display, final Table table, final TableViewer tv, final String flag){
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
	    
	    final TreeColumn column2 = new TreeColumn(tab, SWT.LEFT);			   
	    column2.setWidth(0);
	    
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
		            	if(!tab.getItem(m).getText(1).equals("string")){
		            		tab.getItem(m).setChecked(true);
		            		column1.setImage(RecordLabels.getImage("checked"));
		            	}
		            }
		        } 		
		        for(int m = 0; m<tab.getItemCount(); m++){
		        	if(tab.getItem(m).getChecked()){
		        		String st = tab.getItem(m).getText();
		        		
		        		int idx = 0; String type = "";
		 	   	      	 for(Iterator<String[]> it2 = field.iterator(); it2.hasNext(); ){
		 	   	     	 	 String[] s = it2.next();
		 	   	     	 	 type = s[2];
		 	   	     		 if(s[0].equalsIgnoreCase(st)){
		 	   	     				idx = field.indexOf(s);
		 	   	     				break;
		 	   	     		 }
		 	   	     	 }
		 	   	     	 field.remove(idx);
		 	   	     	 field.add(idx,new String[]{st,"true",type});
		 	   	     	 // to find index of the selected item in the original field array list
		        	}
		        	if(!tab.getItem(m).getChecked()){
		        		String st = tab.getItem(m).getText();
		        		
		        		int idx = 0; String type = "";
		 	   	      	 for(Iterator<String[]> it2 = field.iterator(); it2.hasNext(); ){
		 	   	     	 	 String[] s = it2.next();
		 	   	     	 	 type = s[2];
		 	   	     		 if(s[0].equalsIgnoreCase(st)){
		 	   	     				idx = field.indexOf(s);
		 	   	     				break;
		 	   	     		 }
		 	   	     	 }
		 	   	     	 field.remove(idx);
		 	   	     	 field.add(idx,new String[]{st,"false",type});
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
		
        //String[] items = ap.fieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
        RecordList rec = ap.rawFieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
        
        for(int i = 0; i < rec.getRecords().size(); i++){
            TreeItem item = new TreeItem(tab, SWT.NONE);
            item.setText(0, rec.getRecords().get(i).getColumnName().toLowerCase());
            String type = "String";
            String width = "";
            try{
                   type = rec.getRecords().get(i).getColumnType();
                   width = rec.getRecords().get(i).getColumnWidth();
                   item.setText(1,type+width);
                   if(rec.getRecords().get(i).getColumnType().startsWith("String")){
                  	 item.setBackground(0, new Color(null,211,211,211));
                   }
            }catch (Exception e){
                   System.out.println("Frequency Cant look up column type");
            }
            
            field.add(new String[]{rec.getRecords().get(i).getColumnName().toLowerCase(),"false",type+width});
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
            				I.setText(1,s[2]);
            				if(s[1].equalsIgnoreCase("true")) 
            					I.setChecked(true);
            				if(s[2].equalsIgnoreCase("string") && !flag.equals("group")){ 
            					I.setChecked(false);
            					I.setBackground(new Color(null,211,211,211));
            				}
            			}
            		}
            		tab.setRedraw(true);
            		
            }
        });
        
       
        
        tab.addListener(SWT.Selection, new Listener() {
    	     public void handleEvent(Event event) {
    	    	 if(((TreeItem)event.item).getText(1).equalsIgnoreCase("string") && !flag.equals("group")) 
    	    		 ((TreeItem)event.item).setChecked(false);
    	    	 String st = ((TreeItem)event.item).getText(0);
    	    	 String type = ((TreeItem)event.item).getText(1);
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
 	   	     		 field.add(idx,new String[]{st,"true",type});
 	   	     	 else
 	   	     		 field.add(idx,new String[]{st,"false",type});
    	   	}
       });
        
        Listener okfilter = new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				ArrayList<String> check = new ArrayList<String>();
				
				if(table.getItemCount() > 0){
					for(int i = 0; i<table.getItemCount(); i++){
						check.add(table.getItem(i).getText());
					}
				}
				
							
				for(Iterator<String[]> it = field.iterator(); it.hasNext();){
					String[] S = it.next();
						if(S[1].equalsIgnoreCase("True") && !check.contains(S[0])){
						Cols p = new Cols();
						p.setFirstName(S[0]);
						p.setType(S[2]);
 						if(!flag.equals("group"))
							people.add(p);
						else
							Group.add(p);
					}
					
				}
				if(!flag.equals("group"))	
					tv.setInput(people);
				else
					tv.setInput(Group);
								
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
    
}

class Cols {
	  private String firstName;
	  private String type;
	  
	  

	  public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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
	  case 1:
	  	  return values.getType();//text = values[0];
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

