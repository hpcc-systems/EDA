/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecllibrarybuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.hpccsystems.eclguifeatures.ErrorNotices;
import org.hpccsystems.ecljobentrybase.ECLJobEntryDialog;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
/**
 *
 * @author ChambersJ
 */
public class ECLLibraryBuilderDialog extends ECLJobEntryDialog{//extends JobEntryDialog implements JobEntryDialogInterface {

	
    private ECLLibraryBuilder jobEntry;
    private Text jobEntryName;
    private Text[] libValues;
    private Combo[] libCombos;
    private Group sizeGroup = null;
    private Combo libraryName;
    private Button wOK, wCancel;
    private boolean backupChanged;
    int txtVals = 0;
    private String[] libVals = null;
    private String[] libComb = null;
    private ArrayList<String> entries;
    private String code = null;
    private String[] outVals;
    
    
    
    public String[] getOutVals() {
		return outVals;
	}

	public void setOutVals(String[] outVals) {
		this.outVals = outVals;
	}

	public int getTxtVals() {
		return txtVals;
	}

	public void setTxtVals(int txtVals) {
		this.txtVals = txtVals;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String[] getLibVals() {
		return libVals;
	}

	public void setLibVals(String[] libVals) {
		this.libVals = libVals;
	}

	public String[] getLibComb() {
		return libComb;
	}

	public void setLibComb(String[] libComb) {
		this.libComb = libComb;
	}

	public Text[] getLibValues() {
		return libValues;
	}

	public void setLibValues(Text[] libValues) {
		this.libValues = libValues;
	}

	public Combo[] getLibCombos() {
		return libCombos;
	}

	public void setLibCombos(Combo[] libCombos) {
		this.libCombos = libCombos;
	}

	@SuppressWarnings("unused")
	private SelectionAdapter lsDef;
    
    public ECLLibraryBuilderDialog(Shell parent, JobEntryInterface jobEntryInt, Repository rep, JobMeta jobMeta) {
        super(parent, jobEntryInt, rep, jobMeta);
        jobEntry = (ECLLibraryBuilder) jobEntryInt;
        if (this.jobEntry.getName() == null) {
            this.jobEntry.setName("Library Plugin Builder");
        }
    }

    public JobEntryInterface open() {
        Shell parentShell = getParent();
        Display display = parentShell.getDisplay();
        entries = new ArrayList<String>();
        /*File f = new File("C:\\Program Files\\data-integration\\plugins\\hpcc-common\\eclrepo");
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));*/
        
        File g = new File("C:\\Program Files\\data-integration\\plugins\\hpcc-common\\eclrepo");
        String[] fileNames = g.list();
                
        shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
        
        props.setLook(shell);
        JobDialog.setShellImage(shell, jobEntry);
        
        TabFolder tab = new TabFolder(shell, SWT.FILL | SWT.RESIZE | SWT.MIN | SWT.MAX);
        FormData datatab = new FormData();
        
        datatab.height = 400;
        datatab.width = 650;
        tab.setLayoutData(datatab);
        
        Composite compForGrp = new Composite(tab, SWT.NONE);
        //compForGrp.setLayout(new FillLayout(SWT.VERTICAL));
        compForGrp.setBackground(new Color(tab.getDisplay(),255,255,255));
        compForGrp.setLayout(new FormLayout());
        TabItem item1 = new TabItem(tab, SWT.NULL);
        
        item1.setText ("General");
        props.setLook(shell);
        JobDialog.setShellImage(shell, jobEntry);

        final ModifyListener lsMod = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                jobEntry.setChanged();
            }
        };
        backupChanged = jobEntry.hasChanged();
        
        FormLayout layout = new FormLayout();
		layout.marginWidth = Const.FORM_MARGIN;
		layout.marginHeight = Const.FORM_MARGIN;
		
		final int middle = props.getMiddlePct();
        final int margin = Const.MARGIN;
        
		shell.setLayout(layout);
		shell.setText("HPCC Library Plugin Builder");
		
		final FormLayout groupLayout = new FormLayout();
        groupLayout.marginWidth = 10;
        groupLayout.marginHeight = 10;


        // Stepname line
        Group generalGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(generalGroup);
        generalGroup.setText("General Details");
        generalGroup.setLayout(groupLayout);
        FormData generalGroupFormat = new FormData();
        generalGroupFormat.top = new FormAttachment(0, margin);
        generalGroupFormat.width = 340;
        generalGroupFormat.height = 50;
        generalGroupFormat.left = new FormAttachment(middle, 0);
        generalGroupFormat.right = new FormAttachment(100, 0);
        generalGroup.setLayoutData(generalGroupFormat);
		
		jobEntryName = buildText("Job Entry Name :", null, lsMod, middle, margin, generalGroup);
		
		//All other contols
        //Dataset Declaration
        Group libGroup = new Group(compForGrp, SWT.SHADOW_NONE);
        props.setLook(libGroup);
        libGroup.setText("Library Details");
        libGroup.setLayout(groupLayout);
        FormData libGroupFormat = new FormData();
        libGroupFormat.top = new FormAttachment(generalGroup, margin);
        libGroupFormat.width = 340;
        libGroupFormat.height = 75;
        libGroupFormat.left = new FormAttachment(middle, 0);
        libGroupFormat.right = new FormAttachment(100, 0);
        libGroup.setLayoutData(libGroupFormat);
		
		
        libraryName = buildCombo("Library Name :", jobEntryName, lsMod, middle, margin, libGroup, fileNames);                             
       
        item1.setControl(compForGrp);
        /**
         * TableViewer in new Tab starts
         */
        
        TabItem item2 = new TabItem(tab, SWT.NULL);
        item2.setText("Plugin Builder");
                        
        final FormLayout groupLayout1 = new FormLayout();
        groupLayout1.marginWidth = 10;
        groupLayout1.marginHeight = 10;
        
        ScrolledComposite sc2 = new ScrolledComposite(tab, SWT.H_SCROLL | SWT.V_SCROLL);
        final Composite compForGrp2 = new Composite(sc2, SWT.NONE);
        compForGrp2.setLayout(groupLayout1);
        sc2.setContent(compForGrp2);

        // Set the minimum size
        sc2.setMinSize(300, 200);

        // Expand both horizontally and vertically
        sc2.setExpandHorizontal(true);
        sc2.setExpandVertical(true);
        
        
        
        
        libraryName.addModifyListener(new ModifyListener() {
			
        	@Override
			public void modifyText(ModifyEvent arg0) {		 
				if(sizeGroup != null)
					sizeGroup.dispose();
				if(!libraryName.getText().equalsIgnoreCase("")){
					 sizeGroup = new Group(compForGrp2, SWT.SHADOW_NONE);
				     props.setLook(sizeGroup);
				     sizeGroup.setText("Library Fields");
				     sizeGroup.setLayout(groupLayout);
				     FormData GroupFormat = new FormData();
				     GroupFormat.top = new FormAttachment(0, margin); 
				     GroupFormat.width = 625;
				     GroupFormat.height = 350;
				     GroupFormat.left = new FormAttachment(0, 0);
				     sizeGroup.setLayoutData(GroupFormat);
				     //sizeGroup.setSize(sizeGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				     sizeGroup.layout();
				
				
					String lib = libraryName.getText().split("\\.")[0];
					
					Create(middle, margin, sizeGroup,  lsMod, lib);			
					JSONParser parser  = new JSONParser();
					Object obj;
					try {
						obj = parser.parse(new FileReader("C:\\Program Files\\data-integration\\plugins\\hpcc-common\\ecllibraries\\"+lib+"\\"+lib+".json"));
						JSONObject jsonObject = (JSONObject) obj;
			 			
						
						
						JSONObject ob1 = (JSONObject)jsonObject.get(new String("contract"));	
						
						
						code = (String)ob1.get("template");
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
					
				}
			}

			

			
		});
        
        item2.setControl(sc2);
        
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel");

        BaseStepDialog.positionBottomButtons(compForGrp, new Button[]{wOK, wCancel}, margin, tab);
        
                
        
        // Add listeners
        Listener cancelListener = new Listener() {

            public void handleEvent(Event e) {
                cancel();
            }
        };
        Listener okListener = new Listener() {

            public void handleEvent(Event e) {
            	String[] vals = new String[getLibValues().length];
            	for(int i = 0; i<getLibValues().length; i++){
            		vals[i] = libValues[i].getText();
            	}
            	
            	String[] comb = new String[getLibCombos().length];            	
            	for(int j = 0; j<getLibCombos().length; j++){
					comb[j] = getLibCombos()[j].getText();
            	}
            	            	
            	setLibVals(vals);
            	setLibComb(comb);
                ok();
            }
        };

        wCancel.addListener(SWT.Selection, cancelListener);
        wOK.addListener(SWT.Selection, okListener);

        lsDef = new SelectionAdapter() {

            public void widgetDefaultSelected(SelectionEvent e) {
                ok();
            }
        };


        // Detect X or ALT-F4 or something that kills this window...

        shell.addShellListener(new ShellAdapter() {

            public void shellClosed(ShellEvent e) {
                cancel();
            }
        });

        if (jobEntry.getName() != null) {
            jobEntryName.setText(jobEntry.getName());
        }
        
        if(jobEntry.getEntries() != null){
        	entries = jobEntry.getEntries();
        }
        
        if (jobEntry.getLibraryName() != null) {
            libraryName.setText(jobEntry.getLibraryName());
            if(sizeGroup != null)
				sizeGroup.dispose();
			if(!libraryName.getText().equalsIgnoreCase("")){
				 sizeGroup = new Group(compForGrp2, SWT.SHADOW_NONE);
			     props.setLook(sizeGroup);
			     sizeGroup.setText("Library Fields");
			     sizeGroup.setLayout(groupLayout);
			     FormData GroupFormat = new FormData();
			     GroupFormat.top = new FormAttachment(0, margin); 
			     GroupFormat.width = 625;
			     GroupFormat.height = 350;
			     GroupFormat.left = new FormAttachment(0, 0);
			     sizeGroup.setLayoutData(GroupFormat);
			     //sizeGroup.setSize(sizeGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			     sizeGroup.layout();
			
			
				String lib = libraryName.getText().split("\\.")[0];
				
				if(libVals != null){
					setLibVals(jobEntry.getLibValues());
				}
				if(libComb != null){
					setLibComb(jobEntry.getLibCombos());
				}
				
				Create(middle, margin, sizeGroup,  lsMod, lib);
			}
        }
        
        if(jobEntry.getCode() != null){
        	code = jobEntry.getCode();
        }
        if(jobEntry.getTxtVals().length()>0){        	
        	txtVals = Integer.parseInt(jobEntry.getTxtVals());
        }
        if(jobEntry.getOutVals() != null){
        	setOutVals(jobEntry.getOutVals());
        }
       
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        return jobEntry;

    }

    public void Create(int middle, int margin, Group sizeGroup,  ModifyListener lsMod, String lib) {
		JSONParser parser = new JSONParser();
		try {
			
			Object obj = parser.parse(new FileReader("C:\\Program Files\\data-integration\\plugins\\hpcc-common\\ecllibraries\\"+lib+"\\"+lib+".json"));
	 
			JSONObject jsonObject = (JSONObject) obj;
	 			
			
			
			JSONObject ob1 = (JSONObject)jsonObject.get(new String("contract"));	
			
			if(getCode() == null)
				setCode((String)ob1.get("template"));
			
			JSONObject ob2 = (JSONObject)ob1.get(new String("formconfig"));
			
			JSONObject ob3 = (JSONObject)ob2.get("value");
			Collection<JSONObject> C = ob3.values();
			
			Set inputSet = ob3.keySet();
			
			
			JSONObject ob4 = (JSONObject) ob1.get(new String("outputLayoutAdditions"));
			Set outSet = ob4.keySet();
			int outSize = ob4.size(); 
			
			String[] libEntries = new String[inputSet.size()+outSet.size()];
			int setCnt = 0;
			for(Iterator st = inputSet.iterator(); st.hasNext();){
				String S = (String) st.next();
				libEntries[setCnt] = S;
				setCnt++;
			}
			
			for(Iterator st = outSet.iterator(); st.hasNext();){
				String S = (String) st.next();
				libEntries[setCnt] = S;
				setCnt++;
			}
			
			int Size = C.size();
			for(Iterator<JSONObject> it = C.iterator(); it.hasNext();){
				JSONObject S = it.next();
				String S1 = (String) S.get("fieldType");
				if(S1.toLowerCase().equals("select")){
					Size--;
				}				
			}
			setTxtVals(Size);
			Text[] libValues = new Text[Size+outSize];
			if(getLibVals() == null)
				setLibVals(new String[Size+outSize]);
			
			int i = 0;setCnt = 0;int[] combSize = new int[C.size()-Size];int combCnt = 0;
			for(Iterator<JSONObject> it = C.iterator(); it.hasNext();){
					JSONObject S = it.next();
					String S1 = (String) S.get("fieldType");
					 
					if(S1.toLowerCase().equals("select")){
						// do something in another loop so that one control can be accessed
						combSize[combCnt] = setCnt;
						combCnt++;
					}
					else{
						if(i == 0){
							libValues[i] = buildText((String) S.get("label")+" :", null, lsMod, middle, margin*2, sizeGroup);
							if(getLibVals()[i]==null)
								libValues[i].setText("");
							else
								libValues[i].setText(getLibVals()[i]);
						}
						else{
							libValues[i] = buildText((String) S.get("label")+" :", libValues[i-1], lsMod, middle, margin*2, sizeGroup);
							if(getLibVals()[i]==null)
								libValues[i].setText("");
							else
								libValues[i].setText(getLibVals()[i]);
						}
						i++;
						if(!entries.contains(libEntries[setCnt]))
							entries.add(libEntries[setCnt]);
					}
					setCnt++; 
			}
			
			String[] outers = new String[outSize];
			for(int o = 0; o<outSize; o++){
				libValues[i+o] = buildText("OutName :", libValues[i-1], lsMod, middle, margin*2, sizeGroup);
				if(getLibVals()[i+o]==null){
					libValues[i+o].setText("");
					outers[o] = "";
				}
				else{
					libValues[i+o].setText(getLibVals()[i+o]);
					outers[o] = getLibVals()[i+o];
				}
				if(!entries.contains(libEntries[setCnt]))
					entries.add(libEntries[setCnt]);				
				setCnt++;
			}
			
			Combo[] libCombos = new Combo[C.size()-Size];
			if(getLibComb() == null)
				setLibComb(new String[C.size()-Size]);
			int k = 0;
			combCnt = 0;
			for(Iterator<JSONObject> it = C.iterator(); it.hasNext();){
				JSONObject S = it.next();
				String S1 = (String) S.get("fieldType");
				
				if(S1.toLowerCase().equals("select")){
					JSONArray ar = (JSONArray) S.get("allowedValues");
					String[] items = new String[ar.size()];
					int j = 0;
					for(Iterator I  = ar.iterator(); I.hasNext();){
						JSONObject ar1 = (JSONObject) I.next();
						
						items[j] = (String) ar1.get("value");
						j++;
					}
					if(k == 0){
						libCombos[k] = buildCombo((String) S.get("label")+" :", libValues[i+outSize-1], lsMod, middle, margin*2, sizeGroup, items);
						if(getLibComb()[k]==null)
							libCombos[k].setText("");
						else
							libCombos[k].setText(getLibComb()[k]);
					}
					else{
						libCombos[k] = buildCombo((String) S.get("label")+" :", libCombos[k-1], lsMod, middle, margin*2, sizeGroup, items);
						if(getLibComb()[k]==null)
							libCombos[k].setText("");
						else
							libCombos[k].setText(getLibComb()[k]);
					}
					k++;
					if(!entries.contains(libEntries[combSize[combCnt]]))
						entries.add(libEntries[combSize[combCnt]]);
					combCnt++;
				}
			}
			if(!entries.contains("libraryname"))
				entries.add("libraryname");
			setLibCombos(libCombos);
			setLibValues(libValues);
			setOutVals(outers);
			//setLibVals(libVals);
			//setLibComb(libComb);
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
    
    private boolean validate(){
    	boolean isValid = true;
    	String errors = "";
    	
    	
    	
    	if(this.jobEntryName.getText().equals("")){
    		isValid = false;
    		errors += "\"Job Entry Name\" is a required field!\r\n";
    	}
    	
    	if(this.libraryName.getText().equals("")){
    		isValid = false;
    		errors += "\"Library Name\" is a required field!\r\n";
    	}
    	
    	if(!isValid){
    		ErrorNotices en = new ErrorNotices();
    		errors += "\r\n";
    		errors += "If you continue to save with errors you may encounter compile errors if you try to execute the job.\r\n\r\n";
    		isValid = en.openValidateDialog(getParent(),errors);
    	}
    	return isValid;
    	
    }
    
    private void ok() {
    	if(!validate()){
    		return;
    	}
    	
        jobEntry.setName(jobEntryName.getText());
        jobEntry.setLibraryName(libraryName.getText());
        jobEntry.setLibCombos(getLibComb());
        jobEntry.setLibValues(getLibVals());
        jobEntry.setEntries(entries);
        jobEntry.setCode(code);
        jobEntry.setTxtVals(""+getTxtVals());
        jobEntry.setOutVals(getOutVals());
        dispose();
    }

    private void cancel() {
        jobEntry.setChanged(backupChanged);
        jobEntry = null;
        dispose();
    }

}
