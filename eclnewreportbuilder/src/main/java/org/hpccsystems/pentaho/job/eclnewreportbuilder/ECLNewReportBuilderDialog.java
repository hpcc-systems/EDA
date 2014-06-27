/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclnewreportbuilder;

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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
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
import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import org.hpccsystems.eclguifeatures.AutoPopulate;
import org.hpccsystems.eclguifeatures.ErrorNotices;
import org.hpccsystems.ecljobentrybase.*;
import org.hpccsystems.recordlayout.RecordBO;
import org.hpccsystems.recordlayout.RecordLabels;
import org.hpccsystems.recordlayout.RecordList;
/**
 *
 * @author KeshavS
 */
public class ECLNewReportBuilderDialog extends ECLJobEntryDialog{//extends JobEntryDialog implements JobEntryDialogInterface {

	public ArrayList<String> fields = new ArrayList<String>();
	public static final String VARIABLE = "Variable Name";

	public static final String OPERATOR = "Operator";

	public static final String FIELDS = "Field(s)";

	public static final String[] PROPS = { VARIABLE, OPERATOR, FIELDS};
	
	java.util.List people;
	private ECLNewReportBuilder jobEntry;
    private Text jobEntryName;
    private Combo datasetName;
    private Combo datasetName1;
    private Button Btn2;
    private Button wOK, wCancel;
    private boolean backupChanged;
    private ArrayList<String> Items;
    private ArrayList<String> Types;
    private Text ReportName;
    @SuppressWarnings("unused")
	private SelectionAdapter lsDef;
   
    
    
    public ECLNewReportBuilderDialog(Shell parent, JobEntryInterface jobEntryInt, Repository rep, JobMeta jobMeta) {
        super(parent, jobEntryInt, rep, jobMeta);
        jobEntry = (ECLNewReportBuilder) jobEntryInt;
        if (this.jobEntry.getName() == null) {
            this.jobEntry.setName("A plugin to create Plugins");
        }
    }

    public JobEntryInterface open() {
        Shell parentShell = getParent();
        final Display display = parentShell.getDisplay();
        
        String datasets[] = null;
        String datasets1[] = null;
                
        AutoPopulate ap = new AutoPopulate();
        try{            
            datasets = ap.parseDatasetsRecordsets(this.jobMeta.getJobCopies());
            datasets1 = ap.parseGraphableDefinitions(this.jobMeta.getJobCopies());
        }catch (Exception e){
            System.out.println("Error Parsing existing Datasets");
            System.out.println(e.toString());
            datasets = new String[]{""};            
        }

        
        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
        Items = new ArrayList<String>();
        Types = new ArrayList<String>();
        people = new ArrayList();
        TabFolder tab = new TabFolder(shell, SWT.FILL | SWT.RESIZE | SWT.MIN | SWT.MAX);
        FormData datatab = new FormData();
        
        datatab.height = 350;
        datatab.width = 750;
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

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;


        shell.setLayout(formLayout);//formLayout
        shell.setText("ReportBuilder");

        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        

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
        generalGroupFormat.width = 340;
        generalGroupFormat.height = 65;
        generalGroupFormat.left = new FormAttachment(middle, 0);
        generalGroupFormat.right = new FormAttachment(100, 0);
        generalGroup.setLayoutData(generalGroupFormat);
		
		jobEntryName = buildText("Job Name :", null, lsMod, middle, margin, generalGroup);
		
		//All other contols
        //Dataset Declaration
        Group datasetGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(datasetGroup);
        datasetGroup.setText("Dataset Details");
        datasetGroup.setLayout(groupLayout);
        FormData datasetGroupFormat = new FormData();
        datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
        datasetGroupFormat.width = 340;
        datasetGroupFormat.height = 120;
        datasetGroupFormat.left = new FormAttachment(middle, 0);
        datasetGroupFormat.right = new FormAttachment(100, 0);
        datasetGroup.setLayoutData(datasetGroupFormat);
		
        int aLen = datasets.length;
        int bLen = datasets1.length;
        String[] C= new String[aLen+bLen];
        System.arraycopy(datasets, 0, C, 0, aLen);
        System.arraycopy(datasets1, 0, C, aLen, bLen);
        datasetName = buildCombo("Original Dataset :", jobEntryName, lsMod, middle, margin, datasetGroup, C);     
        ReportName = buildText("Report Name :", datasetName, lsMod, middle, margin, datasetGroup);
        //datasetName1 = buildCombo("Derived Dataset :", datasetName, lsMod, middle, margin, datasetGroup, datasets1);
        item1.setControl(compForGrp);
        /**
         * Code Editor in new Tab starts
         */
        
        TabItem item2 = new TabItem(tab, SWT.NULL);
        item2.setText("Fields Selected");
		
        
        Composite sc2 = new Composite(tab, SWT.NONE);
        sc2.setLayout(new FormLayout()); 
              
        
        item2.setControl(sc2);
        
        Transfer[] type = new Transfer[] { TextTransfer.getInstance() };
        
		final Table tbl = new Table(sc2, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP); 
        tbl.setHeaderVisible(true);
        TableColumn tc = new TableColumn(tbl, SWT.NONE);
        tc.setWidth(150);
        tc.setText("Field(s) Dataset");
        tc = new TableColumn(tbl, SWT.NONE);
        tc.setWidth(80);
        tc.setText("Type");
        
        final TableViewer tv = new TableViewer(sc2, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION | SWT.CHECK);
        tv.setContentProvider(new PersonContentProvider());
        tv.setLabelProvider(new PersonLabelProvider());
        tv.setInput(people);
        final Table table = tv.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        
        final TableColumn tc0 = new TableColumn(table, SWT.LEFT);
	    tc0.setText("Variable Name");
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
	    TableColumn tc1 = new TableColumn(table, SWT.CENTER);
	    tc1.setText("Operator");
	    tc1.setWidth(100);
	    tc1 = new TableColumn(table, SWT.CENTER);
	    tc1.setText("Field(s)");
	    tc1.setWidth(200);
	    
	    CellEditor[] editors = new CellEditor[3];
	    editors[0] = new TextCellEditor(table);
	    
	    editors[1] = new ComboBoxCellEditor(table,OperatorOption.INSTANCES);
	    editors[2] = new TextCellEditor(table);

	    // Set the editors, cell modifier, and column properties
	    tv.setColumnProperties(PROPS);
	    tv.setCellModifier(new PersonCellModifier(tv));
	    tv.setCellEditors(editors); 
	    
        Button Btn = new Button(sc2, SWT.PUSH);
        Btn.setText("Add Column(s)");
        
        
        
        Btn2 = new Button(sc2, SWT.PUSH);
        Btn2.setText("Add Variable");
        
        Button del = new Button(sc2, SWT.PUSH);
	    del.setText("Delete");
	    
        
        FormData data = new FormData(250,280);
        data.top = new FormAttachment(null, 5);
		data.left = new FormAttachment(0,0);		
		tbl.setLayoutData(data);
        
		data = new FormData(350,280);
		data.top = new FormAttachment(null, 5);
		data.left = new FormAttachment(tbl,10);
		data.right = new FormAttachment(100,0);
        table.setLayoutData(data);
        
        data = new FormData();
        data.top = new FormAttachment(tbl,5);
        data.left = new FormAttachment(0,0);
        Btn.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(table,5);
        data.left = new FormAttachment(Btn,190);
        Btn2.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(table,5);
        data.left = new FormAttachment(Btn2,5);
        del.setLayoutData(data);
        
        
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
        
        Btn2.addSelectionListener(new SelectionListener (){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int cnt = table.getItemCount();
				Person p = new Person();
		        p.setVariableName("Var"+(cnt+1));		        
		        p.setOP(Integer.valueOf("0"));
		        p.setFields("");
		        people.add(p);
		        tv.refresh();		        
			}
        	
        });
        
        
        datasetName.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent arg0) {
				tbl.setItemCount(0);				
			}
        	
        });
        

        
        
        
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");

        BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, margin, tab);
        
        Btn.addSelectionListener(new SelectionListener (){
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				AutoPopulate ap = new AutoPopulate();
		    	RecordList rec = null;
		    	String[] items = null;
		    	String[] types = null;
		    	if(!datasetName.getText().equals("")){ 
		    		try{          			                  
		    			rec = ap.rawFieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());	                  	                 
		    		}catch (Exception ex){
		    			System.out.println("failed to load record definitions");
		    			System.out.println(ex.toString());
		    			ex.printStackTrace();
		    		}
		    	}
	              if(!datasetName.getText().equals("")){
	            	  try {
						items = ap.fieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
					} catch (Exception e) {
						
						e.printStackTrace();
					}
	            	  int i = 0;types = new String[items.length];
	            	  for(Iterator<RecordBO> I = rec.getRecords().iterator();I.hasNext();){
							RecordBO ob = (RecordBO) I.next();						
							types[i] = ob.getColumnType()+ob.getColumnWidth();
							i++;
							
						}	            	  
	              }
	              else{
	            	  
	              }
	              if(!datasetName.getText().equals("")){
		              if(tbl.getItemCount() == 0){
		            	  pouplateTable(tbl,items,types);
		   				  for(int i = 0; i<items.length; i++){
		   					  fields.add(rec.getRecords().get(i).getColumnName().toLowerCase());
		   					  Items.add(rec.getRecords().get(i).getColumnName().toLowerCase());
		   					  Types.add(types[i]);
		   				  }			
		              }
	              }
			}        	
        });
        
        
        
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
        
        if (jobEntry.getReportname() != null) {
            ReportName.setText(jobEntry.getReportname());
        }
        
        if(jobEntry.getPeople() != null){
        	people = jobEntry.getPeople();
        	tv.setInput(people);
        }
                     
        if (jobEntry.getItems() != null || jobEntry.getTypes() != null) {        	
            Items = jobEntry.getItems();
            Types = jobEntry.getTypes();
            String[] items = new String[Items.size()];
            String[] types = new String[Types.size()];
            int i = 0;
            for(Iterator<String> it = Items.iterator(); it.hasNext();){            
            	items[i] = it.next();
            	i++;
            }
            i = 0;
            for(Iterator<String> it = Types.iterator(); it.hasNext();){            
            	types[i] = it.next();
            	i++;
            }
            pouplateTable(tbl,items,types);
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
    
    private void pouplateTable(Table tbl, String[] items, String[] types){
    	if(items.length>0){
	    	for(int i = 0; i<items.length; i++){
	    		TableItem it = new TableItem(tbl, SWT.NONE);
	    		it.setText(0, items[i]);
	    		if(types.length > 0)
	    			it.setText(1, types[i]);
	    		else
	    			it.setText(1, "");
	    	}
    	}
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
    	
    	if(this.ReportName.getText().equals("")){
    		isValid = false;
    		errors += "\"Report Name\" is a required field!\r\n";
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
        jobEntry.setDatasetName(datasetName.getText()); 
        jobEntry.setItems(Items);
        jobEntry.setTypes(Types);        
        jobEntry.setPeople(this.people);
        jobEntry.setReportname(ReportName.getText());
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
	    Person p = (Person) element;
	    if (ECLNewReportBuilderDialog.VARIABLE.equals(property))
	      return p.getVariableName();
	    else if (ECLNewReportBuilderDialog.OPERATOR.equals(property))
	      return p.getOP();
	    else if (ECLNewReportBuilderDialog.FIELDS.equals(property))
		      return p.getFields();	    	   
	    else
	      return null;
	  }
 
	  public void modify(Object element, String property, Object value) {
	    if (element instanceof Item)
	      element = ((Item) element).getData();

	    Person p = (Person) element;
	    if (ECLNewReportBuilderDialog.VARIABLE.equals(property))
	      p.setVariableName((String) value);
	    else if (ECLNewReportBuilderDialog.OPERATOR.equals(property))
	      p.setOP((Integer) value);
	    else if (ECLNewReportBuilderDialog.FIELDS.equals(property))
		      p.setFields((String) value);
	    viewer.refresh();
	  }
}

class Person {
	  private String variableName;
	  private Integer operator;
	  private String fields;
	  
	  public String getVariableName() {
		  return variableName;
	  }

	  public void setVariableName(String variableName) {
		  this.variableName = variableName;
	  }

	  public String getFields() {
		  return fields;
	  }

	  public void setFields(String fields) {
		  this.fields = fields;
	  }

	  public Integer getOP() {
		  return operator;
	  }

	  public void setOP(Integer operator) {
		  this.operator = operator;
	  }

}

class PersonLabelProvider implements ITableLabelProvider {


//Constructs a PlayerLabelProvider
	public PersonLabelProvider() {
	}


	public Image getColumnImage(Object arg0, int arg1) {

		return null;
	}


	public String getColumnText(Object arg0, int arg1) {
	  Person values = (Person) arg0;
	//  String text = "";
	  switch(arg1){
	  case 0:
	  	  return values.getVariableName();//text = values[0];
	  	//break;
	  case 1:
		  return OperatorOption.INSTANCES[values.getOP().intValue()];//text = values[1];
	  case 2:
		  return values.getFields();	   
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


class PersonContentProvider implements IStructuredContentProvider {

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
	  public static final String NONE = "Choose";
	  
	  public static final String ADD = "Add";
	  
	  public static final String SUBTRACT = "Sub";
	  
	  public static final String MULTIPLY = "Mult";
	  
	  public static final String DIVIDE = "Div";
	  
	  public static final String MOD = "Mod";

	  public final static String[] INSTANCES = { NONE, ADD, SUBTRACT, MULTIPLY, DIVIDE, MOD };
	
}

