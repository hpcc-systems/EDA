package org.hpccsystems.pentaho.job.eclfrequency;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.hpccsystems.eclguifeatures.AutoPopulate;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;

public class test {
	Combo datasetName;
	Text Name;
	

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new test().createShell(display);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
	
	public Shell createShell(final Display display) {
		final Shell shell = new Shell(display);
		FormLayout layout = new FormLayout();
		layout.marginWidth = 25;
		layout.marginHeight = 25;
		shell.setLayout(layout);
		shell.setText("Learning SWT");
		
		Label name = new Label(shell, SWT.NONE);
		name.setText("MyLabel:");
		Name = new Text(shell, SWT.SINGLE | SWT.BORDER);
		
		Label data = new Label(shell, SWT.NONE);
		data.setText("Dataset:");
		datasetName = new Combo(shell, SWT.NONE);
		datasetName.setItems(new String[] {"DS1", "DS2", "DS3", "DS4"});
		
		Button add = new Button(shell, SWT.PUSH);
		add.setText("Choose Columns");
		
		final Table table = new Table(shell, SWT.NONE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setItemCount(10);
		TableColumn[] column = new TableColumn[1];
		column[0] = new TableColumn(table, SWT.NONE);
        column[0].setWidth(200);
        column[0].setText("Fields");
        
        FormData dat = new FormData();
        dat.top = new FormAttachment(Name, 0, SWT.CENTER);
        name.setLayoutData(dat);
        dat = new FormData();
        dat.left = new FormAttachment(name, 75, SWT.LEFT);
        dat.right = new FormAttachment(100, 0);
        Name.setLayoutData(dat);
        
        dat = new FormData();
        dat.top = new FormAttachment(datasetName, 0, SWT.CENTER);
        data.setLayoutData(dat);
        dat = new FormData();
        dat.top = new FormAttachment(Name, 15);
        dat.left = new FormAttachment(Name, 0, SWT.LEFT);
        dat.right = new FormAttachment(100, 0);
        datasetName.setLayoutData(dat);
        
        dat = new FormData();
        dat.top = new FormAttachment(data, 25);
        dat.left = new FormAttachment(data, 0, SWT.LEFT);
        add.setLayoutData(dat);
        	
        dat = new FormData();
        dat.top = new FormAttachment(add,25);
        dat.left = new FormAttachment(add, 0, SWT.LEFT);
        table.setLayoutData(dat);
        
        datasetName.addModifyListener(new ModifyListener(){
        	
            public void modifyText(ModifyEvent e){
            	table.setRedraw(false);
            	table.setItemCount(0);
            	if(datasetName.getText().equalsIgnoreCase("ds1")){
            		for(int i = 0; i<10; i++){
            			TableItem item = new TableItem(table, SWT.NONE);
            			item.setText("Keshav "+Integer.toString(i+1));
            		}
            		table.setRedraw(true);
				}
            	if(datasetName.getText().equalsIgnoreCase("ds2")){
            		for(int i = 0; i<10; i++){
            			TableItem item = new TableItem(table, SWT.NONE);
            			item.setText("Batman "+Integer.toString(i+1));
            		}
            		table.setRedraw(true);
				}
            	if(datasetName.getText().equalsIgnoreCase("ds3")){
            		for(int i = 0; i<10; i++){
            			TableItem item = new TableItem(table, SWT.NONE);
            			item.setText("Superman "+Integer.toString(i+1));
            		}
            		table.setRedraw(true);
				}
            	if(datasetName.getText().equalsIgnoreCase("ds4")){
            		for(int i = 0; i<10; i++){
            			TableItem item = new TableItem(table, SWT.NONE);
            			item.setText("Darth Vader "+Integer.toString(i+1));
            		}
            		table.setRedraw(true);
				}
            	
            }
        });
        
        Listener addListener = new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				final Shell shell1 = new Shell(display);
				FormLayout layout1 = new FormLayout();
				layout1.marginWidth = 25;
				layout1.marginHeight = 25;
				shell1.setLayout(layout1);
				shell1.setText("Learning SWT");
				
				Label name1 = new Label(shell1, SWT.NONE);
				name1.setText("MyLabel:");
				final Text Name1 = new Text(shell1, SWT.SINGLE | SWT.BORDER);
				
				final Table table1 = new Table(shell1, SWT.CHECK | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
				table1.setHeaderVisible(true);
				table1.setLinesVisible(true);
				TableColumn[] column1 = new TableColumn[1];
				column1[0] = new TableColumn(table1, SWT.NONE);
		        column1[0].setWidth(200);
		        column1[0].setText("Fields");
		        for(int i = 0; i<10; i++){
        			TableItem item1 = new TableItem(table1, SWT.NONE);
        			item1.setText(table.getItems()[i].getText());
        		}
		        table1.setRedraw(true);
				
				FormData dat = new FormData();
		        dat.top = new FormAttachment(Name1, 0, SWT.CENTER);
		        name1.setLayoutData(dat);
		        dat = new FormData();
		        dat.left = new FormAttachment(name1, 75, SWT.LEFT);
		        dat.right = new FormAttachment(100, 0);
		        Name1.setLayoutData(dat);
		        
		        dat = new FormData();
		        dat.top = new FormAttachment(name1, 25);
		        dat.left = new FormAttachment(name1, 0, SWT.LEFT);
		        table1.setLayoutData(dat);
				
		        Name1.addModifyListener(new ModifyListener(){
		        	
		            public void modifyText(ModifyEvent e){
		            		TableItem[] it = table1.getItems();
		            		table1.setRedraw(false);		            		
		            		for(int j = 0; j<it.length; j++){
		            			if(it[j].getText().startsWith(Name1.getText())){
		            				TableItem I = new TableItem(table1, SWT.NONE);
		            				I.setText(it[j].getText());
		            			}
		            		}
		            		table1.setRedraw(true);
		            }
		        });
		        
				shell1.pack();
		        shell1.open();
				while (!shell1.isDisposed()) {
					if (!display.readAndDispatch())
						display.sleep();
				}
				
			}
        	
        };
        add.addListener(SWT.Selection, addListener);
        
        shell.pack();
       
		return shell;
	}
}
