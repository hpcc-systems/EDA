package com.lexisnexis.ui.optimizedlayout;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.lexisnexis.ui.constants.Constants;

import au.com.bytecode.opencsv.CSVReader;

public class OptimizedLayoutMain {
	
	private String recordString = null;
	private CTabFolder folder;
	public OptimizedLayoutMain(String fileName){
		recordString = readFromCSV(fileName);
	}
	
	public String getRecordString(){
		return recordString;
	}
	
	public Text createContents(CTabFolder parent){
		// Create a multiple-line text field
	    Text t = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	    t.setText(getRecordString());
	    return t;

	}
	
	public String readFromCSV(String fileName){
		File file = new File(fileName);
		FileInputStream fstream;
		DataInputStream in;
		BufferedReader br;
		CSVReader reader = null;
		StringBuffer buffer = new StringBuffer();
		try {
			fstream = new FileInputStream(file);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			reader = new CSVReader(br,',','"','\\');
			boolean first = true;
			//how to iterate through
			String [] nextLine;
			String [] arrayHeaders = null;
			String [] arrayValues = null;
			while ((nextLine = reader.readNext()) != null) {
				//nextLine will be an array with your columns
				String[] lineArr = nextLine;
				//Ignore the first line as it contains headers.
				if(first) {
					arrayHeaders = lineArr;
					first = false;
				} else {
					arrayValues = lineArr;
			    	for (int i = 0; i < arrayValues.length; i++) { 
			    		buffer.append(arrayValues[i]);
			    		buffer.append("\n");
					}
				}
			}
						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return buffer.toString();
	}
	
	
	public void run() {
		Display display = new Display();
	    Shell shell = new Shell(display);
	    shell.setLayout(new FillLayout());
	    configureShell(shell);
	    folder = new CTabFolder(shell, SWT.BORDER);
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		folder.setSimple(false);
		CTabItem item = new CTabItem(folder, SWT.CLOSE);
		item.setText(Constants.OPTIMIZED_LAYOUT_TITLE);
	    Text t = createContents(folder);
	    item.setControl(t);
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch()) {
	        display.sleep();
	      }
	    }
	    display.dispose();
	}
	
	protected void configureShell(Shell shell) {
		shell.setSize(950, 400);
	}
	
	public static void main(String[] args) {
		
		String fileName = "Dataprofiling_OptimizedLayout.csv";
		new OptimizedLayoutMain(fileName).run();
	}
	
}
