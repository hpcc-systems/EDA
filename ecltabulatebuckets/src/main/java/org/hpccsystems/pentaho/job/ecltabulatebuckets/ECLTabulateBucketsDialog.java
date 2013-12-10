/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecltabulatebuckets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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
public class ECLTabulateBucketsDialog extends ECLJobEntryDialog{
	
	public static final String NAME = "Name";
	public static final String BUCKETS = "Buckets";
	
	public static final String[] PROP = { NAME, BUCKETS};
	
	java.util.List fields;
	java.util.List rows;
	java.util.List columns;
	java.util.List layers;
	
	private ECLTabulateBuckets jobEntry;
    private Text jobEntryName;
    private Combo datasetName;
   
    private Button wOK, wCancel;
    private boolean backupChanged;
    
    
	private SelectionAdapter lsDef;

	
    public ECLTabulateBucketsDialog(Shell parent, JobEntryInterface jobEntryInt,
			Repository rep, JobMeta jobMeta) {
		super(parent, jobEntryInt, rep, jobMeta);
		jobEntry = (ECLTabulateBuckets) jobEntryInt;
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
        generalGroupFormat.width = 500;
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
        datasetGroupFormat.width = 450;
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
	    final TableColumn tc1 = new TableColumn(table, SWT.LEFT);
	    tc1.setText("buckets");
	    tc1.setWidth(0);
	    tc1.setResizable(false);
	    
	    Button InRow = new Button(datasetGroup, SWT.PUSH);
	    InRow.setText("  >>  ");
	    InRow.setBackground(new Color(null,255,255,255));
	    Button OutRow = new Button(datasetGroup, SWT.PUSH);
	    OutRow.setText("  <<  ");
	    OutRow.setBackground(new Color(null,255,255,255));
	    
	    Button InLayer = new Button(datasetGroup, SWT.PUSH);
	    InLayer.setText("  >>  ");
	    InLayer.setBackground(new Color(null,255,255,255));
	    Button OutLayer = new Button(datasetGroup, SWT.PUSH);
	    OutLayer.setText("  <<  ");
	    OutLayer.setBackground(new Color(null,255,255,255));
	    
	    final TableViewer Row = new TableViewer(datasetGroup, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
	    Row.setContentProvider(new PlayerContentProvider());
	    Row.setLabelProvider(new PlayerLabelProvider());
	    final Table RowTable = Row.getTable();
	    RowTable.setHeaderVisible(true);
	    RowTable.setLinesVisible(true);
	    final TableColumn tcR = new TableColumn(RowTable, SWT.LEFT);
	    tcR.setText("Fields");
	    tcR.setWidth(100);
	    final TableColumn tcR1 = new TableColumn(RowTable, SWT.CENTER);
	    tcR1.setText("Buckets");
	    tcR1.setWidth(100);
	    
	    if(jobEntry.getRows() != null)
            rows = jobEntry.getRows();
	    Row.setInput(rows);
	    if(rows != null && rows.size() > 0) {
            
            for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
                    Player obj = (Player) iterator.next();
            }
	    }
        Row.setInput(rows);
        RowTable.setRedraw(true);
	    	    
	    final TableViewer Layer = new TableViewer(datasetGroup, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	    Layer.setContentProvider(new PlayerContentProvider());
	    Layer.setLabelProvider(new PlayerLabelProvider());
	    final Table LayerTable = Layer.getTable();
	    LayerTable.setHeaderVisible(true);
	    final TableColumn tcL = new TableColumn(LayerTable, SWT.LEFT);
	    tcL.setText("Layer(s)");
	    tcL.setWidth(200);
	    final TableColumn tcL1 = new TableColumn(LayerTable, SWT.LEFT);
	    tcL1.setText("buckets");
	    tcL1.setWidth(0);
	    tcL1.setResizable(false);
	    
	    FormData data = new FormData(100,300);
	    data.top = new FormAttachment(datasetName, 15);
		data.left = new FormAttachment(0,0);		
		table.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(datasetName,80);
		data.left = new FormAttachment(table,40);
		InRow.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(InRow,2);
		data.left = new FormAttachment(table,40);
		OutRow.setLayoutData(data);
				
		data = new FormData();
		data.top = new FormAttachment(OutRow,100);
		data.left = new FormAttachment(table,40);
		InLayer.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(InLayer,2);
		data.left = new FormAttachment(table,40);
		OutLayer.setLayoutData(data);
	    
		data = new FormData(200,135);
		data.top = new FormAttachment(datasetName,30);
		data.left = new FormAttachment(InRow,40);
		RowTable.setLayoutData(data);

		data = new FormData(200,50);
		data.top = new FormAttachment(RowTable,35);
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
							obj.setBuckets(" ");
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
	    				P.setBuckets("0");
	    				rows.add(P);
	    				fields.remove(Math.abs(cnt - i));
						cnt++;
					}
	    		}
	    		tv.refresh();
	    		tv.setInput(fields);	    		
	    		Row.setInput(rows);	    
	    		RowTable.setRedraw(true);
			}
	    });
	    
	    final CellEditor[] editors = new CellEditor[2];
	    editors[0] = new TextCellEditor(RowTable);
	    editors[1] = new TextCellEditor(RowTable);
	    // Set the editors, cell modifier, and column properties
	    Row.setColumnProperties(PROP);
	    Row.setCellModifier(new PersonCellModifier(Row));
	    Row.setCellEditors(editors);

	    
	    OutRow.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				int cnt = 0;
				for(int i = 0; i<RowTable.getItemCount(); i++){
					if(RowTable.getItem(i).getChecked()){
	    				Player P = new Player();
	    				P.setFirstName(RowTable.getItem(i).getText());
	    				P.setBuckets(" ");
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


	    InLayer.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				int cnt = 0;
	    		for(int i = 0; i<table.getItemCount(); i++){
	    			if(table.getItem(i).getChecked()){
	    				Player P = new Player();
	    				P.setFirstName(table.getItem(i).getText());
	    				P.setBuckets("");
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
	    				P.setBuckets(" ");
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
        

        BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, margin, datasetGroup);
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
        if(jobEntry.getLayers() != null){
        	layers = jobEntry.getLayers();
        	Layer.refresh();
        	Layer.setInput(layers);
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
   		if(this.rows.size() <= 1){
   			isValid = false;
   			errors += "Need to Select at least 2 fields!\r\n";
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
        jobEntry.setLayers(this.layers);
        jobEntry.setDatasetName(this.datasetName.getText());
 
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
	    if (ECLTabulateBucketsDialog.NAME.equals(property))
	      return p.getFirstName();
	    else if (ECLTabulateBucketsDialog.BUCKETS.equals(property))
	      return p.getBuckets();
	    else
	      return null;
	  }

	  public void modify(Object element, String property, Object value) {
	    if (element instanceof Item)
	      element = ((Item) element).getData();

	    Player p = (Player) element;
	    if (ECLTabulateBucketsDialog.NAME.equals(property))
	      p.setFirstName((String) value);
	    else if (ECLTabulateBucketsDialog.BUCKETS.equals(property))
	      p.setBuckets((String) value);
	    // Force the viewer to refresh
	    viewer.refresh();
	  }
}


class Player {
	  private String firstName;
	  private String buckets;

	  public String getFirstName() {
		  return firstName;
	  }

	  public void setFirstName(String firstName) {
		  this.firstName = firstName;
	  }

	  public String getBuckets() {
		  return buckets;
	  }

	  public void setBuckets(String buckets) {
		  this.buckets = buckets;
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
	  	  return values.getFirstName();
	  case 1:
		  return values.getBuckets();
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

