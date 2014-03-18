package org.hpccsystems.mapper;

import java.awt.event.TextEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.hpccsystems.eclguifeatures.AutoPopulate;
import org.pentaho.di.job.entry.JobEntryCopy;

public class MainMapperForOutliers {
	private String layoutStyle = "transform";
	// The table viewer
	private TableViewer tableViewer;
	// Create a RecordList and assign it to an instance variable
	private MapperRecordList mapperRecList = new MapperRecordList();	
	//private Text txtVariableName;
	private Combo cmbVariableName;
	private Text txtExpression;
	
	private Button btnSaveExpression;
	private Button btnAddExpression;
	
	private String[] functionList = null;
	private String[] operatorList = null;
	private String[] booleanList = null;
	private String[] cmbListValues = null;
	private Tree treeInputDataSet = null;
	
	private Text txtValue;
	private Text rule;
	private List rulesList = new ArrayList();
	private List rulesListNoDup = null;
	private String[] rList = null;
	
	//Fields to check for EDIT status
	private String oldexpression = "";
	private String newExpression = "";
	private int previousSelectedIndex;
	
	private String filterStatement = "";
	private String oldFilterStatement = "";
	
	private Button save;
	private Button clearAll;
	//private boolean hasChanged = false;
	MapperBO objRecord;
	
	
	public String getLayoutStyle() {
		return layoutStyle;
	}

	public void setLayoutStyle(String layoutStyle) {
		this.layoutStyle = layoutStyle;
	}

	public Tree getTreeInputDataSet() {
		return treeInputDataSet;
	}

	public void setTreeInputDataSet(Tree treeInputDataSet) {
		this.treeInputDataSet = treeInputDataSet;
	}
	
	public String getOldexpression() {
		return oldexpression;
	}

	public void setOldexpression(String oldexpression) {
		this.oldexpression = oldexpression;
	}

	public String getNewExpression() {
		return newExpression;
	}

	public void setNewExpression(String newExpression) {
		this.newExpression = newExpression;
	}
	
	public int getPreviousSelectedIndex() {
		return previousSelectedIndex;
	}

	public void setPreviousSelectedIndex(int previousSelectedIndex) {
		this.previousSelectedIndex = previousSelectedIndex;
	}
	

	public String getFilterStatement() {
		filterStatement = this.txtExpression.getText();
		return filterStatement;
	}

	public void setFilterStatement(String filterStatement) {
		this.filterStatement = filterStatement;
		this.txtExpression.setText(this.filterStatement);
	}
	
	

	public String getOldFilterStatement() {
		return oldFilterStatement;
	}

	public void setOldFilterStatement(String oldFilterStatement) {
		this.oldFilterStatement = oldFilterStatement;
	}

	/**
	 * This method redraws the table.
	 */
	public void reDrawTable(){
		MapperRecordList oldRL = mapperRecList;
		mapperRecList = new MapperRecordList();
                
		if(oldRL.getRecords() != null && oldRL.getRecords().size() > 0) {
			//System.out.println("Size: "+oldRL.getRecords().size());
			for (Iterator<MapperBO> iterator = oldRL.getRecords().iterator(); iterator.hasNext();) {
				MapperBO obj = (MapperBO) iterator.next();
				mapperRecList.addRecord(obj);
			}
		}
		oldRL = null;
		if(this.layoutStyle.equalsIgnoreCase("transform")){
			tableViewer.setInput(mapperRecList);
			tableViewer.getTable().setRedraw(true);
		}
               
	}
	
	
	//The Constructor has input as 
	
	public MainMapperForOutliers(Composite parentComp, Map<String, String[]> mapDataSets, String[] arrCmbValues,String layoutStyle,List<JobEntryCopy> jobs){
		setupVars(parentComp, mapDataSets, arrCmbValues, layoutStyle,jobs);
	}
	
	private void setupVars(Composite parentComp, Map<String, String[]> mapDataSets, String[] arrCmbValues,String layoutStyle,List<JobEntryCopy> jobs){
		this.layoutStyle = layoutStyle;
		setCmbListValues(arrCmbValues);
		populateFunctionList();
		populateOperatorList();
		populateBooleanList();
		this.addChildControls(parentComp, mapDataSets, jobs);
		if(this.layoutStyle.equalsIgnoreCase("transform")){
			int numExpressions = tableViewer.getTable().getItemCount();
			if(numExpressions>1 && !layoutStyle.equalsIgnoreCase("transform")){
				this.btnAddExpression.setEnabled(false);
			}
		}
			
	}
	
	/**
	 * Create a new shell, add the widgets, open the shell
	 * @return the shell that was created	 
	 */
	private void addChildControls(Composite parentComp, Map<String, String[]> mapDataSets, List<JobEntryCopy> jobs) {
		if(this.layoutStyle.equalsIgnoreCase("transform")){
			Composite tblComposite = new Composite(parentComp, SWT.NONE);
			createTable(tblComposite);		// Create the table
			createButtons(tblComposite);
		}
		buildExpressionPanel(parentComp, mapDataSets, jobs);	// Add the widgets needed to build a Expression Panel
		
	}
	
	private void populateFunctionList() {
		String[] fuctionList = {"ABS", "ACOS", "AGGREGATE", "ALLNODES", "APPLY"};
		setFunctionList(fuctionList);
	}
	
	private void populateOperatorList() {
		String operatorList[] = {":=","+", "-", "*", "/", "%", "(", ")", "=", "<>", ">", "<", "<=", ">=","~"};
		setOperatorList(operatorList);
	}
	
	private void populateBooleanList() {
		String booleanList[] = {"AND","IN","NOT","OR","XOR"};
		setBooleanList(booleanList);
	}
	
	public String[] getFunctionList() {
		return functionList;
	}

	public void setFunctionList(String[] functionList) {
		this.functionList = functionList;
	}

	public String[] getOperatorList() {
		return operatorList;
	}

	public void setOperatorList(String[] operatorList) {
		this.operatorList = operatorList;
	}
	
	public String[] getBooleanList() {
		return booleanList;
	}

	public void setBooleanList(String[] booleanList) {
		this.booleanList = booleanList;
	}
	
	public List getRulesList() {
		return rulesList;
	}

	public void setRulesList(List rulesList) {
		this.rulesList = rulesList;
	}

	public MapperRecordList getMapperRecList() {
		return mapperRecList;
	}

	public void setMapperRecList(MapperRecordList mapperRecList) {
		this.mapperRecList = mapperRecList;
	}
	
	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public void setTableViewer(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}
	
	public String[] getCmbListValues() {
		return cmbListValues;
	}

	public void setCmbListValues(String[] cmbListValues) {
		this.cmbListValues = cmbListValues;
	}
	
	public Combo getCmbVariableName() {
		return cmbVariableName;
	}

	public void setCmbVariableName(Combo cmbVariableName) {
		this.cmbVariableName = cmbVariableName;
	}
	
	private void createTable(Composite tblComposite) {
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 250;
		tblComposite.setLayout(layout);
		tblComposite.setLayoutData(data);
		
		int style = SWT.CHECK | SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
		final Table table = new Table(tblComposite, style);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 3;
		table.setLayoutData(gridData);	
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		//1St Column - Output
		final TableColumn tableColumn0 = new TableColumn(table, SWT.LEFT, 0);
		tableColumn0.setImage(MapperLabelsProvider.getImage("unchecked"));
		if(this.layoutStyle.equalsIgnoreCase("transform")){
			tableColumn0.setText("Variable");
		}else{
			tableColumn0.setText("");
		}
		
		tableColumn0.setWidth(200);
		
		// 3rd column - Expression
		TableColumn column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Expression");
		column.setWidth(400);
		
		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new MapperContentProvider());	//Set the Content Provider for the table	
		tableViewer.setLabelProvider(new MapperLabelsProvider());	//Set the Label Provider for the table
		
		tableViewer.setInput(mapperRecList);	//Add an empty MapperRecordList to the TableViewer
		
		tableColumn0.addListener(SWT.Selection, new Listener() {
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
		                tableColumn0.setImage(MapperLabelsProvider.getImage("unchecked"));
		                table.deselectAll();
		            }
		        } else {
		            for (int m = 0; m < table.getItemCount(); m++) {
		                table.getItems()[m].setChecked(true);
		                tableColumn0.setImage(MapperLabelsProvider.getImage("checked"));
		                table.selectAll();
		            }
		        } //end of else
		        
		        tableViewer.refresh();
		        table.redraw();
		    } //end of handleEvent function
		});
		
		table.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event event) {
                    tableViewer.refresh();
                    table.redraw();
            }
        });
		
		//This has been added to act upon double mouse-click on a specific row.
		table.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				
				//if(!getOldexpression().equalsIgnoreCase(getNewExpression())) {
				if(hasChanged()){
					int style = 452; //Message Box Style code for SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL 
					MessageBox mb = new MessageBox(table.getShell(), style);
					mb.setText("Alert");
					mb.setMessage("You will loose your current changes? Do you wish to continue?");
					int val = mb.open();
					switch (val)  {   // val contains the constant of the selected button
						case SWT.CANCEL:
							break;
						case SWT.YES:
							int selectionIndex = table.getSelectionIndex();
							objRecord = mapperRecList.getRecord(selectionIndex);
							cmbVariableName.setText(objRecord.getOpVariable());
							txtExpression.setText(objRecord.getExpression());
							txtExpression.setFocus();
							btnSaveExpression.setEnabled(true);
							setOldexpression(txtExpression.getText());
							
							break;
						case SWT.NO:
							table.setSelection(getPreviousSelectedIndex());
							break;
					 }
				} else {
					int selectionIndex = table.getSelectionIndex();
					if(!tableViewer.getTable().getItem(selectionIndex).getChecked()){
						objRecord = mapperRecList.getRecord(selectionIndex);
						cmbVariableName.setText(objRecord.getOpVariable());
						txtExpression.setText(objRecord.getExpression());
						txtExpression.setFocus();
						btnSaveExpression.setEnabled(true);
						
						setOldexpression(txtExpression.getText());
						setPreviousSelectedIndex(selectionIndex);
					}
				}
			}
		});
	}
	
	private boolean hasChanged(){
		
		boolean changed = false;
		try{
		if(objRecord != null){
			if(!objRecord.getOpVariable().equals(cmbVariableName.getText())){
				changed = true;
			}
			
			if(!objRecord.getExpression().equals(txtExpression.getText())){
				changed = true;
			}
		}
		}catch (Exception e){
			
		}
		return changed;
		
	}
	/**
	 * Add the Buttons
	 * @param parent
	 */
	private void createButtons(Composite tblComposite) {
		
		Button delete = new Button(tblComposite, SWT.PUSH | SWT.CENTER);
		delete.setText("Delete");
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		delete.setLayoutData(gridData);
		delete.addSelectionListener(new SelectionAdapter() {
			//Remove all the records that are checked and refresh the view
			public void widgetSelected(SelectionEvent e) {
				List<Integer> arlCheckedIndexes = new ArrayList<Integer>();
				for (int i = 0; i < tableViewer.getTable().getItemCount(); i++) {
					if (tableViewer.getTable().getItems()[i].getChecked()) {
						arlCheckedIndexes.add(i);
					}
				}
						
				Collections.sort(arlCheckedIndexes);
						
				Integer[] arrSortedIndexes = arlCheckedIndexes.toArray(new Integer[arlCheckedIndexes.size()]);
				for (int j = arrSortedIndexes.length - 1 ; j>=0; j--) {
					mapperRecList.removeRecord(arrSortedIndexes[j]);
				}
				if(arlCheckedIndexes.size() > 0 && !layoutStyle.equalsIgnoreCase("transform")){
					btnAddExpression.setEnabled(true);
				}
				tableViewer.refresh();
				tableViewer.getTable().getColumns()[0].setImage(MapperLabelsProvider.getImage("unchecked"));
			}
		});
		
	}
	
	/**
	 * Uncheck's all the records of the table
	 * @return result
	 */
	private boolean uncheckAll(){
		boolean result = false;
		for (int i = 0; i < tableViewer.getTable().getItemCount(); i++) {
			tableViewer.getTable().getItems()[i].setChecked(false);
		}
		btnSaveExpression.setEnabled(false);
		result = true;
		
		return result;
	}
	private ToolTip inputTip ;
	
	
	
	public void buildExpressionPanel(Composite parentComp, Map<String, String[]> mapDataSets, List<JobEntryCopy> jobs){
		
		Composite comp2 = new Composite(parentComp, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 400;
		comp2.setLayout(layout);
		comp2.setLayoutData(data);
		
		Group group1 = new Group(comp2, SWT.SHADOW_IN);
	    
	    layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;
		data = new GridData(GridData.FILL_BOTH);
		group1.setLayout(layout);
		group1.setLayoutData(data);
		
		Composite compVariable = new Composite(group1, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
		data = new GridData();
		data.horizontalSpan = 2;
		compVariable.setLayout(layout);
		compVariable.setLayoutData(data);
		
		Label lblVariableName = null;
		
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		if(this.layoutStyle.equalsIgnoreCase("transform")){
			lblVariableName = new Label(compVariable, SWT.NONE);
			lblVariableName.setText("Output Variable Name:");
			group1.setText("ECL Expression Builder");
			
			lblVariableName.setLayoutData(gridData);
			
			cmbVariableName = new Combo(compVariable, SWT.DROP_DOWN);
			gridData = new GridData (GridData.FILL_HORIZONTAL);
			gridData.widthHint = 280;
			cmbVariableName.setLayoutData(gridData);
			
			cmbVariableName.setItems(getCmbListValues()); //Set the Combo Values
			if(!this.layoutStyle.equalsIgnoreCase("transform")){
				cmbVariableName.setVisible(false);
			}
		}else{
			//lblVariableName.setText("Column To Filter On:");
			group1.setText("Rule Builder");
			//lblVariableName.setVisible(false);
		}
		
		
		/*txtVariableName = new Text(compVariable, SWT.SINGLE | SWT.BORDER );
		gridData = new GridData (GridData.FILL_HORIZONTAL);
		gridData.widthHint = 280;
		txtVariableName.setLayoutData(gridData);*/
		
		
		
		Composite compTreePanel = new Composite(group1, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 4;
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		compTreePanel.setLayout(layout);
		compTreePanel.setLayoutData(data);
		
		Label lblInput = new Label(compTreePanel, SWT.NONE);
		if(this.layoutStyle.equalsIgnoreCase("transform")){
			lblInput.setText("Input:");
		}else{
			lblInput.setText("Columns:");
		}
		
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblInput.setLayoutData(gridData);
		
		Label lblOperators = new Label(compTreePanel, SWT.NONE);
		lblOperators.setText("Operators:");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblOperators.setLayoutData(gridData);
		
		Label lblText = new Label(compTreePanel, SWT.NONE);
		lblText.setText("Text:");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblText.setLayoutData(gridData);
		
		Label lblBoolean = new Label(compTreePanel, SWT.NONE);
		lblBoolean.setText("Boolean:");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblBoolean.setLayoutData(gridData);

		treeInputDataSet = new Tree(compTreePanel, SWT.SINGLE | SWT.BORDER);
		
		gridData = new GridData(GridData.FILL_HORIZONTAL);
	    gridData.heightHint = 100;
	    gridData.widthHint = 120;
	    treeInputDataSet.setLayoutData(gridData);
	    boolean includeInput = false;
	    if(this.layoutStyle.equalsIgnoreCase("transform")){
	    	includeInput = true;
	    }
	    Utils.fillTree(treeInputDataSet, mapDataSets, includeInput); //Get the values from the HashMap passed as an argument
	    treeInputDataSet.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				//Do Nothing
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				//Do Nothing
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Tree selectedTree = (Tree)e.widget;
				if(selectedTree.getSelection()[0].getParentItem() != null){
					StringBuffer dataField = new StringBuffer(" ( ");
					dataField.append(((Tree)e.widget).getSelection()[0].getText());
					if(txtExpression.getCaretPosition() > 0) {
						StringBuffer buf = new StringBuffer(txtExpression.getText());
						buf.insert(txtExpression.getCaretPosition(), dataField);
						txtExpression.setText(buf.toString());
					} else {
						StringBuffer buf = new StringBuffer(txtExpression.getText());
						buf.append(dataField);
						txtExpression.setText(buf.toString());
					}
				}
			}
		});
	    
	    
	    int style = SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
	    final Table tblOperators = new Table(compTreePanel, style);
		gridData = new GridData();
		gridData.heightHint = 100;
		gridData.widthHint = 100;
		tblOperators.setLayoutData(gridData);
		
		for (int i = getOperatorList().length -1 ; i >= 0 ; i--) {
			TableItem item = new TableItem(tblOperators, SWT.NONE, 0);
			item.setText(getOperatorList()[i]);
		}
		
		tblOperators.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				//Do Nothing
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				//Do Nothing
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				String dataField = ((Table)e.widget).getSelection()[0].getText();
				if(txtExpression.getCaretPosition() > 0) {
					StringBuffer buf = new StringBuffer(txtExpression.getText());
					buf.insert(txtExpression.getCaretPosition(), dataField);
					txtExpression.setText(buf.toString());
				} else {
					StringBuffer buf = new StringBuffer(txtExpression.getText());
					buf.append(dataField);
					txtExpression.setText(buf.toString());
				}
			}
		});
		
		txtValue = new Text(compTreePanel, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		gridData = new GridData (GridData.FILL_BOTH);
		gridData.widthHint = 100;
		gridData.heightHint = 100;
		txtValue.setLayoutData(gridData);
		txtValue.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				String dataField = txtValue.getText();
				if(txtExpression.getCaretPosition() > 0) {
					StringBuffer buf = new StringBuffer(txtExpression.getText());
					buf.insert(txtExpression.getCaretPosition(), dataField);
					txtExpression.setText(buf.toString());
				} else {
					StringBuffer buf = new StringBuffer(txtExpression.getText());
					buf.append(dataField);
					buf.append(" ) ");
					txtExpression.setText(buf.toString());
				}
			}
		});
		
		 int booleanStyle = SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
		    final Table tblBoolean = new Table(compTreePanel, booleanStyle);
			gridData = new GridData();
			gridData.heightHint = 100;
			gridData.widthHint = 100;
			tblBoolean.setLayoutData(gridData);
			
			for (int i = getBooleanList().length -1 ; i >= 0 ; i--) {
				TableItem item = new TableItem(tblBoolean, SWT.NONE, 0);
				item.setText(getBooleanList()[i]);
			}
			
			tblBoolean.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent arg0) {
					//Do Nothing
				}
				
				@Override
				public void mouseDown(MouseEvent arg0) {
					//Do Nothing
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					String dataField = ((Table)e.widget).getSelection()[0].getText();
					if(txtExpression.getCaretPosition() > 0) {
						StringBuffer buf = new StringBuffer(txtExpression.getText());
						buf.insert(txtExpression.getCaretPosition(), dataField);
						txtExpression.setText(buf.toString());
					} else {
						StringBuffer buf = new StringBuffer(txtExpression.getText());
						buf.append(dataField);
						txtExpression.setText(buf.toString());
					}
				}
			});
			
		
		Label lblEclText = new Label(group1, SWT.NONE);
		if(this.layoutStyle.equalsIgnoreCase("transform")){
			lblEclText.setText("ECL Text:");
		}else{
			lblEclText.setText("Rule Statement:");
		}
		
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblEclText.setLayoutData(gridData);
		
		txtExpression = new Text(group1, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		gridData = new GridData (GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		txtExpression.setLayoutData(gridData);
		txtExpression.setText(filterStatement);
		
		Label lblRules = new Label(group1, SWT.NONE);
		lblRules.setText("Rules:");
		
		final AutoPopulate ap = new AutoPopulate();
		
		int ruleStyle = SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
	    final Table ruleTable = new Table(group1, ruleStyle);
	    
		GridData gridData1 = new GridData();
		gridData1.heightHint = 120;
		gridData1.widthHint = 400;
		ruleTable.setLayoutData(gridData1);
		
		String outlRules[] = null;
		String outlierRules[] = null;
		
		try{
	        	outlRules = ap.parseOutlierRules(jobs);
	        }catch (Exception e){
	            System.out.println("Error Parsing existing outlier rules");
	            System.out.println(e.toString());
	            outlRules = new String[]{""};
	    }
		
		String rul = "";
		for(int i=0; i<outlRules.length; i++){
			rul = outlRules[i];
		}
		outlierRules = rul.split("\\|");
		if(outlierRules != null && outlierRules.length > 0){
			for (int i = (outlierRules.length-1) ; i >= 0 ; i--) {
				TableItem item = new TableItem(ruleTable, SWT.NONE, 0);
				if(!outlierRules[i].isEmpty()){
					item.setText(outlierRules[i]);
				}
			}
		}
		
		save = new Button(group1, SWT.PUSH);
		save.setText("Save");
		save.addSelectionListener(new SelectionAdapter() {  
			
       	 @Override
       	  public void widgetSelected(SelectionEvent e) {
       		rulesList.add(txtExpression.getText());
      		setRulesList(rulesList);
      		
      		TableItem item = new TableItem(ruleTable, SWT.NONE, 0);
      		item.setText(txtExpression.getText());
      		
    		/* if(rulesList != null && rulesList.size() > 0){
    			for (int i = rulesList.size()-1 ; i >= 0 ; i--) {
    				TableItem item = new TableItem(ruleTable, SWT.NONE, 0);
    				if(!(rulesList.get(i).toString().isEmpty()) && (!((rulesList.get(i)).equals(item.getText())))){
    					item.setText("Rule"+(i+1)+": "+rulesList.get(i));
    				}
    			}
    		}*/
    	   txtExpression.setText(" ");
       	 }
       });
		
		clearAll = new Button(group1, SWT.PUSH);
		clearAll.setText("ClearAll");
		clearAll.addSelectionListener(new SelectionAdapter() {
       	 @Override
       	  public void widgetSelected(SelectionEvent e) {
       		rulesList = getRulesList();
       		 if (rulesList != null){
       			 rulesList.clear();
       			 setRulesList(rulesList);
       		 }
       		ruleTable.clearAll();
       	 }    
       });
		
		
		Composite compButton = new Composite(group1, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 3;
		data = new GridData();
		compButton.setLayout(layout);  
		compButton.setLayoutData(data);
		
		
		btnSaveExpression = new Button(compButton, SWT.PUSH | SWT.CENTER);
		btnSaveExpression.setText("Save Expression");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 120;
		btnSaveExpression.setLayoutData(gridData);
		btnSaveExpression.addSelectionListener(new SelectionAdapter() {
	   		// Add a record and refresh the view
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = 0;
				if(tableViewer.getTable().getItemCount() >0 ){
					for (int i = 0; i < tableViewer.getTable().getItemCount(); i++) {
						if (tableViewer.getTable().getItems()[i].getChecked()) {
							selectionIndex = i;
						}
					}
					
					selectionIndex = tableViewer.getTable().getSelectionIndex();
					objRecord = mapperRecList.getRecord(selectionIndex);
					objRecord.setOpVariable(cmbVariableName.getText());
					objRecord.setExpression(txtExpression.getText());
					mapperRecList.removeRecord(selectionIndex);
					mapperRecList.addRecordAtIndex(selectionIndex, objRecord);
					cmbVariableName.setText("");
					txtExpression.setText("");
					uncheckAll();
					setOldexpression("");
					setNewExpression("");
					tableViewer.refresh();
					tableViewer.getTable().redraw();
					
					
				}
			}
		});
		
		btnSaveExpression.setEnabled(false);
		
		btnAddExpression = new Button(compButton, SWT.PUSH | SWT.CENTER);
		btnAddExpression.setText("Add Expression");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 120;
		btnAddExpression.setLayoutData(gridData);
		btnAddExpression.addSelectionListener(new SelectionAdapter() {
	   		// Add a record and refresh the view
			public void widgetSelected(SelectionEvent e) {
				/*if( (txtVariableName.getText()!= null && txtVariableName.getText().trim().length() > 0 ) 
						|| (txtExpression.getText()!= null && txtExpression.getText().trim().length() > 0 ) ) {
					MapperBO record = new MapperBO();
					record.setOpVariable(txtVariableName.getText());
					record.setExpression(txtExpression.getText());
					txtVariableName.setText("");
					txtExpression.setText("");
					mapperRecList.addRecord(record);
					uncheckAll();
					tableViewer.refresh();
				}*/
				
				if( (cmbVariableName.getText()!= null && cmbVariableName.getText().trim().length() > 0 ) 
						|| (txtExpression.getText()!= null && txtExpression.getText().trim().length() > 0 ) ) {
					MapperBO record = new MapperBO();
					record.setOpVariable(cmbVariableName.getText());
					record.setExpression(txtExpression.getText());
					cmbVariableName.setText("");
					txtExpression.setText("");
					mapperRecList.addRecord(record);
					uncheckAll();
					setOldexpression("");
					setNewExpression("");
					tableViewer.refresh();
					
					if(!layoutStyle.equalsIgnoreCase("transform")){
						btnAddExpression.setEnabled(false);
					}
					
				}
			}
		});
		
		//Code to Add Cancel Button
		Button btnCancelExpression = new Button(compButton, SWT.PUSH | SWT.CENTER);
		btnCancelExpression.setText("Cancel");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		btnCancelExpression.setLayoutData(gridData);
		btnCancelExpression.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				
				//To check if the expression is modified
				setNewExpression(txtExpression.getText());
				if(hasChanged()){
				//if(!getOldexpression().equalsIgnoreCase(getNewExpression())) {
					//System.out.println("Old Expression: "+getOldexpression());
					//System.out.println("New Expression: "+getNewExpression());
					int style = 452; //Message Box Style code for SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL 
					MessageBox mb = new MessageBox(tableViewer.getTable().getShell(), style);
					mb.setText("Alert");
					mb.setMessage("You will loose your current changes? Do you wish to continue?");
					int val = mb.open();
					switch (val)  {   // val contains the constant of the selected button
						case SWT.CANCEL:
							break;
						case SWT.YES:
							objRecord = null;
							cmbVariableName.setText("");
							txtExpression.setText("");
							setOldexpression("");
							setNewExpression("");
							btnSaveExpression.setEnabled(false);
							break;
						case SWT.NO:
							break;
					 }
				} else {
					objRecord = null;
					cmbVariableName.setText("");
					txtExpression.setText("");
					btnSaveExpression.setEnabled(false);
					setOldexpression("");
					setNewExpression("");
				}
			}
			
		});
		
		
		if(!this.layoutStyle.equalsIgnoreCase("transform")){
			btnCancelExpression.setVisible(false);
			btnAddExpression.setVisible(false);
			btnSaveExpression.setVisible(false);
		}
	}
}
