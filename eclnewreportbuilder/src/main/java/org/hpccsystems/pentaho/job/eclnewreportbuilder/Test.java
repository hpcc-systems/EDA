/*package org.hpccsystems.pentaho.job.eclnewreportbuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


class Test{
	public static void main(String[] args){
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FormLayout());
		shell.setBackground(new Color(null,255,255,255));
		
		Button B = new Button(shell, SWT.PUSH);
		B.setText("Test");
		
		FormData data = new FormData();
		data.top = new FormAttachment(null,0);
		data.left = new FormAttachment(0,5);
		data.right = new FormAttachment(95,0);
		B.setLayoutData(data);
		
		
		
		
		
		shell.open();
		shell.pack();
		while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
	}
}*/