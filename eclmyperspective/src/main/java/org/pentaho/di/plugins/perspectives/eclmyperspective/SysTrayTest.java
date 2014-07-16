package org.pentaho.di.plugins.perspectives.eclmyperspective;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.w3c.dom.Document;

import au.com.bytecode.opencsv.CSVReader;

public class SysTrayTest {
	static String[] filePath;
	static String fileName = "";	
	static ArrayList<String> map = new ArrayList<String>();
	static ArrayList<String[]> columns = new ArrayList<String[]>();
	static TabItem ite;
	static int choice;
	static boolean numeric;
	public static void main(String[] args) throws Exception{
		 
		final Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		shell.setLayout(gridLayout);
		shell.setText("Report Builder");
		shell.setImage(new Image(display, "D:\\Users\\703119704\\Desktop\\reportbuilder.png"));
		
		Color color = shell.getBackground();
		shell.setBackground(new Color(null,255,255,255));
		
		final Composite comp2 = new Composite(shell, SWT.NONE);		
		comp2.setLayout(new GridLayout(3,false));
		comp2.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp2.setBackground(new Color(null,255,255,255));

		
		
		Composite three = new Composite(comp2, SWT.NONE | SWT.BORDER);
		three.setLayout(new GridLayout(1,false));
		three.setLayoutData(new GridData(GridData.FILL_BOTH));//
		three.setBackground(new Color(null,188,188,188));
		
		Composite t1 = new Composite(three, SWT.NONE);
		t1.setLayout(new GridLayout(3,false));
		t1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		t1.setBackground(three.getBackground());
		
		Label tL = new Label(t1, SWT.NONE);
		tL.setText("Unsaved Report");
		tL.setFont(new Font(display,"Arial", 18, SWT.BOLD ));
		tL.setBackground(three.getBackground());
		
		Label tl2 = new Label(t1, SWT.NONE);
		tl2.setText("					");
		tl2.setBackground(three.getBackground());
		tl2.setLayoutData(new GridData(GridData.FILL));
		
		ToolBar tc = new ToolBar(t1,  SWT.FLAT | SWT.WRAP | SWT.RIGHT);
	    tc.setBackground(t1.getBackground());	    
		tc.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		ToolItem item = new ToolItem(tc, SWT.PUSH);
	    item.setToolTipText("Print PDF");
	    Image icon = new Image(display, "D:\\Users\\703119704\\Downloads\\Flow-flow\\assembly\\package-res\\ui\\images\\print.png");
	    item.setImage(icon);
	    item = new ToolItem(tc, SWT.PUSH);
	    item.setToolTipText("Save");
	    icon = new Image(display, "D:\\Users\\703119704\\Downloads\\Flow-flow\\assembly\\package-res\\ui\\images\\save.png");
	    item.setImage(icon);
	    item = new ToolItem(tc, SWT.PUSH);
	    item.setToolTipText("Save As");
	    icon = new Image(display, "D:\\Users\\703119704\\Downloads\\Flow-flow\\assembly\\package-res\\ui\\images\\saveas.png");
	    item.setImage(icon);

				Composite three1 = new Composite(comp2, SWT.NONE | SWT.BORDER);	
				three1.setLayout(new GridLayout(1,false));
				GridData gd = new GridData();
				gd.horizontalSpan = 2;
				three1.setLayoutData(new GridData(GridData.FILL_VERTICAL));//
				three1.setBackground(new Color(null,188,188,188));
				
				Composite threeClose = new Composite(three1, SWT.NONE);
				threeClose.setBackground(three1.getBackground());
				threeClose.setLayout(new GridLayout(3,false));
				threeClose.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				
				Label lbl1 = new Label(threeClose, SWT.NONE);
				lbl1.setText("Available Field(s) for: ");
				//lbl.setFont(new FontData());
				//lbl1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL)); 
				lbl1.setBackground(three1.getBackground());
				
				Label l3 = new Label(threeClose, SWT.NONE);
				l3.setText("         ");
				l3.setBackground(three1.getBackground());
				l3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				
				ToolBar toolBar = new ToolBar(threeClose, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
			    toolBar.setBackground(three1.getBackground());
			    toolBar.setLayoutData(new GridData(GridData.CENTER));
				
			    
			    ToolItem itemPush = new ToolItem(toolBar, SWT.PUSH);
			    itemPush.setToolTipText("Close");
			    Image icon1 = new Image(display, "D:\\Users\\703119704\\Downloads\\Flow-flow\\assembly\\package-res\\ui\\images\\close-panel.png");
			    itemPush.setImage(icon1);
			    
			    
				final Label lbl = new Label(three1, SWT.NONE);
				lbl.setText("");
				//lbl.setFont(new FontData());
				lbl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL)); 
				lbl.setBackground(three1.getBackground());
				
				Composite threeFind = new Composite(three1, SWT.NONE);
				threeFind.setBackground(three1.getBackground());
				threeFind.setLayout(new GridLayout(2,false));
				threeFind.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				
				Label L = new Label(threeFind, SWT.NONE);
				L.setText("Find: ");
				L.setBackground(three1.getBackground());
				
				
				Text txt = new Text(threeFind, SWT.NONE | SWT.BORDER);
			    txt.setText("");
				txt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			    
				final Tree tree = new Tree(three1, SWT.LEFT | SWT.V_SCROLL | SWT.BORDER | SWT.BEGINNING);	    		
			    tree.setLayoutData(new GridData(GridData.FILL_BOTH));
			    tree.setHeaderVisible(true);			    
			    
			    Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
			    DragSource source = new DragSource(tree, DND.DROP_MOVE | DND.DROP_COPY);
			    source.setTransfer(types);
			    
			    source.addDragListener(new DragSourceAdapter() {
			        public void dragSetData(DragSourceEvent event) {
			          // Get the selected items in the drag source
			          DragSource ds = (DragSource) event.widget;
			          Tree tab = (Tree) ds.getControl();
			          System.out.println(tab); 
			          TreeItem[] selection = tree.getSelection();

			          StringBuffer buff = new StringBuffer();
			          for (int i = 0, n = selection.length; i < n; i++) { 
			            buff.append(selection[i].getText());
			          }

			           event.data = buff.toString();
			        }
			      });

			    
			    
			    
		final Composite three2 = new Composite(comp2, SWT.NONE | SWT.BORDER);
		three2.setLayout(new GridLayout(1,false)); 
		three2.setLayoutData(new GridData(GridData.FILL_VERTICAL));//
		three2.setBackground(color);
		
		
		
		CLabel L4 = new CLabel(three2, SWT.NONE);
		L4.setText("Visualization Settings and Tables");
		L4.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    L4.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_GRAY));
	    // Set the background gradient
	    L4.setBackground(new Color[] { shell.getDisplay().getSystemColor(SWT.COLOR_GRAY),
	        shell.getDisplay().getSystemColor(SWT.COLOR_CYAN),
	        shell.getDisplay().getSystemColor(SWT.COLOR_BLUE) }, new int[] { 100, 100 });
		
	    
		
		Group generalGroup = new Group(three2, SWT.SHADOW_NONE);        
        generalGroup.setText("General");
        //generalGroup.setSize(200,700);
        generalGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        generalGroup.setLayout(new GridLayout(2,false));
        
        
        
        Label L1 = new Label(generalGroup, SWT.NONE);
        L1.setText("Tables:");
        
        
        
        final Combo c1 = new Combo(generalGroup, SWT.BORDER | SWT.H_SCROLL);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = (Document) dBuilder.parse("D:\\Users\\703119704\\Desktop\\ECL_Results\\W20140716-132956_results.xml");
		doc.getDocumentElement().normalize();
		String[] file = null;// = "";
		
		if(doc.getElementsByTagName("result") != null){
			int i = 0;
			file = new String[doc.getElementsByTagName("result").getLength()];			
			filePath = new String[doc.getElementsByTagName("result").getLength()];
			while(i < doc.getElementsByTagName("result").getLength()){
				file[i] =  doc.getElementsByTagName("result").item(i).getTextContent();
				filePath[i] = doc.getElementsByTagName("result").item(i).getTextContent();
				String[] S = file[i].split("\\\\");
				file[i] = S[S.length-1].split("_")[1]; 
				if(file[i].length()>15)
					file[i] = file[i].substring(0, 15) + "...\n";
				i++;
			}
			
		}		
		
		c1.setItems(file);
        c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        c1.addModifyListener(new ModifyListener(){
        	
            public void modifyText(ModifyEvent e){
            	lbl.setText("  "+c1.getText());               	
            	int idx = c1.getSelectionIndex();
            	
            	fileName = filePath[idx];
            	System.out.println(fileName); 
            	tree.setItemCount(0);
            	readFile(fileName, true, null, tree, null,null,null,null);
			}
	    });
        
        Group visualGroup = new Group(three2, SWT.SHADOW_NONE);        
        visualGroup.setText("Visualization");
        //generalGroup.setSize(200,700);
        visualGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        visualGroup.setLayout(new GridLayout(2,false));
        
        Label gr = new Label(visualGroup, SWT.NONE);
        gr.setText("Graph:");
        
        final Combo cx = new Combo(visualGroup, SWT.NONE);
        cx.setItems(new String[]{"Piechart","Linechart","LineChart_String","Barchart","Scatterchart"});
		cx.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		
		Label gr1 = new Label(visualGroup, SWT.NONE);
        gr1.setText("Operation:");
        
        final Combo cx1 = new Combo(visualGroup, SWT.NONE);
        cx1.setItems(new String[]{"Sum", "Ave", "Variance", "Add", "Subtract", "Multiply", "Divide", "Mod"});
		cx1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Group tabGroup = new Group(three2, SWT.SHADOW_NONE);        
        tabGroup.setText("Drag 'n Drop ");       
        tabGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        tabGroup.setLayout(new GridLayout(1,false));
        
        Composite axis = new Composite(tabGroup, SWT.NONE);
	    axis.setLayout(new GridLayout(2,false)); 
	    axis.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));//
	    axis.setBackground(color);
        
	    Label xaxis = new Label(axis, SWT.NONE);
	    xaxis.setText("X-Axis:");	    	   
	    
		final Tree tab1 = new Tree(tabGroup, SWT.CHECK | SWT.V_SCROLL | SWT.BORDER);	    		
	    tab1.setLayoutData(new GridData(GridData.FILL_BOTH));
	    tab1.getVerticalBar();
	    tab1.setHeaderVisible(true);
	    
	    final Combo ax = new Combo(axis, SWT.NONE);	    
	    ax.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    
	    
	    Composite buttons = new Composite(tabGroup, SWT.NONE);
	    buttons.setLayout(new GridLayout(2,false)); 
	    buttons.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));//
	    buttons.setBackground(color);
	    
	    
	    Button B1 = new Button(buttons, SWT.PUSH);
	    B1.setText("Delete");
	    
	    Button B2 = new Button(buttons, SWT.PUSH);
	    B2.setText("Create Graph");
	    
	    DropTarget target = new DropTarget(tab1, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT);
	    target.setTransfer(types);
	    target.addDropListener(new DropTargetAdapter() {
	      public void dragEnter(DropTargetEvent event) {
	        if (event.detail == DND.DROP_DEFAULT) {
	          event.detail = (event.operations & DND.DROP_COPY) != 0 ? DND.DROP_COPY : DND.DROP_NONE;
	        }

	        // Allow dropping text only
	        for (int i = 0, n = event.dataTypes.length; i < n; i++) {
	          if (TextTransfer.getInstance().isSupportedType(event.dataTypes[i])) {
	            event.currentDataType = event.dataTypes[i];
	          }
	        }
	      }

	      public void dragOver(DropTargetEvent event) {
	         event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
	      }
	      public void drop(DropTargetEvent event) {
	        if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
	          // Get the dropped data
	          DropTarget target = (DropTarget) event.widget;
	          //Table table = (Table) target.getControl();
	          Tree tab2 = (Tree) target.getControl();	          
	          String data = (String) event.data;
	          
	          // Check if dropped data already in Table
	          if(map.isEmpty()){
	        	  map.add(data);
	        	  
	        	  for(int i = 0; i<tree.getItemCount(); i++){
	        		if(tree.getItem(i).getText().equalsIgnoreCase(data)){
	        			columns.add(new String[]{data,Integer.toString(i)});
	        		}
	        	  }
	        	  
	        	  TreeItem item = new TreeItem(tab2, SWT.NONE);	        	  
		          item.setText(0,data);
		          tab2.redraw();
	          }
	          else{
	        	  if(!map.contains(data)){
	        		  map.add(data);
	        		  for(int i = 0; i<tree.getItemCount(); i++){
	  	        		if(tree.getItem(i).getText().equalsIgnoreCase(data)){
	  	        			columns.add(new String[]{data,Integer.toString(i)});
	  	        		}
	  	        	  }
	        		  TreeItem item = new TreeItem(tab2, SWT.NONE);
	    	          item.setText(0,data);
	    	          tab2.redraw();
	        	  }
	          }
	          // Create a new item in the table to hold the dropped data	          
	          String items[] = new String[tab2.getItemCount()];
	          for(int i = 0; i<tab2.getItemCount(); i++){
	        	  items[i] = tab2.getItem(i).getText();
	          }
	          ax.setItems(items);
	        }
	        
	        
	      }
	    });
	    
	    B1.addListener(SWT.Selection, new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				int cnt = 0;//String txt;
				int len = tab1.getItemCount();
				for(int i = 0; i<len; i++){
					if(tab1.getItem(Math.abs(i-cnt)).getChecked()){
						map.remove(Math.abs(i-cnt));
						columns.remove(Math.abs(i-cnt));
						tab1.getItem(Math.abs(i-cnt)).dispose();
						cnt = cnt + 1;
					}
				}
			}
	    	
	    });
	    
	    final TabFolder tabFolder = new TabFolder(three, SWT.NONE);
	    
	    ite = new TabItem(tabFolder, SWT.NONE);
	    ite.setText("Table");
	    ite.setToolTipText("Table");
	    
	    final Table table = new Table(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	    table.setLinesVisible(true);
	    table.setHeaderVisible(true);	    
	    ite.setControl(table);
	    
	    
	    ite = new TabItem(tabFolder, SWT.NONE);
	    ite.setText("Graph");
	    ite.setToolTipText("Graph");
	    
	    
	    
	    tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
	    
	    B2.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				choice = cx.getSelectionIndex();
				readFile(fileName,false,table,null,tabFolder,ite,ax,tab1);
			}
	    });
	    itemPush.addListener(SWT.Selection, new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				if(tree.getItemCount()>0)
					tree.setItemCount(0);
				if(!lbl.getText().equals(""))
					lbl.setText("");
				if(tab1.getItemCount()>0)
					tab1.setItemCount(0);
				//if(!c1.getText().equalsIgnoreCase(""))
					//c1.setText("");
				if(!cx.getText().equalsIgnoreCase(""))
					cx.setText("");
				if(!cx1.getText().equalsIgnoreCase(""))
					cx1.setText("");
			}
			
	    	
	    });

        
		shell.open();
		while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
	}
	

	
	
	
	
	
	public static void readFile(String filename, boolean header, Table table, Tree tree, TabFolder tabFolder, TabItem ite, Combo ax, Tree tab1){
		if(table != null){
			System.out.println(columns.size()); 
			table.setItemCount(0);
			table.setRedraw(false);
			while ( table.getColumnCount() > 0 ) {
			    table.getColumns()[ 0 ].dispose();
			}
			table.setRedraw(true);			
		}
		if(!filename.equals("")){
			System.out.println(filename); 
			
			CSVReader reader = null;
			try {
				reader = new CSVReader(new FileReader(filename),',','"');
			} catch (FileNotFoundException e2) {
				e2.printStackTrace();
			}
            String [] strLineArr;
            
            // Open the file that is the first 
            // command line parameter
            FileInputStream fstream = null;
			try {
				fstream = new FileInputStream(filename);
			} catch (FileNotFoundException e2) {
				e2.printStackTrace();
			}
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            System.out.println(br); 
            int length = 0;
            boolean first = true;
            if(header == true){
            	try {
					while ((strLineArr = reader.readNext()) != null) {						
						for(int i = 0; i<strLineArr.length; i++){
							TreeItem treeItem0 = new TreeItem(tree, 0);
		        	    	treeItem0.setText(strLineArr[i]);
		        	    	
						}						
						break;					    
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            }
            else{
            	XYSeries[] series = new XYSeries[columns.size()];
            	System.out.println(columns.size()+"Yehi hai"); 
            	int srcnt = 0;
            	for(Iterator<String[]> it = columns.iterator(); it.hasNext();){
            		String[] S = (String[]) it.next();
            		if(!S[0].equalsIgnoreCase(ax.getText())){
            			series[srcnt] = new XYSeries(S[0]);
            			//vars[srcnt] = S[0];
            			srcnt++;
            		}
            	}
            	XYSeriesCollection data = new XYSeriesCollection();
            	DefaultCategoryDataset Catdataset = new DefaultCategoryDataset();
            	DefaultPieDataset piedataset = new DefaultPieDataset();
	            //Read File Line By Line
	            try {       
	            	String[] vars = null;
	            	strLineArr = reader.readNext();	 
	            	if(first){
	            		vars = strLineArr;
  	            	}
	            	
					while ((strLineArr) != null) {
						if(!first){
							switch(choice){
							case 0: //piedataset = new DefaultPieDataset();  
								break;							
							case 1: //series = new XYSeries("first");
								break;
							case 2:
								break;
							default: System.out.println("Naa ho payega!");  
							}
							 
						}
						TableItem item = null;
			              
			              if(first){
			                  length = strLineArr.length;
			              }else{
			                   item = new TableItem (table, SWT.NONE);
			              }
			              
			              int thisLen = strLineArr.length;
			              int j = 0;
			              if(thisLen<=length){
			            	  int fir = 0;
			                  for(int i =0; i<thisLen; i++){
			              
			                      if(first){
			                    	  for(Iterator<String[]> it = columns.iterator(); it.hasNext();){
		                        		  String[] S = it.next();
		                        		  if(i == Integer.parseInt(S[1])){
		                        			  TableColumn column = new TableColumn (table, SWT.LEFT);
				                    		  column.setText(strLineArr[i]);			                          
				                    		  column.setWidth(100);
				                    		  column.pack();
				                    		  
		                        		  }
		                        	  }
			                    		
			                    	    //System.out.println(strLineArr[i]); 
			                      }else{
			                         
			                          //System.out.println("-- "+i+" -- " + strLineArr[i]);
			                    	  
			                    	  for(Iterator<String[]> it = columns.iterator(); it.hasNext();){
		                        		  String[] S = it.next();
		                        		  if(i == Integer.parseInt(S[1])){
		                        			  item.setText(j,strLineArr[i]);
		                        			  j++;
		                        		  }
		                        	  }
			                    	  
			                          if(i > 0){
			                        	  String xaxis = ax.getText();int cnt = 0;
			                        	  for(Iterator<String[]> it = columns.iterator(); it.hasNext();){
			                        		  String[] S = it.next();			                        		    
			                        		  if(xaxis.equalsIgnoreCase(S[0])){
			                        			  cnt = Integer.parseInt(S[1]);
			                        			  //System.out.println("What the hell "+ cnt);  
			                        			  break;
			                        		  }
			                        	  }
			                        	  
			                        	  for(Iterator<String[]> it = columns.iterator(); it.hasNext();){
			                        		  String[] S = it.next();			                        		  
			                        		  if(i == Integer.parseInt(S[1]) &&i!=cnt){
			                        			  switch(choice){
			                        			  case 0: piedataset.setValue(strLineArr[cnt], Double.parseDouble(strLineArr[i]));
			                        			  	break;
			                        			  case 1:
			                        				  if(isNumeric(strLineArr[cnt])){
			                        					  numeric = true;
			                        					  series[fir].add(Double.parseDouble(strLineArr[cnt]), Double.parseDouble(strLineArr[i]));//Double.parseDouble(strLineArr[cnt])
			                        				  }
			                        				  else
			                        					  Catdataset.addValue(Double.parseDouble(strLineArr[i]), vars[i], strLineArr[cnt]);
			                        			  	  break;
			                        			  case 2:
			                        				  break;
			                        			  default: 
			                        			  }
			                        			  System.out.println(Double.parseDouble(strLineArr[cnt]) + " " + i + " " +strLineArr[i]); 
			                        			  fir++;
			                        		  }
			                        		  
			                        	  }
			                        		  
			                          }
			                      }			                      
			                  }				                  
			                  //data.addSeries(series);
			              }
			              first = false;
			              strLineArr = reader.readNext();
			              
					}			
					
					System.out.println(series[0].getAutoSort());   
					
					for(int l = 0;l<columns.size()-1;l++)
						data.addSeries(series[l]);
					XYDataset xydataset = data;
					CategoryDataset dataset = Catdataset;
					System.out.println("dataset "+dataset);  
					switch(choice){
					case 1: 
						JFreeChart chart = null;
 						if(numeric){
						chart = ChartFactory.createLineChart(
				            "Line Chart Demo 6",      // chart title
				            ax.getText(),             // x axis label
				            "Y",                      // y axis label
				            dataset,                  // data
				            PlotOrientation.VERTICAL, // orientation
				            true,                     // include legend
				            true,                     // tooltips
				            false                     // urls
				        );
						chart = ChartFactory.createXYLineChart(
				            "Line Chart Demo 6",      // chart title
				            ax.getText(),             // x axis label
				            "Y",                      // y axis label
				            xydataset,                  // data
				            PlotOrientation.VERTICAL, // orientation
				            true,                     // include legend
				            true,                     // tooltips
				            false                     // urls
				        );
						}
				        
				        if(!numeric){
									final CategoryPlot plot = (CategoryPlot) chart.getPlot();
							        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
							        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
							        rangeAxis.setAutoRangeIncludesZero(true);
									
									
							        final Composite graph = new Composite(tabFolder, SWT.NONE);
								    graph.setLayout(new GridLayout(1,false));
								    graph.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));	
								    ite.setControl(graph);
							        ChartComposite composite = new ChartComposite(graph, SWT.NONE,chart,true);
									composite.setLayout(new FillLayout(SWT.FILL));
									composite.setLayoutData(new GridData(GridData.FILL_BOTH));//SWT.FILL, SWT.FILL, true, true
									composite.setBackground(new Color(null,255,0,0));
									composite.setChart(chart);
									break;
		
								}
								else{
									final XYPlot plot =  chart.getXYPlot();
							        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
							        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
							        rangeAxis.setAutoRangeIncludesZero(true);
									
									
							        final Composite graph = new Composite(tabFolder, SWT.NONE);
								    graph.setLayout(new GridLayout(1,false));
								    graph.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));	
								    ite.setControl(graph);
							        ChartComposite composite = new ChartComposite(graph, SWT.NONE,chart,true);
									composite.setLayout(new FillLayout(SWT.FILL));
									composite.setLayoutData(new GridData(GridData.FILL_BOTH));//SWT.FILL, SWT.FILL, true, true
									composite.setBackground(new Color(null,255,0,0));
									composite.setChart(chart);
									break;
		
								}
					case 0: JFreeChart piechart = ChartFactory.createPieChart("Programmer Population", piedataset, true, true, false);
							PiePlot plotpi = (PiePlot) piechart.getPlot();
							plotpi.setCircular(true);
							final Composite pigraph = new Composite(tabFolder, SWT.NONE);
						    pigraph.setLayout(new GridLayout(1,false));
						    pigraph.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));	
						    ite.setControl(pigraph);
					        ChartComposite picomposite = new ChartComposite(pigraph, SWT.NONE,piechart,true);
							picomposite.setLayout(new FillLayout(SWT.FILL));
							picomposite.setLayoutData(new GridData(GridData.FILL_BOTH));//SWT.FILL, SWT.FILL, true, true
							picomposite.setBackground(new Color(null,255,0,0));
							picomposite.setChart(piechart);
							break;
					}
						
						//chart = null;
				} 
	            catch (IOException e1) {
					e1.printStackTrace();
				}
            }
        }
	}
	public static boolean isNumeric(String str)  
	  {  
	    try  
	    {  
	      double d = Double.parseDouble(str);  
	    }  
	    catch(NumberFormatException nfe)  
	    {  
	      return false;  
	    }  
	    return true;  
	  }
}