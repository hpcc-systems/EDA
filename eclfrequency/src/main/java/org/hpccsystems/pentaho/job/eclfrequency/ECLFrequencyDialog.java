/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclfrequency;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.eclipse.swt.widgets.Item;
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
import org.hpccsystems.recordlayout.RecordLabels;
import org.hpccsystems.recordlayout.RecordList;
import org.hpccsystems.ecljobentrybase.*;
/**
 *
 * @author KeshavS
 */
public class ECLFrequencyDialog extends ECLJobEntryDialog{
	
	public static final String NAME = "Name";
	public static final String OPTION = "Sort Option";
	public static final String COLUMN = "Column";
	public static final String TYPE = "Type";
	public static final String SORTNUM = "Sort as Numeric";
  
	public static final String[] PROP = { NAME, OPTION, COLUMN, TYPE, SORTNUM};
	
	java.util.List people;
	
	private String normlist = "";
	private String data_type = "";
    private ECLFrequency jobEntry;
    private Text jobEntryName;
    private Text TableName;
    private Combo datasetName;
    private Combo sort;
    private String outTables[] = null;
    ArrayList<String> Fieldfilter = new ArrayList<String>();
    private String test = "";
    private int number = 1;
    private static String flag = "true";
   
    private Button wOK, wCancel;
    private boolean backupChanged;
    Map<String, String[]> mapDataSets = null;
    
    
	private SelectionAdapter lsDef;

	public Button chkBox;
    public static Text outputName;
    public static Label label;
    private String persist;
    private Composite composite;
    private String defJobName;
	
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
            defJobName = ap.getGlobalVariable(this.jobMeta.getJobCopies(), "jobName");
            
        }catch (Exception e){
            System.out.println("Error Parsing existing Datasets");
            System.out.println(e.toString());
            datasets = new String[]{""};
        }
        

        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
        people = new ArrayList();
        test = jobEntry.getName().substring(jobEntry.getName().length()-1); 
        if(Character.isDigit(test.toCharArray()[0]) && flag.equals("true")){
        	number = Integer.parseInt(test);
        	number = number - 1;
        	flag = "false";
        }
        TabFolder tab = new TabFolder(shell, SWT.FILL | SWT.RESIZE | SWT.MIN | SWT.MAX);
        FormData datatab = new FormData();
        
        datatab.height = 400;
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
        generalGroupFormat.height = 65;
        generalGroupFormat.left = new FormAttachment(middle, 0);
        generalGroup.setLayoutData(generalGroupFormat);
		
		jobEntryName = buildText("Job Entry Name :    ", null, lsMod, middle, margin, generalGroup);
		
		//All other contols
        //Dataset Declaration
        Group datasetGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(datasetGroup);
        datasetGroup.setText("Dataset & Frequency Details");
        datasetGroup.setLayout(groupLayout);
        FormData datasetGroupFormat = new FormData();
        datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
        datasetGroupFormat.width = 400;
        datasetGroupFormat.height = 100;
        datasetGroupFormat.left = new FormAttachment(middle, 0);
        datasetGroup.setLayoutData(datasetGroupFormat);
        
        datasetName = buildCombo("Dataset Name :    ", jobEntryName, lsMod, middle, margin, datasetGroup, datasets);
		
        sort = buildCombo("Sort :    ", datasetName, lsMod, middle, margin, datasetGroup, new String[]{"NO", "YES"});
        
        //Begin
        
        Group perGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(perGroup);
        perGroup.setText("Persist");
        perGroup.setLayout(groupLayout);
        FormData perGroupFormat = new FormData();
        perGroupFormat.top = new FormAttachment(datasetGroup, margin);
        perGroupFormat.width = 400;
        perGroupFormat.height = 80;
        perGroupFormat.left = new FormAttachment(middle, 0);
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
        
        item1.setControl(compForGrp);
        /**
         * TableViewer in new Tab starts
         */
        
        TabItem item2 = new TabItem(tab, SWT.NULL);
        item2.setText("Fields Selected");
		
        ScrolledComposite sc2 = new ScrolledComposite(tab, SWT.H_SCROLL | SWT.V_SCROLL);
        Composite compForGrp2 = new Composite(sc2, SWT.NONE);
        compForGrp2.setLayout(new GridLayout(1, false));
        sc2.setContent(compForGrp2);

        // Set the minimum size
        sc2.setMinSize(300, 200);

        // Expand both horizontally and vertically
        sc2.setExpandHorizontal(true);
        sc2.setExpandVertical(true);
        item2.setControl(sc2);
        Button button = new Button(compForGrp2, SWT.PUSH);
        button.setLayoutData(new GridData(GridData.FILL));

	    // Create the table viewer to display the players
	    //final Table table = new Table(composite, SWT.CHECK | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
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
	    
	    final TableColumn tc = new TableColumn(table, SWT.CENTER);
	    tc.setText("Sort Option");
	    tc.setWidth(0);
	    tc.setResizable(false);
	    tc.setImage(RecordLabels.getImage("unchecked"));
	    tc.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
		        boolean checkBoxFlag = false;
		        for (int i = 0; i < table.getItemCount(); i++) {
		            if (table.getItems()[i].getText(1).equals("ASC")) {
		                checkBoxFlag = true;
		                
		            }
		        }
		        if (checkBoxFlag) {
		        	people = new ArrayList();
		            for (int m = 0; m < table.getItemCount(); m++) {
		            	Player p = new Player();
						p.setFirstName(table.getItems()[m].getText(0));
						p.setSort(Integer.valueOf("1"));
						if(table.getItems()[m].getText(2).equals("NAME"))
							p.setColumns(Integer.valueOf("0"));
						else
							p.setColumns(Integer.valueOf("1"));
						p.setType(table.getItems()[m].getText(3));
						if(table.getItems()[m].getText(4).equals("NO"))
							p.setSortNumeric(Integer.valueOf("0"));
						else
							p.setSortNumeric(Integer.valueOf("1"));
						people.add(p);
						tc.setImage(RecordLabels.getImage("checked"));
		            }
		        } else {
		        	people = new ArrayList();
		            for (int m = 0; m < table.getItemCount(); m++) {
		            	Player p = new Player();
						p.setFirstName(table.getItems()[m].getText(0));
						p.setSort(Integer.valueOf("0"));
						if(table.getItems()[m].getText(2).equals("NAME"))
							p.setColumns(Integer.valueOf("0"));
						else
							p.setColumns(Integer.valueOf("1"));
						p.setType(table.getItems()[m].getText(3));
						if(table.getItems()[m].getText(4).equals("NO"))
							p.setSortNumeric(Integer.valueOf("0"));
						else
							p.setSortNumeric(Integer.valueOf("1"));
						people.add(p);
						tc.setImage(RecordLabels.getImage("unchecked"));
		            }
		        } 	
		        tv.refresh();
		        tv.setInput(people);
		        table.redraw();
		    } 
		});

	    final TableColumn tc1 = new TableColumn(table, SWT.CENTER);
	    tc1.setText("Column Option");
	    tc1.setWidth(0);
	    tc1.setResizable(false);
	    tc1.setImage(RecordLabels.getImage("unchecked"));
	    tc1.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
		        boolean checkBoxFlag = false;
		        for (int i = 0; i < table.getItemCount(); i++) {
		            if (table.getItems()[i].getText(2).equals("NAME")) {
		                checkBoxFlag = true;
		                
		            }
		        }
		        if (checkBoxFlag) {
		        	people = new ArrayList();
		            for (int m = 0; m < table.getItemCount(); m++) {
		            	Player p = new Player();
						p.setFirstName(table.getItems()[m].getText(0));
						p.setColumns(Integer.valueOf("1"));
						if(table.getItems()[m].getText(1).equals("ASC"))
							p.setSort(Integer.valueOf("0"));
						else
							p.setSort(Integer.valueOf("1"));
						p.setType(table.getItems()[m].getText(3));
						if(table.getItems()[m].getText(4).equals("NO"))
							p.setSortNumeric(Integer.valueOf("0"));
						else
							p.setSortNumeric(Integer.valueOf("1"));
						
						people.add(p);
						tc1.setImage(RecordLabels.getImage("checked"));
		            }
		        } else {
		        	people = new ArrayList();
		            for (int m = 0; m < table.getItemCount(); m++) {
		            	Player p = new Player();
						p.setFirstName(table.getItems()[m].getText(0));
						p.setColumns(Integer.valueOf("0"));
						if(table.getItems()[m].getText(1).equals("ASC"))
							p.setSort(Integer.valueOf("0"));
						else
							p.setSort(Integer.valueOf("1"));
						p.setType(table.getItems()[m].getText(3));
						if(table.getItems()[m].getText(4).equals("NO"))
							p.setSortNumeric(Integer.valueOf("0"));
						else
							p.setSortNumeric(Integer.valueOf("1"));
						people.add(p);
						tc1.setImage(RecordLabels.getImage("unchecked"));
		            }
		        } 	
		        tv.refresh();
		        tv.setInput(people);
		        table.redraw();
		    } 
		});

	    
	    final TableColumn tc2 = new TableColumn(table, SWT.CENTER);
	    tc2.setText("Type");
	    tc2.setWidth(0);
	    tc2.setResizable(false);
	    
	    final TableColumn tc3 = new TableColumn(table, SWT.CENTER);
	    tc3.setText("Sort Numeric?");
	    tc3.setWidth(0);
	    tc3.setResizable(false);
	    
	    if(jobEntry.getPeople() != null)
            people = jobEntry.getPeople();
	    tv.setInput(people);
	    if(people != null && people.size() > 0) {
            
            for (Iterator iterator = people.iterator(); iterator.hasNext();) {
                    Player obj = (Player) iterator.next();
            }
	    }
        tv.setInput(people);
        table.setRedraw(true);
	    

	    
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
	    
	    datasetName.addModifyListener(new ModifyListener(){
        	
            public void modifyText(ModifyEvent e){
            	people = new ArrayList();
            	tv.refresh();
            	tv.setInput(people);            	
            }
	    });

	    button.setText("Add Columns");

	    // Add a listener to change the tableviewer's input
	    button.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
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
			    column2.setText("Type");
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
				                tab.getItems()[m].setChecked(true);
				                column1.setImage(RecordLabels.getImage("checked"));
				                tab.selectAll();
				            }
				        }
				        
				        for(int m = 0; m<tab.getItemCount(); m++){
				        	if(tab.getItem(m).getChecked()){
				        		String st = tab.getItem(m).getText();
				        		String type = tab.getItem(m).getText(1);
				        		int idx = 0; 
				 	   	      	 for(Iterator<String[]> it2 = field.iterator(); it2.hasNext(); ){
				 	   	     	 	 String[] s = it2.next();
				 	   	     		 if(s[0].equalsIgnoreCase(st)){
				 	   	     				idx = field.indexOf(s);
				 	   	     				break;
				 	   	     		 }
				 	   	     	 }
				 	   	     	 field.remove(idx);
				 	   	     	 field.add(idx,new String[]{st,"true",type});
				 	   	     	 // to find index of the selected item in the original field array list
				        	}
				        	else if(!tab.getItem(m).getChecked()){
				        		String st = tab.getItem(m).getText();
				        		String type = tab.getItem(m).getText(1);
				        		int idx = 0; 
				 	   	      	 for(Iterator<String[]> it2 = field.iterator(); it2.hasNext(); ){
				 	   	     	 	 String[] s = it2.next();
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
				RecordList rec = null;
		    	String[] items = null;
		    	String[] types = null;
              try{
          		
<<<<<<< HEAD
                 // String[] items = ap.fieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
                  RecordList rec = ap.rawFieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
                  //org.hpccsystems.recordlayout.RecordBO[] items = (org.hpccsystems.recordlayout.RecordBO[])rec.getRecords().toArray();
                  
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
=======
                  //items = ap.fieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
                  //mapDataSets = ap.parseDefExpressionBuilder(jobMeta.getJobCopies(), datasetName.getText());
                  rec = ap.rawFieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
>>>>>>> 5321a09bc3fa68322f562f57db1a16fc35a5975e
                  
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
		        dat.right = new FormAttachment(100, 0);
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
<<<<<<< HEAD
									I.setText(1,s[2]);
=======
		            				I.setText(1,s[2]);
>>>>>>> 45320053d50cd1f69e42a261625e082f6a2ed5f0
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
								Player p = new Player();
								p.setFirstName(S[0]);
								p.setSort(Integer.valueOf("0"));
								p.setColumns(Integer.valueOf("0"));
								p.setType(S[2]);
								p.setSortNumeric(Integer.valueOf("0"));
								people.add(p);
								
								
							}
							
						}
						
						tv.setInput(people);
						
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

	    final CellEditor[] editors = new CellEditor[5];
	    editors[0] = new TextCellEditor(table);
	    
	    editors[1] = new ComboBoxCellEditor(table, SortOption.INSTANCES, SWT.READ_ONLY);
	   
	    editors[2] = new ComboBoxCellEditor(table, ColOption.INSTANCES1, SWT.READ_ONLY);
	    editors[3] = new TextCellEditor(table);
	    editors[4] = new ComboBoxCellEditor(table, SortAsNumeric.INSTANCES, SWT.READ_ONLY);
	    // Set the editors, cell modifier, and column properties
	    tv.setColumnProperties(PROP);
	    tv.setCellModifier(new PersonCellModifier(tv));
	    tv.setCellEditors(editors);
	    
	    

		/**
		 * Fields Tab ends
		 */
	    
	    sort.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent arg0) {
				if(sort.getText().equals("YES")){
					tc1.setWidth(150);
					tc1.setResizable(true);
					tc.setWidth(150);
					tc.setResizable(true);
					tc3.setWidth(150);
					tc3.setResizable(true);
				}
				if(sort.getText().equals("NO")){
					tc1.setWidth(0);
					tc1.setResizable(false);
					tc.setWidth(0);
					tc.setResizable(false);
					tc3.setWidth(0);
					tc3.setResizable(false);
				}
			}
	    	
	    });
	    
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
            	outTables = new String[table.getItemCount()];
            	normlist = new String();
            	data_type = new String();
				for(int i = 0; i<table.getItemCount(); i++){
					String s1 = table.getItem(i).getText(0)+","+table.getItem(i).getText(1)+","+table.getItem(i).getText(2)+","+table.getItem(i).getText(4)+"-"; 
					normlist += s1;
					data_type += table.getItem(i).getText(3)+",";
					outTables[i] = table.getItem(i).getText(0)+"_"+jobEntryName.getText();
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
        
        if (jobEntry.getnormList() != null) {
        	normlist = jobEntry.getnormList();
        }
        
        if (jobEntry.getSort() != null) {
        	sort.setText(jobEntry.getSort());
        }
        
        if (jobEntry.getDataType() != null) {
        	data_type = jobEntry.getDataType();
        }
        
        if(jobEntry.getPeople() != null){
        	people = jobEntry.getPeople();
        	tv.setInput(people);
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
   		if(this.sort.getText().equals("")){
   			isValid = false;
       		errors += "\"Sort\" is a required field!\r\n";
   		}
   		if(this.normlist.equals("") || this.outTables == null){
   			isValid = false;
   			errors += "You need to select  a field to compute Frequency";
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
        jobEntry.setSort(this.sort.getText());
        jobEntry.setPeople(this.people);
        jobEntry.setDataType(this.data_type);
        jobEntry.setoutTables(outTables);
        jobEntry.setflag(flag);
        jobEntry.setNumber(Integer.toString(number));
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
	    if (ECLFrequencyDialog.NAME.equals(property))
	      return p.getFirstName();
	    else if (ECLFrequencyDialog.OPTION.equals(property))
	      return p.getSort();
	    else if (ECLFrequencyDialog.COLUMN.equals(property))
		      return p.getColumns();
	    if (ECLFrequencyDialog.TYPE.equals(property))
		      return p.getType();
	    else if (ECLFrequencyDialog.SORTNUM.equals(property))
		      return p.getSortNumeric();	    
	    else
	      return null;
	  }

	  public void modify(Object element, String property, Object value) {
	    if (element instanceof Item)
	      element = ((Item) element).getData();

	    Player p = (Player) element;
	    if (ECLFrequencyDialog.NAME.equals(property))
	      p.setFirstName((String) value);
	    else if (ECLFrequencyDialog.OPTION.equals(property))
	      p.setSort((Integer) value);
	    else if (ECLFrequencyDialog.COLUMN.equals(property))
		      p.setColumns((Integer) value);
	    if (ECLFrequencyDialog.TYPE.equals(property))
		      p.setType((String) value);
	    else if (ECLFrequencyDialog.SORTNUM.equals(property))
		      p.setSortNumeric((Integer) value);
	    // Force the viewer to refresh
	    viewer.refresh();
	  }
}


class Player {
	  private String firstName;
	  private Integer sortoption;
	  private Integer Column;
	  private String type;
	  private Integer SortNumeric;

	  public String getFirstName() {
		  return firstName;
	  }

	  public void setFirstName(String firstName) {
		  this.firstName = firstName;
	  }

	  public String getType() {
		  return type;
	  }

	  public void setType(String type) {
		  this.type = type;
	  }

	  public Integer getSortNumeric() {
		  return SortNumeric;
	  }

	  public void setSortNumeric(Integer SortNumeric) {
		  this.SortNumeric = SortNumeric;
	  }

	  public Integer getSort() {
		  return sortoption;
	  }

	  public void setSort(Integer sortoption) {
		  this.sortoption = sortoption;
	  }
	  public Integer getColumns() {
		  return Column;
	  }

	  public void setColumns(Integer Column) {
		  this.Column = Column;
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
	  	//break;
	  case 1:
		  return SortOption.INSTANCES[values.getSort().intValue()];//text = values[1];
	  case 2:
		  return ColOption.INSTANCES1[values.getColumns().intValue()];
	  case 3:
		  return values.getType();
	  case 4:
		  return SortAsNumeric.INSTANCES[values.getSortNumeric().intValue()];
	  	//break;
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

class SortOption {
	  //public static final String NONE = " ";
	
	  public static final String DESC = "DESC";
	  
	  public static final String ASC = "ASC";

	  public final static String[] INSTANCES = { DESC, ASC };
	
}

class ColOption {
	  //public static final String NONE = " ";
	
	  public static final String NAME = "NAME";
	  
	  public static final String VAL = "FREQUENCY";

	  public final static String[] INSTANCES1 = { VAL, NAME };
	
}

class SortAsNumeric {
	  //public static final String NONE = " ";
	
	  public static final String NO = "NO";
	  
	  public static final String YES = "YES";

	  public final static String[] INSTANCES = { NO, YES };
	
}
