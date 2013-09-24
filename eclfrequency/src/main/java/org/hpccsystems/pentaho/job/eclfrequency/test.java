package org.hpccsystems.pentaho.job.eclfrequency;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
//import org.hpccsystems.pentaho.job.eclrandom.ECLRandom;
import org.pentaho.di.ui.trans.step.BaseStepDialog;


public class test{// extends ApplicationWindow {
	//private ECLRandom jobEntry;
    private static Text jobEntryName;
    private static Text ResultDataset;
    private static Combo datasetName;
    private static Button wOK, wCancel;
    private boolean backupChanged;
    private SelectionAdapter lsDef;
    
    
	public static void main(String[] args){
		/*String S = "Keshav,Shrikant,Graduate-John,Day, ,";
		String[] norm = S.split("-");
		System.out.println(norm[0]);
		System.out.println(norm[1]);
		String[] me = norm[1].split(",");
		System.out.println(me[0]);
		System.out.println(me[1]);
		System.out.println(me[2]);*/
		Display display = new Display();
		Shell shell = new Shell(display, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
		FormData datatab = new FormData();
        
        datatab.height = 300;
        datatab.width = 350;
        //shell.setLayoutData(datatab);
        
        
        Composite compForGrp = new Composite(shell, SWT.NONE);
        compForGrp.setBackground(new Color(shell.getDisplay(),255,255,255));
        compForGrp.setLayout(new FormLayout());
        compForGrp.setLayoutData(datatab);
        //compForGrp.setSize(340, 250);
        
        
        
        
        int margin = Const.MARGIN;
        
        FormLayout layout = new FormLayout();
		layout.marginWidth = 5;
		layout.marginHeight = 5;
		shell.setLayout(layout);
        shell.setText("Random Number Generator");
        Label Name = new Label(compForGrp, SWT.NONE);
		Name.setText("Job Entry Name:");
		jobEntryName = new Text(compForGrp, SWT.SINGLE | SWT.BORDER);
		
		Label dataset = new Label(compForGrp, SWT.NONE);
		dataset.setText("Dataset:");
		datasetName = new Combo(compForGrp, SWT.NONE | SWT.BORDER);
		datasetName.setItems(new String[]{"aslda","askljdl"});
		
		Label result = new Label(compForGrp, SWT.NONE);
		result.setText("Result Dataset:" );
		ResultDataset = new Text(compForGrp, SWT.SINGLE | SWT.BORDER);
		
        
		
		FormData data = new FormData();
		data.top = new FormAttachment(jobEntryName, 0, SWT.CENTER);
		Name.setLayoutData(data);
		data = new FormData();
		data.left = new FormAttachment(Name, 5);
		data.right = new FormAttachment(100, 0);
		jobEntryName.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(datasetName, 0, SWT.CENTER);
		dataset.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(jobEntryName, 15);
		data.left = new FormAttachment(jobEntryName, 0, SWT.LEFT);
		data.right = new FormAttachment(100, 0);
		datasetName.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(dataset, 25);
		result.setLayoutData(data);
		data = new FormData();
		data.top = new FormAttachment(datasetName, 20);
		data.left = new FormAttachment(result,14);
		data.right = new FormAttachment(100, 0);
		ResultDataset.setLayoutData(data);
		
		

        //BaseStepDialog.positionBottomButtons(shell, new Button[]{wOK, wCancel}, 25, ResultDataset);
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		
	}
}



