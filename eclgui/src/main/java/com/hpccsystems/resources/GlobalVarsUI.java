package com.hpccsystems.resources;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hpccsystems.ui.constants.Constants;

public class GlobalVarsUI {
	private Shell shell;
	public void addChildControls(){
		
		Composite compositeForConcept = new Composite(shell, SWT.NONE);
		GridLayout layout = new GridLayout();
	    layout.numColumns = 2;
	    layout.makeColumnsEqualWidth = true;
	    compositeForConcept.setLayout(layout);
	    
	    GridData data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.verticalAlignment = GridData.FILL;
	    data.horizontalSpan = 2;
	    data.grabExcessHorizontalSpace = true;
	    data.grabExcessVerticalSpace = true;
	    compositeForConcept.setLayoutData(data);
	    
	    Label labelDefaultCluster = new Label(compositeForConcept, SWT.NONE);
	    labelDefaultCluster.setText("Default Cluster:");
	    
	    final Text textDefaultCluster = new Text(compositeForConcept, SWT.BORDER);
	    textDefaultCluster.setText(PropertiesReader.getProperty("hpcc.cluster"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textDefaultCluster.setLayoutData(data);
	    
	    Label labelDefaultHost = new Label(compositeForConcept, SWT.NONE);
	    labelDefaultHost.setText("Default Host:");
	    
	    final Text textDefaultHost = new Text(compositeForConcept, SWT.BORDER);
	    textDefaultHost.setText(PropertiesReader.getProperty("hpcc.host"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textDefaultHost.setLayoutData(data);
	    
	    Label labelDefaultPort = new Label(compositeForConcept, SWT.NONE);
	    labelDefaultPort.setText("Default Port:");
	    
	    final Text textDefaultPort = new Text(compositeForConcept, SWT.BORDER);
	    textDefaultPort.setText(PropertiesReader.getProperty("hpcc.port"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textDefaultPort.setLayoutData(data);
	    
	    Label labelDefaultLandingZone = new Label(compositeForConcept, SWT.NONE);
	    labelDefaultLandingZone.setText("Default Landing Zone:");
	    
	    final Text textDefaultLandingZone = new Text(compositeForConcept, SWT.BORDER);
	    textDefaultLandingZone.setText(PropertiesReader.getProperty("hpcc.landingzone"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textDefaultLandingZone.setLayoutData(data);
	    
	    Label labelDefaultMaxReturn = new Label(compositeForConcept, SWT.NONE);
	    labelDefaultMaxReturn.setText("Default Return Rows Limit:");
	    
	    final Text textDefaultMaxReturn = new Text(compositeForConcept, SWT.BORDER);
	    textDefaultMaxReturn.setText(PropertiesReader.getProperty("hpcc.maxreturn"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textDefaultMaxReturn.setLayoutData(data);
	    
	    
	    Label labelECLCCInstallDir = new Label(compositeForConcept, SWT.NONE);
	    labelECLCCInstallDir.setText("Default Location of eclcc:");
	    
	    final Text textECLCCInstallDir = new Text(compositeForConcept, SWT.BORDER);
	    textECLCCInstallDir.setText(PropertiesReader.getProperty("hpcc.eclcc"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textECLCCInstallDir.setLayoutData(data);
	    
	    
	    Label labelMLLibrary = new Label(compositeForConcept, SWT.NONE);
	    labelMLLibrary.setText("Default ML Library Location:");
	    
	    final Text textMLLibrary  = new Text(compositeForConcept, SWT.BORDER);
	    textMLLibrary.setText(PropertiesReader.getProperty("ml.lib"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textMLLibrary.setLayoutData(data);
	    
	    Label labelSaltLibrary = new Label(compositeForConcept, SWT.NONE);
	    labelSaltLibrary.setText("Default SALt Library Location:");
	    
	    final Text textSaltLibrary = new Text(compositeForConcept, SWT.BORDER);
	    textSaltLibrary.setText(PropertiesReader.getProperty("salt.lib"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textSaltLibrary.setLayoutData(data);
	    
	    Label labelSaltExe = new Label(compositeForConcept, SWT.NONE);
	    labelSaltExe.setText("Default SALt Executable Location:");
	    
	    final Text textSaltExe = new Text(compositeForConcept, SWT.BORDER);
	    textSaltExe.setText(PropertiesReader.getProperty("salt.exe"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textSaltExe.setLayoutData(data);
	    
	    Label labelSaltInclude = new Label(compositeForConcept, SWT.NONE);
	    labelSaltInclude.setText("Default SALt Include:");
	    
	    final Text textSaltInclude = new Text(compositeForConcept, SWT.BORDER);
	    textSaltInclude.setText(PropertiesReader.getProperty("salt.include"));
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.grabExcessHorizontalSpace = true;
	    textSaltInclude.setLayoutData(data);
	    
	    
	    // Composite for holding Buttons(Ok, Cancel)
	    Composite comp = new Composite(shell, SWT.NONE);
	    layout = new GridLayout();
	    layout.numColumns = 2;
	    layout.makeColumnsEqualWidth = true;
	    comp.setLayout(layout);
	    
	    data = new GridData();
	    data.horizontalAlignment = GridData.FILL;
	    data.verticalAlignment = GridData.FILL;
	    data.horizontalSpan = 3;
	    data.grabExcessHorizontalSpace = true;
	    comp.setLayoutData(data);
	    
	    Button btnOk = new Button(comp, SWT.PUSH);
	    btnOk.setText(Constants.BTN_OK);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		data.grabExcessHorizontalSpace = true;
		data.widthHint = 80;
		btnOk.setLayoutData(data);
		btnOk.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				Map<String, String> mapProperties = new HashMap<String, String>();
				mapProperties.put("hpcc.cluster", textDefaultCluster.getText());
				mapProperties.put("hpcc.host", textDefaultHost.getText());
				mapProperties.put("hpcc.port", textDefaultPort.getText());
				mapProperties.put("hpcc.landingzone", textDefaultLandingZone.getText());
				mapProperties.put("salt.include", textSaltInclude.getText());
				mapProperties.put("hpcc.eclcc", textECLCCInstallDir.getText());
				mapProperties.put("ml.lib", textMLLibrary.getText());
				mapProperties.put("salt.lib", textSaltLibrary.getText());
				mapProperties.put("salt.exe", textSaltExe.getText());
				mapProperties.put("salt.include", textSaltInclude.getText());
				mapProperties.put("hpcc.maxreturn", textDefaultMaxReturn.getText());
				
				writeToPropertiesFile(mapProperties);
				//display.dispose();
				shell.dispose();
	
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void writeToPropertiesFile(Map<String, String> mapProperties) {
		
		if(mapProperties != null && mapProperties.size() > 0) {
			Set<String> setKeys = mapProperties.keySet();
			PropertiesReader.setFileName(mapProperties.get("filename"));
			for (Iterator<String> iterator = setKeys.iterator(); iterator.hasNext();) {
				String key = iterator.next();
				PropertiesReader.setProperty(key, mapProperties.get(key));
				
			}
		}
	}
	
	public void run(Display display) {
		shell = new Shell(display);
		
	    shell.setText(Constants.ADD_CONCEPTS_TITLE);
	    shell.setSize(800, 550);
	    GridLayout layout = new GridLayout();
	    layout.numColumns = 3;
	    layout.marginLeft = 10;
	    layout.marginRight = 10;
	    layout.makeColumnsEqualWidth = true;
	    shell.setLayout(layout);
	    
	    addChildControls();
		
	    shell.open();
	   
	}
	
	public static void main(String[] args) {
		Display display = new Display();
	    
		new GlobalVarsUI().run(display);
		 while (!display.isDisposed()) {
		      if (!display.readAndDispatch())
		        display.sleep();
		    }
		    display.dispose();
		 
	}
	
}
