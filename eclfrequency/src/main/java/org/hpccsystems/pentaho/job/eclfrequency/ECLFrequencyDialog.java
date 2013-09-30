/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclfrequency;

import java.util.ArrayList;
import java.util.Iterator;
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
import org.hpccsystems.recordlayout.RecordLabels;
import org.hpccsystems.ecljobentrybase.*;
/**
 *
 * @author KeshavS
 */
public class ECLFrequencyDialog extends ECLJobEntryDialog{
	
	public static final String NAME = "Name";
	public static final String OPTION = "Sort Option";
	public static final String COLUMN = "Column";
  
	public static final String[] PROP = { NAME, OPTION, COLUMN};
	
	java.util.List people;
	
	private String normlist = "";
    private ECLFrequency jobEntry;
    private Text jobEntryName;
    private Text TableName;
    private Combo datasetName;
    private Combo sort;
    ArrayList<String> Fieldfilter = new ArrayList<String>();

   
    private Button wOK, wCancel;
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
        people = new ArrayList();

        TabFolder tab = new TabFolder(shell, SWT.FILL | SWT.RESIZE | SWT.MIN | SWT.MAX);
        FormData datatab = new FormData();
        
        datatab.height = 270;
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
		
		jobEntryName = buildText("Job Name :    ", null, lsMod, middle, margin, generalGroup);
		
		//All other contols
        //Dataset Declaration
        Group datasetGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(datasetGroup);
        datasetGroup.setText("Dataset & Frequency Details");
        datasetGroup.setLayout(groupLayout);
        FormData datasetGroupFormat = new FormData();
        datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
        datasetGroupFormat.width = 400;
        datasetGroupFormat.height = 120;
        datasetGroupFormat.left = new FormAttachment(middle, 0);
        datasetGroup.setLayoutData(datasetGroupFormat);
		
		
        datasetName = buildCombo("Dataset :    ", jobEntryName, lsMod, middle, margin, datasetGroup, datasets);
		
        TableName = buildText("OutTablename :", datasetName, lsMod, middle, margin, datasetGroup);
        
        sort = buildCombo("Sort :", TableName, lsMod, middle, margin, datasetGroup, new String[]{"NO", "YES"});
        
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
	    tc0.setText("First Name");
	    tc0.setWidth(100);
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
	    final TableColumn tc1 = new TableColumn(table, SWT.CENTER);
	    tc1.setText("Column Option");
	    tc1.setWidth(0);
	    tc1.setResizable(false);
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
				        	if(!tab.getItem(m).getChecked()){
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
				 	   	     	 field.add(idx,new String[]{st,"false"});
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

	    final CellEditor[] editors = new CellEditor[3];
	    editors[0] = new TextCellEditor(table);
	    
	    editors[1] = new ComboBoxCellEditor(table, SortOption.INSTANCES, SWT.READ_ONLY);
	   
	    editors[2] = new ComboBoxCellEditor(table, ColOption.INSTANCES1, SWT.READ_ONLY);
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
					tc1.setWidth(100);
					tc1.setResizable(true);
					tc.setWidth(100);
					tc.setResizable(true);
				}
				if(sort.getText().equals("NO")){
					tc1.setWidth(0);
					tc1.setResizable(false);
					tc.setWidth(0);
					tc.setResizable(false);
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
            	
            	normlist = new String();
				for(int i = 0; i<table.getItemCount(); i++){
					String s1 = table.getItem(i).getText(0)+","+table.getItem(i).getText(1)+","+table.getItem(i).getText(2)+"-";
					//String s2 = table.getItem(i).getText(1);
					normlist += s1;
					
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
        
        if (jobEntry.gettablename() != null) {
        	TableName.setText(jobEntry.gettablename());
        }
        
        if(jobEntry.getPeople() != null){
        	people = jobEntry.getPeople();
        	tv.setInput(people);
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
   		if(this.sort.getText().equals("")){
   			isValid = false;
       		errors += "\"Choice of Sort\" is a required field!\r\n";
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
        jobEntry.setSort(this.sort.getText());
        jobEntry.settablename(this.TableName.getText());
        jobEntry.setPeople(this.people);

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
	    // Force the viewer to refresh
	    viewer.refresh();
	  }
}


class Player {
	  private String firstName;
	  private Integer sortoption;
	  private Integer Column;


	  public String getFirstName() {
		  return firstName;
	  }

	  public void setFirstName(String firstName) {
		  this.firstName = firstName;
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
	  public static final String NONE = " ";
	
	  public static final String DESC = "DESC";
	  
	  public static final String ASC = "ASC";

	  public final static String[] INSTANCES = { NONE, ASC, DESC };
	
}

class ColOption {
	  public static final String NONE = " ";
	
	  public static final String NAME = "NAME";
	  
	  public static final String VAL = "VALUE";

	  public final static String[] INSTANCES1 = { NONE, NAME, VAL };
	
}

