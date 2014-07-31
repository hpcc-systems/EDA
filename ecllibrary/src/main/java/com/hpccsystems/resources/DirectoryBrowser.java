package com.hpccsystems.resources;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * This class demonstrates the DirectoryDialog class
 */
public class DirectoryBrowser {
	/**
	 * Creates the window contents
	 * 
	 * @param shell
	 *            the parent shell
	 * @throws IOException  
	 */
	protected void createContents(final Shell shell) throws IOException  {
		
		DirectoryDialog dlg = new DirectoryDialog(shell);
		dlg.setText("HPCC Library"); 

		// Customizable message displayed in the dialog
		
		dlg.setMessage("Select the HPCC Library Directory");

		// Calling open() will open and run the dialog.
		// It will return the selected directory, or
		// null if user cancels
		String dir = dlg.open();
		if (dir != null) {
			// Set the text box to the new selection
			//text.setText(dir);
			System.out.println("My name is Anthony Gonzalves");
			System.out.println("Main duniya mein akela hun");
			int i = dir.split("\\\\").length;
			String libName = dir.split("\\\\")[i-1];
			System.out.println(dir.split("\\\\")[i-1]); 
			CopyFolder obj = new CopyFolder();
			System.out.println("Copying...");
			File folder = new File("C:\\Program Files\\data-integration\\plugins\\hpcc-common\\ecllibraries\\"+libName);
			folder.mkdir();
 			obj.copy(new File(dir+"\\Contract.json"), new File("C:\\Program Files\\data-integration\\plugins\\hpcc-common\\ecllibraries\\"+libName+"\\addCounter.json"));
 			obj.copy(new File(dir+"\\images"), new File("C:\\Program Files\\data-integration\\plugins\\hpcc-common\\ecllibraries\\"+libName));
 			obj.copy(new File(dir+"\\eclLibrary"), new File("C:\\Program Files\\data-integration\\plugins\\hpcc-common\\eclrepo\\"+libName));
 			System.out.println("Done!"); 
		}
	}

}