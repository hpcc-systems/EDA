package com.hpccsystems.resources;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class GlobalVarsUI {
	private Shell shell;
	public void addChildControls() throws IOException{
		DirectoryBrowser obj = new DirectoryBrowser();
		obj.createContents(shell);
	}
	
	
	
	public void run(Display display) throws IOException {
		
		shell = new Shell(display);
		
		addChildControls();
		
	} 
	
	public static void main(String[] args) throws IOException {
		Display display = new Display();
	    
		new GlobalVarsUI().run(display);
		 while (!display.isDisposed()) {
		      if (!display.readAndDispatch())
		        display.sleep();
		    }
		    display.dispose();
		 
	}
	
}
