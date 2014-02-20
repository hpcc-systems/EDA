package org.hpccsystems.swing.recordlayout;

import java.awt.Robot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellNavigationStrategy;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyListener;

public class CreateTable {
    private Shell shell;
         
	private Table table;
	private TableViewer tableViewer;
	private TabFolder tabFolder;
	
	// Create a RecordList and assign it to an instance variable
	private RecordList recordList = new RecordList();
	
	private Button closeButton;
	
	// Set the table column property names
	public final static String NAME_COLUMN 			= "column_name";
	public final static String DEFAULT_VALUE 			= "column_default_value";
	public final static String TYPE_COLUMN 			= "column_type";
	public final static String WIDTH_COLUMN 			= "column_width";
	public final static String SORT_ORDER				= "column_sort_order";

	// Set column names
	private String[] columnNames = new String[] { NAME_COLUMN, DEFAULT_VALUE, TYPE_COLUMN, WIDTH_COLUMN };
	
	private RecordList parentFields = null;
	
	private boolean includeCopyParent = false;
	private boolean selectColumns = false;

	
	public boolean isSelectColumns() {
		return selectColumns;
	}
	public void setSelectColumns(boolean selectColumns) {
		this.selectColumns = selectColumns;
	}
	public void setParentLayout(RecordList fields){
		parentFields = fields;
	}
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	public String[] getColumnNamesArr() {
		return columnNames;
	}

	public boolean isIncludeCopyParent() {
		return includeCopyParent;
	}
	public void setIncludeCopyParent(boolean includeCopyParent) {
		this.includeCopyParent = includeCopyParent;
	}
	public TabItem buildDefTab(String tabName, TabFolder tabFolder) {
		this.tabFolder = tabFolder;
		TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
		tabItem.setText(tabName);
		
		/*Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);*/
		
		ScrolledComposite sc2 = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		Composite compForGrp2 = new Composite(sc2, SWT.NONE);
		sc2.setContent(compForGrp2);

		// Set the minimum size
		sc2.setMinSize(650, 450);

		// Expand both horizontally and vertically
		sc2.setExpandHorizontal(true);
		sc2.setExpandVertical(true);
        
		tabItem.setControl(sc2);
		
		this.addChildControls(compForGrp2);

		return tabItem;
	}

	public void setRecordList(RecordList rl) {
		this.recordList = rl;
		// tableViewer.setInput(recordList);
		// table.setRedraw( true );
	}

	public CreateTable(Shell shell) {
		this.shell = shell;
	}

	
	public void buildTest(Shell shell){
		
		tabFolder = new TabFolder (shell, SWT.NONE);
		
		TabItem item1 = new TabItem (tabFolder, SWT.NULL);
		item1.setText ("Tab Item 1");
		
		Group grp = new Group(tabFolder, SWT.NONE);
		grp.setText("Test Group");
		
		
		String[] ITEMS = { "Select", "Variable", "Fixed"};
		final Combo combo = new Combo(grp, SWT.DROP_DOWN);
		combo.setItems(ITEMS);
		combo.setBounds(10, 100, 100, 100);
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(combo.getText().equals("Variable")) {
					redrawTable(false);
				} else {
					redrawTable(true);
				}
			}
		});
		
		item1.setControl(grp);
		
		TabItem item2 = new TabItem (tabFolder, SWT.NULL);
		item2.setText ("Tab Item 2");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		item2.setControl(composite);
		
		this.addChildControls(composite);
	}
	
	/**
	 * The main method
	 * @param args
	 */
	public static void main(String[] args) {
		
		Shell shell = new Shell();
		shell.setText("HPCC Grid");

		// Set layout for shell
		shell.setLayout(new FillLayout());
		
		TabFolder tabFolder = new TabFolder (shell, SWT.FILL | SWT.RESIZE | SWT.MIN | SWT.MAX);
        FormData data = new FormData();
        
        data.height = 525;
        data.width = 650;
        tabFolder.setLayoutData(data);
     // Create a composite to hold the children
        final CreateTable createTableObjectDS = new CreateTable(shell);
 		
		
		TabItem item1 = createTableObjectDS.buildDefTab("DATASET EX", tabFolder);
		
		
     	final CreateTable createTableObject = new CreateTable(shell);
     		
        String[] cNames = new String[] { createTableObject.NAME_COLUMN,createTableObject.SORT_ORDER  };
        createTableObject.setColumnNames(cNames);
    	
        createTableObject.setSelectColumns(true);
        //includeCopyParent
		
		TabItem item2 = createTableObject.buildDefTab("SORT EX", tabFolder);
		
		//createTableObject.getControl().addDisposeListener(new DisposeListener() {
		//	public void widgetDisposed(DisposeEvent e) {
		//		createTableObject.dispose();			
		//	}
		//});
		
		// Ask the shell to display its content
		//shell.open();
		//createTableObject.run(shell);
		
		// shell.pack();
	        shell.open();
	        while (!shell.isDisposed()) {
	        	
	            if (!shell.getDisplay().readAndDispatch()) {
	          	shell.getDisplay().sleep();
	            }
	        }

	}
	
	/**
	 * This method redraws the table with 3 or columns depending upon the flag(status) value.
	 * @param status
	 */
	public void redrawTable(boolean status){
            
		//while ( table.getColumnCount() > 0 ) {
		//    table.getColumns()[0].dispose();
		//}
		
		
		//this.renderTable();
		RecordList oldRL = recordList;
		recordList = new RecordList();
                
                if(oldRL.getRecords() != null && oldRL.getRecords().size() > 0) {
                        System.out.println("Size: "+oldRL.getRecords().size());
                        for (Iterator<RecordBO> iterator = oldRL.getRecords().iterator(); iterator.hasNext();) {
                                RecordBO obj = (RecordBO) iterator.next();
                                recordList.addRecordBO(obj);

                        }
                }
                oldRL = null;
		tableViewer.setInput(recordList);
		//table.setRedraw(true);
               
                //tableViewer.refresh();
	}
	
	/**
	 * Run and wait for a close event
	 * @param shell Instance of Shell
	 */
	private void run(Shell shell) {
		
		// Add a listener for the close button
		closeButton.addSelectionListener(new SelectionAdapter() {
			// Close the view i.e. dispose of the composite's parent
			public void widgetSelected(SelectionEvent e) {
				table.getParent().getParent().getParent().dispose();
			}
		});
		
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
	
	/**
	 * Create a new shell, add the widgets, open the shell
	 * @return the shell that was created	 
	 */
	private void addChildControls(Composite composite) {

		// Create a composite to hold the children
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_BOTH);
		composite.setLayoutData (gridData);

		// Set numColumns to 6 for the buttons 
		GridLayout layout = new GridLayout(6, false);
		composite.setLayout (layout);
		
		createTable(composite);		// Create the table 
		createTableViewer();	// Create and setup the TableViewer
		tableViewer.setContentProvider(new ExampleContentProvider());	//Set the Content Provider for the table	
		tableViewer.setLabelProvider(new RecordLabels());	//Set the Label Provider for the table
		//recordList = new RecordList();
		tableViewer.setInput(recordList);	//Add an empty RecordList to the TableViewer

		createButtons(composite);	//	Add the buttons
		addCellNavigation();	//	Add Cell Navigation(Tab and Arrow keys for column navigation)
		addMenu(composite);		//	Add Right Click Menu options
	}
	
	/**
	 * This method is used to add Right Click Menu options
	 * @param composite
	 */
	private void addMenu(final Composite composite){
		Menu menu = new Menu(composite.getShell(), SWT.POP_UP);
		table.setMenu(menu);
	    MenuItem item = new MenuItem(menu, SWT.PUSH);
	    item.setText("Add Rows");
	    item.addListener(SWT.Selection, new Listener() {
	    	public void handleEvent(Event event) {
	    		int noOfRows = 0;
	    		int index = table.getSelectionIndex();
	    		InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(), "", "How many Rows?", "Please enter number of rows here", new LengthValidator());
	    		if (dlg.open() == Window.OK) {
	    			noOfRows = Integer.parseInt(dlg.getValue());
	    		}
	    		if(noOfRows > 0){
	    			for(int i = 0; i < noOfRows; i++) {
	  	    		  recordList.addRecord(index);
	  	    		  index++;
	  	    	  	}
	    		}
	    	}
	    });
	    
	    MenuItem deleteRow = new MenuItem(menu, SWT.PUSH);
	    deleteRow.setText("Delete Row");
	    deleteRow.addListener(SWT.Selection, new Listener() {
	    	public void handleEvent(Event event) {
	    		recordList.removeRecord(table.getSelectionIndex());
	    		tableViewer.refresh();
	    	}
	    });
	}
	
		private boolean showColumn(String colName){
			/*
			 * public final String NAME_COLUMN 			= "column_name";
				public final String DEFAULT_VALUE 			= "column_default_value";
				public final String TYPE_COLUMN 			= "column_type";
				public final String WIDTH_COLUMN 			= "column_width";
				public final String SORT_ORDER				= "column_sort_order";
			 */
			for(int i = 0; i<columnNames.length; i++){
				if(columnNames[i].equalsIgnoreCase(colName)){
					return true;
				}
			}
			return false;
		}
		
		private int columnPosition(String colName){
			for(int i = 0; i<columnNames.length; i++){
				if(columnNames[i].equalsIgnoreCase(colName)){
					return i;
				}
			}
			return -1;
		}
        private void renderTable(){
            // 1st column - COLUMN_NAME
		final TableColumn tableColumn0 = new TableColumn(table, SWT.LEFT, 0);
		tableColumn0.setImage(RecordLabels.getImage("unchecked"));
		tableColumn0.setText("Column Name");
		tableColumn0.setWidth(150);
		int colCount = 1;	
		
		for(int k = 1; k<columnNames.length; k++){
			String name = "";
			if(columnNames[k].equalsIgnoreCase("column_default_value")){
				name = "Default Value";
			}
			if(columnNames[k].equalsIgnoreCase("column_type")){
				name = "Column Type";
			}
			if(columnNames[k].equalsIgnoreCase("column_width")){
				name = "Column Width";
			}
			if(columnNames[k].equalsIgnoreCase("column_sort_order")){
				name = "Sort Order";
			}
			TableColumn column = new TableColumn(table, SWT.CENTER, k);
            column.setText(name);
            column.setWidth(100);
			
		}
		/*
                //if(columnNames.length >= 2){
				if(showColumn("column_default_value")){
                    // 2nd column - DEFAULT_VALUE
                    TableColumn column = new TableColumn(table, SWT.CENTER, colCount++);
                    column.setText("Default Value");
                    column.setWidth(100);
                }
                // if(columnNames.length >= 3){
				if(showColumn("column_type")){
		// 3rd column - COLUMN_TYPE
                    TableColumn column = new TableColumn(table, SWT.LEFT, colCount++);
                    column.setText("Column Type");
                    column.setWidth(100);
                 }
                 //if(columnNames.length >= 4){
				if(showColumn("column_width")){
                    // 4th column - COLUMN_WIDTH
                    TableColumn column = new TableColumn(table, SWT.CENTER, colCount++);
                    column.setText("Column Width");
                    column.setWidth(100);
                 }
				if(showColumn("column_sort_order")){
					// 3rd column - COLUMN_TYPE
                    TableColumn column = new TableColumn(table, SWT.LEFT, colCount++);
                    column.setText("Sort Order");
                    column.setWidth(100);
                 }
			*/	
		tableColumn0.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
                            System.out.println("Selection Listner - ");
		        boolean checkBoxFlag = false;
		        for (int i = 0; i < table.getItemCount(); i++) {
		            if (table.getItems()[i].getChecked()) {
		                checkBoxFlag = true;
		            }
		        }
		        if (checkBoxFlag) {
		            for (int m = 0; m < table.getItemCount(); m++) {
		                table.getItems()[m].setChecked(false);
		                tableColumn0.setImage(RecordLabels.getImage("unchecked"));
		                table.deselectAll();
		            }
		        } else {
		            for (int m = 0; m < table.getItemCount(); m++) {
		                table.getItems()[m].setChecked(true);
		                tableColumn0.setImage(RecordLabels.getImage("checked"));
		                table.selectAll();
		            }
		        } //end of else
                        tableViewer.refresh();
                        table.redraw();
		    } //end of handleEvent function
		});
        }
	/**
	 * Create the Table
	 */
	private void createTable(Composite parent) {
		int style = SWT.CHECK | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION;

		table = new Table(parent, style);
		

                
                table.addListener (SWT.Selection, new Listener () {
                    public void handleEvent (Event event) {
                            tableViewer.refresh();
                            table.redraw();
                    }
                });
                
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 6;
		table.setLayoutData(gridData);		
					
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		this.renderTable();
		
	}// End of createTable method
	
	/**
	 * Create a TableViewer 
	 */
	private void createTableViewer() {

		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(columnNames);

		// Create the cell editors
		CellEditor[] editors = new CellEditor[columnNames.length];
		
		// Column 1 : Column Name(Free text)
		//if(columnNames.length >= 1) {
		if(showColumn("column_name")){
			// Column 1 : Column Name(Free text)
			TextCellEditor colNameTextEditor = new TextCellEditor(table);
			((Text) colNameTextEditor.getControl()).setTextLimit(30);
			editors[columnPosition("column_name")] = colNameTextEditor;
		}
		
		// Column 2 : Default Value(Free text)
		//if(columnNames.length >= 2) {
		if(showColumn("column_default_value")){
			// Column 2 : Default Value(Free text)
			TextCellEditor colDefaultTextEditor = new TextCellEditor(table);
			((Text) colDefaultTextEditor.getControl()).setTextLimit(30);
			editors[columnPosition("column_default_value")] = colDefaultTextEditor;
		}
        
		// Column 3 : Column Type (Combo Box) 
		//if(columnNames.length >= 3) {
		if(showColumn("column_type")){
			// Column 3 : Column Type (Combo Box) 
			final ComboBoxCellEditor typeComboBox = new ComboBoxCellEditor(table, recordList.getColTypes(), SWT.DROP_DOWN|SWT.READ_ONLY);
			typeComboBox.getControl().addKeyListener(new KeyListener() {
				String selectedItem = "";
				public String letter = "";
				public List<String> tempList = null;
				public void keyPressed(KeyEvent e) {
					
				} //End of keyPressed event

				public void keyReleased(KeyEvent e) {
					String key = Character.toString(e.character);
					String[] items = typeComboBox.getItems();
					for (int i = 0; i < items.length; i++) {
						if (items[i].toLowerCase().startsWith(key.toLowerCase())) {
							if(!letter.equals(key)){
								tempList = new ArrayList<String>();
							}
							if (items[i].equalsIgnoreCase("select")) {
								continue;
							} else {
								if(!tempList.contains(items[i])){
									tempList.add(items[i]);
									if(isLastItem(recordList.getColTypes(), items[i], key)){
										((CCombo) typeComboBox.getControl()).select(i);
										tempList = new ArrayList<String>();
									} else {
										((CCombo) typeComboBox.getControl()).select(i);
									}
									letter = key;
									return;
								} 
							}
						}
					} //End of for loop
				} //End of keyReleased event

			}); //End of addKeyListener
			
			editors[columnPosition("column_type")] = typeComboBox;
		}
		
		// Column 4 : Column Width (Text with digits only)
		//if(columnNames.length >= 4){
		if(showColumn("column_width")){
			TextCellEditor colWidthTextEditor = new TextCellEditor(table);
			((Text) colWidthTextEditor.getControl()).setTextLimit(10);
			editors[columnPosition("column_width")] = colWidthTextEditor;
		}
		
		if(showColumn("column_sort_order")){
			// Column 3 : Column Type (Combo Box)
			//String[] COLUMN_SORT_ORDER_ARRAY = {"ASCENDING", 
			//	"DESENDING"
			//	};
			//ComboBoxCellEditor colSortTextEditor = new ComboBoxCellEditor(table,COLUMN_SORT_ORDER_ARRAY);
			//colSortTextEditor.setItems(COLUMN_SORT_ORDER_ARRAY);
			//TextCellEditor colSortTextEditor = new TextCellEditor(table);
			//((Text) colSortTextEditor.getControl()).setTextLimit(10);
			//editors[columnPosition("column_sort_order")] = colSortTextEditor;
			
			TableColumn sportColumn = table.getColumn(columnPosition("column_sort_order"));
			JComboBox comboBox = new JComboBox();
			comboBox.addItem("Snowboarding");
			comboBox.addItem("Rowing");
			comboBox.addItem("Chasing toddlers");
			comboBox.addItem("Speed reading");
			comboBox.addItem("Teaching high school");
			comboBox.addItem("None");
			sportColumn.setData(sportColumn);
			//sportColumn.set .setCellEditor(new DefaultCellEditor(comboBox));
			//editors[columnPosition("column_sort_order")] = comboBox;
			
			/*
			final ComboBoxCellEditor c = new ComboBoxCellEditor(table, recordList.getColSortOrder(), SWT.DROP_DOWN|SWT.READ_ONLY);
			c.activate();
			c.getControl().addKeyListener(new KeyListener() {
				String selectedItem = "";
				public String letter = "";
				public List<String> tempList = null;
				public void keyPressed(KeyEvent e) {
					
				} //End of keyPressed event

				public void keyReleased(KeyEvent e) {
					String key = Character.toString(e.character);
					String[] items = c.getItems();
					for (int i = 0; i < items.length; i++) {
						if (items[i].toLowerCase().startsWith(key.toLowerCase())) {
							if(!letter.equals(key)){
								tempList = new ArrayList<String>();
							}
							if (items[i].equalsIgnoreCase("select")) {
								continue;
							} else {
								if(!tempList.contains(items[i])){
									tempList.add(items[i]);
									if(isLastItem(recordList.getColSortOrder(), items[i], key)){
										((CCombo) c.getControl()).select(i);
										tempList = new ArrayList<String>();
									} else {
										((CCombo) c.getControl()).select(i);
									}
									letter = key;
									return;
								} 
							}
						}
					} //End of for loop
				} //End of keyReleased event

			}); //End of addKeyListener
			
			editors[columnPosition("column_sort_order")] = c;
			*/
			
		}
		// Assign the cell editors to the viewer 
		tableViewer.setCellEditors(editors);
		// Set the cell modifier for the viewer
		tableViewer.setCellModifier(new ColumnCellModifiers(this));
	}
	
	/**
	 * Check if the item is the last item in the array for the key.
	 * @param array
	 * @param item
	 * @param key
	 * @return true if it is the last element
	 */
	public boolean isLastItem(String[] array, String item, String key){ 
		
		boolean result = false;
		List<String> listArl = new ArrayList<String>();
		
		for (int i = 0; i < array.length; i++) {
			if(array[i].toLowerCase().startsWith(key)) {
				listArl.add(array[i]);
			}
		}
		
		if(listArl != null && listArl.size() > 0) {
			if(listArl.indexOf(item)+1 == listArl.size()){
				result = true;
			} 
		}
		
		return result;
	}
	
	/**
	 * Add the Buttons
	 * @param parent
	 */
	private void createButtons(Composite parent) {
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		
		// Create and configure the "Add" button
		if(!this.selectColumns){
			Button add = new Button(parent, SWT.PUSH | SWT.CENTER);
			add.setText("Add");
			
			
			add.setLayoutData(gridData);
			add.addSelectionListener(new SelectionAdapter() {
	       		// Add a record and refresh the view
				public void widgetSelected(SelectionEvent e) {
					recordList.addRecord(table.getSelectionIndex());
	                                tableViewer.refresh();
	                                table.redraw();
				}
			});
		}
		
		if(this.selectColumns){
			Button add = new Button(parent, SWT.PUSH | SWT.CENTER);
			add.setText("Select Columns");
			
			
			add.setLayoutData(gridData);
			add.addSelectionListener(new SelectionAdapter() {
	       		// Add a record and refresh the view
				public void widgetSelected(SelectionEvent e) {
					/*recordList.addRecord(table.getSelectionIndex());
	                                tableViewer.refresh();
	                                table.redraw();*/
					AddColumnsDialog acd = new AddColumnsDialog(shell.getDisplay());
					String[] testData = {"test", 
						"test2",
						"abc",
						"fname",
						"lname"
						};
					acd.setItems(testData);
					acd.run();
					ArrayList<String> selCols = acd.getSelectedColumns();
					if(selCols != null && selCols.size() > 0) {
                        //System.out.println("Size: "+parentFields.getRecords().size());
						for(int selI = 0; selI < selCols.size(); selI++){
							RecordBO obj = new RecordBO();
							obj.setColumnName(selCols.get(selI));
							obj.setSortOrder("ASCENDING");
							recordList.addRecordBO(obj);
						}

                }
				}
			});
		}

		//	Create and configure the "Delete" button
		Button delete = new Button(parent, SWT.PUSH | SWT.CENTER);
		delete.setText("Delete");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80; 
		delete.setLayoutData(gridData); 
		delete.addSelectionListener(new SelectionAdapter() {
			//Remove all the records that are checked and refresh the view
			public void widgetSelected(SelectionEvent e) {
				List<Integer> arlCheckedIndexes = new ArrayList<Integer>();
				for (int i = 0; i < table.getItemCount(); i++) {
					if (table.getItems()[i].getChecked()) {
						arlCheckedIndexes.add(i);
					}
				}
						
				Collections.sort(arlCheckedIndexes);
						
				Integer[] arrSortedIndexes = arlCheckedIndexes.toArray(new Integer[arlCheckedIndexes.size()]);
				for (int j = arrSortedIndexes.length - 1 ; j>=0; j--) {
					recordList.removeRecord(arrSortedIndexes[j]);
					//table.remove(arrSortedIndexes[j]);
					tableViewer.refresh();
                                        table.redraw();
				}
                                //table.getItem(0).setImage(RecordLabels.getImage("unchecked"));
                                //tableViewer.refresh();
                                //table.redraw();
                                table.getColumns()[0].setImage(RecordLabels.getImage("unchecked"));
			}
		});
		
		// Create and configure the "GetAll" button
		if(includeCopyParent){
		Button getAll = new Button(parent, SWT.PUSH | SWT.CENTER);
		getAll.setText("Copy Parent Format");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 180;
		getAll.setLayoutData(gridData);
		getAll.addSelectionListener(new SelectionAdapter() {
			// Remove the selection and refresh the view
			public void widgetSelected(SelectionEvent e) {
				if(parentFields!=null && parentFields.getRecords().size()>0){
					
					//tableViewer.remove(index)tableViewer.remove(index);
					//tableViewer.getTable().getSize();
					recordList = new RecordList();
	                
	                if(parentFields.getRecords() != null && parentFields.getRecords().size() > 0) {
	                        //System.out.println("Size: "+parentFields.getRecords().size());
	                        for (Iterator<RecordBO> iterator = parentFields.getRecords().iterator(); iterator.hasNext();) {
	                                //System.out.println("Record -- ");
	                                RecordBO obj = (RecordBO) iterator.next();
	                                recordList.addRecordBO(obj);

	                        }
	                }
	               
	                redrawTable(true);
				}
				/*
				if(recordList.getRecords() != null && recordList.getRecords().size() > 0) {
					System.out.println("Size: "+recordList.getRecords().size());
					for (Iterator<RecordBO> iterator = recordList.getRecords().iterator(); iterator.hasNext();) {
						RecordBO obj = (RecordBO) iterator.next();
						System.out.println("*******************");
						System.out.println("Column Name: "+obj.getColumnName());
						System.out.println("Column Type: "+obj.getColumnType());
						System.out.println("Height: "+obj.getColumnWidth());
						System.out.println("*******************");
						
					}
				}*/
				
			}
		});
		}
		//Create and configure the "Move Up" button
		Button btnRowUp = new Button(parent, SWT.PUSH | SWT.CENTER);
		btnRowUp.setImage(RecordLabels.getImage("upArrow"));
		btnRowUp.setText("Move Up");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 90; 
		btnRowUp.setLayoutData(gridData); 

		btnRowUp.addSelectionListener(new SelectionAdapter() {
			//Move the selected record one level up and refresh the view
			public void widgetSelected(SelectionEvent e) {
				if(recordList.getRecords() != null && recordList.getRecords().size() > 0) {
					int selectionIndex = 0;
					for (int i = 0; i < table.getItemCount(); i++) {
						if (table.getItems()[i].getChecked()) {
							selectionIndex = i;
						}
					}
					
					if(table.getItem(selectionIndex).getChecked() && selectionIndex > 0){
						Collections.swap(recordList.getRecords(), selectionIndex-1, selectionIndex);
					}
					
					tableViewer.refresh();
					if(selectionIndex > 0){
						for (int i = 0; i < table.getItemCount(); i++) {
							table.getItems()[i].setChecked(false);
						}
						table.getItem(selectionIndex-1).setChecked(true);
					}
				}
                                tableViewer.refresh();
                                table.redraw();
			}
		});
		
		///Create and configure the "Move Down" button
		Button btnRowDown = new Button(parent, SWT.PUSH | SWT.CENTER);
		btnRowDown.setImage(RecordLabels.getImage("downArrow"));
		btnRowDown.setText("Move Down");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 90;
		btnRowDown.setLayoutData(gridData); 

		btnRowDown.addSelectionListener(new SelectionAdapter() {
			//Move the selected record one level down and refresh the view
			public void widgetSelected(SelectionEvent e) {
				if(recordList.getRecords() != null && recordList.getRecords().size() > 0) {
					int selectionIndex = 0;
					for (int i = 0; i < table.getItemCount(); i++) {
						if (table.getItems()[i].getChecked()) {
							selectionIndex = i;
							break;
						}
					}
					
					if(table.getItem(selectionIndex).getChecked() && selectionIndex < table.getItems().length -1){
						Collections.swap(recordList.getRecords(), selectionIndex+1, selectionIndex);
					}
					
					tableViewer.refresh();
					if(selectionIndex < table.getItems().length - 1) {
						for (int i = 0; i < table.getItemCount(); i++) {
							table.getItems()[i].setChecked(false);
						}
						table.getItem(selectionIndex+1).setChecked(true);
					}
				}
                                tableViewer.refresh();
                                table.redraw();
			}
		});
		
		//	Create and configure the "Close" button
		//closeButton = new Button(parent, SWT.PUSH | SWT.CENTER);
		//closeButton.setText("Close");
		//gridData = new GridData (GridData.HORIZONTAL_ALIGN_END);
		//gridData.widthHint = 80; 
		//closeButton.setLayoutData(gridData);
		
		/*closeButton = new Button(parent, SWT.PUSH | SWT.CENTER);
		closeButton.setText("Close");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_END);
		gridData.widthHint = 80; 
		closeButton.setLayoutData(gridData);*/
		
		
		//This filter is used to handle the CRTL+ENTER Event, which adds the new row to the Grid
		table.getDisplay().addFilter( SWT.KeyDown, new Listener() {
			  public void handleEvent( Event event ) {
				    //HARD CODED TabItem Text
				  	if(!tabFolder.isDisposed() && tabFolder.getSelection()[0].getText().equals("Fields")) {
				  		if(event.stateMask == SWT.CTRL && event.keyCode == SWT.CR) {
				  			recordList.addRecord(table.getSelectionIndex());
				  			table.select(table.getSelectionIndex()+1);
						}
				  	}
			  }
		} );
		
	}
	
	/**
	 * This method is used to add the Cell Navigation in the table
	 */
	private void addCellNavigation(){
		
		CellNavigationStrategy naviStrat = new CellNavigationStrategy() {
			
			public ViewerCell findSelectedCell(ColumnViewer viewer, ViewerCell currentSelectedCell, Event event) {
				ViewerCell cell = super.findSelectedCell(viewer, currentSelectedCell, event);
				if( cell != null ) {
					tableViewer.getTable().showColumn(tableViewer.getTable().getColumn(cell.getColumnIndex()));
				}
				
				return cell;
			}
		};
		
		TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(tableViewer,new FocusCellOwnerDrawHighlighter(tableViewer));
		
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(tableViewer) {
			protected boolean isEditorActivationEvent( ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.TAB)
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};
		
		TableViewerEditor.create(tableViewer, focusCellManager, actSupport, ColumnViewerEditor.TABBING_HORIZONTAL
				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
				| ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);
		
		
		//Used to override the default TAB behavior which focuses on the next component
		tableViewer.getTable().addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent arg0) {
				if (arg0.keyCode == SWT.TAB){
					arg0.doit = false;
				}
			}
		});
		
	}
	
	/**
	 * InnerClass that acts as a proxy for the RecordList providing content for the Table. It implements the IRecordListViewer 
	 * interface since it must register changeListeners with the RecordList 
	 */
	class ExampleContentProvider implements IStructuredContentProvider, IRecordListViewer {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if (newInput != null) {
				((RecordList) newInput).addChangeListener(this);
			}
			if (oldInput != null)
				((RecordList) oldInput).removeChangeListener(this);
		}

		public void dispose() {
			recordList.removeChangeListener(this);
		}

		// Return the tasks as an array of Objects
		public Object[] getElements(Object parent) {
			return recordList.getRecords().toArray();
		}

		public void addRecord(RecordBO record) {
                    System.out.println("addRecord");
			//Insert the record at a specific position
			if(tableViewer.getTable().getSelectionIndex() >= 0){
				tableViewer.insert(record, tableViewer.getTable().getSelectionIndex()+1);
			} else {
				tableViewer.add(record);
			}
                        tableViewer.refresh();
                        table.redraw();
			
		}

		public void removeRecord(int index) {
			tableViewer.remove(index);	
		}

		public void modifyRecord(RecordBO record) {
			tableViewer.update(record, null);	
		}
	}
	
	/**
	 * This class is used to validate that you enter only numeric values in the "Add Rows" Dialog box
	 */
	class LengthValidator implements IInputValidator {
	/**
	 * Validates the String. Returns null for no error, or an error message
	 * @param newText the String to validate
	 * @return String
	 */
		public String isValid(String newText) {
			String returnValue = "Not Correct";
			if(newText != null && newText.trim().length() > 0){
				boolean isInteger = Pattern.matches("^\\d*$", newText);
				if(isInteger){
					returnValue = null;
				} 
					
			}
		    
		    return returnValue; // Input must be OK
		}
	}
	
	/**
	 * Return the parent composite
	 */
	public Control getControl() {
		return table.getParent();
	}
	
	/**
	 * Release resources
	 */
	public void dispose() {
		// Tell the label provider to release its resources
		tableViewer.getLabelProvider().dispose();
	}
	
	/*
	 * Close the window and dispose of resources
	 */
	public void close() {
		Shell shell = table.getShell();

		if (shell != null && !shell.isDisposed())
			shell.dispose();
	}
	
	/**
	 * Return the array of choices for a multiple choice cell
	 */
	public String[] getChoices(String property) {
		if (TYPE_COLUMN.equals(property))
			return recordList.getColTypes();
		else if (SORT_ORDER.equals(property))
			return recordList.getColSortOrder();
		else
			return new String[]{};
	}
	
	/**
	 * Return the column names in a collection
	 * @return List  containing column names
	 */
	public java.util.List<String> getColumnNames() {
		return Arrays.asList(columnNames);
	}
	
	/**
	 * @return currently selected item
	 */
	public ISelection getSelection() {
		return tableViewer.getSelection();
	}
	
	/**
	 * Return the RecordList
	 */
	public RecordList getRecordList() {
		return recordList;	
	}
}
