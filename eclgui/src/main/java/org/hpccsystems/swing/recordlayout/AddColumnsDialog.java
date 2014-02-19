package org.hpccsystems.swing.recordlayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
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
import org.hpccsystems.recordlayout.CreateTable;
import org.hpccsystems.recordlayout.RecordBO;
import org.hpccsystems.recordlayout.RecordLabels;
import org.hpccsystems.recordlayout.RecordList;
import org.hpccsystems.ecljobentrybase.*;

public class AddColumnsDialog {
	
	private String datasetName = "";
	private ArrayList<String> selectedColumns = null;
	private Display display = null;
	String[] items = null;//ap.fieldsByDataset( datasetName,jobMeta.getJobCopies());
	
	public AddColumnsDialog(Display display){
		this.display=display;
		selectedColumns = new ArrayList<String>();
	}
	
	
	public String getDatasetName() {
		return datasetName;
	}




	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}




	public ArrayList<String> getSelectedColumns() {
		return selectedColumns;
	}




	public void setSelectedColumns(ArrayList<String> selectedColumns) {
		selectedColumns = selectedColumns;
	}




	public Display getDisplay() {
		return display;
	}




	public void setDisplay(Display display) {
		this.display = display;
	}




	public String[] getItems() {
		return items;
	}




	public void setItems(String[] items) {
		this.items = items;
	}




	public void run(){

			// Need to preserve checked status

			final Shell shellFilter = new Shell(display);
			FormLayout layoutFilter = new FormLayout();
			layoutFilter.marginWidth = 25;
			layoutFilter.marginHeight = 25;
			shellFilter.setLayout(layoutFilter);
			shellFilter.setText("Select Columns");
			
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
					
					ArrayList<String[]> Str = new ArrayList<String[]>();
					
					//for(Iterator<RecordBO> iterator = recordlist.getRecords().iterator(); iterator.hasNext();){
					//	String[] s1 = {iterator.next().getColumnName(),"STRING",""};
					//	Str.add(s1);
	            	//}
					
	            	for(int i = 0; i<tab.getItemCount(); i++){		            		
            			if(tab.getItem(i).getChecked()){
            				boolean flag = true;
            				selectedColumns.add(tab.getItem(i).getText());
            				/*recordlist = ct.getRecordList();
	    	 				if(recordlist.getRecords() != null && recordlist.getRecords().size() > 0) {
	    	 					for(Iterator<RecordBO> iterator = recordlist.getRecords().iterator(); iterator.hasNext();){
	    	 						if(tab.getItem(i).getText().equals(iterator.next().getColumnName()))
	    	 							{flag = false; break;}
	    	 					}
	    	 				}*/
	    	 				if(flag){
	    	 					String[] S = {tab.getItem(i).getText(),"STRING",""};
	    	 					Str.add(S);
	    	 				}
            			}	            				            		
	            	}
	            		            	
	            	//ct.setRecordList(jobEntry.ArrayListToRecordList(Str));
	            	//ct.redrawTable(true);
	            	//recordlist = ct.getRecordList();
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
    	
    
	

}
