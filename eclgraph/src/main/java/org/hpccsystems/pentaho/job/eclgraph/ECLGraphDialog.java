/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclgraph;

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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
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
import org.hpccsystems.recordlayout.AddColumnsDialog;
import org.hpccsystems.recordlayout.RecordBO;
import org.hpccsystems.recordlayout.RecordLabels;
import org.hpccsystems.recordlayout.RecordList;
import org.hpccsystems.ecljobentrybase.*;
/**
 *
 * @author KeshavS
 */
public class ECLGraphDialog extends ECLJobEntryDialog{
	
	public static final String NAME = "Name";
	public static final String OPTION = "Color Option";
	public static final String DATTYPE = "DataType";
  
	public static final String[] PROP = { NAME, OPTION};
	
	java.util.List people;	
	private String filePath;
	private String normlist = "";
    private ECLGraph jobEntry;
    private Text jobEntryName;
    private Combo datasetName;
    private Combo datasetNameOriginal;
    private Combo GraphType;
    ArrayList<String> Fieldfilter = new ArrayList<String>();
    
    String checkList[] = null;
   
    private Button wOK, wCancel;
    private boolean backupChanged;
    
    
	private SelectionAdapter lsDef;

	
    public ECLGraphDialog(Shell parent, JobEntryInterface jobEntryInt,
			Repository rep, JobMeta jobMeta) {
		super(parent, jobEntryInt, rep, jobMeta);
		jobEntry = (ECLGraph) jobEntryInt;
        if (this.jobEntry.getName() == null) {
            this.jobEntry.setName("Graph");
        }
	}

    

	// Building GUI
	public JobEntryInterface open() {
        Shell parentShell = getParent();
        final Display display = parentShell.getDisplay();
        
        String datasets[] = null;
        String datasets1[] = null;
        
        AutoPopulate ap = new AutoPopulate();
        try{
            //Object[] jec = this.jobMeta.getJobCopies().toArray();
        	
            datasets1 = ap.parseDatasetsRecordsets(this.jobMeta.getJobCopies());
            datasets = ap.parseGraphableDefinitions(this.jobMeta.getJobCopies());
            checkList = ap.parseUnivariate(this.jobMeta.getJobCopies());
            filePath = ap.getGlobalVariable(this.jobMeta.getJobCopies(), "compileFlags"); 
        }catch (Exception e){
            System.out.println("Error Parsing existing Datasets");
            System.out.println(e.toString());
            datasets = new String[]{""};
            //System.out.println("Error Parsing existing Datasets");
            //System.out.println(e.toString());
            datasets1 = new String[]{""};
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
		shell.setText("Graph");
		
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
        datasetGroup.setText("Dataset and Graph Details");
        datasetGroup.setLayout(groupLayout);
        FormData datasetGroupFormat = new FormData();
        datasetGroupFormat.top = new FormAttachment(generalGroup, margin);
        datasetGroupFormat.width = 400;
        datasetGroupFormat.height = 120;
        datasetGroupFormat.left = new FormAttachment(middle, 0);
        datasetGroup.setLayoutData(datasetGroupFormat);
		
		
        datasetNameOriginal = buildCombo("Original Dataset Name : ", jobEntryName, lsMod, middle, margin, datasetGroup, datasets1);
        
        datasetName = buildCombo("Dataset Name :    ", datasetNameOriginal, lsMod, middle, margin, datasetGroup, datasets);
		
        GraphType = buildCombo("Graph Type :", datasetName, lsMod, middle, margin, datasetGroup, new String[]{"PieChart", "LineChart","ScatterChart","BarChart"});
        
        item1.setControl(compForGrp);
        /**
         * TableViewer in new Tab starts
         */
        
        TabItem item2 = new TabItem(tab, SWT.NULL);
        item2.setText("Columns for Graph");
		
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
	    
	    final TableColumn tc1 = new TableColumn(table, SWT.CENTER);
	    tc1.setText("Color-Axis Option");
	    tc1.setWidth(0);
	    tc1.setResizable(false);
	    final TableColumn tc2 = new TableColumn(table, SWT.CENTER);
	    tc2.setText("Type");
	    tc2.setWidth(150);
	    tc2.setResizable(true);
	    
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
	    	  AutoPopulate ap = new AutoPopulate();
	    	  AddColumnsDialog obj = new AddColumnsDialog(display);
	    	  RecordList rec = null;
	    	  String[] items = null;
	    	  String[] types = null;
	    	 
              try{          		
                  //String[] items = ap.fieldsByDataset( datasetName.getText(),jobMeta.getJobCopies());
                  rec = ap.rawFieldsByDataset( datasetNameOriginal.getText(),jobMeta.getJobCopies());
                  
                  //obj.setItems(items);
              }catch (Exception ex){
                  System.out.println("failed to load record definitions");
                  System.out.println(ex.toString());
                  ex.printStackTrace();
              }
              if(!datasetNameOriginal.getText().equals("") && datasetName.getText().equals("")){
            	  try {
					items = ap.fieldsByDataset( datasetNameOriginal.getText(),jobMeta.getJobCopies());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	  int i = 0;types = new String[items.length];
            	  for(Iterator<RecordBO> I = rec.getRecords().iterator();I.hasNext();){
						RecordBO ob = (RecordBO) I.next();						
						types[i] = ob.getColumnType();
						i++;
						
					}
              }
              else{//(datasetName.getText() != null){
		    	  if(datasetName.getText().split("_")[1].equalsIgnoreCase("Percentile")){
		    		  items = new String[]{"field","percentile","value"};
		    		  types = new String[]{"STRING","REAL","REAL"};
		    	  }
		    	  else if(datasetName.getText().split("_")[1].equalsIgnoreCase("Frequency")){
		    		  String type = "";
		    		  for(Iterator<RecordBO> I = rec.getRecords().iterator();I.hasNext();){
							RecordBO ob = (RecordBO) I.next();
							if(ob.getColumnName().equalsIgnoreCase(datasetName.getText().split("_")[0])){
								type = ob.getColumnType();
								break;
							}
						}
		    		  items = new String[]{datasetName.getText().split("_")[0],"frequency","percent"};
		    		  types = new String[]{type,"REAL","REAL"};
		    	  }
		    	  else if(datasetName.getText().split("_")[1].startsWith("Univariate")){
		    		  
		    		 if(datasetName.getText().split("_")[0].equalsIgnoreCase("UniStats")){
		    			 String[] stats = new String[]{"Mean","Median","Mode","Sd","Maxval","Minval"};
		    			 int num = Integer.parseInt(datasetName.getText().split("_")[1].substring(10)); 
		    			 
		    			 
		    			 String[] check = checkList[num-1].split(",");
		    			 String[] items1 = new String[check.length];
		    			 String[] types1 = new String[check.length];
		    			 items1[0] = "field"; types1[0] = "STRING";int j = 1;
		    			 for(int i = 0; i<check.length; i++){
		    				 if(check[i].equalsIgnoreCase("true") && i != 2)
		    					 {
		    					 	items1[j] = stats[i];
		    					 	types1[j] = "REAL";
		    					 	j++;
		    					 }	    				 
		    			 }
		    			 
		    			 items = new String[j];
		    			 types = new String[j];
		    			 for(int i = 0; i<j; i++){
		    				 items[i] = items1[i];
		    				 types[i] = types1[i];
		    			 }
					}
		    		 else if(datasetName.getText().split("_")[0].equalsIgnoreCase("ModeTable")){
		    			 items = new String[]{"field","mode","cnt"};
		    			 types = new String[]{"STRING","REAL","UNSIGNED"};
		    		 }
		    	  }
		    	  else if(datasetName.getText().split("_")[2].equalsIgnoreCase("random")){
		    		  
		    		  try {
						String[] item = ap.fieldsByDataset( datasetNameOriginal.getText(),jobMeta.getJobCopies());
						items = new String[item.length+1];
						types = new String[item.length];
						for(int i = 0; i<item.length+1; i++){
							if(i == 0)
								items[i] = "rand";
							else
								items[i] = item[i-1];
						} 
						int i = 0;
						types[i] = "UNSIGNED DECIMAL8_8";
						i++;
						for(Iterator<RecordBO> I = rec.getRecords().iterator();I.hasNext();){
							RecordBO ob = (RecordBO) I.next();
							types[i] = ob.getColumnType();
							i++;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
		    		  
		    	  }
	          }
	    	  obj.setItems(items);
              obj.setSelectedColumns(null);
              obj.run();
              ArrayList<String> check = new ArrayList<String>();
              if(table.getItemCount() > 0){
            	  for(int i = 0; i<table.getItemCount(); i++){
            		  check.add(table.getItem(i).getText());
            	  }
              }
              for(Iterator<String> it = obj.getSelectedColumns().iterator(); it.hasNext();){
					String S = it.next();
					/*for(Iterator<RecordBO> I = rec.getRecords().iterator();I.hasNext();){
						RecordBO ob = (RecordBO) I.next();
						if(ob.getColumnName().equalsIgnoreCase(S)){
							type = ob.getColumnType();
							break;
						}
					}*/
					int idx = 0;
					for(int i = 0; i<items.length; i++){
						if(S.equalsIgnoreCase(items[i]))
							{idx = i;break;}
					}
					if(!check.contains(S)){
						Player p = new Player();
						p.setFirstName(S);
						p.setTy(types[idx]);
						p.setColor(Integer.valueOf("0"));
						people.add(p);
						
						
					}
              }
              tv.setInput(people);
              table.setRedraw(true);
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
	    
	    GraphType.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent arg0) {
				if(!GraphType.getText().equals("PieChart")){
					tc1.setWidth(150);
					tc1.setResizable(true);
				}	
				if(GraphType.getText().equals("PieChart")){
					tc1.setWidth(0);
					tc1.setResizable(false);
				}	
			}
	    	
	    });
	    

	    final CellEditor[] editors = new CellEditor[3];
	    editors[0] = new TextCellEditor(table);
	    
	    editors[1] = new ComboBoxCellEditor(table, ColorOption.INSTANCES, SWT.READ_ONLY);
	    editors[2] = new TextCellEditor(table);
	   
	    // Set the editors, cell modifier, and column properties
	    tv.setColumnProperties(PROP);
	    tv.setCellModifier(new PersonCellModifier(tv));
	    tv.setCellEditors(editors);
	    
	    

		/**
		 * Fields Tab ends
		 */
	    
	    
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

        if (jobEntry.getDatasetNameOriginal() != null) {
            datasetNameOriginal.setText(jobEntry.getDatasetNameOriginal());
        }

        if (jobEntry.getnormList() != null) {
        	normlist = jobEntry.getnormList();
        }
        
        if (jobEntry.getTyp() != null) {
        	GraphType.setText(jobEntry.getTyp());
        }
        
        if(jobEntry.getPeople() != null){
        	people = jobEntry.getPeople();
        	tv.setInput(people);
        }               
        
        if(jobEntry.getFilePath() != null){
        	filePath = jobEntry.getFilePath();
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
   		if(this.datasetNameOriginal.getText().equals("")){
   			isValid = false;
       		errors += "\"Original Dataset Name\" is a required field!\r\n";
   		}
   		if(this.GraphType.getText().equals("")){
   			isValid = false;
       		errors += "\"Graph Type\" is a required field!\r\n";
   		}
   		if(this.normlist.equals("")){
   			isValid = false;
   			errors += "You need to select a field to produce Graph\r\n";
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
        jobEntry.setDatasetNameOriginal(this.datasetNameOriginal.getText());
        jobEntry.setnormList(this.normlist);
        jobEntry.setTyp(this.GraphType.getText());
        jobEntry.setPeople(this.people);
        jobEntry.setFilePath(this.filePath);
        if(checkList.length>0)
        	jobEntry.setTest(this.checkList[0]);// Only when one needs to plot Univariate statistics
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
	    if (ECLGraphDialog.NAME.equals(property))
	      return p.getFirstName();
	    else if (ECLGraphDialog.OPTION.equals(property))
	      return p.getColor();
	    if (ECLGraphDialog.DATTYPE.equals(property))
		      return p.getTy();
		    	    
	    else
	      return null;
	  }

	  public void modify(Object element, String property, Object value) {
	    if (element instanceof Item)
	      element = ((Item) element).getData();

	    Player p = (Player) element;
	    if (ECLGraphDialog.NAME.equals(property))
	      p.setFirstName((String) value);
	    else if (ECLGraphDialog.OPTION.equals(property))
	      p.setColor((Integer) value);
	    if (ECLGraphDialog.DATTYPE.equals(property))
		      p.setTy((String) value);
		    
	    // Force the viewer to refresh
	    viewer.refresh();
	  }
}


class Player {
	  private String firstName;
	  private Integer coloroption;
	  private String type;

	  public String getFirstName() {
		  return firstName;
	  }

	  public void setFirstName(String firstName) {
		  this.firstName = firstName;
	  }

	  public String getTy() {
		  return type;
	  }

	  public void setTy(String type) {
		  this.type = type;
	  }
	  
	  public Integer getColor() {
		  return coloroption;
	  }

	  public void setColor(Integer coloroption) {
		  this.coloroption = coloroption;
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
		  return ColorOption.INSTANCES[values.getColor().intValue()];//text = values[1];
		  
	  case 2:
		  return values.getTy();
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

class ColorOption {
	  public static final String NONE = "Color";
	  
	  public static final String AXIS = "XAxis";
	
	  public static final String RED = "Red";
	  
	  public static final String YELLOW = "Yellow";
	  
	  public static final String BLUE = "Blue";
	  
	  public static final String GREEN = "Green";
	  
	  public static final String PURPLE = "Purple";
	  
	  public static final String BLACK = "Black";
	  
	  public static final String BROWN = "Brown";

	  public final static String[] INSTANCES = { NONE, AXIS, RED, YELLOW, BLUE, GREEN, PURPLE, BLACK, BROWN };
	
}

