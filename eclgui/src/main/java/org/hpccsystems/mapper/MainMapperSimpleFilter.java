package org.hpccsystems.mapper;

import java.awt.Color;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
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
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.hpccsystems.mapper.filter.PersonCellModifierForFilter;
import org.hpccsystems.mapper.filter.PersonContentProviderForFilter;
import org.hpccsystems.mapper.filter.PersonForFilter;
import org.hpccsystems.mapper.filter.PersonLabelProviderForFilter;
import org.hpccsystems.recordlayout.IRecordListViewer;
import org.hpccsystems.recordlayout.RecordBO;
import org.hpccsystems.recordlayout.RecordLabels;
import org.hpccsystems.recordlayout.RecordList;

public class MainMapperSimpleFilter {
	public static final String COLUMNS = "Columns";
	public static final String OPERATORS = "Operators";
	public static final String VALUE = "Value";
	public static final String BOOLEAN_OPERATORS = "Boolean_Operators";
  
	public static final String[] PROP = { COLUMNS, OPERATORS, VALUE, BOOLEAN_OPERATORS};
	
    List people = new ArrayList();	
	public List getPeople() {
		return people;
	}

	public void setPeople(List people) {
		this.people = people;
	}
	private String layoutStyle = "transform";
	// The table viewer
	private TableViewer tableViewer;
	// Create a RecordList and assign it to an instance variable
	private MapperRecordList mapperRecList = new MapperRecordList();	
	//private Text txtVariableName;
	private Combo cmbVariableName;
	private Text txtExpression;
	
	//Start 
	
	public static TableViewer tv;
	
	public  static String[] columnList = new String[]{};
	public static String[] booleanOperatorsList = new String[]{" ","AND","IN","NOT","OR","XOR"};
	public static String[] operatorsList = new String[]{" ",":=","+", "-", "*", "/", "%", "||", "(", ")", "=", "<>", ">", "<", "<=", ">=","~"};
	
	// Set the table column property names
	
	private RecordList recordList = new RecordList();
	
	//End
	
	private Button btnSaveExpression;
	private Button btnAddExpression;
	
	private String[] functionList = null;
	private String[] operatorList = null;
	private String[] cmbListValues = null;
	private Tree treeInputDataSet = null;
	
	
	//Fields to check for EDIT status
	private String oldexpression = "";
	private String newExpression = "";
	private int previousSelectedIndex;
	
	private String filterStatement = "";
	private String oldFilterStatement = "";
	
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
	
	public void setRecordList(RecordList rl) {
		this.recordList = rl;
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
	
	public MainMapperSimpleFilter(Composite parentComp, Map<String, String[]> mapDataSets, String[] arrCmbValues){
		setupVars(parentComp, mapDataSets, arrCmbValues, "transform", null);
	}
	public MainMapperSimpleFilter(Composite parentComp, Map<String, String[]> mapDataSets, String[] arrCmbValues,String layoutStyle, List peopleLs){
		setupVars(parentComp, mapDataSets, arrCmbValues, layoutStyle, peopleLs);
	}
	
	private void setupVars(Composite parentComp, Map<String, String[]> mapDataSets, String[] arrCmbValues,String layoutStyle, List peopleLs){
		this.layoutStyle = layoutStyle;
		setCmbListValues(arrCmbValues);
		populateFunctionList();
		populateOperatorList();
		this.addChildControls(parentComp, mapDataSets, peopleLs);
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
	private void addChildControls(Composite parentComp, Map<String, String[]> mapDataSets, List peopleLs) {
		if(this.layoutStyle.equalsIgnoreCase("transform")){
			Composite tblComposite = new Composite(parentComp, SWT.NONE);
			createTable(tblComposite);		// Create the table
			createButtons(tblComposite);
		}
		buildExpressionPanel(parentComp, mapDataSets, peopleLs);	// Add the widgets needed to build a Expression Panel
		
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
		 // Create and configure the "Edit" button
		/*Button edit = new Button(tblComposite, SWT.PUSH | SWT.CENTER);
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
		});*/
		
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
	
	
	
	public void buildExpressionPanel(Composite parentComp, final Map<String, String[]> mapDataSets, List peopleLs){
		
		final Composite comp2 = new Composite(parentComp, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 350;
		comp2.setLayout(layout);
		comp2.setLayoutData(data);
		
		Group group1 = new Group(comp2, SWT.SHADOW_IN);
	    
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
			group1.setText("Filter Builder");
			//lblVariableName.setVisible(false);
		}
		
		
		/*txtVariableName = new Text(compVariable, SWT.SINGLE | SWT.BORDER );
		gridData = new GridData (GridData.FILL_HORIZONTAL);
		gridData.widthHint = 280;
		txtVariableName.setLayoutData(gridData);*/
		
		
		
		Composite compTreePanel = new Composite(group1, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 3;
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		data.heightHint = 128;
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
		
		Label lblFunctions = new Label(compTreePanel, SWT.NONE);
		lblFunctions.setText("Functions:");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblFunctions.setLayoutData(gridData);
		
		Label lblOperators = new Label(compTreePanel, SWT.NONE);
		lblOperators.setText("Operators:");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		lblOperators.setLayoutData(gridData);
		
		treeInputDataSet = new Tree(compTreePanel, SWT.SINGLE | SWT.BORDER);
		
		gridData = new GridData(GridData.FILL_HORIZONTAL);
	    gridData.heightHint = 80;
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
	    gridData.heightHint = 80;
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
	    BuildHelpIndex bhi = new BuildHelpIndex();
	    
	    String indexFile = "plugins/hpcc-common/helpfiles/index.xml";
	    
	    URL url = null;
	 	try{
		 	URL baseURL = this.getClass().getResource("../../../");
		 	
		 	System.out.println("BaseURL: " + baseURL.getPath());
		 	if(baseURL != null){
		 		url = new URL(baseURL, indexFile);
		 	}else {
		 		url = this.getClass().getResource(indexFile);
		 	}
	 	}catch (MalformedURLException e){
	 		System.out.println("can't find helf files" + e.toString());
	 	}
	 	System.out.println("URL: " + url.toString());
	    
	    bhi.setUrl(url.toString().replace("%20", " ").replace("file:", ""));
	    final HashMap<String,String> helpIndex = bhi.getMap();
	   
	    Listener functionsList = new Listener(){

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				
				switch (event.type) {
				case SWT.MouseExit:
				case SWT.MouseMove: {
					functionsTip.setAutoHide(true);
					//functionsTip.setVisible(false);
					break;
				}
				
					case SWT.MouseHover: {
						
						break;
					}
					case SWT.KeyDown: {
						
						if(event.keyCode == SWT.F1){
							Point coords = new Point(event.x, event.y);
							//TreeItem item = treeFunctions.getItem(coords);
							TreeItem item = treeFunctions.getSelection()[0];
							Shell tip = new Shell(treeFunctions.getDisplay());
							System.out.println(event.keyCode);
							String help = "";
							String htmlFile = "plugins/hpcc-common/helpfiles/";
							System.out.println("Item: " + item.getText().replace("STD.", ""));
							if(helpIndex.containsKey(item.getText().replace("STD.", ""))){
								htmlFile += helpIndex.get(item.getText().replace("STD.", ""));
								//if(Utils.getHelpMap().containsKey(item.getText())){
								//	help += Utils.getHelpMap().get(item.getText());
								//}
							}else{
								htmlFile += "html/index.html";
							}
							Browser browser;
							try {
								 	browser = new Browser(tip, SWT.BALLOON);
								 	browser.setBounds(0, 0, 800, 800);
								 	//browser.setText(help);
								 	//ClassLoader classLoader = getClass().getClassLoader();
								 	//System.out.println("PATHbase: " + classLoader.getResource(".").getFile());
								 	
								 	
								 	URL url = null;
								 	try{
									 	URL baseURL = this.getClass().getResource("../../../");
									 	
									 	System.out.println("BaseURL: " + baseURL.getPath());
									 	if(baseURL != null){
									 		url = new URL(baseURL, htmlFile);
									 	}else {
									 		url = this.getClass().getResource(htmlFile);
									 	}
								 	}catch (MalformedURLException e){
								 		System.out.println("can't find helf files" + e.toString());
								 	}
								 	System.out.println("URL: " + url.toString());
								 	browser.setUrl(url.toString());
								 	
								 	//file:/C:/Program Files/data-integration/plugins/jobentries/eclproject/../../hpcc-common/eclgui.jar!/org/hpccsystems/mapper/html/bk01apa.html
							} catch (SWTError e) {
							 	System.out.println("Could not instantiate Browser: " + e.getMessage());
							 	
						 	}
							
							
							tip.pack ();
							tip.open();
							
						}
						break;
					}
				}
			}};
			treeFunctions.addListener(SWT.KeyDown, functionsList);
			//treeFunctions.addListener(SWT.MouseHover, functionsList);
			//treeFunctions.addListener(SWT.MouseMove, functionsList);
			//treeFunctions.addListener(SWT.MouseExit, functionsList);
	    
	    int style = SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
	    final Table tblOperators = new Table(compTreePanel, style);
		gridData = new GridData();
		gridData.heightHint = 80;
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
		
		
		if(group1.getText().equals("Filter Builder")){
			
			ScrolledComposite sc = new ScrolledComposite(comp2, SWT.H_SCROLL | SWT.V_SCROLL );
			Composite compVar = new Composite(sc, SWT.NONE); 
			compVar.setLayout(new GridLayout(4, false));
			sc.setContent(compVar);
			
			sc.setMinSize(620, 80);
			sc.setExpandHorizontal(true);
		    sc.setExpandVertical(true);
		    
		   
	        tv = new TableViewer(compVar, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION | SWT.CHECK);
	        tv.setContentProvider(new PersonContentProviderForFilter());
	        tv.setLabelProvider(new PersonLabelProviderForFilter());
	        tv.setInput(people);
	        
	        GridData gd = new GridData(GridData.FILL_BOTH);
	        gd.horizontalSpan = 2;
	        final Table table = tv.getTable();
	        table.setLayoutData(gd);
	        table.setLinesVisible(true);
	        table.setHeaderVisible(true);
	        
	        final TableColumn tc0 = new TableColumn(table, SWT.LEFT);
		    tc0.setText("Columns");
		    tc0.setWidth(160);
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
		    tc1.setWidth(150);
		    
		    TableColumn tc2 = new TableColumn(table, SWT.CENTER);
		    tc2.setText("Value");
		    tc2.setWidth(160);
		    
		    TableColumn tc3 = new TableColumn(table, SWT.CENTER);
		    tc3.setText("Boolean Operator");
		    tc3.setWidth(150);
		    
		    List<String> ls = new ArrayList<String>();
 			
 			Set setOfKeys = mapDataSets.keySet();
 			Iterator it = setOfKeys.iterator();
 			while(it.hasNext()){
 				String key = (String)it.next();
 				for(int i=0; i<mapDataSets.get(key).length; i++){
 					ls.add(mapDataSets.get(key)[i]);
 				}
 			}
 			
 			Set<String> lsToSet = new LinkedHashSet<String>(ls);
 			List<String> lsNoDup = new ArrayList<String>(lsToSet);
 			columnList = new String[lsNoDup.size()];
 			
 			for(int i=lsNoDup.size()-1; i>=0; i--){
 				columnList[i] = lsNoDup.get(i); 
 			} 
		    
		    
		    CellEditor[] editors = new CellEditor[4];
		    editors[0] = new ComboBoxCellEditor(table,columnList); 
		    editors[1] = new ComboBoxCellEditor(table,operatorsList);
		    editors[2] = new TextCellEditor(table);
		    editors[3] = new ComboBoxCellEditor(table,booleanOperatorsList);
		    
		    // Set the editors, cell modifier, and column properties
		    tv.setColumnProperties(PROP);
		    tv.setCellModifier(new PersonCellModifierForFilter(tv));
		    tv.setCellEditors(editors); 
		    
		    people = new ArrayList(peopleLs);
		    
		    Composite compVar0 = new Composite(comp2, SWT.NONE);
	    	layout = new GridLayout();
			layout.numColumns = 3;
			data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 1;
			compVar0.setLayout(layout);
			compVar0.setLayoutData(data); 
		    
		    Button add = new Button(compVar0, SWT.PUSH);
		    add.setText("Add Filter");
	        gd = new GridData(GridData.FILL);
	        gd.horizontalSpan = 1;
	        add.setLayoutData(gd);
	        
	        add.addSelectionListener(new SelectionListener (){ 
	        	@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
				}
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					
					tv.setInput(people);
					int cnt = table.getItemCount();
					PersonForFilter p = new PersonForFilter();
			        p.setColumns(Integer.valueOf("0"));		        
			        p.setOperators(Integer.valueOf("0"));
			        p.setValue("");
			        p.setBoolean_operators(Integer.valueOf("0"));
			        people.add(p);
			        tv.refresh();		        
				}
	        	
	        });
	         
	        Button del = new Button(compVar0, SWT.PUSH);
		    del.setText("Delete");
		    gd = new GridData(GridData.FILL);
	        gd.horizontalSpan = 1;
	        del.setLayoutData(gd);
		    del.addSelectionListener(new SelectionAdapter(){
		    	public void widgetSelected(SelectionEvent event){
		    		int cnt = 0;
		    		people = getPeople();
		    		if(people.size()>0){
			    		for(int i = 0; i<table.getItemCount(); i++){
			    			if(table.getItem(i).getChecked()){
			    				people.remove(Math.abs(cnt - i));
								cnt++;
							}
			    		}
		    		}
		    		if(tc0.getImage().equals(RecordLabels.getImage("checked"))){
		    			tc0.setImage(RecordLabels.getImage("unchecked")); 
		    		}
		    		tv.refresh();
		    		tv.setInput(people);
		    		
		    	}
		    });
	        
	        Button select = new Button(compVar0, SWT.PUSH);
	        select.setText("Select"); 
	        gd = new GridData(GridData.FILL);
	        gd.horizontalSpan = 1;
	        select.setLayoutData(gd);
	        
	        select.addSelectionListener(new SelectionListener () {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					for(int i = 0; i<table.getItemCount(); i++){
		    			if(table.getItem(i).getChecked()){
		    				StringBuffer dataField = new StringBuffer("");
		    				dataField.append("(");
		    				dataField.append(table.getItem(i).getText(0));
		    				dataField.append(table.getItem(i).getText(1));
		    				dataField.append(table.getItem(i).getText(2));
		    				dataField.append(")");
		    				dataField.append(table.getItem(i).getText(3));
		    				
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
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		}
		
		if(group1.getText().equals("Filter Builder")){
			Group group3 = new Group(comp2, SWT.SHADOW_IN);
		    
		    GridLayout layout2 = new GridLayout();
			layout2.numColumns = 2;
			layout2.makeColumnsEqualWidth = true;
			GridData data2 = new GridData(GridData.FILL_BOTH);
			group3.setLayout(layout2);
			group3.setLayoutData(data2);
			
			Label lblEclText = new Label(group3, SWT.NONE);
			lblEclText.setText("Filter Statement:");
			
			gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
			lblEclText.setLayoutData(gridData);
			
			txtExpression = new Text(group3, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			gridData = new GridData (GridData.FILL_BOTH);
			gridData.horizontalSpan = 6;
			gridData.widthHint = 620;
			txtExpression.setLayoutData(gridData);
			txtExpression.setText(filterStatement);
		}
		else
		{
			GridLayout layout2 = new GridLayout();
			layout2.numColumns = 2;
			layout2.makeColumnsEqualWidth = true;
			GridData data2 = new GridData(GridData.FILL_BOTH);
			group1.setLayout(layout2);
			group1.setLayoutData(data2);
			
			Label lblEclText = new Label(group1, SWT.NONE);
			lblEclText.setText("Filter Statement:");
			
			gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
			lblEclText.setLayoutData(gridData);
			
			txtExpression = new Text(group1, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			gridData = new GridData (GridData.FILL_BOTH);
			gridData.horizontalSpan = 6;
			gridData.widthHint = 680;
			txtExpression.setLayoutData(gridData);
			txtExpression.setText(filterStatement);
		}
		
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
					
					/*if(tableViewer.getTable().getItem(selectionIndex).getChecked()){
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
					}*/
					
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


