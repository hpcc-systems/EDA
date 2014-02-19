package org.hpccsystems.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class MainMapper {
	
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
	private String[] cmbListValues = null;
	private Tree treeInputDataSet = null;
	
	
	public Tree getTreeInputDataSet() {
		return treeInputDataSet;
	}


	public void setTreeInputDataSet(Tree treeInputDataSet) {
		this.treeInputDataSet = treeInputDataSet;
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
		tableViewer.setInput(mapperRecList);
		tableViewer.getTable().setRedraw(true);
               
	}
	
	
	//The Constructor has input as 
	public MainMapper(Composite parentComp, Map<String, String[]> mapDataSets, String[] arrCmbValues){
		setCmbListValues(arrCmbValues);
		populateFunctionList();
		populateOperatorList();
		this.addChildControls(parentComp, mapDataSets);
	}
	
	/**
	 * Create a new shell, add the widgets, open the shell
	 * @return the shell that was created	 
	 */
	private void addChildControls(Composite parentComp, Map<String, String[]> mapDataSets) {
		Composite tblComposite = new Composite(parentComp, SWT.NONE);
		createTable(tblComposite);		// Create the table
		createButtons(tblComposite);
		buildExpressionPanel(parentComp, mapDataSets);	// Add the widgets needed to build a Expression Panel
		
	}
	
	private void populateFunctionList() {
		String[] fuctionList = {"ABS", "ACOS", "AGGREGATE", "ALLNODES", "APPLY"};
		setFunctionList(fuctionList);
	}
	
	private void populateOperatorList() {
		//String[] operatorList = {"+", "-", "*", "/", "%", "||", "(", ")", "=", "<>", ">", "<", "<=", ">="};
		String operatorList[] = {":=","+", "-", "*", "/", "%", "||", "(", ")", "=", "<>", ">", "<", "<=", ">=","~","AND","IN","NOT","OR","XOR"};
		setOperatorList(operatorList);
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
		tableColumn0.setText("Variable");
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
	}
	
	/**
	 * Add the Buttons
	 * @param parent
	 */
	private void createButtons(Composite tblComposite) {
		 // Create and configure the "Add" button
		Button edit = new Button(tblComposite, SWT.PUSH | SWT.CENTER);
		edit.setText("Edit");
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		edit.setLayoutData(gridData);
		edit.addSelectionListener(new SelectionAdapter() {
       		// Add a record and refresh the view
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = 0;
				if(tableViewer.getTable().getItemCount() >0 ){
					for (int i = 0; i < tableViewer.getTable().getItemCount(); i++) {
						if (tableViewer.getTable().getItems()[i].getChecked()) {
							selectionIndex = i;
						}
					}
					if(tableViewer.getTable().getItem(selectionIndex).getChecked()){
						MapperBO objRecord = mapperRecList.getRecord(selectionIndex);
						//txtVariableName.setText(objRecord.getOpVariable());
						cmbVariableName.setText(objRecord.getOpVariable());
						txtExpression.setText(objRecord.getExpression());
						txtExpression.setFocus();
						btnSaveExpression.setEnabled(true);
					}
				}
			}
		});
		
		Button delete = new Button(tblComposite, SWT.PUSH | SWT.CENTER);
		delete.setText("Delete");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
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
	public void buildExpressionPanel(Composite parentComp, Map<String, String[]> mapDataSets){
		
		Composite comp2 = new Composite(parentComp, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 400;
		comp2.setLayout(layout);
		comp2.setLayoutData(data);
		
		Group group1 = new Group(comp2, SWT.SHADOW_IN);
	    group1.setText("ECL Expression Builder");
	    layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;
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
		
		Label lblVariableName = new Label(compVariable, SWT.NONE);
		lblVariableName.setText("Variable Name:");
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblVariableName.setLayoutData(gridData);
		
		/*txtVariableName = new Text(compVariable, SWT.SINGLE | SWT.BORDER );
		gridData = new GridData (GridData.FILL_HORIZONTAL);
		gridData.widthHint = 280;
		txtVariableName.setLayoutData(gridData);*/
		
		cmbVariableName = new Combo(compVariable, SWT.DROP_DOWN);
		gridData = new GridData (GridData.FILL_HORIZONTAL);
		gridData.widthHint = 280;
		cmbVariableName.setLayoutData(gridData);
		
		cmbVariableName.setItems(getCmbListValues()); //Set the Combo Values
		
		Composite compTreePanel = new Composite(group1, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 3;
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		compTreePanel.setLayout(layout);
		compTreePanel.setLayoutData(data);
		
		Label lblInput = new Label(compTreePanel, SWT.NONE);
		lblInput.setText("Input:");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblInput.setLayoutData(gridData);
		
		Label lblFunctions = new Label(compTreePanel, SWT.NONE);
		lblFunctions.setText("Functions:");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblFunctions.setLayoutData(gridData);
		
		Label lblOperators = new Label(compTreePanel, SWT.NONE);
		lblOperators.setText("Operators:");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblOperators.setLayoutData(gridData);
		
		
		
		
		treeInputDataSet = new Tree(compTreePanel, SWT.SINGLE | SWT.BORDER);
		
		
		/*inputTip = new ToolTip(compTreePanel.getShell(), SWT.BALLOON | SWT.ICON_INFORMATION);		
	    Listener inputList = new Listener(){

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				switch (event.type) {
				case SWT.MouseMove: {
					inputTip.setVisible(false);
				}
					case SWT.MouseHover: {
						Point coords = new Point(event.x, event.y);
						TreeItem item = treeInputDataSet.getItem(coords);
						
						
						if(item != null){
							//System.out.println("Show tool tip " + coords + " " + item.getText());
							inputTip.setText("Info");
							inputTip.setMessage(item.getText());
							inputTip.setVisible(true);
						}else{
							//System.out.println("hide tool tip");
							inputTip.setVisible(false);
						}
					}
				}
			}};
			
			treeInputDataSet.addListener(SWT.MouseHover, inputList);
			treeInputDataSet.addListener(SWT.MouseMove, inputList);
		*/
		//treeInputDataSet.setToolTipText("Select the column from the input dataset");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
	    gridData.heightHint = 100;
	    treeInputDataSet.setLayoutData(gridData);
	    Utils.fillTree(treeInputDataSet, mapDataSets); //Get the values from the HashMap passed as an argument
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
					String dataField = ((Tree)e.widget).getSelection()[0].getText();
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
		
	    final Tree treeFunctions = new Tree(compTreePanel, SWT.SINGLE | SWT.BORDER);
	    gridData = new GridData(GridData.FILL_HORIZONTAL);
	    gridData.heightHint = 100;
	    treeFunctions.setLayoutData(gridData);
	    Map<String, List<String>> mapFunctionValues = Utils.getFunctionValueMap();
	    Utils.fillTreeForFunctions(treeFunctions, mapFunctionValues);
	    treeFunctions.addMouseListener(new MouseListener() {
			
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
					String dataField = ((Tree)e.widget).getSelection()[0].getText();
					//System.out.println("Text Position:: "+txtExpression.getCaretPosition());
					if(txtExpression.getSelectionText() != null && txtExpression.getSelectionText().length() > 0) {
						String txtToBeReplaced = dataField+"( "+txtExpression.getSelectionText()+" )";
						StringBuffer buf = new StringBuffer(txtExpression.getText());
						buf.replace(txtExpression.getSelection().x, txtExpression.getSelection().y, txtToBeReplaced);
						txtExpression.setText(buf.toString());
					} else if(txtExpression.getCaretPosition() > 0) {
						StringBuffer buf = new StringBuffer(txtExpression.getText());
						buf.insert(txtExpression.getCaretPosition(), dataField+"()");
						txtExpression.setText(buf.toString());
					} else {
						StringBuffer buf = new StringBuffer(txtExpression.getText());
						buf.append(dataField+"()");
						txtExpression.setText(buf.toString());
					}
				}
			}
		});
	    
	    final ToolTip functionsTip = new ToolTip(compTreePanel.getShell(), SWT.BALLOON | SWT.ICON_INFORMATION);
	    
	    Listener functionsList = new Listener(){

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				
				switch (event.type) {
				case SWT.MouseExit:
				case SWT.MouseMove: {
					functionsTip.setVisible(false);
					break;
				}
				
					case SWT.MouseHover: {
						Point coords = new Point(event.x, event.y);
						TreeItem item = treeFunctions.getItem(coords);
						Point tipLocation = treeFunctions.getLocation();
						
						if(item != null){
							String help = "";
							
							if(Utils.getHelpMap().containsKey(item.getText())){
								help += Utils.getHelpMap().get(item.getText());
							}
							//System.out.println("Show tool tip " + coords + " " + item.getText());
							functionsTip.setText(item.getText());
							
							functionsTip.setMessage(help);
							//functionsTip.setLocation(tipLocation.x,tipLocation.y);
							functionsTip.setVisible(true);
						}else{
							//System.out.println("hide tool tip");
							functionsTip.setVisible(false);
						}
						break;
					}
				}
			}};
			
			treeFunctions.addListener(SWT.MouseHover, functionsList);
			treeFunctions.addListener(SWT.MouseMove, functionsList);
			treeFunctions.addListener(SWT.MouseExit, functionsList);
	    
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
		
		Label lblEclText = new Label(group1, SWT.NONE);
		lblEclText.setText("ECL Text:");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblEclText.setLayoutData(gridData);
		
		txtExpression = new Text(group1, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		gridData = new GridData (GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		txtExpression.setLayoutData(gridData);
		
		
		Composite compButton = new Composite(group1, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
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
					if(tableViewer.getTable().getItem(selectionIndex).getChecked()){
						MapperBO objRecord = mapperRecList.getRecord(selectionIndex);
						//objRecord.setOpVariable(txtVariableName.getText());
						objRecord.setOpVariable(cmbVariableName.getText());
						objRecord.setExpression(txtExpression.getText());
						mapperRecList.removeRecord(selectionIndex);
						mapperRecList.addRecordAtIndex(selectionIndex, objRecord);
						//txtVariableName.setText("");
						
						cmbVariableName.setText("");
						txtExpression.setText("");
						uncheckAll();
						tableViewer.refresh();
						tableViewer.getTable().redraw();
					}
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
					tableViewer.refresh();
				}
			}
		});
		
		/*Composite comp3 = new Composite(parentComp, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = true;
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = 40;
		comp3.setLayout(layout);
		comp3.setLayoutData(data);

		Button btnOk = new Button(comp3, SWT.PUSH | SWT.CENTER);
		btnOk.setText("Ok");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_END);
		gridData.widthHint = 80;
		btnOk.setLayoutData(gridData);
		btnOk.addSelectionListener(new SelectionAdapter() {
	   		// Add a record and refresh the view
			public void widgetSelected(SelectionEvent e) {
				//mapperRecList.addRecord(table.getSelectionIndex());
			}
		});
		
		Button btnClose = new Button(comp3, SWT.PUSH | SWT.CENTER);
		btnClose.setText("Cancel");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		btnClose.setLayoutData(gridData);*/
		
	}
	
}



